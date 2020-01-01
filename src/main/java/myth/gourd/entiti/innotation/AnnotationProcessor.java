package myth.gourd.entiti.innotation;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.auto.service.AutoService;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;

import myth.gourd.entiti.innotation.handler.CopyHandler;
import myth.gourd.entiti.innotation.handler.DefaultHandler;


/**
 * 
 * @author zhangjian
 *
 */
@SupportedAnnotationTypes("myth.gourd.entiti.innotation.*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor {

	private final static Logger LOG = LoggerFactory.getLogger(AnnotationProcessor.class);
	
	private Messager messager;
	private JavacTrees trees;
	private TreeMaker treeMaker;
	private Names names;
	private Filer mFiler;

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		mFiler = processingEnv.getFiler();
		this.messager = processingEnv.getMessager();
		this.trees = JavacTrees.instance(processingEnv);
		Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
		this.treeMaker = TreeMaker.instance(context);
		this.names = Names.instance(context);
		
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		if(annotations.size() > 0)
		{
			//final JavacElements elementUtils = (JavacElements) processingEnv.getElementUtils();
			
			LOG.info("---------------------------------------------------------------------------");
			LOG.info("Myth Gourd Innotation Process");
			LOG.info("---------------------------------------------------------------------------");
			
			Set<? extends Element> copyElements = roundEnv.getElementsAnnotatedWith(Copy.class);
			if (copyElements.size() > 0)
			{
				CopyHandler handler = new CopyHandler(processingEnv, roundEnv, treeMaker, names);
				handler.handle(copyElements);
			}
			
			Set<? extends Element> defaultElements = roundEnv.getElementsAnnotatedWith(Default.class);
			if (defaultElements.size() > 0)
			{
				DefaultHandler handler = new DefaultHandler(processingEnv, roundEnv, treeMaker, names);
				handler.handle(defaultElements);
			}
			
		}
		return true;
	}
}