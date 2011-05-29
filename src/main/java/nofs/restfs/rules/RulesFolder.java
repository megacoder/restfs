package nofs.restfs.rules;

import nofs.Library.Annotations.DomainObject;
import nofs.Library.Annotations.FolderObject;
import nofs.Library.Annotations.NeedsContainerManager;
import nofs.Library.Containers.IDomainObjectContainer;
import nofs.Library.Containers.IDomainObjectContainerManager;
import nofs.restfs.BaseFileObject;
import nofs.restfs.RestfulFolder;

import java.io.File;
import java.util.List;

@DomainObject
@FolderObject(ChildTypeFilterMethod = "Filter")
public class RulesFolder extends RestfulFolder<BaseFileObject> {

    public boolean Filter(Class<?> possibleChildType) {
        return possibleChildType == RulesSourceFile.class ||
                possibleChildType == RulesErrorFile.class ||
                possibleChildType == RulesASTFile.class;
    }

    private IDomainObjectContainerManager _containerManager;
	@NeedsContainerManager
	public void setContainerManager(IDomainObjectContainerManager containerManager) {
		_containerManager = containerManager;
	}

    @Override
    protected void CreatingList(List<BaseFileObject> newList) {
    }

    @Override
    protected void AddingObject(BaseFileObject baseFile) {
        if(baseFile instanceof RulesSourceFile) {
            RulesSourceFile sourceFile = (RulesSourceFile)baseFile;
            RulesASTFile astFile = FindASTFile(sourceFile);
            RulesErrorFile errFile = FindErrorFile(sourceFile);
            if(astFile == null) {
                astFile = CreateASTFile(sourceFile);
            }
            if(errFile == null) {
                errFile = CreateErrorFile(sourceFile);
            }
            add(astFile);
            add(errFile);
            sourceFile.ast(astFile);
            sourceFile.err(errFile);
            ObjectChanged();
        }
    }

    private void ObjectChanged() {
        try {
            IDomainObjectContainer<RulesFolder> container =
                    _containerManager.GetContainer(RulesFolder.class);
            container.ObjectChanged(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private RulesErrorFile FindErrorFile(RulesSourceFile sourceFile) {
        final String name = sourceFile.getName() + ".err";
        for(BaseFileObject file : this) {
            if(file instanceof RulesErrorFile) {
                RulesErrorFile errFile = (RulesErrorFile)file;
                if(errFile.getName().compareTo(name) == 0) {
                    return errFile;
                }
            }
        }
        return null;
    }

    private RulesASTFile FindASTFile(RulesSourceFile sourceFile) {
        final String name = sourceFile.getName() + ".ast";
        for(BaseFileObject file : this) {
            if(file instanceof RulesASTFile) {
                RulesASTFile astFile = (RulesASTFile)file;
                if(astFile.getName().compareTo(name) == 0) {
                    return astFile;
                }
            }
        }
        return null;
    }

    private RulesErrorFile CreateErrorFile(RulesSourceFile sourceFile) {
        RulesErrorFile errFile = null;
        String name = sourceFile.getName() + ".err";
        try {
            IDomainObjectContainer<RulesErrorFile> container =
                    _containerManager.GetContainer(RulesErrorFile.class);
            errFile = container.NewPersistentInstance();
            errFile.setName(name);
            container.ObjectChanged(errFile);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return errFile;
    }

    private RulesASTFile CreateASTFile(RulesSourceFile sourceFile) {
        RulesASTFile astFile = null;
        String name = sourceFile.getName() + ".ast";
        try {
            IDomainObjectContainer<RulesASTFile> container =
                    _containerManager.GetContainer(RulesASTFile.class);
            astFile = container.NewPersistentInstance();
            astFile.setName(name);
            container.ObjectChanged(astFile);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return astFile;
    }

    @Override
    protected void RemovingObject(BaseFileObject baseFile) {
        if(baseFile instanceof RulesSourceFile) {
            RulesSourceFile sourceFile = (RulesSourceFile)baseFile;

        }
    }
}
