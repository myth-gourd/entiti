package myth.gourd.entiti.innotation.handler.writer;

import java.lang.annotation.Annotation;

import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.util.List;

import myth.gourd.entiti.innotation.ValidateSize;
import myth.gourd.entiti.schema.FieldStructure;
import myth.gourd.entiti.util.jctree.JCTreeGloable;

public class OutOfSizeStatementWriter extends StatementWriter {

	public OutOfSizeStatementWriter(FieldStructure fieldStruc, String code) {
		super(fieldStruc, code);
	}

	private static final String VALIDATOR_METHOD_NAME = "validateSize";
	
	
	@Override
	protected final String getValidatorMethodName() {
		return VALIDATOR_METHOD_NAME;
	}

	@Override
	protected final Class<? extends Annotation> getFieldValidateAnnotationClass() {
		return ValidateSize.class;
	}
	
	@Override
	protected final List<JCExpression> args(JCExpression argValue, JCExpression argCode, JCExpression argTitle)
	{
		ValidateSize validateAnnoration = this.fieldStruc.getAnnotation(ValidateSize.class);
		int min = validateAnnoration.min();
		int max = validateAnnoration.max();
		JCExpression minArg = JCTreeGloable.TREEMAKER.Literal(min);
		JCExpression maxArg = JCTreeGloable.TREEMAKER.Literal(max);
		List<JCExpression> args = List.of(argValue, argCode, argTitle, minArg, maxArg);
		return args;
	}
}