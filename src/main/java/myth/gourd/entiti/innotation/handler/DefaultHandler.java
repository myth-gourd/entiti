package myth.gourd.entiti.innotation.handler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Names;

import myth.gourd.entiti.innotation.Copy;
import myth.gourd.entiti.innotation.DefaultValue;
import myth.gourd.entiti.innotation.FieldGroup;
import myth.gourd.entiti.schema.ClassStructure;
import myth.gourd.entiti.schema.FieldStructure;
import myth.gourd.entiti.schema.reader.ClassReader;
import myth.gourd.entiti.util.ValueUtil;
import myth.gourd.entiti.util.jctree.JCStatmentUtil;

public class DefaultHandler extends Handler {

	private final static Logger LOG = LoggerFactory.getLogger(CopyHandler.class);

	public DefaultHandler(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, TreeMaker treeMaker,
			Names names) {
		super(processingEnv, roundEnv, treeMaker, names);

	}

	public void handle(Set<? extends Element> elementsSet) {
		Iterator<? extends Element> itor = elementsSet.iterator();
		while (itor.hasNext()) {
			Element methodElement = itor.next();

			LOG.info("------Default Mehtod-------");

			JCMethodDecl jcMethodDecl = (JCTree.JCMethodDecl) elementUtils.getTree(methodElement);

			Set<String> groups = getGroups(methodElement);

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
					JCVariableDecl fieldVariableDecl = fields.get(i).getVariableDecl();
					String varType = fieldVariableDecl.vartype.toString();
					
					DefaultValue dv = fields.get(i).getAnnotation(DefaultValue.class);
					Object value = ValueUtil.toObj(varType, dv.value());
					JCLiteral valueLiteral = treeMaker.Literal(value);
					JCStatement statement = JCStatmentUtil.thisFieldEqualValue(fieldVariableDecl, valueLiteral);
					statementList.add(statement);
				}
				
				JCTree.JCBlock body = treeMaker.Block(0, statementList.toList());
				jcMethodDecl.body = body;
			}
			
			LOG.info(jcMethodDecl.toString());
		}

	}

	private Set<String> getGroups(Element methodElement) {
		Set<String> set = new HashSet<String>();
		Copy groupAnnotation = methodElement.getAnnotation(Copy.class);
		if (groupAnnotation != null) {
			String[] groups = groupAnnotation.groups();
			if (groups != null) {
				for (int i = 0; i < groups.length; i++) {
					set.add(groups[i]);
				}
			}
		}
		return set;
	}

	private List<FieldStructure> analyseFields(ClassStructure clsStruct, Set<String> groupSet) {
		List<FieldStructure> fields = new ArrayList<FieldStructure>();
		Set<Entry<String, FieldStructure>> set = clsStruct.getFieldStructures().entrySet();
		for (Entry<String, FieldStructure> entry : set) {
			FieldStructure field = entry.getValue();
			//DefaultValue defaultValue = field.getAnnotation(DefaultValue.class);
			if (!field.hasAnnotation(DefaultValue.class))
			{
				continue;
			}
			if (groupSet.size() > 0 && !fieldContainOneOfGroupSet(field, groupSet))
			{
				continue;
			}
			fields.add(field);
		}
		return fields;
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
