package de.seerhein_lab.jic.vm;

import java.util.UUID;

import de.seerhein_lab.jic.slot.Slot;

public class ReferenceSlot extends Slot {
	private final static ReferenceSlot nullReference = new ReferenceSlot();

	private final UUID objectID;

	private ReferenceSlot() {
		objectID = null;
	}

	private ReferenceSlot(HeapObject object) {
		if (object == null)
			throw new NullPointerException("argument must not be null");

		this.objectID = object.getId();
	}

	public static ReferenceSlot createNewInstance(HeapObject object) {
		if (object == null)
			return getNullReference();
		else
			return new ReferenceSlot(object);
	}

	public static ReferenceSlot getNullReference() {
		return nullReference;
	}

	public boolean isNullReference() {
		return this.equals(ReferenceSlot.getNullReference());
	}

	@Override
	public int getNumSlots() {
		return 1;
	}

	@Override
	public Slot copy() {
		return this;
	}

	/**
	 * @return the possibleObjects
	 */
	public UUID getID() {
		return objectID;
	}

	@Override
	public String toString() {
		if (this.isNullReference())
			return "NullReferenceSlot";
		return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((objectID == null) ? 0 : objectID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (!(obj instanceof ReferenceSlot))
			return false;
		ReferenceSlot other = (ReferenceSlot) obj;
		if (objectID == null) {
			if (other.objectID != null)
				return false;
		} else if (!objectID.equals(other.objectID))
			return false;
		return true;
	}
}