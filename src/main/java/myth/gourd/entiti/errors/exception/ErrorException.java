package myth.gourd.entiti.errors.exception;

import myth.gourd.entiti.errors.Error;

public class ErrorException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected String code;
	
	protected String message;
	
	public ErrorException(String code, String message)
	{
		this.code = code;
		this.message = message;
	}
	
	public ErrorException(Error error)
	{
		this.setCode(error.getCode());
		this.setMessage(error.getMessage());
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}