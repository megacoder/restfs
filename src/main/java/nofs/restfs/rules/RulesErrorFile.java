package nofs.restfs.rules;

import nofs.Library.Annotations.DomainObject;
import nofs.Library.Containers.IProvidesUnstructuredData;
import nofs.restfs.BaseFileObject;

import java.nio.ByteBuffer;

@DomainObject(CanWrite = false)
public class RulesErrorFile extends BaseFileObject implements IProvidesUnstructuredData {
    private byte[] _data;

    public RulesErrorFile() {
        _data = new byte[0];
    }

    public void setErrorData(String error) {
        _data = new byte[error.length()];
        int i = 0;
        for(char ch : error.toCharArray()) {
            _data[i++] = (byte)ch;
        }
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
        long end = offset + length;
        for(long i = offset; i < end && i < _data.length; i++) {
			buffer.put(_data[(int)i]);
		}
    }

    @Override
    public void Write(ByteBuffer byteBuffer, long l, long l1) throws Exception {
    }

    @Override
    public void Truncate(long l) {
    }
}
