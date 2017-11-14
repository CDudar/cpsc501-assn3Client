import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class ChooseDocuments {

	int userFileCount = 0;
	
	Scanner userInput;
	
	Document[] docList;
	
	Vector<Document> eligibleDocuments = new Vector<Document>();
	
	XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
	
	String line;
	
	ChooseDocuments(Document[] docList){
		
		this.docList = docList;
		
		userInput = new Scanner(System.in);
	}
	
	
	public void chooseEligibleDocuments(){
		
		while(true){
		
		System.out.println("Choose a document to store/print out/send over a network connection");
		System.out.println("Enter the desired documents number");
		System.out.println("Enter -1 to finish and send chosen documents over network");
		
		for(int i = 0; i < docList.length; i++){
			
			System.out.println("Document " + i);
			}
		
			line = userInput.nextLine();
			
			int userInput = 0;
			try{
			userInput = Integer.valueOf(line);
			}
			catch(NumberFormatException e){
				System.out.println("Invalid number");
				continue;
			}
			
			if(userInput >= 0 && userInput < docList.length){
				System.out.println("Document " + userInput + " chosen");	
			}
			else if(userInput == -1){
				System.out.println("Sending chosen docs over network connection");
				break;
			}
			else{
				System.out.println("Invalid number, out of range");
				continue;
			}
			
			chooseDocumentAction(docList[userInput]);	
		}
		
	}
	
	
	
	public void chooseDocumentAction(Document doc){		
		
		while(true){
			System.out.println("Enter -1 to return to document selection");
			System.out.println("Enter 0 to store document to file");
			System.out.println("Enter 1 to print out document");
			System.out.println("Enter 2 to make document elgible for sending over a network connection");

			
			line = userInput.nextLine();
			
			int userInput = 0;
			try{
			userInput = Integer.valueOf(line);
			}
			catch(NumberFormatException e){
				System.out.println("Invalid number");
				continue;
			}
			
			if(userInput == -1){
				System.out.println("Returning to document selection");
				break;
			}
			else if(userInput == 0){
				System.out.println("Document stored to file UserStorage" + String.valueOf(userFileCount) + ".xml");
				try {
					xmlOutput.output(doc, new FileOutputStream(new File("UserStorage" + String.valueOf(userFileCount++) + ".xml")));
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			else if(userInput == 1){
				System.out.println("Printing document to console\n");
				String docString = xmlOutput.outputString(doc);
				System.out.println(docString);
				
			}
			else if(userInput == 2){
				System.out.println("Adding document to eligible documents");
				eligibleDocuments.add(doc);
			}
			else{
				System.out.println("Input out of range");
				continue;
			}
			
			
		}
		
		
		
	}
	
	
	public Vector<Document> getEligibleDocuments(){
		return eligibleDocuments;
		
	}
	
	
	
	
}
