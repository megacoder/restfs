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
public abstract class RestfulFolder<T extends BaseFileObject> extends BaseFileObject implements List<T> {
	private final List<T> _innerList;
	
	protected RestfulFolder() {
		_innerList = new LinkedList<T>();
	}
	
	
	protected abstract void AddingObject(BaseFileObject baseFile);
	protected abstract void RemovingObject(BaseFileObject baseFile);
	
	@Override
	public boolean add(T baseFile) {
		AddingObject(baseFile);
		return _innerList.add(baseFile);
	}
	
	@Override
	public void add(int index, T element) {
		AddingObject(element);
		_innerList.add(index, element);
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
		_innerList.clear();
		for(int i = 0; objects.size() > 0 && objectsToAdd.size() > 0; i++) {
			if(i > index && objectsToAdd.size() > 0) {
				T obj = objectsToAdd.remove(0);
				AddingObject(obj);
				_innerList.add(obj);
			} else {
				T obj = objects.remove(0);
				AddingObject(obj);
				_innerList.add(obj);
			}
		}
		return true;
	}

	@Override
	public void clear() {
		for(T o : _innerList) {
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
	public T get(int index) {
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
	public Iterator<T> iterator() {
		return _innerList.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return _innerList.lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		return _innerList.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return _innerList.listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		return _innerList.remove(o);
	}

	@Override
	public T remove(int index) {
		BaseFileObject obj = _innerList.get(index);
		RemovingObject(obj);
		return _innerList.remove(index);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		for(Object obj : c) {
			RemovingObject((BaseFileObject)obj);
		}
		return _innerList.removeAll(c);
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
		return _innerList.retainAll(c);
	}

	@Override
	public T set(int index, T element) {
		T obj = _innerList.set(index, element);
		RemovingObject(obj);
		AddingObject(element);
		return obj;
	}

	@Override
	public int size() {
		return _innerList.size();
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return _innerList.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return _innerList.toArray();
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] a) {
		return _innerList.toArray(a);
	}
}
