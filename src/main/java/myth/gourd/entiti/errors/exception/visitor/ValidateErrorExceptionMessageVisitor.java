package myth.gourd.entiti.errors.exception.visitor;

import myth.gourd.entiti.errors.ValidateErrorMessageTemplate;
import myth.gourd.entiti.errors.exception.IntegerOutOfSizeException;
import myth.gourd.entiti.errors.exception.NotEmptyException;
import myth.gourd.entiti.errors.exception.NotNullException;
import myth.gourd.entiti.errors.exception.StringOutOfSizeException;

public class ValidateErrorExceptionMessageVisitor implements ValidateErrorExceptionVisite {
	
	public ValidateErrorMessageTemplate template;
	
	public ValidateErrorExceptionMessageVisitor(ValidateErrorMessageTemplate template)
	{
		this.template = template;
	}
	
	@Override
	public void visite(NotNullException ept) {
		String message = template.getNotNullTemplate();
		message = message.replace("{0}", ept.getTitle());
		ept.setMessage(message);
	}

	@Override
	public void visite(NotEmptyException ept) {
		String message = template.getNotEmptyTemplate();
		message = message.replace("{0}", ept.getTitle());
		ept.setMessage(message);
	}

	@Override
	public void visite(StringOutOfSizeException ept) {
		String message = template.getStringOutOfSizeTemplate();
		message = message.replace("{0}", ept.getTitle());
		message = message.replace("{1}", String.valueOf(ept.getMin()));
		message = message.replace("{2}", String.valueOf(ept.getMax()));
		ept.setMessage(message);
	}

	@Override
	public void visite(IntegerOutOfSizeException ept) {
		String message = template.getIntegerOutOfSizeTemplate();
		message = message.replace("{0}", ept.getTitle());
		message = message.replace("{1}", String.valueOf(ept.getMin()));
		message = message.replace("{2}", String.valueOf(ept.getMax()));
		ept.setMessage(message);
	}
}