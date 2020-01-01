package myth.groud.entiti.util.jctree;

import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

public class JCTreeGloable {
	
	public static TreeMaker TREEMAKER;
	
	public static Names NAMES;
	
	public static void ready(TreeMaker treeMaker, Names names)
	{
		TREEMAKER = treeMaker;
		NAMES = names;
	}
}
