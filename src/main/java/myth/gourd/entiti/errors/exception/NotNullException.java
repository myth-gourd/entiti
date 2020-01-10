package myth.gourd.entiti.errors.exception;

import myth.gourd.entiti.errors.exception.visitor.ValidateErrorExceptionVisite;

public class NotNullException extends ValidateErrorException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotNullException(String code, String title){
		super(code, title);
	}

	@Override
	public void accept(ValidateErrorExceptionVisite visitor){
		visitor.visite(this);
	}
}