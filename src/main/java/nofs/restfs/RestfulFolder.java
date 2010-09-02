package nofs.restfs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import nofs.Library.Annotations.DomainObject;
import nofs.Library.Annotations.FolderObject;

@FolderObject
@DomainObject
public abstract class RestfulFolder<T extends BaseFileObject> extends BaseFileObject implements List<T> {
	private List<T> _innerList;
	
	protected RestfulFolder() {
		_innerList = null;
	}
	
	private List<T> getInnerList() {
		if(_innerList == null) {
			_innerList = new ArrayList<T>();
			CreatingList(_innerList);
		}
		return _innerList;
	}
	
	protected abstract void CreatingList(List<T> newList);
	protected abstract void AddingObject(BaseFileObject baseFile);
	protected abstract void RemovingObject(BaseFileObject baseFile);
	
	@Override
	public boolean add(T baseFile) {
		AddingObject(baseFile);
		return getInnerList().add(baseFile);
	}
	
	@Override
	public void add(int index, T element) {
		AddingObject(element);
		getInnerList().add(index, element);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean addAll(Collection c) {
		boolean stat = false;
		for(Object obj : c) {
			stat |= add((T)obj);
		}
		return stat;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean addAll(int index, Collection c) {
		List<T> objects = new LinkedList<T>();
		List<T> objectsToAdd = new LinkedList<T>();
		objects.addAll(_innerList);
		objectsToAdd.addAll(c);
		getInnerList().clear();
		for(int i = 0; objects.size() > 0 && objectsToAdd.size() > 0; i++) {
			if(i > index && objectsToAdd.size() > 0) {
				T obj = objectsToAdd.remove(0);
				AddingObject(obj);
				getInnerList().add(obj);
			} else {
				T obj = objects.remove(0);
				AddingObject(obj);
				getInnerList().add(obj);
			}
		}
		return true;
	}

	@Override
	public void clear() {
		for(T o : getInnerList()) {
			RemovingObject(o);
		}
		getInnerList().clear();
	}

	@Override
	public boolean contains(Object o) {
		return getInnerList().contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return getInnerList().containsAll(c);
	}

	@Override
	public T get(int index) {
		return getInnerList().get(index);
	}

	@Override
	public int indexOf(Object o) {
		return getInnerList().indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return getInnerList().isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return getInnerList().iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return getInnerList().lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		return getInnerList().listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return getInnerList().listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		return getInnerList().remove(o);
	}

	@Override
	public T remove(int index) {
		BaseFileObject obj = getInnerList().get(index);
		RemovingObject(obj);
		return getInnerList().remove(index);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		for(Object obj : c) {
			RemovingObject((BaseFileObject)obj);
		}
		return getInnerList().removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		List<BaseFileObject> allItems = new LinkedList<BaseFileObject>();
		for(Object obj : c) {
			allItems.remove(obj);
		}
		for(BaseFileObject obj : allItems) {
			RemovingObject(obj);
		}
		return getInnerList().retainAll(c);
	}

	@Override
	public T set(int index, T element) {
		T obj = getInnerList().set(index, element);
		RemovingObject(obj);
		AddingObject(element);
		return obj;
	}

	@Override
	public int size() {
		return getInnerList().size();
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return getInnerList().subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return getInnerList().toArray();
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] a) {
		return getInnerList().toArray(a);
	}
}
