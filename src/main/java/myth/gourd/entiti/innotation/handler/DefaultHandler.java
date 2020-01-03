package myth.gourd.entiti.innotation.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.lang.model.element.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.ListBuffer;

import myth.gourd.entiti.innotation.Default;
import myth.gourd.entiti.innotation.DefaultObject;
import myth.gourd.entiti.innotation.DefaultValue;
import myth.gourd.entiti.schema.ClassStructure;
import myth.gourd.entiti.schema.FieldStructure;
import myth.gourd.entiti.schema.reader.ClassReader;
import myth.gourd.entiti.util.CollectionUtil;
import myth.gourd.entiti.util.jctree.JCFieldAccessUtil;
import myth.gourd.entiti.util.jctree.JCIdentUtil;
import myth.gourd.entiti.util.jctree.JCStatmentUtil;
import myth.gourd.entiti.util.jctree.JCTreeGloable;

/**
 * 
 * @author Handling @Default Mehtod Annotation, setting default value for the
 *         fields of the class that the method is in. if field has
 * @DefaultValue、@DefaultObject or the field is value type (for examples
 *                              String、int、long..) and field is null then
 *                              setting the field default value;
 */
public class DefaultHandler extends Handler {

	private final static Logger LOG = LoggerFactory.getLogger(CopyHandler.class);

	public DefaultHandler() {
		super();
	}

	private Set<String> getGroups(Element methodElement) {
		Default groupAnnotation = methodElement.getAnnotation(Default.class);
		return CollectionUtil.toSet(groupAnnotation.groups());
	}

	private JCStatement defaultValueStatement(FieldStructure field) {
		JCVariableDecl fieldVariableDecl = field.getVariableDecl();
		DefaultValue dv = field.getAnnotation(DefaultValue.class);
		String varType = fieldVariableDecl.vartype.toString();
		Object value = null;
		if (dv == null) {
			value = ValueHandler.defValueByVarType(varType);
		} else {
			value = ValueHandler.toObj(varType, dv.value());
		}
		if (value != null) {
			JCStatement settingExpression = this.setDefaultValueStatement(field, value);
			JCBinary ifb = JCTreeGloable.TREEMAKER.Binary(JCTree.Tag.EQ, JCIdentUtil.ident(field.getName()),
					JCTreeGloable.TREEMAKER.Literal(TypeTag.BOT, null));
			JCStatement statement = JCTreeGloable.TREEMAKER.If(ifb, settingExpression, null);
			return statement;
		}
		return null;
	}

	private JCStatement setDefaultValueStatement(FieldStructure field, Object value) {
		JCVariableDecl fieldVariableDecl = field.getVariableDecl();
		JCLiteral valueLiteral = JCTreeGloable.TREEMAKER.Literal(value);
		JCStatement statement = JCStatmentUtil.thisFieldEqualValue(fieldVariableDecl, valueLiteral);
		return statement;
	}

	private JCStatement defaultObjectStatement(FieldStructure field) {
		DefaultObject dv = field.getAnnotation(DefaultObject.class);
		JCVariableDecl fieldVariableDecl = field.getVariableDecl();
		JCExpression method = JCFieldAccessUtil.memberAccess(dv.value());
		JCMethodInvocation methodInvocation = JCTreeGloable.TREEMAKER.Apply(com.sun.tools.javac.util.List.nil(), method,
				com.sun.tools.javac.util.List.nil());
		JCStatement statement = JCStatmentUtil.thisFieldEqualValue(fieldVariableDecl, methodInvocation);
		return statement;
	}

	private ListBuffer<JCStatement> defaultStatements(ClassStructure clsStruct, Set<String> groupSet) {
		ListBuffer<JCStatement> statementList = new ListBuffer<>();
		Set<Entry<String, FieldStructure>> set = clsStruct.getFieldStructures().entrySet();
		JCStatement statement = null;
		for (Entry<String, FieldStructure> entry : set) {
			FieldStructure field = entry.getValue();
			if (groupSet.size() == 0 || this.fieldHasOneGroup(field, groupSet)) {

				if (field.hasAnnotation(DefaultObject.class)) {
					statement = this.defaultObjectStatement(field);
				} else {
					statement = this.defaultValueStatement(field);
				}
				if (statement != null) {
					statementList.add(statement);
				}
			}
		}
		return statementList;
	}

	private List<FieldStructure> analyseFields(ClassStructure clsStruct, Set<String> groupSet) {
		List<FieldStructure> fields = new ArrayList<FieldStructure>();
		Set<Entry<String, FieldStructure>> set = clsStruct.getFieldStructures().entrySet();
		for (Entry<String, FieldStructure> entry : set) {
			FieldStructure field = entry.getValue();
			if (!field.hasAnnotation(DefaultValue.class)) {
				continue;
			}
			if (groupSet.size() > 0 && !fieldHasOneGroup(field, groupSet)) {
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
			ListBuffer<JCStatement> statementList = defaultStatements(clsStructure, groups);
			if (statementList.size() > 0) {
				JCTree.JCBlock body = JCTreeGloable.TREEMAKER.Block(0, statementList.toList());
				jcMethodDecl.body = body;
			}
		}
		LOG.info(jcMethodDecl.toString());
	}
}