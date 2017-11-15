import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Text;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;

public class UnitTests1 {

	@Test
	public void test() {
		
		Document testDoc = new Document();
		
		XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
		
		
		Element theRoot = new Element("serialized");
		
		Element primitiveTest = new Element("object");
		
		primitiveTest.setAttribute("class", "PrimitiveOnlyType");
		primitiveTest.setAttribute("id", "0");
		
		Element field1 = new Element("field");
		field1.setAttribute("name", "testInteger");
		field1.setAttribute("declaringclass", "PrimitiveOnlyType");
		
		Element value1 = new Element("value");
		value1.addContent(new Text("1"));
		
		field1.addContent(value1);
		
		
		Element field2 = new Element("field");
		field2.setAttribute("name", "testShort");
		field2.setAttribute("declaringclass", "PrimitiveOnlyType");
		
		Element value2 = new Element("value");
		value2.addContent(new Text("2"));		
		
		field2.addContent(value2);
		
		primitiveTest.addContent(field1);
		primitiveTest.addContent(field2);
		
		theRoot.addContent(primitiveTest);
		
		testDoc.setRootElement(theRoot);
		
		Class primOnlyClass = null;
		Object obj = null;
		try {
			primOnlyClass = Class.forName("PrimitiveOnlyType");
			obj = new PrimitiveOnlyType();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		Field testInt = null;
		Field testShort = null;
		try {
			testInt = primOnlyClass.getDeclaredField("testInteger");
			testShort = primOnlyClass.getDeclaredField("testShort");
		} catch (NoSuchFieldException | SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Short s = 2;

		try {
			testInt.setAccessible(true);
			testInt.set(obj, 1);
			testShort.setAccessible(true);
			testShort.set(obj, s);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

		
		Serializer serializer = new Serializer();
		Document serialTestDoc = serializer.serialize(obj);
		
		
		assertEquals(xmlOutput.outputString(testDoc), xmlOutput.outputString(serialTestDoc));

		
		
	}

}
