package myth.gourd.entiti.innotation.handler;

import java.util.HashMap;
import java.util.Map;

public class ValueHandler {

	private static enum Type {
		String, 
		Integer,
		Long,
		Double,
		Float
	}
	
	private static final Map<String, Type> varTypes = new HashMap<String, Type>(){
		private static final long serialVersionUID = 1L;
	{
		put("String", Type.String);
		put("Integer", Type.Integer);
		put("Long", Type.Long);
		put("Double", Type.Double);
		put("Float", Type.Float);
	}};
	
	private static final Map<String, Object> defaultValue = new HashMap<String, Object>(){
		private static final long serialVersionUID = 1L;
	{
		put("String", "");
		put("Integer", 0);
		put("Long", 0L);
		put("Double", 0);
		put("Float", 0f);
	}};
	
	public static Object defValueByVarType(String varType)
	{
		Object value = defaultValue.get(varType);
		return value;
	}
	
	public static Object toObj(String varType, String value)
	{
		Type type = varTypes.get(varType);
		if (type != null)
		{
			return toObj(type, value);
		}
		else
		{
			return null;
		}
	}
	
	public static Object toObj(Type type, String value)
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
			case Long:
				returnValue = Long.valueOf(value);
				break;
			case Double:
				returnValue = Double.valueOf(value);
				break;
			case Float:
				returnValue = Float.valueOf(value);
				break;
			default:
				break;
		}
		return returnValue;
	}
}
