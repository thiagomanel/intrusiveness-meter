package analysis;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class FastSummaryTest {

	@Test
	public void testGetNumberOfDiscomfortReports() {
		List<Long> discomfortTimes1 = new LinkedList<Long>();
		discomfortTimes1.add(0L);
		discomfortTimes1.add(1L);
		discomfortTimes1.add(2L);
		discomfortTimes1.add(8L);
		discomfortTimes1.add(14L);
		discomfortTimes1.add(15L);
		discomfortTimes1.add(16L);
		discomfortTimes1.add(17L);
		discomfortTimes1.add(25L);
		discomfortTimes1.add(36L);
		
		Assert.assertEquals(5, FastSummary.getNumberOfDiscomfortReports(discomfortTimes1, 3));
		
		List<Long> discomfortTimes2 = new LinkedList<Long>();
		discomfortTimes2.add(0L);
		discomfortTimes2.add(5L);
		discomfortTimes2.add(10L);
		discomfortTimes2.add(15L);
		discomfortTimes2.add(20L);
		discomfortTimes2.add(25L);
		
		Assert.assertEquals(6, FastSummary.getNumberOfDiscomfortReports(discomfortTimes2, 0));
		Assert.assertEquals(3, FastSummary.getNumberOfDiscomfortReports(discomfortTimes2, 5));
	}
}
