package de.htwg_konstanz.in.jca.analyzer.propConAnalyzer;

import java.util.ArrayList;
import java.util.Stack;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;

import de.htwg_konstanz.in.jca.Frame;
import de.htwg_konstanz.in.jca.Heap;
import de.htwg_konstanz.in.jca.analyzer.BaseInstructionsAnalysisVisitor;
import de.htwg_konstanz.in.jca.analyzer.BaseMethodAnalyzer;
import de.htwg_konstanz.in.jca.slot.ReferenceSlot;
import de.htwg_konstanz.in.jca.slot.Slot;
import edu.umd.cs.findbugs.ba.ClassContext;

public class PropConMethodAnalyzer extends BaseMethodAnalyzer {

	public PropConMethodAnalyzer(ClassContext classContext, MethodGen methodGen) {
		super(classContext, methodGen);
	}

	public PropConMethodAnalyzer(ClassContext classContext,
			MethodGen methodGen,
			ArrayList<AlreadyVisitedMethod> alreadyVisitedMethods, int depth) {
		super(classContext, methodGen, alreadyVisitedMethods, depth);
	}

	protected BaseInstructionsAnalysisVisitor getInstructionAnalysisVisitor(
			Frame frame, InstructionHandle instructionHandle) {
		return new PropConInstructionsAnalysisVisitor(classContext, method,
				frame, new ConstantPoolGen(method.getConstantPool()),
				instructionHandle, exceptionHandlers, alreadyVisitedMethods,
				depth);
	}

	@Override
	public void analyze() {
		Stack<Slot> callerStack = new Stack<Slot>();
		Heap callerHeap = new Heap();

		ReferenceSlot thisReference = new ReferenceSlot(callerHeap.getThisID());
		ReferenceSlot externalReference = new ReferenceSlot(
				callerHeap.getExternalID());

		// push this + args onto the stack
		callerStack.push(thisReference);

		Type[] argTypes = method.getArgumentTypes();

		for (Type argType : argTypes) {
			Slot argument = Slot.getDefaultSlotInstance(argType);
			if (argument instanceof ReferenceSlot) {
				argument = externalReference;
			}
			for (int i = 0; i < argument.getNumSlots(); i++) {
				callerStack.push(argument);
			}
		}

		Frame callerFrame = new Frame(0, callerStack, 0, callerHeap);

		analyze(callerFrame);
	}
}
