package de.seerhein_lab.jca.analyzer.propCon;

import java.util.Set;

import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.CodeExceptionGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.MethodGen;

import de.seerhein_lab.jca.Frame;
import de.seerhein_lab.jca.Pair;
import de.seerhein_lab.jca.analyzer.BaseInstructionsAnalysisVisitor;
import de.seerhein_lab.jca.analyzer.BaseMethodAnalyzer;
import de.seerhein_lab.jca.heap.Heap;
import de.seerhein_lab.jca.heap.ExternalObject;
import de.seerhein_lab.jca.slot.ReferenceSlot;
import de.seerhein_lab.jca.slot.Slot;
import edu.umd.cs.findbugs.annotations.Confidence;
import edu.umd.cs.findbugs.ba.ClassContext;

public class PropConInstructionsAnalysisVisitor extends
		BaseInstructionsAnalysisVisitor {

	protected PropConInstructionsAnalysisVisitor(ClassContext classContext,
			Method method, Frame frame, ConstantPoolGen constantPoolGen,
			Set<Pair<InstructionHandle, Boolean>> alreadyVisitedIfBranch,
			Set<Pair<Method, Slot[]>> alreadyVisitedMethods,
			InstructionHandle instructionHandle,
			CodeExceptionGen[] exceptionHandlers, int depth) {
		super(classContext, method, frame, constantPoolGen,
				alreadyVisitedIfBranch, alreadyVisitedMethods,
				instructionHandle, exceptionHandlers, depth);
	}

	public PropConInstructionsAnalysisVisitor(ClassContext classContext,
			Method method, Frame frame, ConstantPoolGen constantPoolGen,
			InstructionHandle instructionHandle,
			CodeExceptionGen[] exceptionHandlers,
			Set<Pair<Method, Slot[]>> alreadyVisitedMethods, int depth) {
		super(classContext, method, frame, constantPoolGen, instructionHandle,
				exceptionHandlers, alreadyVisitedMethods, depth);
	}

	@Override
	protected BaseInstructionsAnalysisVisitor getInstructionsAnalysisVisitor(
			Frame frame,
			Set<Pair<InstructionHandle, Boolean>> alreadyVisitedIfBranch,
			InstructionHandle instructionHandle) {
		return new PropConInstructionsAnalysisVisitor(classContext, method,
				frame, constantPoolGen, alreadyVisitedIfBranch,
				alreadyVisitedMethods, instructionHandle, exceptionHandlers,
				depth);
	}

	@Override
	protected BaseMethodAnalyzer getMethodAnalyzer(MethodGen targetMethodGen) {
		return new PropConMethodAnalyzer(classContext, targetMethodGen,
				alreadyVisitedMethods, depth);
	}

	// ******************************************************************//
	// Bug detection section //
	// ******************************************************************//

	@Override
	protected void detectVirtualMethodBug(ReferenceSlot argument) {
		Heap heap = frame.getHeap();
		if (argument.getID().equals(heap.getThisInstance().getId())) {
			// 'this' is passed into a virtual method
			addBug(Confidence.HIGH,
					"'this' is passed into a virtual method and escapes",
					instructionHandle);
		} else if (heap.get(argument.getID()).transitivelyRefers(
				heap.getThisInstance())) {
			// argument that refers to 'this' is passed into a virtual method
			addBug(Confidence.HIGH,
					"a reference that refers to 'this' is passed into a virtual method letting 'this' escape",
					instructionHandle);
		}
	}

	@Override
	protected void detectXAStoreBug(ReferenceSlot arrayReference,
			Slot valueToStore) {
		if (!(valueToStore instanceof ReferenceSlot)) {
			// if the value is not a reference we do not analyze
			return;
		}

		ReferenceSlot referenceToStore = (ReferenceSlot) valueToStore;
		Heap heap = frame.getHeap();
		if (heap.getObject(arrayReference) instanceof ExternalObject) {
			// the array is externally known
			if (referenceToStore.getID() != null && referenceToStore.getID().equals(heap.getThisInstance().getId())) {
				// this is assigned to the array
				addBug(Confidence.HIGH,
						"'this' is assigned to an external array and escapes",
						instructionHandle);
			} else if (arrayReference.getID() != null && heap.get(arrayReference.getID()).transitivelyRefers(
					heap.getThisInstance())) {
				// a reference containing this is assigned to the array
				addBug(Confidence.HIGH,
						"a reference containing 'this' is assigned to an external array and 'this' escapes",
						instructionHandle);
			}
		}
	}

	@Override
	protected void detectPutFieldBug(ReferenceSlot targetReference,
			Slot valueToPut) {
		if (!(valueToPut instanceof ReferenceSlot)) {
			// if the value is not a reference we do not analyze
			return;
		}
		ReferenceSlot referenceToPut = (ReferenceSlot) valueToPut;
		Heap heap = frame.getHeap();
		if (heap.getObject(targetReference) instanceof ExternalObject ) {
			// the left side of the assignment is externally known
			if (referenceToPut.getID() != null && referenceToPut.getID().equals(heap.getThisInstance().getId())) {
				// this is on the right side
				addBug(Confidence.HIGH,
						"'this' is assigned to an external field and escapes",
						instructionHandle);
			} else if (referenceToPut.getID() != null && heap.get(referenceToPut.getID()).transitivelyRefers(
					heap.getThisInstance())) {
				// this is contained in the right side
				addBug(Confidence.HIGH,
						"a reference containing 'this' is assigned to an external field and 'this' escapes",
						instructionHandle);
			}

		}
	}

	@Override
	protected void detectPutStaticBug(ReferenceSlot referenceToPut) {
		Heap heap = frame.getHeap();
		if (referenceToPut.getID().equals(heap.getThisInstance().getId())) {
			addBug(Confidence.HIGH,
					"'this' is assigned to a static field and escapes",
					instructionHandle);
		} else if (heap.get(referenceToPut.getID()).transitivelyRefers(
				heap.getThisInstance())) {
			// the reference contains this
			addBug(Confidence.HIGH,
					"a reference containing 'this' is assigned to a static field and 'this' escapes",
					instructionHandle);
		}
	}
}
