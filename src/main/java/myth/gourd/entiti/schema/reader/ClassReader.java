package myth.gourd.entiti.schema.reader;

import javax.lang.model.element.Element;

import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.tree.JCTree;

import myth.gourd.entiti.schema.ClassStructure;
import myth.gourd.entiti.schema.FieldStructure;
import myth.gourd.entiti.schema.MethodStructure;

public class ClassReader {
	
	private JavacElements javacElements; 
	
	public ClassReader(JavacElements javacElements)
	{
		this.javacElements = javacElements;
	}
	
	public ClassStructure read(Element classElement)
	{
		String name = classElement.toString();
		ClassStructure classStructure = ClassStructure.getClassStructure(name);
		if (classStructure != null)
		{
			return classStructure;
		}
		ClassStructure clsStruct = new ClassStructure();
		clsStruct.setElement(classElement);
		clsStruct.setName(classElement.toString());
		java.util.List<? extends Element> childrenElements = classElement.getEnclosedElements();
		for(int i=0; i<childrenElements.size(); i++)
		{
			Element element = childrenElements.get(i);
			JCTree tree = javacElements.getTree(element);
			switch(tree.getKind())
			{
				case VARIABLE:
					FieldReader fReader = new FieldReader(this.javacElements);
					FieldStructure fStructure = fReader.read(element);
					fStructure.setClassStructure(clsStruct);
					clsStruct.getFieldStructures().put(fStructure.getName(), fStructure);
					break;
				case METHOD:
					MethodReader mReader = new MethodReader(this.javacElements);
					MethodStructure mStructure = mReader.read(element);
					if (!mStructure.getName().equals("<init>"))
					{
						clsStruct.getMethodStructures().put(mStructure.getName(), mStructure);
					}
					break;
				default:
					break;
			}
		}
		return clsStruct;
	}
}
