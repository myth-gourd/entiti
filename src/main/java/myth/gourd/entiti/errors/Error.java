package myth.gourd.entiti.errors;

import myth.gourd.entiti.errors.exception.ErrorException;

public class Error {
	
	private String code;
	
	private String message;

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
	
	public void throwErrorException() throws ErrorException
	{
		throw new ErrorException(this);
	}
}