package myth.gourd.entiti.errors.exception;

import myth.gourd.entiti.errors.exception.visitor.ValidateErrorExceptionVisite;

public class NotEmptyException extends ValidateErrorException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotEmptyException(String code, String title){
		super(code, title);
	}
	
	@Override
	public void accept(ValidateErrorExceptionVisite visitor){
		visitor.visite(this);
	}
}
