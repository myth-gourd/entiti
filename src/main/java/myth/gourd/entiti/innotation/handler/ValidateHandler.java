package myth.gourd.entiti.innotation.handler;

import java.util.Map.Entry;
import java.util.Set;

import javax.lang.model.element.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.util.ListBuffer;

import myth.gourd.entiti.innotation.Validate;
import myth.gourd.entiti.innotation.handler.writer.NotEmptyStatementWriter;
import myth.gourd.entiti.innotation.handler.writer.NotNullStatementWriter;
import myth.gourd.entiti.innotation.handler.writer.OutOfSizeStatementWriter;
import myth.gourd.entiti.schema.ClassStructure;
import myth.gourd.entiti.schema.FieldStructure;
import myth.gourd.entiti.schema.reader.ClassReader;
import myth.gourd.entiti.util.CollectionUtil;
import myth.gourd.entiti.util.jctree.JCTreeGloable;

public class ValidateHandler extends Handler {

	private static final Logger LOG = LoggerFactory.getLogger(ValidateHandler.class);

	private Set<String> getGroups(Element methodElement) {
		Validate groupAnnotation = methodElement.getAnnotation(Validate.class);
		return CollectionUtil.toSet(groupAnnotation.groups());
	}

	public ValidateHandler() {
		super();
	}

	@Override
	public void handleElement(Element element) {

		LOG.info("------Validate Mehtod-------");

		JCMethodDecl jcMethodDecl = (JCTree.JCMethodDecl) elementUtils.getTree(element);

		Set<String> groups = getGroups(element);

		Element classElement = element.getEnclosingElement();

		LOG.info("Class: " + classElement.toString());

		ClassReader clsReader = new ClassReader(elementUtils);
		ClassStructure clsStruct = clsReader.read(classElement);

		Validate validate = element.getAnnotation(Validate.class);
		String code = validate.code();

		Set<Entry<String, FieldStructure>> set = clsStruct.getFieldStructures().entrySet();
		ListBuffer<JCStatement> statements = new ListBuffer<>();
		for (Entry<String, FieldStructure> entry : set) {
			FieldStructure field = entry.getValue();
			if (groups.size() > 0 && !fieldHasOneGroup(field, groups)) {
				continue;
			}
			(new NotNullStatementWriter(field, code)).write(statements);
			(new NotEmptyStatementWriter(field, code)).write(statements);
			(new OutOfSizeStatementWriter(field, code)).write(statements);
		}
		if (statements.size() > 0) {
			JCTree.JCBlock body = JCTreeGloable.TREEMAKER.Block(0, statements.toList());
			jcMethodDecl.body = body;
		}
		LOG.info(jcMethodDecl.toString());
	}
}