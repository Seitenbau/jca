package de.seerhein_lab.jic.vm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

/**
 * Class representing an Array. An Array has an Id, a reference to the heap
 * where its stored and a set of referring + referred objects.
 */
public final class Array extends HeapObject {
	private Set<UUID> refers = new HashSet<UUID>();

	public Array(Heap heap) {
		super(heap);
	}

	/**
	 * Copy-Constructor.
	 * 
	 * @param original
	 *            The Array to copy from.
	 * @param heap
	 *            The heap where the object is stored.
	 */
	public Array(Array original, Heap heap) {
		super(original, heap);
		refers.addAll(original.refers);
	}

	@Override
	Array copy(Heap heap) {
		return new Array(this, heap);
	}

	@Override
	public Iterator<HeapObject> getReferredIterator() {
		return new Iterator<HeapObject>() {
			Iterator<UUID> idIterator = refers.iterator();
			UUID lookAhead;
			{
				lookAhead();
			}

			private void lookAhead() {
				lookAhead = null;
				while (lookAhead == null && idIterator.hasNext()) {
					lookAhead = idIterator.next();
				}
			}

			@Override
			public boolean hasNext() {
				return lookAhead != null;
			}

			public HeapObject next() {
				HeapObject result = heap.get(lookAhead);
				lookAhead();
				return result;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	/**
	 * Replace the oldObject by the newObject.
	 */
	@Override
	public void replaceAllOccurrencesOfReferredObject(HeapObject oldObject, HeapObject newObject) {
		if (refers.remove(oldObject.getId()))
			refers.add(newObject.getId());
	}

	public void addComponent(HeapObject obj) {
		if (obj != null && refers.add(obj.getId()))
			obj.addReferringObject(this);
	}

	// @Override
	// public int hashCode() {
	// final int prime = 31;
	// int result = super.hashCode();
	// result = prime * result + ((refers == null) ? 0 : refers.hashCode());
	// return result;
	// }
	//
	// @Override
	// public boolean equals(Object obj) {
	// if (this == obj)
	// return true;
	// if (!super.equals(obj))
	// return false;
	// if (!(obj instanceof Array))
	// return false;
	// Array other = (Array) obj;
	// if (refers == null) {
	// if (other.refers != null)
	// return false;
	// } else if (!refers.equals(other.refers))
	// return false;
	// return true;
	// }

}