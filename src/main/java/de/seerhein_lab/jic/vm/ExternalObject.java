package de.seerhein_lab.jic.vm;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Class whose instances represent the external object that doubles as both an
 * external class instance and an external array. An external object only has
 * referring objects as defined in the superclass <code>HeapObject</code>, but
 * no referred objects, because an object that is referred by the external
 * object is itself external.
 * 
 */
public final class ExternalObject extends HeapObject {

	public ExternalObject(Heap heap) {
		super(heap);
		referredBy.add(getId());
	}

	public ExternalObject(ExternalObject external, Heap heap) {
		super(external, heap);
	}

	@Override
	public void replaceAllOccurrencesOfReferredObjectByExternal(HeapObject oldObject) {
		throw new AssertionError("must not be called.");
	}

	@Override
	protected ExternalObject copy(Heap heap) {
		return new ExternalObject(this, heap);
	}

	@Override
	protected HeapObject deepCopy(Heap heap, Map<HeapObject, HeapObject> visited) {
		return heap.getExternalObject();
	}

	@Override
	public Iterable<HeapObject> getReferredObjects() {
		return new Iterable<HeapObject>() {
			@Override
			public Iterator<HeapObject> iterator() {
				return new Iterator<HeapObject>() {
					boolean hasNext = true;

					@Override
					public boolean hasNext() {
						return hasNext;
					}

					public HeapObject next() {
						if (hasNext) {
							hasNext = false;
							return ExternalObject.this;
						}
						throw new NoSuchElementException();
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}

		};
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		return (obj instanceof ExternalObject);
	}

}
