package nofs.restfs;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import nofs.Library.Annotations.DomainObject;
import nofs.Library.Annotations.FolderObject;

@FolderObject
@DomainObject
public class RestfulFolder extends BaseRestfulFileObject implements List<BaseRestfulFileObject> {
	private final List<BaseRestfulFileObject> _innerList;
	
	public RestfulFolder() {
		_innerList = new LinkedList<BaseRestfulFileObject>();
		_settings = new RestfulSettingsFolder();
	}
	
	private void AddingObject(BaseRestfulFileObject baseFile) {
		if(baseFile instanceof RestfulFile) {
			RestfulFile file = (RestfulFile)baseFile;
			if(file.getSettings() == null) {
				RestfulSetting settings = null;
				for(RestfulSetting possibleSettings : _settings) {
					if(possibleSettings.getName().compareTo(file.getName()) == 0) {
						settings = possibleSettings;
						break;
					}
				}
				if(settings == null) {
					settings = new RestfulSetting();
					_settings.add(settings);
				}
				file.setSettings(settings);
			}
		}
	}
	
	private void RemovingObject(BaseRestfulFileObject baseFile) {
		
	}
	
	@Override
	public boolean add(BaseRestfulFileObject baseFile) {
		AddingObject(baseFile);
		return _innerList.add(baseFile);
	}
	
	@Override
	public void add(int index, BaseRestfulFileObject element) {
		AddingObject(element);
		_innerList.add(index, element);
	}
	
	private RestfulSettingsFolder _settings;
	public RestfulSettingsFolder getSettings() { return _settings; }

	@Override
	public boolean addAll(Collection<? extends BaseRestfulFileObject> c) {
		boolean stat = false;
		for(BaseRestfulFileObject obj : c) {
			stat |= add(obj);
		}
		return stat;
	}

	@Override
	public boolean addAll(int index, Collection<? extends BaseRestfulFileObject> c) {
		List<BaseRestfulFileObject> objects = new LinkedList<BaseRestfulFileObject>();
		List<BaseRestfulFileObject> objectsToAdd = new LinkedList<BaseRestfulFileObject>();
		objects.addAll(_innerList);
		objectsToAdd.addAll(c);
		_innerList.clear();
		for(int i = 0; objects.size() > 0 && objectsToAdd.size() > 0; i++) {
			if(i > index && objectsToAdd.size() > 0) {
				BaseRestfulFileObject obj = objectsToAdd.remove(0);
				AddingObject(obj);
				_innerList.add(obj);
			} else {
				BaseRestfulFileObject obj = objects.remove(0);
				AddingObject(obj);
				_innerList.add(obj);
			}
		}
		return true;
	}

	@Override
	public void clear() {
		for(BaseRestfulFileObject o : _innerList) {
			RemovingObject(o);
		}
		_innerList.clear();
	}

	@Override
	public boolean contains(Object o) {
		return _innerList.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return _innerList.containsAll(c);
	}

	@Override
	public BaseRestfulFileObject get(int index) {
		return _innerList.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return _innerList.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return _innerList.isEmpty();
	}

	@Override
	public Iterator<BaseRestfulFileObject> iterator() {
		return _innerList.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return _innerList.lastIndexOf(o);
	}

	@Override
	public ListIterator<BaseRestfulFileObject> listIterator() {
		return _innerList.listIterator();
	}

	@Override
	public ListIterator<BaseRestfulFileObject> listIterator(int index) {
		return _innerList.listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		return _innerList.remove(o);
	}

	@Override
	public BaseRestfulFileObject remove(int index) {
		BaseRestfulFileObject obj = _innerList.get(index);
		RemovingObject(obj);
		return _innerList.remove(index);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		for(Object obj : c) {
			RemovingObject((BaseRestfulFileObject)obj);
		}
		return _innerList.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		List<BaseRestfulFileObject> allItems = new LinkedList<BaseRestfulFileObject>();
		for(Object obj : c) {
			allItems.remove(obj);
		}
		for(BaseRestfulFileObject obj : allItems) {
			RemovingObject(obj);
		}
		return _innerList.retainAll(c);
	}

	@Override
	public BaseRestfulFileObject set(int index, BaseRestfulFileObject element) {
		BaseRestfulFileObject obj = _innerList.set(index, element);
		RemovingObject(obj);
		AddingObject(element);
		return obj;
	}

	@Override
	public int size() {
		return _innerList.size();
	}

	@Override
	public List<BaseRestfulFileObject> subList(int fromIndex, int toIndex) {
		return _innerList.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return _innerList.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return _innerList.toArray(a);
	}
}
