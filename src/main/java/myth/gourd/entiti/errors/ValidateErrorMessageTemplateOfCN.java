package myth.gourd.entiti.errors;

public class ValidateErrorMessageTemplateOfCN implements ValidateErrorMessageTemplate
{
	public static final String NOT_NULL_ERROR = "{0}不能为空";
	public static final String NOT_EMPTY_ERROR = "{0}不能为空";
	public static final String STRING_SIZE_ERROR = "{0}在{1}-{2}个字符之间";
	public static final String INTEGER_SIZE_ERROR = "{0}为{1}-{2}之间整数";
	
	private static ValidateErrorMessageTemplateOfCN template = null;
	
	public static ValidateErrorMessageTemplateOfCN instance()
	{
		if (template == null)
		{
			synchronized (ValidateErrorMessageTemplateOfCN.class){
                if(template == null){
                	template = new ValidateErrorMessageTemplateOfCN();
                }
            }
		}
		return template;
	}
	
	@Override
	public String getNotNullTemplate() {
		return NOT_NULL_ERROR;
	}
	@Override
	public String getNotEmptyTemplate() {
		return NOT_EMPTY_ERROR;
	}
	@Override
	public String getStringOutOfSizeTemplate() {
		return STRING_SIZE_ERROR;
	}
	
	@Override
	public String getIntegerOutOfSizeTemplate() {
		return INTEGER_SIZE_ERROR;
	}
}