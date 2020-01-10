package myth.gourd.entiti.innotation.handler.writer;

import java.lang.annotation.Annotation;

import myth.gourd.entiti.innotation.ValidateNotEmpty;
import myth.gourd.entiti.schema.FieldStructure;

public class NotEmptyStatementWriter extends StatementWriter {

	public NotEmptyStatementWriter(FieldStructure fieldStruc, String code) {
		super(fieldStruc, code);
	}

	private static String VALIDATOR_METHOD_NAME = "validateNotEmpty";
	
	@Override
	protected String getValidatorMethodName() {
		return VALIDATOR_METHOD_NAME;
	}

	@Override
	protected Class<? extends Annotation> getFieldValidateAnnotationClass() {
		return ValidateNotEmpty.class;
	}
}