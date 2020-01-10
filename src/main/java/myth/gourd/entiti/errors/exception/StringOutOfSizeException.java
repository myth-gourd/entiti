package myth.gourd.entiti.errors.exception;

import myth.gourd.entiti.errors.exception.visitor.ValidateErrorExceptionVisite;

public class StringOutOfSizeException extends OutOfSizeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public StringOutOfSizeException(String code,String title, int min, int max) {
		super(code, title, min, max);
	}

	@Override
	public void accept(ValidateErrorExceptionVisite visitor) {
		visitor.visite(this);
	}
}