package de.htwg_konstanz.in.jca;

import org.junit.Ignore;
import org.junit.runner.RunWith;

import de.htwg_konstanz.in.jca.testutils.ClassAnalyzerRunner;
import de.htwg_konstanz.in.jca.testutils.ClassAnalyzerRunner.BindAnalyzerMethod;
import de.htwg_konstanz.in.jca.testutils.ImproperlyConstructed;
import de.htwg_konstanz.in.jca.testutils.ProperlyConstructed;
import edu.umd.cs.findbugs.BugCollection;

/**
 * Functional acceptance tests for the method properlyConstructed of the class
 * ClassAnalyzer.
 * 
 * TODO: JavaDoc
 * 
 * @see IsImmutableTestRunner
 */
@Ignore("activate this test class when the method IsImmutable will be implemented.")
@RunWith(ClassAnalyzerRunner.class)
public class IsImmutableAcceptanceTest {

	@BindAnalyzerMethod
	public static BugCollection bindIsImmutable(ClassAnalyzer analyzer) {
		return analyzer.isImmutable();
	}

	/**
	 * Simple class without fields and with no constructor.
	 */
	@ProperlyConstructed
	public static class Story01_SimpleImmutableClass {
	}

	/**
	 * Simple class with empty constructor
	 */
	@ProperlyConstructed
	public static class Story02_SimpleImmutableClassWithConstructor {
		public Story02_SimpleImmutableClassWithConstructor() {
		}
	}

	/**
	 * Immutable class with a final field and no constructor.
	 */
	@ProperlyConstructed
	public static class Story03_ImmutableClassWithFinalField {
		final Object value = 01;
	}

	/**
	 * Class with a private field, the value of the private field could not be
	 * changed.
	 */
	@ProperlyConstructed
	public static class Story04_ImmutableClassWithPrivateField {
		@SuppressWarnings("unused")
		private String value = "01";
	}

	/**
	 * Not Immutable class because the field value could be changed.
	 */
	@ImproperlyConstructed
	public static class Story05_ClassWithNonFinalField {
		Object value = 01;
	}

	/**
	 * Not Immutable class because access to the this pointer is possible.
	 */
	@ImproperlyConstructed
	public static class Story06_ClassWithAccessToThisPointer {
		final Object thisValue;

		public Story06_ClassWithAccessToThisPointer() {
			thisValue = this;
		}
	}

	/**
	 * Immutable class because class with mutable state but the reference of to
	 * the state is not access able.
	 */
	@ImproperlyConstructed
	public static class Story07_ClassWithNoAccessToMutableState {

		public static class StringHolder {
			String value = "default";
		}

		@SuppressWarnings("unused")
		private final StringHolder value = new StringHolder();

	}

	/**
	 * Not Immutable class because the reference to the mutable state could be
	 * access.
	 */
	@ImproperlyConstructed
	public static class Story08_ClassWithAccessToMutableState {

		public static final class StringHolder {
			String value = "default";
		}

		public final StringHolder value = new StringHolder();

	}

	/**
	 * Not Immutable class because the reference to the mutable state could be
	 * access.
	 */
	@ImproperlyConstructed
	public static class Story09_ClassWithAccessByGetterToMutableState {

		public static final class StringHolder {
			String value = "default";
		}

		private final StringHolder value = new StringHolder();

		public StringHolder getValue() {
			return value;
		}

	}

}
