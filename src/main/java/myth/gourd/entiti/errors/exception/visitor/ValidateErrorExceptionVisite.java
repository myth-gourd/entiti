package myth.gourd.entiti.errors.exception.visitor;

import myth.gourd.entiti.errors.exception.IntegerOutOfSizeException;
import myth.gourd.entiti.errors.exception.NotEmptyException;
import myth.gourd.entiti.errors.exception.NotNullException;
import myth.gourd.entiti.errors.exception.StringOutOfSizeException;

public interface ValidateErrorExceptionVisite 
{
	void visite(NotNullException ept);
	
	void visite(NotEmptyException ept);
	
	void visite(StringOutOfSizeException ept);
	
	void visite(IntegerOutOfSizeException ept);
}