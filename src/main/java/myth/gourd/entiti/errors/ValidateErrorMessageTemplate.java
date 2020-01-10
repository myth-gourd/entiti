package myth.gourd.entiti.errors;

public interface ValidateErrorMessageTemplate 
{
	public String getNotNullTemplate();

	public String getNotEmptyTemplate();

	public String getStringOutOfSizeTemplate();

	public String getIntegerOutOfSizeTemplate();
}