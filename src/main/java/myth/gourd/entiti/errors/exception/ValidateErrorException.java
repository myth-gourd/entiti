package myth.gourd.entiti.errors.exception;

import myth.gourd.entiti.errors.exception.visitor.ValidateErrorExceptionVisite;

public abstract class ValidateErrorException extends ErrorException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public abstract void accept(ValidateErrorExceptionVisite visitor);
	
	public ValidateErrorException(String code, String title)
	{
		super(code, "");
		this.title = title;
	}
	
	protected String title;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}