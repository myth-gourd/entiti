package myth.gourd.entiti.innotation.handler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Names;

import myth.gourd.entiti.innotation.Copy;
import myth.gourd.entiti.innotation.CopyIgnore;
import myth.gourd.entiti.innotation.FieldGroup;
import myth.gourd.entiti.schema.ClassStructure;
import myth.gourd.entiti.schema.FieldStructure;
import myth.gourd.entiti.schema.MethodStructure;
import myth.gourd.entiti.schema.reader.ClassReader;
import myth.gourd.entiti.util.jctree.JCFieldAccessUtil;
import myth.gourd.entiti.util.jctree.JCStatmentUtil;

public class CopyHandler extends Handler
{
	private final static Logger LOG = LoggerFactory.getLogger(CopyHandler.class);
	
	private static final String LOMBOK_DATA = "@lombok.Data";
	
	public CopyHandler(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, TreeMaker treeMaker, Names names) {
		super(processingEnv, roundEnv, treeMaker, names);
	}

	
	public void handle(Set<? extends Element> elementsSet) {
		
		Iterator<? extends Element> itor = elementsSet.iterator();
		while (itor.hasNext()) {
			
			Element methodElement = itor.next();
			
			LOG.info("------Copy Mehtod-------");
			
			JCMethodDecl jcMethodDecl = (JCTree.JCMethodDecl) elementUtils.getTree(methodElement);
			
			List<JCVariableDecl> paramtersVariableDecl = jcMethodDecl.getParameters();
			if (paramtersVariableDecl.size() == 0) {
				return;
			}
			JCVariableDecl valuleObjectDecl = paramtersVariableDecl.get(0);
			
			Set<String> groups = getGroups(methodElement);
			
			// 获取方法所属的类定义
			Element classElement = methodElement.getEnclosingElement();
			
			LOG.info("Class: " + classElement.toString());
			
			ClassReader clsReader = new ClassReader(elementUtils);
			ClassStructure clsStructure = clsReader.read(classElement);
			
			List<FieldStructure> fields = analyseFields(clsStructure, groups);
			
			if (fields.size() > 0)
			{
				ListBuffer<JCStatement> statementList = new ListBuffer<>();
				for(int i=0;i<fields.size();i++)
				{
					//JCVariableDecl fieldVariableDecl = fields.get(i).getVariableDecl();
					String fieldName = fields.get(i).getName();
					String thisSetterName = JCFieldAccessUtil.setterMethodName(fieldName);
					String objGetterMethodName = JCFieldAccessUtil.getterMethodName(fieldName);
					
					JCStatement statement = JCStatmentUtil.thisFieldSetterWithObjGetterMethod(thisSetterName, valuleObjectDecl, objGetterMethodName);
					statementList.add(statement);
				}
				
				JCTree.JCBlock body = treeMaker.Block(0, statementList.toList());
				jcMethodDecl.body = body;
			}
			LOG.info(jcMethodDecl.toString());
			
		}
	}
	
	private Set<String> getGroups(Element methodElement)
	{
		Set<String> set = new HashSet<String>();
		Copy groupAnnotation = methodElement.getAnnotation(Copy.class);
		if (groupAnnotation != null)
		{
			String[] groups = groupAnnotation.groups();
			if (groups != null)
			{
				for(int i=0;i< groups.length;i++)
				{
					set.add(groups[i]);
				}
			}
		}
		return set;
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
	
	private boolean fieldContainOneOfGroupSet(FieldStructure field, Set<String> groupSet)
	{
		FieldGroup groupAnnotation = field.getAnnotation(FieldGroup.class);
		if (groupAnnotation != null)
		{
			String[] fieldGroups = groupAnnotation.value().split(",");
			for(int i=0;i<fieldGroups.length;i++)
			{
				String group = fieldGroups[i];
				if (groupSet.contains(group))
				{
					return true;
				}
			}
		}
		return false;
	}
}
