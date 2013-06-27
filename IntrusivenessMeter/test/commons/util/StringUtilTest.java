package commons.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class StringUtilTest {

	private static final String STRING_1 = "STRING 1";
	private static final String STRING_2 = "STRING 2";
	private static final String STRING_3 = "STRING 3";
	private static final String STRING_4 = "STRING 4";

	@Test
	public void testConcatSeparatorListObjectValues() {
		String separator = "-";
		List<Object> list = new ArrayList<Object>();
		list.add(STRING_1);
		list.add(STRING_2);
		list.add(STRING_3);
		list.add(STRING_4);
		
		String result = StringUtil.concat(separator, list);
		
		assertEquals(STRING_1 + separator + STRING_2 + separator + STRING_3 + separator + STRING_4, result);
	}
}
