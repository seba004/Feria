package frisbit.profit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validador {
	private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String  PATTERN_NAME ="^[A-Za-z]{4,} [A-Za-z]{4,}";
	private static final String PATTERN_DATE="^(19|20)[0-9]{2}[\\-](0?[1-9]|1[012])[\\-](0?[1-9]|[12][0-9]|3[01])";
	private static final String PATTERN_PASS="^[a-zA-Z0-9]{6,15}";
	
	 public static boolean validateemail(String email) {
		 
	        // Compiles the given regular expression into a pattern.
	        Pattern pattern = Pattern.compile(PATTERN_EMAIL);
	 
	        // Match the given input against this pattern
	        Matcher matcher = pattern.matcher(email);
	        return matcher.matches();//devuellve true cuando calza
	 
	    }
	
	 public static boolean validatename(String nombre) {
		 
	        // Compiles the given regular expression into a pattern.
	        Pattern pattern = Pattern.compile(PATTERN_NAME);
	 
	        // Match the given input against this pattern
	        Matcher matcher = pattern.matcher(nombre);
	        return matcher.matches();//devuellve true cuando calza
	 
	    }
	 
	 public static boolean validatedate(String fecha) {
		 
	        // Compiles the given regular expression into a pattern.
	        Pattern pattern = Pattern.compile(PATTERN_DATE);
	 
	        // Match the given input against this pattern
	        Matcher matcher = pattern.matcher(fecha);
	        return matcher.matches();//devuellve true cuando calza
	 
	    }
	 public static boolean validatepass(String pass) {
		 
	        // Compiles the given regular expression into a pattern.
	        Pattern pattern = Pattern.compile(PATTERN_PASS);
	 
	        // Match the given input against this pattern
	        Matcher matcher = pattern.matcher(pass);
	        return matcher.matches();//devuellve true cuando calza
	 
	    }
}
