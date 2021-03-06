package de.seerhein_lab.jic.analyzer.unmod.ctor;

import java.util.HashSet;
import java.util.Set;

import net.jcip.annotations.ThreadSafe;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.MethodGen;

import de.seerhein_lab.jic.Pair;
import de.seerhein_lab.jic.analyzer.BaseMethodAnalyzer;
import de.seerhein_lab.jic.analyzer.BaseVisitor;
import de.seerhein_lab.jic.analyzer.ClassHelper;
import de.seerhein_lab.jic.analyzer.QualifiedMethod;
import de.seerhein_lab.jic.cache.AnalysisCache;
import de.seerhein_lab.jic.vm.Frame;
import de.seerhein_lab.jic.vm.Heap;
import de.seerhein_lab.jic.vm.PC;
import edu.umd.cs.findbugs.ba.ClassContext;

@ThreadSafe
// Superclass is thread-safe, this sub-class doesn't add any public methods
public final class CtorUnmodifiableAnalyzer extends BaseMethodAnalyzer {

	public CtorUnmodifiableAnalyzer(ClassContext classContext,
			MethodGen methodGen, AnalysisCache cache, int methodInvocationDepth) {
		this(classContext, methodGen, new HashSet<QualifiedMethod>(), -1,
				cache, methodInvocationDepth);
		alreadyVisitedMethods.add(new QualifiedMethod(classContext
				.getJavaClass(), methodGen.getMethod()));
	}

	protected CtorUnmodifiableAnalyzer(ClassContext classContext,
			MethodGen methodGen, Set<QualifiedMethod> alreadyVisitedMethods,
			int depth, AnalysisCache cache, int methodInvocationDepth) {
		super(classContext, methodGen, alreadyVisitedMethods, depth, cache,
				methodInvocationDepth);
	}

	protected BaseVisitor getInstructionVisitor(Frame frame, Heap heap, PC pc,
			Set<Pair<InstructionHandle, Boolean>> alreadyVisitedIfBranch) {
		return new CtorUnmodifiableVisitor(classContext, methodGen, frame,
				heap, methodGen.getConstantPool(), pc, exceptionHandlers,
				alreadyVisitedMethods, depth, alreadyVisitedIfBranch, cache,
				methodInvocationDepth);
	}

	@Override
	protected Heap getHeap() {
		String className = this.classContext.getJavaClass().getClassName();
		return new Heap(ClassHelper.isImmutable(className), className);
	}

}
