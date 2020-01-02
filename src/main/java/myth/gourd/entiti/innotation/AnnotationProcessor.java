package myth.gourd.entiti.innotation;

import java.lang.annotation.Annotation;
import java.util.HashSet;
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
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;

import myth.gourd.entiti.innotation.handler.Handler;
import myth.gourd.entiti.util.jctree.JCTreeGloable;

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
		
		JCTreeGloable.ready(this.treeMaker, this.names);
	}

	private static final Set<Class<? extends Annotation>> MethodAnnotation = new HashSet<Class<? extends Annotation>>() {
		private static final long serialVersionUID = 1L;

		{
			add(Copy.class);
			add(Default.class);
		}
	};

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		if (annotations.size() > 0) {
			LOG.info("---------------------------------------------------------------------------");
			LOG.info("Myth Gourd Innotation Process");
			LOG.info("---------------------------------------------------------------------------");
			for (Class<? extends Annotation> annotation : MethodAnnotation) {
				Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
				if (elements.size() > 0) {
					try {
						String handlerClassName = annotation.getSimpleName() + "Handler";
						Class<?> handlerClass;
						handlerClass = Class.forName("myth.gourd.entiti.innotation.handler." + handlerClassName);
						Handler handler = (Handler)handlerClass.newInstance();
						handler.setProcessingEnv(processingEnv);
						handler.setAnnotationClass(annotation);
						handler.setRoundEnv(roundEnv);
						handler.handle();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return true;
	}
}