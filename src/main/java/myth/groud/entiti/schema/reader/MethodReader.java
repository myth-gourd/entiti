package myth.groud.entiti.schema.reader;

import javax.lang.model.element.Element;

import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

import myth.groud.entiti.schema.MethodStructure;

public class MethodReader extends ElementReader {

	public MethodReader(JavacElements javacElements) {
		super(javacElements);
	}
	
	public MethodStructure read(Element element)
	{
		MethodStructure m = new MethodStructure();
		m.setElement(element);
		JCTree tree = javacElements.getTree(element);
		if (tree != null)
		{
			JCMethodDecl dec = (JCMethodDecl) tree;
			String name = dec.getName().toString();
			m.setName(name);
		}
		return m;
	}
}
