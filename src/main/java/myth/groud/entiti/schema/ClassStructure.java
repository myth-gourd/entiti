package myth.groud.entiti.schema;

import java.util.HashMap;
import java.util.Map;

public class ClassStructure extends ElementStructure {
	
	private static final Map<String, ClassStructure> CONTAINER = new HashMap<String, ClassStructure>();
	
	private String name;
	
	private Map<String, FieldStructure> fieldStructures = new HashMap<String, FieldStructure>();
	
	private Map<String, MethodStructure> methodStructures = new HashMap<String, MethodStructure>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static ClassStructure getClassStructure(String name)
	{
		return CONTAINER.get(name);
	}

	public Map<String, MethodStructure> getMethodStructures() {
		return methodStructures;
	}

	public void setMethodStructures(Map<String, MethodStructure> methodStructures) {
		this.methodStructures = methodStructures;
	}

	public Map<String, FieldStructure> getFieldStructures() {
		return fieldStructures;
	}

	public void setFieldStructures(Map<String, FieldStructure> fieldStructures) {
		this.fieldStructures = fieldStructures;
	}
}