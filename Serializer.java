import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.Vector;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Text;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;;





public class Serializer {
	
		private int IDNumber;
	
		private IdentityHashMap<Object, Integer> objectsSerialized;
		
		Document doc;
		
		Element theRoot;
		
		String fileName = "test.xml";
		
		
		public Document serialize(Object obj){
			
		IDNumber = 0;
		objectsSerialized = new IdentityHashMap<Object, Integer>();
		doc = new Document();
		theRoot = new Element("serialized");
			
		
		

		System.out.println("serializing obj " + obj.getClass());
		serializeCurrentObject(obj, IDNumber++);
			
		doc.setRootElement(theRoot);
		
		return doc;
	}
		




public void serializeCurrentObject(Object obj, int ID){
	
	if(objectsSerialized.containsKey(obj)){
		System.out.println("Object already serialized");
		return;
	}
	
	Vector<Pair<Object, Integer>> referencesToSerialize = new Vector<Pair<Object, Integer>>();
	
	Element objElement = new Element("object");
	objElement.setAttribute("class", obj.getClass().getName());
	
	objElement.setAttribute("id", String.valueOf(ID));
	objectsSerialized.put(obj, ID);
	
	
	//--IF ARRAY, EXPAND ARRAY OUT
	
	if(obj.getClass().isArray()){
		
		System.out.println("ARRAY OBJECT");
		
		Class<?> componentType = (obj.getClass().getComponentType());

		int arrayLength = Array.getLength(obj);
		
		objElement.setAttribute("length", String.valueOf(arrayLength));
		
		
		if(componentType.isPrimitive()){
			System.out.println("Serializing primitive array");
			
			for(int j = 0; j < arrayLength; j++){
				
				Element value = new Element("value");
				
				value.addContent(new Text(Array.get(obj, j).toString()));
				
				objElement.addContent(value);
			}

		}
		
		else{
			System.out.println("Serializing object reference array");
			
			
			for(int j = 0; j < arrayLength; j++){
				
				Element reference = new Element("reference");
				
				reference.addContent(new Text(String.valueOf(IDNumber)));
				
				referencesToSerialize.add(new Pair(Array.get(obj, j), IDNumber++));
				
				objElement.addContent(reference);
			}
			
			
			
		}
		
	}
	
//************
	
	
	
	
	Class objClass = obj.getClass();
		
	
	Field[] fields = objClass.getDeclaredFields();
	
	System.out.println("accessing fields");
	
	for(int i = 0; i < fields.length; i++){
		
		System.out.println(fields.length);
		
		Field f = fields[i];
		Object fieldObject = null;
		f.setAccessible(true);
		try {
			fieldObject = f.get(obj);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		System.out.println(f.getType());
		System.out.println(f.getName());
		
		
	
		System.out.println("Not an array");
		
		if(f.getType().isPrimitive()){
			
			System.out.println("SERIALIZING PRIMITIVE FIELD");
			
			Element primitiveField = new Element("field");
			primitiveField.setAttribute("name", f.getName());
			primitiveField.setAttribute("declaringclass", f.getDeclaringClass().getName());
			
			
			Element value = new Element("value");
			try {
				value.addContent(new Text(f.get(obj).toString()));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
			primitiveField.addContent(value);
			
			objElement.addContent(primitiveField);
			

		}
		
		else{
			System.out.println("SERIALIZING OBJECT FIELD");
			
			Element objectTag = new Element("field");
			
			objectTag.setAttribute("name", f.getName());
			objectTag.setAttribute("declaringclass", f.getDeclaringClass().getName());
			
			Element reference = new Element("reference");
			
			reference.addContent(new Text(String.valueOf(IDNumber)));
			
			referencesToSerialize.add(new Pair(fieldObject, IDNumber++));
			
			
			objectTag.addContent(reference);
			
			objElement.addContent(objectTag);
			
			
			
		}
			

		
	}
	
		theRoot.addContent(objElement);
	
		for(int i = 0; i < referencesToSerialize.size(); i++)
			serializeCurrentObject(referencesToSerialize.get(i).getL(), referencesToSerialize.get(i).getR());
			
	
	
	}


	

}
