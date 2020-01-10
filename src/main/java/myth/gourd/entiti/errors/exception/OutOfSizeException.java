package myth.gourd.entiti.errors.exception;

import myth.gourd.entiti.errors.exception.visitor.ValidateErrorExceptionVisite;

public abstract class OutOfSizeException extends ValidateErrorException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected int min;
	
	protected int max;
	
	public OutOfSizeException(String code, String title, int min, int max) {
		super(code, title);
		this.min = min;
		this.max = max;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	@Override
	public abstract void accept(ValidateErrorExceptionVisite visitor);
}