package de.seerhein_lab.jca.heap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class HeapTest {
	private Heap heap;
	private ClassInstance a;
	private ClassInstance b;
	private ClassInstance c;
	private ClassInstance d;
	private ClassInstance e;
	private Array f;

	@Before
	public void setUpReferences() {
		setUpHeap();

		a.setField("f", b);
		b.setField("f", c);
		b.setField("f2", d);
		c.setField("f", e);
		d.setField("f", e);
		e.setField("f", f);
		f.addComponent(d);
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
		assertEquals(a, heap.get(a.getId()));
		assertEquals(b, heap.get(b.getId()));
		assertEquals(c, heap.get(c.getId()));
		assertEquals(d, heap.get(d.getId()));
		assertEquals(e, heap.get(e.getId()));
		assertEquals(f, heap.get(f.getId()));

		heap.publish(b);

		assertEquals(a, heap.get(a.getId()));
		assertEquals(heap.getExternalObject(), heap.get(b.getId()));
		assertEquals(heap.getExternalObject(), heap.get(c.getId()));
		assertEquals(heap.getExternalObject(), heap.get(d.getId()));
		assertEquals(heap.getExternalObject(), heap.get(e.getId()));
		assertEquals(heap.getExternalObject(), heap.get(f.getId()));
	}

	@Test
	public void testLinkObjects() {

		assertTrue(a.transitivelyRefers(b));
		assertTrue(b.transitivelyRefers(c));
		assertTrue(b.transitivelyRefers(d));
		assertTrue(c.transitivelyRefers(e));
		assertTrue(d.transitivelyRefers(e));
		assertTrue(e.transitivelyRefers(f));
		assertTrue(f.transitivelyRefers(d));

		assertTrue(b.isTransitivelyReferredBy(a));
		assertTrue(c.isTransitivelyReferredBy(b));
		assertTrue(d.isTransitivelyReferredBy(b));
		assertTrue(e.isTransitivelyReferredBy(c));
		assertTrue(e.isTransitivelyReferredBy(d));
		assertTrue(f.isTransitivelyReferredBy(e));
		assertTrue(d.isTransitivelyReferredBy(f));
	}

	@Test
	public void testLinkObjectsReplaceField() {
		assertTrue(a.transitivelyRefers(b));
		a.setField("f", e);
		assertTrue(a.transitivelyRefers(e));
		assertFalse(a.transitivelyRefers(b));
	}

	@Test
	public void testLinkObjectsAddReferingField() {
		assertTrue(a.transitivelyRefers(b));
		a.setField("f2", e);
		assertTrue(a.transitivelyRefers(e));
		assertTrue(a.transitivelyRefers(b));
	}

	@Test
	public void testNotPublishThis() {
		ClassInstance thisInstance = heap.getThisInstance();
		heap.publish(thisInstance);
		assertEquals(thisInstance, heap.get(thisInstance.getId()));
	}

	@Test
	public void testRepublish() {
		assertEquals(f, heap.get(f.getId()));
		heap.publish(heap.get(f.getId()));
		assertEquals(heap.getExternalObject(), heap.get(f.getId()));
		heap.publish(heap.get(f.getId()));
		assertEquals(heap.getExternalObject(), heap.get(f.getId()));
	}

	@Test
	public void testGetReferredIterator() {
		setUpHeap();

		a.setField("f0", b);
		a.setField("f1", null);
		a.setField("f2", null);
		a.setField("f3", null);
		a.setField("f4", c);
		a.setField("f5", d);

		HashSet<HeapObject> referredObjects = new HashSet<HeapObject>();
		referredObjects.add(b);
		referredObjects.add(c);
		referredObjects.add(d);

		checkReferredIterator(referredObjects);

	}

	@Test
	public void testGetReferredIterator2() {
		setUpHeap();

		a.setField("f0", null);
		a.setField("f1", null);
		a.setField("f2", null);
		a.setField("f3", null);
		a.setField("f4", null);
		a.setField("f5", null);

		HashSet<HeapObject> referredObjects = new HashSet<HeapObject>();

		checkReferredIterator(referredObjects);

	}

	@Test
	public void testGetReferredIterator3() {
		setUpHeap();

		a.setField("f0", null);
		a.setField("f1", null);
		a.setField("f2", null);
		a.setField("f3", null);
		a.setField("f4", null);
		a.setField("f5", b);

		HashSet<HeapObject> referredObjects = new HashSet<HeapObject>();
		referredObjects.add(b);

		checkReferredIterator(referredObjects);

	}

	@Test
	public void testGetReferredIterator4() {
		setUpHeap();

		a.setField("f0", b);
		a.setField("f1", null);
		a.setField("f2", null);
		a.setField("f3", null);
		a.setField("f4", null);
		a.setField("f5", null);

		HashSet<HeapObject> referredObjects = new HashSet<HeapObject>();
		referredObjects.add(b);

		checkReferredIterator(referredObjects);

	}

	public void checkReferredIterator(HashSet<HeapObject> referredObjects) {

		Iterator<HeapObject> referredIterator = a.getReferredIterator();

		while (referredIterator.hasNext()) {
			assertTrue(referredObjects.contains(referredIterator.next()));
		}

		assertFalse(referredIterator.hasNext());
	}

	public void setUpHeap() {
		heap = new Heap();

		a = heap.newClassInstance();
		b = heap.newClassInstance();
		c = heap.newClassInstance();
		d = heap.newClassInstance();
		e = heap.newClassInstance();
		f = heap.newArray();
	}
}
