import java.util.HashMap;
import java.util.Map;

public class UtilityMethods {

	public final static Map<Class<?>, Class<?>> map = new HashMap<Class<?>, Class<?>>();
	static {
	    map.put(boolean.class, Boolean.class);
	    map.put(byte.class, Byte.class);
	    map.put(short.class, Short.class);
	    map.put(char.class, Character.class);
	    map.put(int.class, Integer.class);
	    map.put(long.class, Long.class);
	    map.put(float.class, Float.class);
	    map.put(double.class, Double.class);
	}
	
	
	public static Object toObject( Class clazz, String value ) {
		
		System.out.println("Class being passed to utility is: " + clazz.toString());
		
		if(map.containsKey(clazz)){
			clazz = map.get(clazz);
		}
		
	    if( Boolean.class == clazz ) return Boolean.parseBoolean( value );
	    if( Byte.class == clazz ) return Byte.parseByte( value );
	    if( Short.class == clazz ) return Short.parseShort( value );
	    if( Integer.class == clazz ) return Integer.parseInt( value );
	    if( Long.class == clazz ) return Long.parseLong( value );
	    if( Float.class == clazz ) return Float.parseFloat( value );
	    if( Double.class == clazz ) return Double.parseDouble( value );
	    
	    return value;
	}
}
