package myth.gourd.entiti.util.jctree;

import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;

/**
 * Access JCTree
 * @author zhangjian01
 *
 */
public class AccessUtil {

	public static JCIdent jcIdent(Name name) {
		return JCTreeGloable.TREEMAKER.Ident(name);
	}

	public static JCIdent jcIdent(String name) {
		Name n = JCTreeGloable.NAMES.fromString(name);
		return JCTreeGloable.TREEMAKER.Ident(n);
	}

	public static JCFieldAccess jcFieldAccess(JCExpression parentExpr, Name methodName) {
		JCFieldAccess access = JCTreeGloable.TREEMAKER.Select(parentExpr, methodName);
		return access;
	}

	public static JCFieldAccess jcFieldAccess(JCExpression parentExpr, String methodName) {
		Name name = JCTreeGloable.NAMES.fromString(methodName);
		return jcFieldAccess(parentExpr, name);
	}

	public static JCMethodInvocation jcMethodInvocation(JCExpression parentExpr, Name methodName) {
		JCFieldAccess fieldAccess = jcFieldAccess(parentExpr, methodName);
		JCMethodInvocation methodInvocation = JCTreeGloable.TREEMAKER.Apply(List.nil(), fieldAccess, List.nil());
		return methodInvocation;
	}

	public static JCMethodInvocation jcMethodInvocation(JCExpression parentExpr, String methodName) {
		Name name = JCTreeGloable.NAMES.fromString(methodName);
		return jcMethodInvocation(parentExpr, name);
	}

	public static JCExpression jcMethodExpression(String classPath, String methodName)
	{
		JCExpression classExpr = jcExpression(classPath);
		return jcFieldAccess(classExpr, methodName);
	}
	
	public static JCExpression jcExpression(String path)
	{
		String[] names = path.split("\\.");
		JCExpression expr = jcExpression(names);
		return expr;
	}
	
	public static JCExpression jcExpression(String[] names)
	{
		JCExpression expr = jcIdent(names[0]);
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
	
	public static JCMethodInvocation jcMethodInvocation(String path) {
		String[] names = path.split("\\.");
		JCExpression expr = jcIdent(names[0]);
		String name;
		for (int i = 1; i < names.length - 1; i++) {
			String filedName = names[i];
			if (filedName.endsWith("()")) {
				String methodName = filedName.substring(0, filedName.length() - 2);
				expr = jcMethodInvocation(expr, methodName);
			} else {
				expr = jcFieldAccess(expr, filedName);
			}
		}
		name = names[names.length - 1];
		name = name.substring(0, name.length() - 2);
		JCMethodInvocation methodInvocation = jcMethodInvocation(expr, name);
		return methodInvocation;
	}
}