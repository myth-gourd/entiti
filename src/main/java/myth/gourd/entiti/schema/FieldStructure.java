package myth.gourd.entiti.schema;

import com.sun.tools.javac.tree.JCTree.JCVariableDecl;

public class FieldStructure extends ElementStructure
{
	private JCVariableDecl variableDecl;

	private ClassStructure classStructure;
	
	public JCVariableDecl getVariableDecl() {
		return variableDecl;
	}

	public void setVariableDecl(JCVariableDecl variableDecl) {
		this.variableDecl = variableDecl;
	}

	public ClassStructure getClassStructure() {
		return classStructure;
	}

	public void setClassStructure(ClassStructure classStructure) {
		this.classStructure = classStructure;
	}
}