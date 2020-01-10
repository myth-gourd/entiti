package myth.gourd.entiti.innotation.handler.writer;

import java.lang.annotation.Annotation;

import myth.gourd.entiti.innotation.ValidateNotNull;
import myth.gourd.entiti.schema.FieldStructure;

public class NotNullStatementWriter extends StatementWriter {

	public NotNullStatementWriter(FieldStructure fieldStruc, String code) {
		super(fieldStruc, code);
	}

	private static final String VALIDATOR_METHOD_NAME = "validateNotNull";
	
	@Override
	protected String getValidatorMethodName() {
		return VALIDATOR_METHOD_NAME;
	}

	@Override
	protected Class<? extends Annotation> getFieldValidateAnnotationClass() {
		return ValidateNotNull.class;
	}
}