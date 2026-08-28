package dev.lonmo.tfc2mecompat.ast;

import com.ishland.c2me.opts.dfc.common.ast.AstNode;
import com.ishland.c2me.opts.dfc.common.ast.unary.AbstractUnaryNode;

/**
 * e^x — the missing transcendental node in dfc's AST. Needed to express
 * Twilight Forest's TanhHillFunction (which mixes tanh and exp terms)
 * in compiled density functions, including OpenCL kernels.
 */
public class ExpNode extends AbstractUnaryNode {

	public ExpNode(AstNode operand) {
		super(operand);
	}

	@Override
	protected AstNode newInstance(AstNode operand) {
		return new ExpNode(operand);
	}
}
