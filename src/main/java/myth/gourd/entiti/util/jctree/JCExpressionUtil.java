package myth.gourd.entiti.util.jctree;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.Tag;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;

public class JCExpressionUtil {
    
	public static final Map<String, Tag> OPERRATOR_MAP = new HashMap<String, Tag>(){{
		put("==", Tag.EQ);
		put("!=", Tag.NE);
		put(">=", Tag.GE);
		put(">",  Tag.GT);
		put("<=", Tag.LE);
		put("<",  Tag.LT);
	}};
	
	public static JCIdent jcIdent(String name) {
		Name n = JCTreeGloable.NAMES.fromString(name);
		return jcIdent(n);
	}
	
	public static JCIdent jcIdent(Name name) {
		return JCTreeGloable.TREEMAKER.Ident(name);
	}
	
	public static JCExpression jcExpression(String path)
	{
		String[] names = path.split("\\.");
		JCExpression expr = jcExpression(names);
		return expr;
	}
	
	public static JCExpression jcExpression(String[] names)
	{
		JCExpression expr = JCExpressionUtil.jcIdent(names[0]);
		for (int i = 1; i < names.length; i++) {
			String fieldName = names[i];
			if (fieldName.endsWith("()")) {
				String methodName = fieldName.substring(0, fieldName.length() - 2);
				expr = jcMethodInvocation(expr, methodName);
			} else {
				expr = jcFieldAccess(expr, fieldName);
			}
		}
		return expr;
	}
	
	
	
	
	
/*	public static JCExpression memberAccess(String memberPath)
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
	}*/
	
	
	
	public static JCMethodInvocation jcMethodInvocation(JCExpression parentExpr, String methodName) {
		Name name = JCTreeGloable.NAMES.fromString(methodName);
		return jcMethodInvocation(parentExpr, name);
	}
	
	public static JCMethodInvocation jcMethodInvocation(JCExpression parentExpr, Name methodName) {
		JCFieldAccess fieldAccess = jcFieldAccess(parentExpr, methodName);
		JCMethodInvocation methodInvocation = JCTreeGloable.TREEMAKER.Apply(List.nil(), fieldAccess, List.nil());
		return methodInvocation;
	}
	
/*	public static JCExpression thisFieldEqualValue(JCVariableDecl thisFieldVariableDecl, JCExpression value)
	{
		JCFieldAccess thisFieldAccess = JCFieldAccessUtil.thisFieldAccess(thisFieldVariableDecl);
		JCAssign assing = JCTreeGloable.TREEMAKER.Assign(thisFieldAccess, value);
		return assing;
	}*/
	
	public static JCExpression jcMethodExpression(String classPath, String methodName)
	{
		JCExpression classExpr = jcExpression(classPath);
		return jcFieldAccess(classExpr, methodName);
	}
	
	public static JCFieldAccess jcFieldAccess(JCExpression parentExpr, String methodName) {
		Name name = JCTreeGloable.NAMES.fromString(methodName);
		return jcFieldAccess(parentExpr, name);
	}
	
	public static JCFieldAccess jcFieldAccess(JCExpression parentExpr, Name methodName) {
		JCFieldAccess access = JCTreeGloable.TREEMAKER.Select(parentExpr, methodName);
		return access;
	}
	
	public static JCBinary jcBinary(String condition)
	{
		Entry<String, Tag> tagEntry = containedOperatorTag(condition);
		if (tagEntry == null)
		{
			
		}
		String[] params = condition.split(tagEntry.getKey());
		return null;
	}
	
	private static Entry<String, Tag> containedOperatorTag(String condition)
	{
		for(Entry<String, Tag> item :  OPERRATOR_MAP.entrySet())
		{
			if (condition.indexOf(item.getKey()) > -1)
			{
				return item;
			}
		}
		return null;
	}
	
	
	
	
	
	
	
}