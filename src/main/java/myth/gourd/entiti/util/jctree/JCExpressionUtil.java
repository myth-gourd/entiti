package myth.gourd.entiti.util.jctree;

import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.util.Name;

public class JCExpressionUtil {
    
	public static JCExpression memberAccess(String memberPath)
	{
		String[] members = memberPath.split("\\.");
	    Name first = JCTreeGloable.NAMES.fromString(members[0]);
	    
	    JCExpression expr = JCTreeGloable.TREEMAKER.Ident(first);
	    for (int i = 1; i < members.length; i++) {
	    	Name name = JCTreeGloable.NAMES.fromString(members[i]);
	        expr = JCTreeGloable.TREEMAKER.Select(expr, name);
	    }
	    return expr;
	}
}