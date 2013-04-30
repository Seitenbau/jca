package de.seerhein_lab.jca.analyzer.propConAnalyzer;

import java.util.Set;
import java.util.UUID;

import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.CodeExceptionGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.MethodGen;

import de.seerhein_lab.jca.Frame;
import de.seerhein_lab.jca.analyzer.BaseInstructionsAnalysisVisitor;
import de.seerhein_lab.jca.analyzer.BaseMethodAnalyzer;
import de.seerhein_lab.jca.analyzer.BaseMethodAnalyzer.AlreadyVisitedMethod;
import de.seerhein_lab.jca.heap.HeapObject;
import de.seerhein_lab.jca.slot.ReferenceSlot;
import edu.umd.cs.findbugs.annotations.Confidence;
import edu.umd.cs.findbugs.ba.ClassContext;

public class PropConInstructionsAnalysisVisitor extends
		BaseInstructionsAnalysisVisitor {

	protected PropConInstructionsAnalysisVisitor(ClassContext classContext,
			Method method, Frame frame, ConstantPoolGen constantPoolGen,
			Set<AlreadyVisitedIfInstruction> alreadyVisited,
			Set<AlreadyVisitedMethod> alreadyVisitedMethods,
			InstructionHandle instructionHandle,
			CodeExceptionGen[] exceptionHandlers, int depth) {
		super(classContext, method, frame, constantPoolGen, alreadyVisited,
				alreadyVisitedMethods, instructionHandle, exceptionHandlers,
				depth);
	}

	public PropConInstructionsAnalysisVisitor(ClassContext classContext,
			Method method, Frame frame, ConstantPoolGen constantPoolGen,
			InstructionHandle instructionHandle,
			CodeExceptionGen[] exceptionHandlers,
			Set<AlreadyVisitedMethod> alreadyVisitedMethods, int depth) {
		super(classContext, method, frame, constantPoolGen, instructionHandle,
				exceptionHandlers, alreadyVisitedMethods, depth);
	}

	@Override
	protected BaseInstructionsAnalysisVisitor getInstructionsAnalysisVisitor(
			Frame frame, Set<AlreadyVisitedIfInstruction> alreadyVisited,
			InstructionHandle instructionHandle) {
		return new PropConInstructionsAnalysisVisitor(classContext, method,
				frame, constantPoolGen, alreadyVisited, alreadyVisitedMethods,
				instructionHandle, exceptionHandlers, depth);
	}

	@Override
	protected BaseMethodAnalyzer getMethodAnalyzer(MethodGen targetMethodGen) {
		return new PropConMethodAnalyzer(classContext, targetMethodGen,
				alreadyVisitedMethods, depth);
	}

	@Override
	protected String getBugType() {
		return "PROPER_CONSTRUCTION_BUG";
	}

	// ******************************************************************//
	// Bug detection section //
	// ******************************************************************//

	@Override
	protected void detectVirtualMethodBug(ReferenceSlot argument) {
		if (argument.getID().equals(frame.getHeap().getThisID())) {
			// this is passed to a method that can not be analyzed
			addBug(Confidence.HIGH,
					"this reference is passed to a method that can not be analyzed by static analyzis and escapes",
					instructionHandle);
		}
		if (refersThis(frame.getHeap().get(argument.getID()))) {
			// the reference contains this and it might be published
			addBug(Confidence.HIGH,
					"a reference containing this is passed to a method that can not be analyzed by static analyzis and this might escape",
					instructionHandle);
		}
	}

	boolean refersThis(HeapObject obj) {
		for (UUID refered : obj.getReferredObjects()) {
			if (refered.equals(frame.getHeap().getThisID())) {
				return true;
			}
			return refersThis(frame.getHeap().get(refered));
		}
		return false;
	}

	@Override
	protected void detectAAStoreBug(ReferenceSlot arrayReference,
			ReferenceSlot referenceToStore) {
		if (arrayReference.getID().equals(frame.getHeap().getExternalID())) {
			// the array is externally known
			if (referenceToStore.equals(referenceToStore.getID().equals(
					frame.getHeap().getThisID()))) {
				// this is assigned to the array
				addBug(Confidence.HIGH,
						"this reference is assigned to an external array and escapes",
						instructionHandle);
			}
			if (refersThis(frame.getHeap().get(referenceToStore.getID()))) {
				// a reference containing this is assigned to the array
				addBug(Confidence.HIGH,
						"a reference containing this is assigned to an external array and this escapes",
						instructionHandle);
			}
		}
	}

	@Override
	protected void detectPutFieldBug(ReferenceSlot targetReference,
			ReferenceSlot referenceToPut) {
		if (targetReference.getID().equals(frame.getHeap().getExternalID())) {
			// the left side of the assignment is externally known
			if (referenceToPut.getID().equals(frame.getHeap().getThisID())) {
				// this is on the right side
				addBug(Confidence.HIGH,
						"this reference is assigned to an external field and escapes",
						instructionHandle);
			}
			if (refersThis(frame.getHeap().get(referenceToPut.getID()))) {
				// this is contained in the right side
				addBug(Confidence.HIGH,
						"a reference containing this is assigned to an external field and this escapes",
						instructionHandle);
			}
		}
	}

	@Override
	protected void detectPutStaticBug(ReferenceSlot referenceToPut) {
		if (referenceToPut.getID().equals(frame.getHeap().getThisID())) {
			addBug(Confidence.HIGH,
					"this reference is assigned to a static field and escapes",
					instructionHandle);
		}
		if (refersThis(frame.getHeap().get(referenceToPut.getID()))) {
			// the reference contains this
			addBug(Confidence.HIGH,
					"a reference containing this is assigned to a static field and this escapes",
					instructionHandle);

		}
	}

}
