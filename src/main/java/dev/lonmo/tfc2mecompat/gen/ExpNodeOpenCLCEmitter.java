package dev.lonmo.tfc2mecompat.gen;

import com.ishland.c2me.opts.dfc.common.gen.opencl.OpenCLCEmitter;
import com.ishland.c2me.opts.dfc.common.gen.opencl.OpenCLCGenFunctionContext;
import com.ishland.c2me.opts.dfc.common.gen.meta.ValuesMethodDefF64;
import dev.lonmo.tfc2mecompat.ast.ExpNode;

/**
 * OpenCL C emitter for {@link ExpNode}: emits native exp() into the kernel,
 * matching dfc's generic unary emitters.
 */
public final class ExpNodeOpenCLCEmitter implements OpenCLCEmitter<ExpNode> {

	public static final ExpNodeOpenCLCEmitter INSTANCE = new ExpNodeOpenCLCEmitter();

	private ExpNodeOpenCLCEmitter() {
	}

	@Override
	public String doCLGen(ExpNode node, OpenCLCGenFunctionContext context, String storeTo) {
		StringBuilder sb = new StringBuilder();
		ValuesMethodDefF64 operand = context.newVarF64(node.operand);
		sb.append(storeTo)
				.append(" = exp(")
				.append(context.getDelegateVar(operand))
				.append(");\n");
		return sb.toString();
	}
}
