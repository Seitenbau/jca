package de.seerhein_lab.jic.heap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import de.seerhein_lab.jic.vm.Array;
import de.seerhein_lab.jic.vm.ClassInstance;
import de.seerhein_lab.jic.vm.Heap;
import de.seerhein_lab.jic.vm.ReferenceSlot;

public class HeapTest {
	private Heap heap;
	private ClassInstance a;
	private ClassInstance b;
	private ClassInstance c;
	private ClassInstance d;
	private ClassInstance e;
	private Array f;

	private ReferenceSlot aRef;
	private ReferenceSlot bRef;
	private ReferenceSlot cRef;
	private ReferenceSlot dRef;
	private ReferenceSlot eRef;
	private ReferenceSlot fRef;

	public void setUpHeap() {
		heap = new Heap(this.getClass().getCanonicalName());

		a = heap.newClassInstance("");
		b = heap.newClassInstance("");
		c = heap.newClassInstance("");
		d = heap.newClassInstance("");
		e = heap.newClassInstance("");
		f = heap.newArray("");

		aRef = new ReferenceSlot(a);
		bRef = new ReferenceSlot(b);
		cRef = new ReferenceSlot(c);
		dRef = new ReferenceSlot(d);
		eRef = new ReferenceSlot(e);
		fRef = new ReferenceSlot(f);
	}

	@Before
	public void setUpReferences() {
		setUpHeap();

		a.setField("f", b);
		b.setField("f", c);
		b.setField("f2", d);
		c.setField("f", e);
		d.setField("f", e);
		e.setField("f", f);
		f.addReferredObject(d);
	}

	@Test
	public void testInitalization() {
		assertNotNull(heap.getExternalObject());
		assertNotNull(heap.getThisInstance());
	}

	@Test
	public void testPublishNull() {
		heap.publish(null);
	}

	@Test
	public void testPublish() {
		assertEquals(a, aRef.getObject(heap));
		assertEquals(b, bRef.getObject(heap));
		assertEquals(c, cRef.getObject(heap));
		assertEquals(d, dRef.getObject(heap));
		assertEquals(e, eRef.getObject(heap));
		assertEquals(f, fRef.getObject(heap));

		heap.publish(b);

		assertEquals(a, aRef.getObject(heap));
		assertEquals(heap.getExternalObject(), bRef.getObject(heap));
		assertEquals(heap.getExternalObject(), cRef.getObject(heap));
		assertEquals(heap.getExternalObject(), dRef.getObject(heap));
		assertEquals(heap.getExternalObject(), eRef.getObject(heap));
		assertEquals(heap.getExternalObject(), fRef.getObject(heap));
	}

	@Test
	public void testNotPublishThis() {
		ClassInstance thisInstance = heap.getThisInstance();
		heap.publish(thisInstance);
		// assertEquals(thisInstance, heap.get(thisInstance.getId()));
		assertEquals(thisInstance, heap.getThisInstance());
	}

	@Test
	public void testRepublish() {
		assertEquals(f, fRef.getObject(heap));
		heap.publish(f);
		assertEquals(heap.getExternalObject(), fRef.getObject(heap));
		heap.publish(f);
		assertEquals(heap.getExternalObject(), fRef.getObject(heap));
	}
}
