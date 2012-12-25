package de.htwg_konstanz.in.jca;


/**
 * JUnit test class for LocalVars.
 */
public class LocalVarsTest {
	//
	// private LocalVariable[] localVariableTableExample;
	//
	// /**
	// * Creates the LocalVariable array from the test file and stores it in
	// * localVariableTableExample. Is needed because a LocalVariableTable is
	// hard
	// * to create.
	// *
	// * @throws ClassNotFoundException
	// */
	// @Before
	// public void setUp() throws ClassNotFoundException {
	// String usedClass = ClassForLocalVariableTable.class.getName();
	// Method[] methods = Repository.lookupClass(usedClass).getMethods();
	// localVariableTableExample = methods[0].getLocalVariableTable()
	// .getLocalVariableTable();
	// }
	//
	// /**
	// * Tests the case when the LocalVariable array is null.
	// */
	// @Test(expected = IllegalArgumentException.class)
	// public void testLocalVarsNull() {
	// new LocalVars((LocalVariable[]) null);
	// }
	//
	// /**
	// * Tests the case when LocalVariable array with length 0 is passed to the
	// * constructor.
	// */
	// @Test
	// public void testLocalVarsZeroLength() {
	// LocalVars localVars = new LocalVars(new LocalVariable[0]);
	// assertEquals(0, localVars.getEntriesLength());
	// assertEquals(0, localVars.getIndexesLength());
	// }
	//
	// /**
	// * Tests the constructor with an example VariableTable array. Therefore
	// the
	// * arrays must have the right length and the values must be validated.
	// */
	// @Test
	// public void testLocalVars() {
	// LocalVars localVars = new LocalVars(localVariableTableExample);
	// // checks lengths
	// assertEquals(10, localVars.getEntriesLength());
	// assertEquals(10, localVars.getIndexesLength());
	// assertEquals(localVars.getEntriesLength(), localVars.getIndexesLength());
	// // checks entries
	// assertEquals(Slot.notThisReference, localVars.getEntry(0));
	// assertEquals(Slot.someByte, localVars.getEntry(1));
	// assertEquals(Slot.someShort, localVars.getEntry(2));
	// assertEquals(Slot.notThisReference, localVars.getEntry(3));
	// assertEquals(Slot.someInt, localVars.getEntry(4));
	// assertEquals(Slot.someLong, localVars.getEntry(5));
	// assertEquals(Slot.someFloat, localVars.getEntry(6));
	// assertEquals(Slot.someDouble, localVars.getEntry(7));
	// assertEquals(Slot.someChar, localVars.getEntry(8));
	// assertEquals(Slot.someBoolean, localVars.getEntry(9));
	// // checks indexes
	// assertEquals(0, localVars.getIndex(0));
	// assertEquals(1, localVars.getIndex(1));
	// assertEquals(2, localVars.getIndex(2));
	// assertEquals(3, localVars.getIndex(3));
	// assertEquals(4, localVars.getIndex(4));
	// assertEquals(5, localVars.getIndex(5));
	// assertEquals(7, localVars.getIndex(6));
	// assertEquals(8, localVars.getIndex(7));
	// assertEquals(10, localVars.getIndex(8));
	// assertEquals(11, localVars.getIndex(9));
	// }
	//
	// /**
	// * Tests initWithArgs() on LocalVars with zero length entries array and
	// * filled callerStack.
	// */
	// @Test
	// public void testInitWithArgsOnZeroLengthEntries() {
	// LocalVars localVars = new LocalVars(new LocalVariable[0]);
	// Stack<Slot> callerStack = new Stack<Slot>();
	// callerStack.add(Slot.notThisReference);
	// localVars.initWithArgs(callerStack, 1);
	// assertEquals(true, callerStack.isEmpty());
	// }
	//
	// /**
	// * Tests case when more items should be popped than the stack contains.
	// */
	// @Test(expected = EmptyStackException.class)
	// public void testInitWithArgsInvalidArgs() {
	// // creates example LocalVars with 10 entries
	// LocalVars localVars = new LocalVars(localVariableTableExample);
	// Stack<Slot> callerStack = new Stack<Slot>();
	// callerStack.add(Slot.someFloat);
	// // only 1 entry on the stack but trying to pop 2
	// localVars.initWithArgs(callerStack, 2);
	// }
	//
	// /**
	// * Tests if LocalVars does not change when argsNum is 0.
	// */
	// @Test
	// public void testInitWithArgsNoChange() {
	// // creates example LocalVars with 10 entries
	// LocalVars localVars = new LocalVars(localVariableTableExample);
	// Stack<Slot> callerStack = new Stack<Slot>();
	// callerStack.add(Slot.someFloat);
	// localVars.initWithArgs(callerStack, 0);
	// assertEquals(true, !callerStack.isEmpty());
	// assertEquals(Slot.notThisReference, localVars.getEntry(0));
	// assertEquals(Slot.someByte, localVars.getEntry(1));
	// assertEquals(Slot.someShort, localVars.getEntry(2));
	// assertEquals(Slot.notThisReference, localVars.getEntry(3));
	// assertEquals(Slot.someInt, localVars.getEntry(4));
	// assertEquals(Slot.someLong, localVars.getEntry(5));
	// assertEquals(Slot.someFloat, localVars.getEntry(6));
	// assertEquals(Slot.someDouble, localVars.getEntry(7));
	// assertEquals(Slot.someChar, localVars.getEntry(8));
	// assertEquals(Slot.someBoolean, localVars.getEntry(9));
	// }
	//
	// /**
	// * Tests if half of the array can be overwritten.
	// */
	// @Test
	// public void testInitWhithArgsHalfStack() {
	// // creates example LocalVars with 10 entries
	// LocalVars localVars = new LocalVars(localVariableTableExample);
	// Stack<Slot> callerStack = new Stack<Slot>();
	// for (int i = 0; i < 5; i++) {
	// callerStack.add(Slot.thisReference);
	// }
	// localVars.initWithArgs(callerStack, 5);
	// for (int i = 0; i < 5; i++) {
	// assertEquals(Slot.thisReference, localVars.getEntry(i));
	// }
	// assertEquals(Slot.someLong, localVars.getEntry(5));
	// assertEquals(Slot.someFloat, localVars.getEntry(6));
	// assertEquals(Slot.someDouble, localVars.getEntry(7));
	// assertEquals(Slot.someChar, localVars.getEntry(8));
	// assertEquals(Slot.someBoolean, localVars.getEntry(9));
	// }
	//
	// /**
	// * Tests if complete entries can be overwritten.
	// */
	// @Test
	// public void testInitWhithArgsCompleteStack() {
	// // creates example LocalVars with 10 entries
	// LocalVars localVars = new LocalVars(localVariableTableExample);
	// Stack<Slot> callerStack = new Stack<Slot>();
	// for (int i = 0; i < localVars.getEntriesLength(); i++) {
	// callerStack.add(Slot.thisReference);
	// }
	// localVars.initWithArgs(callerStack, callerStack.size());
	// for (int i = 0; i < localVars.getEntriesLength(); i++) {
	// assertEquals(Slot.thisReference, localVars.getEntry(i));
	// }
	// }
	//
	// /**
	// * Tests if an exception is thrown when argsNum is greater than
	// * entries.length.
	// */
	// @Test(expected = ArrayIndexOutOfBoundsException.class)
	// public void testInitWithArgsTooGreatArgNum() {
	// // creates example LocalVars with 10 entries
	// LocalVars localVars = new LocalVars(localVariableTableExample);
	// Stack<Slot> callerStack = new Stack<Slot>();
	// for (int i = 0; i < localVars.getEntriesLength() + 1; i++) {
	// callerStack.add(Slot.thisReference);
	// }
	// localVars.initWithArgs(callerStack, callerStack.size());
	// }
	//
	// /**
	// * Tests if getForIndex() throws AssertionError when index is out of
	// bounds.
	// */
	// @Test(expected = AssertionError.class)
	// public void testGetForIndexOutOfBounds() {
	// // creates example LocalVars with 10 entries
	// LocalVars localVars = new LocalVars(localVariableTableExample);
	// localVars.getForIndex(-1);
	// }
	//
	// /**
	// * Tests if getForIndex() throws AssertionError when index is within the
	// * bounds but invalid.
	// */
	// @Test(expected = AssertionError.class)
	// public void testGetForIndexWithinBoundsError() {
	// // creates example LocalVars with 10 entries
	// LocalVars localVars = new LocalVars(localVariableTableExample);
	// // tries to access index 6 (the long starts at 5)
	// localVars.getForIndex(6);
	// }
	//
	// /**
	// * Tests if getForIndex() returns the right entries.
	// */
	// @Test
	// public void testGetForIndex() {
	// // creates example LocalVars with 10 entries
	// LocalVars localVars = new LocalVars(localVariableTableExample);
	// assertEquals(Slot.notThisReference, localVars.getForIndex(0));
	// assertEquals(Slot.someByte, localVars.getForIndex(1));
	// assertEquals(Slot.someShort, localVars.getForIndex(2));
	// assertEquals(Slot.notThisReference, localVars.getForIndex(3));
	// assertEquals(Slot.someInt, localVars.getForIndex(4));
	// assertEquals(Slot.someLong, localVars.getForIndex(5));
	// assertEquals(Slot.someFloat, localVars.getForIndex(7));
	// assertEquals(Slot.someDouble, localVars.getForIndex(8));
	// assertEquals(Slot.someChar, localVars.getForIndex(10));
	// assertEquals(Slot.someBoolean, localVars.getForIndex(11));
	// }
	//
	// /**
	// * Tests if setForIndex() throws AssertionError when index is out of
	// bounds.
	// */
	// @Test(expected = AssertionError.class)
	// public void testSetForIndexOutOfBoundsError() {
	// // creates example LocalVars with 10 entries
	// LocalVars localVars = new LocalVars(localVariableTableExample);
	// localVars.setForIndex(-1, Slot.notThisReference);
	// }
	//
	// /**
	// * Tests if setForIndex() throws AssertionError when index is within
	// bounds.
	// */
	// @Test(expected = AssertionError.class)
	// public void testSetForIndexWithinBoundsError() {
	// // creates example LocalVars with 10 entries
	// LocalVars localVars = new LocalVars(localVariableTableExample);
	// // tries to access index 9 (the double starts at 8)
	// localVars.setForIndex(9, Slot.notThisReference);
	// }
	//
	// /**
	// * Tests if setForIndex() sets the right Entries.
	// */
	// @Test
	// public void testSetForIndex() {
	// // creates example LocalVars with 10 entries
	// LocalVars localVars = new LocalVars(localVariableTableExample);
	// localVars.setForIndex(0, Slot.someBoolean);
	// localVars.setForIndex(1, Slot.someChar);
	// localVars.setForIndex(2, Slot.someDouble);
	// localVars.setForIndex(3, Slot.someFloat);
	// localVars.setForIndex(4, Slot.someLong);
	// localVars.setForIndex(5, Slot.someInt);
	// localVars.setForIndex(7, Slot.notThisReference);
	// localVars.setForIndex(8, Slot.someShort);
	// localVars.setForIndex(10, Slot.someByte);
	// localVars.setForIndex(11, Slot.thisReference);
	// assertEquals(Slot.someBoolean, localVars.getEntry(0));
	// assertEquals(Slot.someChar, localVars.getEntry(1));
	// assertEquals(Slot.someDouble, localVars.getEntry(2));
	// assertEquals(Slot.someFloat, localVars.getEntry(3));
	// assertEquals(Slot.someLong, localVars.getEntry(4));
	// assertEquals(Slot.someInt, localVars.getEntry(5));
	// assertEquals(Slot.notThisReference, localVars.getEntry(6));
	// assertEquals(Slot.someShort, localVars.getEntry(7));
	// assertEquals(Slot.someByte, localVars.getEntry(8));
	// assertEquals(Slot.thisReference, localVars.getEntry(9));
	// }

}