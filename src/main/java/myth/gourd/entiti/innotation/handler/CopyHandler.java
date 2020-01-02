package myth.gourd.entiti.innotation.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.lang.model.element.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.ListBuffer;

import myth.gourd.entiti.innotation.Copy;
import myth.gourd.entiti.innotation.CopyIgnore;
import myth.gourd.entiti.schema.ClassStructure;
import myth.gourd.entiti.schema.FieldStructure;
import myth.gourd.entiti.schema.MethodStructure;
import myth.gourd.entiti.schema.reader.ClassReader;
import myth.gourd.entiti.util.CollectionUtil;
import myth.gourd.entiti.util.jctree.JCFieldAccessUtil;
import myth.gourd.entiti.util.jctree.JCStatmentUtil;
import myth.gourd.entiti.util.jctree.JCTreeGloable;

public class CopyHandler extends Handler
{
	private final static Logger LOG = LoggerFactory.getLogger(CopyHandler.class);
	
	private static final String LOMBOK_DATA = "@lombok.Data";
	
	public CopyHandler(){
		super();
	}
	
	private Set<String> getGroups(Element methodElement)
	{
		Copy groupAnnotation = methodElement.getAnnotation(Copy.class);
		return CollectionUtil.toSet(groupAnnotation.groups());
	}
	
	private List<FieldStructure> analyseFields(ClassStructure clsStruct, Set<String> groupSet)
	{
		boolean hasLombokData = clsStruct.hasAnnotation(LOMBOK_DATA);
		List<FieldStructure> fields = new ArrayList<FieldStructure>();
		Set<Entry<String, FieldStructure>> set = clsStruct.getFieldStructures().entrySet();
		for (Entry<String, FieldStructure> entry : set) {
			FieldStructure field = entry.getValue();
			if (field.hasAnnotation(CopyIgnore.class))
			{
				continue;
			}
			if (groupSet.size() > 0 && !fieldContainOneOfGroupSet(field, groupSet))
			{
				continue;
			}
			if (!hasLombokData && !hasSetterFieldMethod(clsStruct.getMethodStructures(), field.getName()))
			{
				continue;
			}
			fields.add(field);
			
		}
		return fields;
	}
	
	private boolean hasSetterFieldMethod(Map<String, MethodStructure> methods, String fieldName)
	{
		String setterName = JCFieldAccessUtil.setterMethodName(fieldName);
		return methods.keySet().contains(setterName);
	}
	
	@Override
	public void handleElement(Element element) {
		LOG.info("------Copy Mehtod-------");
		JCMethodDecl jcMethodDecl = (JCTree.JCMethodDecl) elementUtils.getTree(element);
		List<JCVariableDecl> paramtersVariableDecl = jcMethodDecl.getParameters();
		if (paramtersVariableDecl.size() == 0) {
			return;
		}
		JCVariableDecl valuleObjectDecl = paramtersVariableDecl.get(0);
		Set<String> groups = getGroups(element);
		Element classElement = element.getEnclosingElement();
		LOG.info("Class: " + classElement.toString());
		ClassReader clsReader = new ClassReader(elementUtils);
		ClassStructure clsStructure = clsReader.read(classElement);
		List<FieldStructure> fields = analyseFields(clsStructure, groups);
		if (fields.size() > 0)
		{
			ListBuffer<JCStatement> statementList = new ListBuffer<>();
			for(int i=0;i<fields.size();i++)
			{
				String fieldName = fields.get(i).getName();
				String thisSetterName = JCFieldAccessUtil.setterMethodName(fieldName);
				String objGetterMethodName = JCFieldAccessUtil.getterMethodName(fieldName);
				JCStatement statement = JCStatmentUtil.thisFieldSetterWithObjGetterMethod(thisSetterName, valuleObjectDecl, objGetterMethodName);
				statementList.add(statement);
			}
			JCTree.JCBlock body = JCTreeGloable.TREEMAKER.Block(0, statementList.toList());
			jcMethodDecl.body = body;
		}
		LOG.info(jcMethodDecl.toString());
	}
}
