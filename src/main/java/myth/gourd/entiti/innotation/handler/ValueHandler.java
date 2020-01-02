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
		put("int", Type.Integer);
		put("Long", Type.Long);
		put("long", Type.Long);
		put("Double", Type.Double);
		put("double", Type.Double);
		put("Float", Type.Float);
		put("float", Type.Float);
	}};
	
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
