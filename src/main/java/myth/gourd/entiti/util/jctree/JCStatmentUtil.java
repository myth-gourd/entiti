package myth.gourd.entiti.util.jctree;

import com.sun.tools.javac.tree.JCTree.JCAssign;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.List;

public class JCStatmentUtil {

	
	
	
	
	
	public static JCStatement thisFieldEqualValue(JCVariableDecl thisFieldVariableDecl, JCExpression value)
	{
		JCFieldAccess thisFieldAccess = JCFieldAccessUtil.thisFieldAccess(thisFieldVariableDecl);
		JCAssign assing = JCTreeGloable.TREEMAKER.Assign(thisFieldAccess, value);
		JCStatement statement = JCTreeGloable.TREEMAKER.Exec(assing);
		return statement;
	}
	
	public static JCStatement thisFieldEqualObjField(JCVariableDecl thisFieldVariableDecl, JCVariableDecl objVariableDecl, String objFieldName)
	{
		JCFieldAccess thisFieldAccess = JCFieldAccessUtil.thisFieldAccess(thisFieldVariableDecl);
		JCFieldAccess objFieldAccess  = JCFieldAccessUtil.objFieldAccess(objVariableDecl, objFieldName);
		JCAssign assing = JCTreeGloable.TREEMAKER.Assign(thisFieldAccess, objFieldAccess);
		JCStatement statement = JCTreeGloable.TREEMAKER.Exec(assing);
		return statement;
	}
	
	public static JCStatement thisFieldEqualObjField(String thisFieldName, String objName, String objFieldName)
	{
		JCFieldAccess thisFieldAccess = JCFieldAccessUtil.thisFieldAccess(thisFieldName);
		JCFieldAccess objFieldAccess  = JCFieldAccessUtil.objFieldAccess(objName, objFieldName);
		JCAssign assing = JCTreeGloable.TREEMAKER.Assign(thisFieldAccess, objFieldAccess);
		JCStatement statement = JCTreeGloable.TREEMAKER.Exec(assing);
		return statement;
	}
	
	public static JCStatement thisFieldEqualObjGetterMethod(JCVariableDecl thisFieldVariableDecl, JCVariableDecl objVariableDecl, String objGetterMethodName)
	{
		JCFieldAccess thisFieldAccess = JCFieldAccessUtil.thisFieldAccess(thisFieldVariableDecl);
		JCFieldAccess methodAccess  = JCFieldAccessUtil.objFieldAccess(objVariableDecl, objGetterMethodName);
		JCMethodInvocation methodInvocation = JCTreeGloable.TREEMAKER.Apply(List.nil(), methodAccess, List.nil());
		JCAssign assing = JCTreeGloable.TREEMAKER.Assign(thisFieldAccess, methodInvocation);
		JCStatement statement = JCTreeGloable.TREEMAKER.Exec(assing);
		return statement;
	}
	
	public static JCStatement thisFieldSetterWithObjGetterMethod(String thisSetterName, JCVariableDecl objVariableDecl, String objGetterMethodName)
	{
		JCFieldAccess thisSetterMethodAccess = JCFieldAccessUtil.thisSetterMethodAccess(thisSetterName);
		JCFieldAccess objGetterMethodAccess  = JCFieldAccessUtil.objFieldAccess(objVariableDecl, objGetterMethodName);
		JCMethodInvocation getterMethodInvocation = JCTreeGloable.TREEMAKER.Apply(List.nil(), objGetterMethodAccess, List.nil());
		List<JCExpression> setterArgs = List.of(getterMethodInvocation);
		//setterArgs.add(getterMethodInvocation);
		JCMethodInvocation settermethodInvocation = JCTreeGloable.TREEMAKER.Apply(List.nil(), thisSetterMethodAccess, setterArgs);
		//JCAssign assing = JCTreeGloable.TREEMAKER.Assign(thisFieldAccess, getterMethodInvocation);
		JCStatement statement = JCTreeGloable.TREEMAKER.Exec(settermethodInvocation);
		return statement;
	}
	
	
	
}