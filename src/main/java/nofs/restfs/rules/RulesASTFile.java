package nofs.restfs.rules;

import nofs.Library.Annotations.DomainObject;
import nofs.restfs.BaseFileObject;
import nofs.restfs.query.ast.ProgramStm;

import java.util.ArrayList;
import java.util.List;

@DomainObject(CanWrite = false)
public class RulesASTFile extends BaseFileObject {
    private List<ProgramStm> _stms = new ArrayList<ProgramStm>();

    public void setStatements(List<ProgramStm> stms) {
        _stms = stms;
    }

    public List<ProgramStm> getStatements() {
        return _stms;
    }
}
