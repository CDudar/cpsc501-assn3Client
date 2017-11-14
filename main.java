import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Text;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;;

public class main {

	public static void main(String[] args){
		
		ObjectCreator objCreator = new ObjectCreator();
		
		Vector<Object> objectsToSerialize = objCreator.createUserObjects();
		
		Serializer serializer = new Serializer();
		Document[] docList = new Document[objectsToSerialize.size()];
		
		for(int i = 0; i < objectsToSerialize.size(); i++){
			docList[i] = serializer.serialize(objectsToSerialize.get(i));
		}
		
		
		
		
		XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
		int count = 0;
		
		ChooseDocuments choose = new ChooseDocuments(docList);
		
		choose.chooseEligibleDocuments();
		
		Vector<Document> documentsToSend = choose.getEligibleDocuments();
		
		Client client = new Client(documentsToSend.size());
		
		for(int i = 0; i < documentsToSend.size(); i++){

			try {
				System.out.println("trying send " + i);
				xmlOutput.output(documentsToSend.get(i), new FileOutputStream(new File("send" + String.valueOf(i) + ".xml")));
				client.send("send" + String.valueOf(i) + ".xml");
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		

	}
	
}
