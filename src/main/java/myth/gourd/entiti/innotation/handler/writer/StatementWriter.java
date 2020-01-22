package myth.gourd.entiti.innotation.handler.writer;

import java.lang.annotation.Annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

import myth.gourd.entiti.innotation.Field;
import myth.gourd.entiti.schema.FieldStructure;
import myth.gourd.entiti.util.jctree.AccessUtil;
import myth.gourd.entiti.util.jctree.JCExpressionUtil;
import myth.gourd.entiti.util.jctree.JCTreeGloable;	

public abstract class StatementWriter {
	
	private static final Logger LOG = LoggerFactory.getLogger(StatementWriter.class);

	private static final String VALIDATOR_CLASS_PATH = "myth.gourd.entiti.errors.validate.Validator";
	
	private String code;
	
	protected FieldStructure fieldStruc;
	
	public StatementWriter(FieldStructure fieldStruc, String code)
	{
		this.fieldStruc = fieldStruc;
		this.code = code;
	}
	
	public final void write(ListBuffer<JCStatement> statements)
	{
		Class<? extends Annotation> annotationClass = getFieldValidateAnnotationClass();
		
		if (fieldStruc.hasAnnotation(annotationClass)) {
			Field fAnno = fieldStruc.getAnnotation(Field.class);
			if (fAnno == null) {
				LOG.error("must define Field annotation for" + annotationClass.getName() + " annotation, field:" + fieldStruc.getName());
			}
			if (fAnno.title() == null)
			{
				LOG.error("must set title property for Field annotation, field:" + fieldStruc.getName());
			}
			
			JCExpression method = JCExpressionUtil.jcMethodExpression(VALIDATOR_CLASS_PATH, getValidatorMethodName());
			JCExpression argValue = JCExpressionUtil.jcExpression("this." + fieldStruc.getName());
			JCExpression argCode = JCTreeGloable.TREEMAKER.Literal(code);
			JCExpression argTitle = JCTreeGloable.TREEMAKER.Literal(fAnno.title());
			
			List<JCExpression> args = args(argValue, argCode, argTitle);
			
			JCMethodInvocation methodInvo = JCTreeGloable.TREEMAKER.Apply(List.nil(), method, args);
			JCStatement statement = JCTreeGloable.TREEMAKER.Exec(methodInvo);
			statements.add(statement);
		}
	}
	
	protected List<JCExpression> args(JCExpression argValue, JCExpression argCode, JCExpression argTitle)
	{
		return List.of(argValue, argCode, argTitle);
	}
	
	protected abstract String getValidatorMethodName();
	
	protected abstract Class<? extends Annotation> getFieldValidateAnnotationClass();
}
