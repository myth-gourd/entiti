package myth.gourd.entiti.innotation.handler;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

import com.sun.tools.javac.model.JavacElements;

import myth.gourd.entiti.innotation.FieldGroup;
import myth.gourd.entiti.schema.FieldStructure;

public abstract class Handler {

	protected ProcessingEnvironment processingEnv;
	protected RoundEnvironment roundEnv;
	protected JavacElements elementUtils;
	
	private Class<? extends Annotation> annotationClass;
	
	public Class<? extends Annotation> getAnnotationClass() {
		return annotationClass;
	}

	public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
		this.annotationClass = annotationClass;
	}

	public ProcessingEnvironment getProcessingEnv() {
		return processingEnv;
	}

	public void setProcessingEnv(ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
		this.elementUtils = (JavacElements) this.processingEnv.getElementUtils();
	}

	public RoundEnvironment getRoundEnv() {
		return roundEnv;
	}

	public void setRoundEnv(RoundEnvironment roundEnv) {
		this.roundEnv = roundEnv;
	}

	public Handler(){
		
	}
	
	public void handle(){
		Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotationClass);
		Iterator<? extends Element> itor = elements.iterator();
		while (itor.hasNext()) {
			Element element = itor.next();
			handleElement(element);
		}
	}
	
	public abstract void handleElement(Element element);
	
	protected boolean fieldContainOneOfGroupSet(FieldStructure field, Set<String> groupSet)
	{
		FieldGroup groupAnnotation = field.getAnnotation(FieldGroup.class);
		if (groupAnnotation != null)
		{
			String[] fieldGroups = groupAnnotation.value().split(",");
			for(int i=0;i<fieldGroups.length;i++)
			{
				String group = fieldGroups[i];
				if (groupSet.contains(group))
				{
					return true;
				}
			}
		}
		return false;
	}
}