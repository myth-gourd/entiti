package myth.gourd.entiti.innotation.handler;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;

import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

import myth.gourd.entiti.util.jctree.JCTreeGloable;

public class Handler {

	protected ProcessingEnvironment processingEnv;
	protected RoundEnvironment roundEnv;
	protected TreeMaker treeMaker; 
	protected Names names;
	protected JavacElements elementUtils;
	
	public Handler(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, TreeMaker treeMaker, Names names)
	{
		this.processingEnv = processingEnv;
		this.roundEnv = roundEnv;
		this.treeMaker = treeMaker;
		this.names = names;
		this.elementUtils = (JavacElements) this.processingEnv.getElementUtils();
		JCTreeGloable.ready(this.treeMaker, this.names);
	}
}