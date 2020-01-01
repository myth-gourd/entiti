package myth.gourd.entiti.schema;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;

public class ElementStructure {
	
	private Element element;
	
	private String name;

	public boolean hasAnnotation(Class<? extends Annotation> cls)
	{
		Annotation annotation = element.getAnnotation(cls);
		return annotation != null;
	}
	
	public boolean hasAnnotation(String name)
	{
		List<? extends AnnotationMirror> mirrors = element.getAnnotationMirrors();
		for(int i=0;i<mirrors.size();i++)
		{
			AnnotationMirror mirror = mirrors.get(i);
			if (mirror.toString().equals(name))
			{
				return true;
			}
		}
		return false;
	}
	
	public <A extends Annotation> A getAnnotation(Class<A> cls)
	{
		return this.element.getAnnotation(cls);
	}
	
	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}