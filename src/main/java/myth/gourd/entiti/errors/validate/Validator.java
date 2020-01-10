package myth.gourd.entiti.errors.validate;

import myth.gourd.entiti.errors.exception.IntegerOutOfSizeException;
import myth.gourd.entiti.errors.exception.NotEmptyException;
import myth.gourd.entiti.errors.exception.NotNullException;
import myth.gourd.entiti.errors.exception.OutOfSizeException;
import myth.gourd.entiti.errors.exception.StringOutOfSizeException;

public class Validator
{
	public static void validateNotNull(Object value, String code, String title) throws NotNullException
	{
		if (value== null)
		{
			throw new NotNullException(code, title);
		}
	}
	
	public static void validateNotEmpty(Object value, String code, String title) throws NotEmptyException
	{
		if (value== null)
		{
			throw new NotEmptyException(code, title);
		}
		if (value instanceof String && "".equals(value.toString())) 
		{
			throw new NotEmptyException(code, title);
		}
	}
	
	public static void validateSize(Object value, String code, String title, int min, int max) throws OutOfSizeException
	{
		if (value instanceof String && value != null && (value.toString().length()<min || value.toString().length()>max))
		{
			throw new StringOutOfSizeException(code, title, min, max);
		}
		if (value instanceof Integer && value != null && (Integer.valueOf(value.toString()) <min ||(Integer.valueOf(value.toString())>max)))
		{
			throw new IntegerOutOfSizeException(code, title, min, max);
		}
	}
}