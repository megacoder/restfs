package nofs.restfs.rules;

import nofs.Library.Annotations.HideMethod;
import nofs.Library.Containers.IListensToEvents;
import nofs.Library.Containers.IProvidesUnstructuredData;
import nofs.restfs.BaseFileObject;
import nofs.restfs.query.ConfigGrammar;
import nofs.restfs.query.ast.ProgramStm;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class RulesSourceFile extends BaseFileObject implements IProvidesUnstructuredData, IListensToEvents {

    private RulesASTFile _ast;
    private RulesErrorFile _err;
    private volatile byte[] _data;

    @HideMethod
    public RulesASTFile ast() { return _ast; }
    @HideMethod
    public void ast(RulesASTFile ast) { _ast = ast; }
    @HideMethod
    public void err(RulesErrorFile err) { _err = err; }

    private String dataToString() {
        StringBuilder buffer = new StringBuilder();
        buffer.ensureCapacity(_data.length);
        for(byte byt : _data) {
            buffer.append((char)byt);
        }
        return buffer.toString();
    }

    @Override
    public void Closed() throws Exception {
        _err.setErrorData("");
        _ast.setStatements(new ArrayList<ProgramStm>());
        ConfigGrammar grammar = new ConfigGrammar();
        try {
            String source = dataToString();
            List<ProgramStm> stms = grammar.Parse_JavaList(source);
            _ast.setStatements(stms);
        } catch(Exception e) {
            _err.setErrorData(e.getMessage());
        }
    }

    @Override
    public void Opened() throws Exception {
    }

    @Override
    public void Deleting() throws Exception {
    }

    @Override
    public void Created() throws Exception {
    }

    @Override
    public long DataSize() {
        return _data.length;
    }

    @Override
    public boolean Cacheable() {
        return false;
    }

    @Override
    public void Read(ByteBuffer buffer, long offset, long length) throws Exception {
        long endPoint = offset + length;
        for(long i = offset; i < endPoint && i < _data.length; i++) {
            buffer.put(_data[(int)i]);
        }
    }

    @Override
    public void Write(ByteBuffer buffer, long offset, long length) throws Exception {
        long endPoint = offset + length;
        if(endPoint > _data.length) {
            Truncate(endPoint);
        }
        for(long i = offset; i < endPoint && i < _data.length; i++) {
            _data[(int)i] = buffer.get();
        }
    }

    @Override
    public void Truncate(long newLength) {
        byte[] newData = new byte[(int)newLength];
        System.arraycopy(_data, 0, newData, 0, (int)Math.min(_data.length, newLength));
        _data = newData;
    }
}
