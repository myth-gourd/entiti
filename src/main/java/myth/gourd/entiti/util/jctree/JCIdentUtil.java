package myth.gourd.entiti.util.jctree;

import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.util.Name;

public class JCIdentUtil {
	public static JCIdent ident(String name) {
		Name n = JCTreeGloable.NAMES.fromString(name);
		JCIdent ident = JCTreeGloable.TREEMAKER.Ident(n);
		return ident;
	}
}