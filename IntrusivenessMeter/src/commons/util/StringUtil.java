package commons.util;

import java.util.Iterator;
import java.util.List;

public class StringUtil {
	/**
	 * Checks if all the characters of the given string are numeric or equal to 
	 * '.' or '-'. It returns false if find a blank character.
	 * @param string
	 * @return
	 */
	// FIXME test it !!!
	public static boolean isNumeric(String string) {
		for (int i = 0; i < string.length(); i++) {
			if (!Character.isDigit(string.charAt(i)) && !(string.charAt(i) == '.') && !(string.charAt(i) == '-')) {
				return false;
			}
		}
		return true;
	}
	
	// FIXME test it !!!
	public static String concat(String ... strings) {
		StringBuilder builder = new StringBuilder();
		for (String string : strings) {
			builder.append(string);
		}
		return builder.toString();
	}
	
	public static String concat(String separator, String[] strings, int startIndex, int endIndex) {
		StringBuilder builder = new StringBuilder();
		
		int index = startIndex;
		builder.append(strings[index++]);
		
		for (; index <= endIndex; index++) {
			builder.append(separator);
			builder.append(strings[index]);
		}
		
		return builder.toString();
	}

	public static String concat(String separator, List<Object> values) {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		Iterator<Object> iterator = values.iterator();
		
		while (iterator.hasNext()) {
			if (!first) {
				builder.append(separator);
			}
			builder.append(iterator.next().toString());
			first = false;
		}
		
		return builder.toString();
	}
}
