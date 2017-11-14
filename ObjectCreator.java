import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Scanner;
import java.util.Vector;

public class ObjectCreator {

	private Scanner userInput;
	private String line;
	private Object userValue;
	
	Vector<Object> objectsToSerialize = new Vector<Object>();
	
	public ObjectCreator(){
	}
	
	
	public Vector<Object> createUserObjects(){
		
		userInput = new Scanner(System.in);
		
		while(true){
			
			System.out.println( 
					  "Enter 0 when done creating objects\n"
					+ "Enter 1 to create a simple object with only primitives for instance variables\n"
					+ "Enter 2 to create an object that contains references to other objects\n"
					+ "Enter 3 to create an object that contains an array of primitives\n"
					+ "Enter 4 to create an object that contains an array of object references\n"
					+ "Enter 5 to create an object that uses a Java collection class that refers to several other objects"
					);
			
			
			line = userInput.nextLine();
			int userChoice = 0;
			
			try{
				userChoice = Integer.valueOf(line);
			}
			catch(NumberFormatException e){
				System.out.println("Invalid input\n");
				continue;
			}
			
			if(userChoice == 0){
				System.out.println("Quitting");
				break;
			}
			else if(userChoice == 1){
				System.out.println("Creating simple object");
				objectsToSerialize.add(createSimpleObject(new PrimitiveOnlyType(0)));
			}
			else if(userChoice == 2){
				System.out.println("Creating object with references to other objects");
				objectsToSerialize.add(createSimpleObject(new ObjectA()));
			}
			else if(userChoice == 3){
				System.out.println("Creating object containing array of primitives");
				objectsToSerialize.add(createSimpleObject(new ArrayOfPrimitivesTypes()));	
				
			}
			else if(userChoice == 4){
				System.out.println("Creating object containing an array of object references");
				objectsToSerialize.add(createSimpleObject(new ArrayOfObjectReferencesType()));
				
			}
			else if(userChoice == 5){
				System.out.println("Creating object that uses an instance of Java collection classes");
				
			}
			
			
			System.out.println("");
			
			for(int i = 0; i < objectsToSerialize.size(); i++){
				Object currentObject = objectsToSerialize.get(i);
				if( currentObject instanceof ArrayOfObjectReferencesType ){
					System.out.println( ((ArrayOfObjectReferencesType)currentObject).objReferences[0]);
					System.out.println( ((ObjectB)(((ArrayOfObjectReferencesType)currentObject).objReferences[1])).test );
				}
				
			}
			
			
		}

		return objectsToSerialize;
		
	}
	
	
	
	
	public Object createSimpleObject(Object objectToSerialize){
		
		Object obj = objectToSerialize;
		Class objClass = obj.getClass();
		
		Field[] fields = objClass.getDeclaredFields();
		
		System.out.println(obj.toString() + " has " + fields.length + " fields");
		
		for(int i = 0; i < fields.length; i++){
			
			Field currentField = fields[i];
	
			currentField.setAccessible(true);
			Object fieldObject = null;
			try {
				fieldObject = currentField.get(obj);
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}
			
			System.out.println("Checking");
			
			
			//================= ARRAY CHECK =====================
			
			if(currentField.getType().isArray()){
				System.out.println("IN");
				Class<?> componentType = (currentField.getType().getComponentType());
				
				int arrayLength = 0;
				try{
				arrayLength = Array.getLength(fieldObject);
				}
				catch(NullPointerException e){
				System.out.println("Setting null array to default length of 2");
				arrayLength = 2;

				}
				
				//================ PRIMITIVE ARRAY ==============
				
				if(componentType.isPrimitive()) {
					System.out.println("SUPERIN");
					
					System.out.println("Field " + i + " is an array that holds primitives of type" + componentType.toString());
					
					for(int j = 0; j < arrayLength; j++){
						
						System.out.println("Enter a valid value for index " + j);
						
						
						while(true){
							
							try{
							line = userInput.nextLine();
							
							userValue = UtilityMethods.toObject(componentType, line);					

							Array.set(fieldObject, j, userValue);
							}
							catch(NumberFormatException e){
								System.out.println("Invalid value entered");
								continue;
							}
							
							break;
						}
						
					}
					
				}
				//================== OBJECT REFERENCE ARRAY ==============
				else{
					System.out.println("Field " + i + " is an array that holds Object references");
					
					for(int j = 0; j < arrayLength; j++){
			
						
						Object objToRecurseOn;						
						while(true){
							
							System.out.println("Choose an object for index " + j);
							System.out.println("Enter 1 for ObjectA");
							System.out.println("Enter 2 for ObjectB");
								
							line = userInput.nextLine();
							


							if(line.equals("1")) {
								objToRecurseOn = new ObjectA();
							}
							else if(line.equals("2")) {
								objToRecurseOn = new ObjectB();
							}
							else {
								System.out.println("Invalid value entered");
								continue;
							}
							
							break;
						}		
							
						Array.set(fieldObject, j, createSimpleObject(objToRecurseOn));
							

							

						
							
					}
					
				}
			}
			
			
			//============== NON-ARRAY FIELDS ==============
			
			
			
			else{
			
				//================ PRIMITIVE FIELD ============
				
				if( currentField.getType().isPrimitive() || currentField.getType().equals(String.class)){
				
					while(true){
		
						
						System.out.println("Field " + i + " has type " + currentField.getType().toString() );
						System.out.println("Enter a valid value for the field type");
						
						try{
						line = userInput.nextLine();
						
						userValue = line;
						if(! currentField.getType().equals(String.class)){
							userValue = UtilityMethods.toObject(currentField.getType(), line);					
						}
						
						System.out.println(userValue.getClass());
						
						currentField.set(obj, userValue);
						
						}
						catch(NumberFormatException e){
							System.out.println("Invalid value entered");
							continue;
						} catch (IllegalArgumentException e) {
							System.out.println("Illegal argument");
							continue;
						} catch (IllegalAccessException e) {
							System.out.println("Illegal access attempted");
							continue;
						}
		
						break;
					}
				}
				
				
				
				//============= SINGLE OBJECT FIELD ==============
				
				else{
					
					System.out.println("Field " + i + " is an Object reference");
					
					
					Object objToRecurseOn;
					
					while(true) {
						
						System.out.println("Choose an object for the field value");
						System.out.println("Enter 1 for ObjectA");
						System.out.println("Enter 2 for ObjectB");
							
						line = userInput.nextLine();
						


						if(line.equals("1")) {
							objToRecurseOn = new ObjectA();
						}
						else if(line.equals("2")) {
							objToRecurseOn = new ObjectB();
						}
						else {
							System.out.println("Invalid value entered");
							continue;
						}
						
						break;
					}		
						
						try {
							currentField.set(obj, createSimpleObject(objToRecurseOn));
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
				}
				
			}

		
//		try {
//		System.out.println(objClass.getDeclaredField("stringList").get(obj) );
//	} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
//		e.printStackTrace();
//	}
		
		return obj;
	
	}
		

}


	
		
		
	

	
	
	
	
	

