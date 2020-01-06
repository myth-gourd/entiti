package myth.gourd.entiti.util.jctree;

import com.sun.tools.javac.tree.JCTree.JCAssign;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.Name;

public class JCExpressionUtil {
    
	public static JCExpression memberAccess(String memberPath)
	{
		String[] members = memberPath.split("\\.");
	    Name first = JCTreeGloable.NAMES.fromString(members[0]);
	    JCExpression expr = JCTreeGloable.TREEMAKER.Ident(first);
	    for (int i = 1; i < members.length; i++) {
	    	String strName = members[i];
	    	if (strName.endsWith("()"))
	    	{
	    		strName = strName.substring(0, strName.length() - 2);
	    	}
	    	Name name = JCTreeGloable.NAMES.fromString(strName);
	        expr = JCTreeGloable.TREEMAKER.Select(expr, name);
	    }
	    return expr;
	}
	
	public static JCExpression thisFieldEqualValue(JCVariableDecl thisFieldVariableDecl, JCExpression value)
	{
		JCFieldAccess thisFieldAccess = JCFieldAccessUtil.thisFieldAccess(thisFieldVariableDecl);
		JCAssign assing = JCTreeGloable.TREEMAKER.Assign(thisFieldAccess, value);
		return assing;
	}
}