package myth.gourd.entiti.innotation.handler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.lang.model.element.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.ListBuffer;

import myth.gourd.entiti.innotation.Copy;
import myth.gourd.entiti.innotation.Default;
import myth.gourd.entiti.innotation.DefaultValue;
import myth.gourd.entiti.innotation.FieldGroup;
import myth.gourd.entiti.schema.ClassStructure;
import myth.gourd.entiti.schema.FieldStructure;
import myth.gourd.entiti.schema.reader.ClassReader;
import myth.gourd.entiti.util.CollectionUtil;
import myth.gourd.entiti.util.jctree.JCFieldAccessUtil;
import myth.gourd.entiti.util.jctree.JCStatmentUtil;
import myth.gourd.entiti.util.jctree.JCTreeGloable;

public class DefaultHandler extends Handler {

	private final static Logger LOG = LoggerFactory.getLogger(CopyHandler.class);

	public DefaultHandler() {
		super();
	}

	private Set<String> getGroups(Element methodElement) {
		Default groupAnnotation = methodElement.getAnnotation(Default.class);
		return CollectionUtil.toSet(groupAnnotation.groups());
	}
	
	private List<FieldStructure> analyseFields(ClassStructure clsStruct, Set<String> groupSet) {
		List<FieldStructure> fields = new ArrayList<FieldStructure>();
		Set<Entry<String, FieldStructure>> set = clsStruct.getFieldStructures().entrySet();
		for (Entry<String, FieldStructure> entry : set) {
			FieldStructure field = entry.getValue();
			if (!field.hasAnnotation(DefaultValue.class)) {
				continue;
			}
			if (groupSet.size() > 0 && !fieldContainOneOfGroupSet(field, groupSet)) {
				continue;
			}
			fields.add(field);
		}
		return fields;
	}

	@Override
	public void handleElement(Element element) {
		LOG.info("------Default Mehtod-------");

		JCMethodDecl jcMethodDecl = (JCTree.JCMethodDecl) elementUtils.getTree(element);

		Set<String> groups = getGroups(element);

		Element classElement = element.getEnclosingElement();

		LOG.info("Class: " + classElement.toString());

		ClassReader clsReader = new ClassReader(elementUtils);
		ClassStructure clsStructure = clsReader.read(classElement);

		List<FieldStructure> fields = analyseFields(clsStructure, groups);
		
		if (fields.size() > 0) {
			ListBuffer<JCStatement> statementList = new ListBuffer<>();
			for (int i = 0; i < fields.size(); i++) {
				
				JCVariableDecl fieldVariableDecl = fields.get(i).getVariableDecl();
				String varType = fieldVariableDecl.vartype.toString();
				
				if (varType.equals("Date")) {
					DefaultValue dv = fields.get(i).getAnnotation(DefaultValue.class);
					//Object value = ValueUtil.toObj(varType, dv.value());
					//JCExpression method = JCFieldAccessUtil.memberAccess("myth.gourd.entiti.util.DateUtil.currentDate");
					JCExpression method = JCFieldAccessUtil.memberAccess(dv.value());
					JCMethodInvocation methodInvocation = JCTreeGloable.TREEMAKER
							.Apply(com.sun.tools.javac.util.List.nil(), method, com.sun.tools.javac.util.List.nil());

					JCStatement statement = JCStatmentUtil.thisFieldEqualValue(fieldVariableDecl, methodInvocation);
					statementList.add(statement);
				} else {
					DefaultValue dv = fields.get(i).getAnnotation(DefaultValue.class);
					Object value = ValueHandler.toObj(varType, dv.value());
					JCLiteral valueLiteral = JCTreeGloable.TREEMAKER.Literal(value);
					JCStatement statement = JCStatmentUtil.thisFieldEqualValue(fieldVariableDecl, valueLiteral);
					statementList.add(statement);
				}
			}
			JCTree.JCBlock body = JCTreeGloable.TREEMAKER.Block(0, statementList.toList());
			jcMethodDecl.body = body;
		}
		LOG.info(jcMethodDecl.toString());
	}
}