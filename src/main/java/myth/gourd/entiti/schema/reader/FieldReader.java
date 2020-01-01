package myth.groud.entiti.schema.reader;

import javax.lang.model.element.Element;

import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;

import myth.groud.entiti.schema.FieldStructure;

public class FieldReader extends ElementReader {
	
	public FieldReader(JavacElements javacElements) {
		super(javacElements);
	}
	
	public FieldStructure read(Element element)
	{
		FieldStructure f = new FieldStructure();
		f.setElement(element);
		JCTree tree = javacElements.getTree(element);
		if (tree != null)
		{
			JCVariableDecl varDec = (JCVariableDecl) tree;
			f.setVariableDecl(varDec);
			String name = varDec.name.toString();
			f.setName(name);;
		}
		return f;
	}
}