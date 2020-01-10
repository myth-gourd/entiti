package myth.gourd.entiti.util;

public final class StringUtil 
{
	public static boolean isEmpty(String value)
	{
		return value == null || "".equals(value);
	}
	
	public static String getterMethodName(String fieldName)
	{
		return "get" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
	}
	
	public static String setterMethodName(String fieldName)
	{
		return "set" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
	}
}