package de.seerhein_lab.jca;

import org.junit.runner.RunWith;

import de.seerhein_lab.jca.analyzer.ClassAnalyzer;
import de.seerhein_lab.jca.testutils.BugsExpected;
import de.seerhein_lab.jca.testutils.ClassAnalyzerRunner;
import de.seerhein_lab.jca.testutils.ClassAnalyzerRunner.BindAnalyzerMethod;
import de.seerhein_lab.jca.testutils.NoBugsExpected;
import edu.umd.cs.findbugs.BugCollection;

/**
 * Functional acceptance tests for the method properlyConstructed of the class
 * ClassAnalyzer.
 * 
 * @see ProperlyConstructedTestRunner
 */
@RunWith(ClassAnalyzerRunner.class)
public class fieldsNotModifiedAcceptanceTest {
	@BindAnalyzerMethod
	public static BugCollection bindClassAnalyzerToProperlyConstructed(
			ClassAnalyzer analyzer) {
		analyzer.ctorParamsAreCopied();
		return analyzer.stateUnmodified();
	}

	@BugsExpected
	public static class DetectXAStoreBug_Int {
		private final int[] f = new int[5];

		public void modifie(int i) {
			f[0] = i;
		}
	}

	@BugsExpected
	public static class DetectXAStoreBug_ReferenceToArrayReferredByThis {
		private final Object[] f = new Object[5];

		public void modifie(Object i) {
			f[0] = i;
		}
	}

	@NoBugsExpected
	public static class DetectXAStoreBug_ReferenceToInternalArray {
		public void modifie() {
			Object[] array = new Object[1];
			array[0] = new Object();
		}
	}

	@BugsExpected
	public static class DetectXAStoreBug_ReferenceToArrayTransitivelyReferredByThis {
		private final TestClass[] f = new TestClass[5];

		public DetectXAStoreBug_ReferenceToArrayTransitivelyReferredByThis() {
			f[0] = new TestClass();
		}

		public void modifie(int i) {
			f[0].i = 5;
		}
	}

	@BugsExpected
	public static class DetectXAStoreBug_ReferenceToArrayTransitivelyReferredByThis2 {
		private final TestClass[] f = new TestClass[5];

		public DetectXAStoreBug_ReferenceToArrayTransitivelyReferredByThis2() {
			f[0] = new TestClass();
		}

		public void modifie(int i) {
			f[0].klass = new Object();
		}
	}

	@BugsExpected
	public static class DetectPutFieldBug_ReferenceToObjectReferredByThis {
		private final Object[] f = new Object[5];

		public void modifie(Object i) {
			f[0] = i;
		}
	}

	@NoBugsExpected
	public static class DetectPutFieldBug_ReferenceToInternalObject {
		public void modifie() {
			TestClass tc = new TestClass();
			tc.klass = new Object();
		}
	}

	@BugsExpected
	public static class DetectPutFieldBug_ReferenceToObjectTransitivelyReferredByThis {
		private final TestClass f = new TestClass();

		public DetectPutFieldBug_ReferenceToObjectTransitivelyReferredByThis() {
			f.tc = new TestClass();
		}

		public void modifie(int i) {
			f.tc.i = 5;
		}
	}

	@BugsExpected
	public static class DetectPutFieldBug_ReferenceToObjectTransitivelyReferredByThis2 {
		private final TestClass f = new TestClass();

		public DetectPutFieldBug_ReferenceToObjectTransitivelyReferredByThis2() {
			f.tc = new TestClass();
		}

		public void modifie(int i) {
			f.tc.klass = new Object();
		}
	}

	private static class TestClass {
		public Object[] array;
		public Object klass;
		public TestClass tc;
		public int i;
	}

	private static class TestClassStatic {
		public static Object[] array;
		public static Object klass;
		public static TestClass tc;
		public static int i;
	}
}