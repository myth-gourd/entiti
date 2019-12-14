package myth.gourd.entiti.generator;

import com.sun.tools.javac.tree.JCTree.JCAssign;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

public class CodeGenerator {

	private TreeMaker treeMaker;
	
	private Names names;
	
	public CodeGenerator(TreeMaker treeMaker, Names names)
	{
		this.treeMaker = treeMaker;
		this.names = names;
	}
	
	public JCStatement generateSettingThisFiledStatement(JCVariableDecl filedVarDecl, JCVariableDecl valuleObjectDeclDecl, String vauleField)
	{
		JCIdent thisIdent = treeMaker.Ident(names.fromString("this"));
		JCFieldAccess thisFieldAccess = treeMaker.Select(thisIdent, filedVarDecl.getName());
		JCIdent varIdent = treeMaker.Ident(valuleObjectDeclDecl.getName());
		JCFieldAccess valueAccess = treeMaker.Select(varIdent, names.fromString(vauleField));
		JCAssign assing = treeMaker.Assign(thisFieldAccess, valueAccess);
		JCStatement statement = treeMaker.Exec(assing);
		return statement;
	}
}
