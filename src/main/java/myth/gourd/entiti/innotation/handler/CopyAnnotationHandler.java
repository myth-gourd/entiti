package myth.gourd.entiti.innotation.handler;

import java.util.Iterator;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

import com.sun.source.tree.Tree.Kind;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Names;

import myth.gourd.entiti.generator.CodeGenerator;
import myth.gourd.entiti.innotation.Copy;
import myth.gourd.entiti.innotation.CopyIgnore;

public class CopyAnnotationHandler 
{
	private ProcessingEnvironment processingEnv;
	private RoundEnvironment roundEnv;
	private TreeMaker treeMaker; 
	private Names names;
	
	public CopyAnnotationHandler(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, TreeMaker treeMaker, Names names)
	{
		this.processingEnv = processingEnv;
		this.roundEnv = roundEnv;
		this.treeMaker = treeMaker;
		this.names = names;
	}
	
	public void handle() {
		final JavacElements elementUtils = (JavacElements) processingEnv.getElementUtils();
		Set<? extends Element> copyElements = roundEnv.getElementsAnnotatedWith(Copy.class);
		Iterator<? extends Element> itor = copyElements.iterator();
		while (itor.hasNext()) {
			// 获取方法定义
			Element methodElement = itor.next();
			
			JCMethodDecl jcMethodDecl = (JCTree.JCMethodDecl) elementUtils.getTree(methodElement);
			// 获取方法参数
			List<JCVariableDecl> paramtersVariableDecl = jcMethodDecl.getParameters();
			if (paramtersVariableDecl.size() == 0) {
				return;
			}
			JCVariableDecl valuleObjectDeclDecl = paramtersVariableDecl.get(0);
			
			// 获取方法所属的类定义
			Element classElement = methodElement.getEnclosingElement();
			
			ListBuffer<JCStatement> statementList = new ListBuffer<>();
			java.util.List<? extends Element> lst = classElement.getEnclosedElements();
			if (lst.size() > 0)
			{
				CodeGenerator generator = new CodeGenerator(this.treeMaker, this.names);
				
				for(int i=0; i<lst.size(); i++)
				{
					Element element = lst.get(i);
					JCTree tree = elementUtils.getTree(element);
					if (tree.getKind() == Kind.VARIABLE)
					{
						CopyIgnore copyIgnore = element.getAnnotation(CopyIgnore.class);
						if (copyIgnore == null)
						{
							JCVariableDecl thisFieldVariableDec = (JCVariableDecl) tree;
							String fileName = thisFieldVariableDec.getName().toString();
							JCStatement statement = generator.generateSettingThisFiledStatement(thisFieldVariableDec, valuleObjectDeclDecl, fileName);
							statementList.add(statement);
						}
					}
				}
				
			}
			JCTree.JCBlock body = treeMaker.Block(0, statementList.toList());
			jcMethodDecl.body = body;
		}
	}
}
