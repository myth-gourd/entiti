package myth.gourd.entiti.schema;

import com.sun.tools.javac.tree.JCTree.JCVariableDecl;

public class FieldStructure extends ElementStructure
{
	private JCVariableDecl variableDecl;

	public JCVariableDecl getVariableDecl() {
		return variableDecl;
	}

	public void setVariableDecl(JCVariableDecl variableDecl) {
		this.variableDecl = variableDecl;
	}
}