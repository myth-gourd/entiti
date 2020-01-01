package myth.gourd.entiti.util.jctree;

import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.Name;

public class JCFieldAccessUtil 
{
	public static String getterMethodName(String fieldName)
	{
		return "get" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
	}
	
	public static String setterMethodName(String fieldName)
	{
		return "set" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
	}
	
	public static JCFieldAccess thisFieldAccess(Name fieldName)
	{
		JCIdent thisIdent = JCTreeGloable.TREEMAKER.Ident(JCTreeGloable.NAMES.fromString("this"));
		JCFieldAccess thisFieldAccess = JCTreeGloable.TREEMAKER.Select(thisIdent, fieldName);
		return thisFieldAccess;
	}
	
	public static JCFieldAccess thisSetterMethodAccess(Name setterName)
	{
		return thisFieldAccess(setterName);
	}
	
	public static JCFieldAccess thisSetterMethodAccess(String setterName)
	{
		return thisFieldAccess(setterName);
	}
	
	public static JCFieldAccess thisFieldAccess(String fieldName)
	{
		Name name = JCTreeGloable.NAMES.fromString(fieldName);
		return thisFieldAccess(name);
	}
	
	public static JCFieldAccess thisFieldAccess(JCVariableDecl fieldJCVariableDecl)
	{
		return thisFieldAccess(fieldJCVariableDecl.getName());
	}

	public static JCFieldAccess objFieldAccess(Name objName, Name fieldName)
	{
		JCIdent objIdent = JCTreeGloable.TREEMAKER.Ident(objName);
		JCFieldAccess objFieldAccess = JCTreeGloable.TREEMAKER.Select(objIdent, fieldName);
		return objFieldAccess;
	}
	
	public static JCFieldAccess objFieldAccess(String objName, String fieldName)
	{
		return objFieldAccess(JCTreeGloable.NAMES.fromString(objName), JCTreeGloable.NAMES.fromString(fieldName));
	}
	
	public static JCFieldAccess objFieldAccess(JCVariableDecl objVariableDecl, String fieldName)
	{
		return objFieldAccess(objVariableDecl.getName(), JCTreeGloable.NAMES.fromString(fieldName));
	}
	
	public static JCFieldAccess objGetterMethodAccess(String objName, String fieldName)
	{
		String getterName = getterMethodName(fieldName);
		return objFieldAccess(objName, getterName);
	}
}