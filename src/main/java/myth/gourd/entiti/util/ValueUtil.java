package myth.gourd.entiti.util;

import myth.gourd.entiti.schema.ValueType;

public class ValueUtil {

	public static ValueType toValueType(String strType)
	{
		if (strType.equals("String"))
		{
			return ValueType.String;
		}
		else
		{
			if (strType.equals("int") || strType.equals("Integer"))
			{
				return ValueType.Integer;
			}
		}
		return null;
	}
	
	
	public static Object toObj(String type, String value)
	{
		ValueType typeEnum = ValueUtil.toValueType(type);
		return ValueUtil.toObj(typeEnum, value);
	}
	
	public static Object toObj(ValueType type, String value)
	{
		Object returnValue = null;
		switch(type)
		{
			case String:
				returnValue = value;
				break;
			case Integer:
				returnValue = Integer.valueOf(value);
				break;
			default:
				break;
		}
		return returnValue;
	}
}
