package analysis;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import analysis.data.Execution;
import analysis.data.HadoopInformation;
import analysis.data.HadoopMachineUsage;

public class ClusteringTest {
	
	private Discomfort discomfort1;
	private Discomfort discomfort2;
	private Discomfort discomfort3;
	private Discomfort discomfort4;
	private Discomfort discomfort5;
	
	private Hadoop hadoop1;
	private Hadoop hadoop2;
	private Hadoop hadoop3;
	private Hadoop hadoop4;
	private Hadoop hadoop5;
	
	private Clustering clustering;
	
	private Map<Long, List<Integer>> hadoopProcesses1;
	private Map<Long, List<Integer>> hadoopProcesses2;
	private Map<Long, List<Integer>> hadoopProcesses3;
	private Map<Long, List<Integer>> hadoopProcesses4;
	private Map<Long, List<Integer>> hadoopProcesses5;
	
	private HadoopMachineUsage hadoopMachineUsage1;
	private HadoopMachineUsage hadoopMachineUsage2;
	private HadoopMachineUsage hadoopMachineUsage3;
	private HadoopMachineUsage hadoopMachineUsage4;
	private HadoopMachineUsage hadoopMachineUsage5;
	
	private HadoopInformation hadoopInfo1;
	private HadoopInformation hadoopInfo2;
	private HadoopInformation hadoopInfo3;
	private HadoopInformation hadoopInfo4;
	private HadoopInformation hadoopInfo5;
	
	private List<Execution> executions1;
	private List<Execution> executions2;
	private List<Execution> executions3;
	private List<Execution> executions4;
	private List<Execution> executions5;
	
	private Map<Long, Double> hadoopCPUUsage1;
	private Map<Long, Double> hadoopCPUUsage2;
	private Map<Long, Double> hadoopCPUUsage3;
	private Map<Long, Double> hadoopCPUUsage4;
	private Map<Long, Double> hadoopCPUUsage5;
	
	private Map<Long, Double> hadoopMemoryUsage1;
	private Map<Long, Double> hadoopMemoryUsage2;
	private Map<Long, Double> hadoopMemoryUsage3;
	private Map<Long, Double> hadoopMemoryUsage4;
	private Map<Long, Double> hadoopMemoryUsage5;
	
	private Map<Long, String> benchmarks1;
	private Map<Long, String> benchmarks2;
	private Map<Long, String> benchmarks3;
	private Map<Long, String> benchmarks4;
	private Map<Long, String> benchmarks5;
	
	private static final String BENCHMARK_1 = "mr";
	private static final String BENCHMARK_2 = "teragen";
	private static final String BENCHMARK_3 = "dfread";
	
	private static long time1 = 1000;
	private static long time2 = time1 + 1;
	private static long time3 = time2 + 1;
	private static long time4 = time3 + 1;
	private static long time5 = time4 + 1;
	private static long time6 = time5 + 1;
	private static long time7 = time6 + 1;
	private static long time8 = time7 + 1;
	private static long time9 = time8 + 1;
	private static long time10 = time9 + 1;
	private static long time11 = time10 + 1;
	private static long time12 = time11 + 1;
	private static long time13 = time12 + 1;
	private static long time14 = time13 + 1;
	private static long time15 = time14 + 1;
	private static long time16 = time15 + 1;
	private static long time17 = time16 + 1;
	private static long time18 = time17 + 1;
	private static long time19 = time18 + 1;
	private static long time20 = time19 + 1;
	
	private static final Double HADOOP_CPU_1 = 20.0;
	private static final Double HADOOP_CPU_2 = HADOOP_CPU_1 + 10;
	private static final Double HADOOP_CPU_3 = HADOOP_CPU_2 + 10;
	private static final Double HADOOP_CPU_4 = HADOOP_CPU_3 + 10;
	private static final Double HADOOP_CPU_5 = HADOOP_CPU_4 + 10;
	private static final Double HADOOP_CPU_6 = HADOOP_CPU_5 + 10;
	
	private static final Double HADOOP_MEMORY_1 = 23.0;
	private static final Double HADOOP_MEMORY_2 = HADOOP_MEMORY_1 + 20;
	private static final Double HADOOP_MEMORY_3 = HADOOP_MEMORY_2 + 20;
	private static final Double HADOOP_MEMORY_4 = HADOOP_MEMORY_3 + 20;
	private static final Double HADOOP_MEMORY_5 = HADOOP_MEMORY_4 + 20;
	
	@Before
	public void setUp() throws Exception {
		setUpConfiguration1();
		setUpConfiguration2();
		setUpConfiguration3();
		setUpConfiguration4();
		setUpConfigurationNotSynchronizedTimes();
	}

	private void setUpConfiguration1() {
		List<Long> discomfortReportTimes = new LinkedList<Long>();
		discomfortReportTimes.add(time1);
		discomfortReportTimes.add(time4);
		
		discomfort1 = new Discomfort(discomfortReportTimes);
		
		executions1 = new LinkedList<Execution>();
		executions1.add(new Execution(time1, time5));
		
		hadoopCPUUsage1 = new HashMap<Long, Double>();
		hadoopCPUUsage1.put(time1, HADOOP_CPU_1);
		hadoopCPUUsage1.put(time2, HADOOP_CPU_2);
		hadoopCPUUsage1.put(time3, HADOOP_CPU_3);
		hadoopCPUUsage1.put(time4, HADOOP_CPU_4);
		hadoopCPUUsage1.put(time5, HADOOP_CPU_5);
		hadoopCPUUsage1.put(time6, HADOOP_CPU_6);
		
		hadoopMemoryUsage1 = new HashMap<Long, Double>();
		hadoopMemoryUsage1.put(time1, HADOOP_MEMORY_1);
		hadoopMemoryUsage1.put(time2, HADOOP_MEMORY_2);
		hadoopMemoryUsage1.put(time3, HADOOP_MEMORY_3);
		hadoopMemoryUsage1.put(time4, HADOOP_MEMORY_4);
		hadoopMemoryUsage1.put(time5, HADOOP_MEMORY_5);
		
		hadoopProcesses1 = new HashMap<Long, List<Integer>>();
		
		benchmarks1 = new HashMap<Long, String>();
		benchmarks1.put(time1, BENCHMARK_1);
		
		hadoopMachineUsage1 = new HadoopMachineUsage(hadoopCPUUsage1, hadoopMemoryUsage1, hadoopProcesses1);
		hadoopInfo1 = new HadoopInformation(benchmarks1);
		hadoop1 = new Hadoop(hadoopMachineUsage1, hadoopInfo1);
	}

	private void setUpConfiguration2() {
		List<Long> discomfortReportTimes = new LinkedList<Long>();
		discomfortReportTimes.add(time2);
		discomfortReportTimes.add(time4);
		
		discomfort2 = new Discomfort(discomfortReportTimes);
		
		executions2 = new LinkedList<Execution>();
		executions2.add(new Execution(time1, time5));
		
		hadoopCPUUsage2 = new HashMap<Long, Double>();
		hadoopCPUUsage2.put(time1, 0.0);
		hadoopCPUUsage2.put(time2, 100.0);
		hadoopCPUUsage2.put(time3, 50.0);
		hadoopCPUUsage2.put(time4, 25.0);
		hadoopCPUUsage2.put(time5, 70.0);
		hadoopCPUUsage2.put(time6, 10.0);
		
		hadoopMemoryUsage2 = new HashMap<Long, Double>();
		hadoopMemoryUsage2.put(time1, HADOOP_MEMORY_1);
		hadoopMemoryUsage2.put(time2, HADOOP_MEMORY_2);
		hadoopMemoryUsage2.put(time3, HADOOP_MEMORY_3);
		hadoopMemoryUsage2.put(time4, HADOOP_MEMORY_4);
		hadoopMemoryUsage2.put(time5, HADOOP_MEMORY_5);
		
		hadoopProcesses2 = new HashMap<Long, List<Integer>>();
		
		benchmarks2 = new HashMap<Long, String>();
		benchmarks2.put(time1, BENCHMARK_1);
		
		hadoopMachineUsage2 = new HadoopMachineUsage(hadoopCPUUsage2, hadoopMemoryUsage2, hadoopProcesses2);
		hadoopInfo2 = new HadoopInformation(benchmarks2);
		hadoop2 = new Hadoop(hadoopMachineUsage2, hadoopInfo2);
	}
	
	// 2 executions
	private void setUpConfiguration3() {
		List<Long> discomfortReportTimes = new LinkedList<Long>();
		discomfortReportTimes.add(time2);
		discomfortReportTimes.add(time4);
		discomfortReportTimes.add(time7);
		discomfortReportTimes.add(time9);
		
		discomfort3 = new Discomfort(discomfortReportTimes);
		
		executions3 = new LinkedList<Execution>();
		executions3.add(new Execution(time1, time5));
		executions3.add(new Execution(time6, time10));
		
		hadoopCPUUsage3 = new HashMap<Long, Double>();
		hadoopCPUUsage3.put(time1, 0.0);
		hadoopCPUUsage3.put(time2, 100.0);
		hadoopCPUUsage3.put(time3, 50.0);
		hadoopCPUUsage3.put(time4, 25.0);
		hadoopCPUUsage3.put(time5, 75.0);
		hadoopCPUUsage3.put(time6, 15.0);
		hadoopCPUUsage3.put(time7, 51.0);
		hadoopCPUUsage3.put(time8, 49.0);
		hadoopCPUUsage3.put(time9, 100.0);
		hadoopCPUUsage3.put(time10, 30.0);
		
		hadoopMemoryUsage3 = new HashMap<Long, Double>();
		hadoopMemoryUsage3.put(time1, HADOOP_MEMORY_1);
		hadoopMemoryUsage3.put(time2, HADOOP_MEMORY_2);
		hadoopMemoryUsage3.put(time3, HADOOP_MEMORY_3);
		hadoopMemoryUsage3.put(time4, HADOOP_MEMORY_4);
		hadoopMemoryUsage3.put(time5, HADOOP_MEMORY_5);
		hadoopMemoryUsage3.put(time6, 0.0);
		hadoopMemoryUsage3.put(time7, 0.0);
		hadoopMemoryUsage3.put(time8, 0.0);
		hadoopMemoryUsage3.put(time9, 0.0);
		hadoopMemoryUsage3.put(time10, 0.0);
		
		hadoopProcesses3 = new HashMap<Long, List<Integer>>();
		
		benchmarks3 = new HashMap<Long, String>();
		benchmarks3.put(time1, BENCHMARK_1);
		benchmarks3.put(time6, BENCHMARK_2);
		
		hadoopMachineUsage3 = new HadoopMachineUsage(hadoopCPUUsage3, hadoopMemoryUsage3, hadoopProcesses3);
		hadoopInfo3 = new HadoopInformation(benchmarks3);
		hadoop3 = new Hadoop(hadoopMachineUsage3, hadoopInfo3);
	}
	
	// 3 executions
	private void setUpConfiguration4() {
		List<Long> discomfortReportTimes = new LinkedList<Long>();
		discomfortReportTimes.add(time2);
		discomfortReportTimes.add(time4);
		discomfortReportTimes.add(time7);
		discomfortReportTimes.add(time10);
		
		discomfort4 = new Discomfort(discomfortReportTimes);
		
		executions4 = new LinkedList<Execution>();
		executions4.add(new Execution(time1, time3));
		executions4.add(new Execution(time4, time7));
		executions4.add(new Execution(time8, time10));
		
		hadoopCPUUsage4 = new HashMap<Long, Double>();
		hadoopCPUUsage4.put(time1, 0.0);
		hadoopCPUUsage4.put(time2, 100.0);
		hadoopCPUUsage4.put(time3, 50.0);
		hadoopCPUUsage4.put(time4, 25.0);
		hadoopCPUUsage4.put(time5, 75.0);
		hadoopCPUUsage4.put(time6, 15.0);
		hadoopCPUUsage4.put(time7, 51.0);
		hadoopCPUUsage4.put(time8, 49.0);
		hadoopCPUUsage4.put(time9, 100.0);
		hadoopCPUUsage4.put(time10, 30.0);
		
		hadoopMemoryUsage4 = new HashMap<Long, Double>();
		hadoopMemoryUsage4.put(time1, HADOOP_MEMORY_1);
		hadoopMemoryUsage4.put(time2, HADOOP_MEMORY_2);
		hadoopMemoryUsage4.put(time3, HADOOP_MEMORY_3);
		hadoopMemoryUsage4.put(time4, HADOOP_MEMORY_4);
		hadoopMemoryUsage4.put(time5, HADOOP_MEMORY_5);
		hadoopMemoryUsage4.put(time6, 0.0);
		hadoopMemoryUsage4.put(time7, 0.0);
		hadoopMemoryUsage4.put(time8, 0.0);
		hadoopMemoryUsage4.put(time9, 0.0);
		hadoopMemoryUsage4.put(time10, 0.0);
		
		hadoopProcesses4 = new HashMap<Long, List<Integer>>();
		
		benchmarks4 = new HashMap<Long, String>();
		benchmarks4.put(time1, BENCHMARK_1);
		benchmarks4.put(time4, BENCHMARK_2);
		benchmarks4.put(time7, BENCHMARK_3);
		
		hadoopMachineUsage4 = new HadoopMachineUsage(hadoopCPUUsage4, hadoopMemoryUsage4, hadoopProcesses4);
		hadoopInfo4 = new HadoopInformation(benchmarks4);
		hadoop4 = new Hadoop(hadoopMachineUsage4, hadoopInfo4);
	}	
	
	private void setUpConfigurationNotSynchronizedTimes() {
		List<Long> discomfortReportTimes = new LinkedList<Long>();
		discomfortReportTimes.add(time2);
		discomfortReportTimes.add(time17);
		
		discomfort5 = new Discomfort(discomfortReportTimes);
		
		executions5 = new LinkedList<Execution>();
		executions5.add(new Execution(time1, time10));
		executions5.add(new Execution(time11, time20));
		
		hadoopCPUUsage5 = new HashMap<Long, Double>();
		hadoopCPUUsage5.put(time1, 100.0);
		hadoopCPUUsage5.put(time3, 0.0);
		hadoopCPUUsage5.put(time5, 50.0);
		hadoopCPUUsage5.put(time8, 25.0);
		hadoopCPUUsage5.put(time10, 75.0);
		hadoopCPUUsage5.put(time11, 12.0);
		hadoopCPUUsage5.put(time15, 24.0);
		hadoopCPUUsage5.put(time18, 13.0);
		hadoopCPUUsage5.put(time20, 99.0);
		
		hadoopMemoryUsage5 = new HashMap<Long, Double>();
		hadoopMemoryUsage5.put(time1, HADOOP_MEMORY_1);
		hadoopMemoryUsage5.put(time2, HADOOP_MEMORY_2);
		hadoopMemoryUsage5.put(time3, HADOOP_MEMORY_3);
		hadoopMemoryUsage5.put(time4, HADOOP_MEMORY_4);
		hadoopMemoryUsage5.put(time5, HADOOP_MEMORY_5);
		hadoopMemoryUsage5.put(time6, 0.0);
		hadoopMemoryUsage5.put(time7, 0.0);
		hadoopMemoryUsage5.put(time8, 0.0);
		hadoopMemoryUsage5.put(time9, 0.0);
		hadoopMemoryUsage5.put(time10, 0.0);
		
		hadoopProcesses5 = new HashMap<Long, List<Integer>>();
		
		benchmarks5 = new HashMap<Long, String>();
		benchmarks5.put(time1, BENCHMARK_1);
		benchmarks5.put(time11, BENCHMARK_2);
		
		hadoopMachineUsage5 = new HadoopMachineUsage(hadoopCPUUsage5, hadoopMemoryUsage5, hadoopProcesses5);
		hadoopInfo5 = new HadoopInformation(benchmarks5);
		hadoop5 = new Hadoop(hadoopMachineUsage5, hadoopInfo5);
	}
	
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testGetCPUUsageDiscomfortProbability() {
		clustering = new Clustering(hadoop1, discomfort1, executions1);
		Map<Double, Double> result1 = clustering.getHadoopCPUUsageDiscomfortProbability();
		
		assertEquals(0.0, result1.get(0.0), 0.005);
		assertEquals(0.0, result1.get(10.0), 0.005);
		assertEquals(1.0, result1.get(20.0), 0.005);
		assertEquals(1.0, result1.get(30.0), 0.005);
		assertEquals(1.0, result1.get(40.0), 0.005);
		assertEquals(1.0, result1.get(50.0), 0.005);
		assertEquals(1.0, result1.get(60.0), 0.005);
		assertEquals(1.0, result1.get(70.0), 0.005);
		assertEquals(1.0, result1.get(80.0), 0.005);
		assertEquals(1.0, result1.get(90.0), 0.005);
		assertEquals(1.0, result1.get(100.0), 0.005);
		
		clustering = new Clustering(hadoop2, discomfort2, executions2);
		Map<Double, Double> result2 = clustering.getHadoopCPUUsageDiscomfortProbability();
		
		assertEquals(0.0, result2.get(0.0), 0.005);
		assertEquals(0.0, result2.get(10.0), 0.005);
		assertEquals(0.0, result2.get(20.0), 0.005);
		assertEquals(0.0, result2.get(30.0), 0.005);
		assertEquals(0.0, result2.get(40.0), 0.005);
		assertEquals(0.0, result2.get(50.0), 0.005);
		assertEquals(0.0, result2.get(60.0), 0.005);
		assertEquals(0.0, result2.get(70.0), 0.005);
		assertEquals(0.0, result2.get(80.0), 0.005);
		assertEquals(0.0, result2.get(90.0), 0.005);
		assertEquals(1.0, result2.get(100.0), 0.005);
		
		clustering = new Clustering(hadoop3, discomfort3, executions3);
		Map<Double, Double> result3 = clustering.getHadoopCPUUsageDiscomfortProbability();
		
		assertEquals(0.0, result3.get(0.0), 0.005);
		assertEquals(0.0, result3.get(10.0), 0.005);
		assertEquals(0.0, result3.get(20.0), 0.005);
		assertEquals(0.0, result3.get(30.0), 0.005);
		assertEquals(0.0, result3.get(40.0), 0.005);
		assertEquals(0.0, result3.get(50.0), 0.005);
		assertEquals(0.5, result3.get(60.0), 0.005);
		assertEquals(0.5, result3.get(70.0), 0.005);
		assertEquals(0.5, result3.get(80.0), 0.005);
		assertEquals(0.5, result3.get(90.0), 0.005);
		assertEquals(1.0, result3.get(100.0), 0.005);
		
		clustering = new Clustering(hadoop4, discomfort4, executions4);
		Map<Double, Double> result4 = clustering.getHadoopCPUUsageDiscomfortProbability();
		
		assertEquals(0.0, result4.get(0.0), 0.005);
		assertEquals(0.0, result4.get(10.0), 0.005);
		assertEquals(0.0, result4.get(20.0), 0.005);
		assertEquals(2.0/3, result4.get(30.0), 0.005);
		assertEquals(2.0/3, result4.get(40.0), 0.005);
		assertEquals(2.0/3, result4.get(50.0), 0.005);
		assertEquals(2.0/3, result4.get(60.0), 0.005);
		assertEquals(2.0/3, result4.get(70.0), 0.005);
		assertEquals(2.0/3, result4.get(80.0), 0.005);
		assertEquals(2.0/3, result4.get(90.0), 0.005);
		assertEquals(1.0, result4.get(100.0), 0.005);
		
		clustering = new Clustering(hadoop5, discomfort5, executions5);
		Map<Double, Double> result5 = clustering.getHadoopCPUUsageDiscomfortProbability();
		
		assertEquals(0.0, result5.get(0.0), 0.005);
		assertEquals(0.0, result5.get(10.0), 0.005);
		assertEquals(0.0, result5.get(20.0), 0.005);
		assertEquals(0.5, result5.get(30.0), 0.005);
		assertEquals(0.5, result5.get(40.0), 0.005);
		assertEquals(0.5, result5.get(50.0), 0.005);
		assertEquals(0.5, result5.get(60.0), 0.005);
		assertEquals(0.5, result5.get(70.0), 0.005);
		assertEquals(0.5, result5.get(80.0), 0.005);
		assertEquals(0.5, result5.get(90.0), 0.005);
		assertEquals(1.0, result5.get(100.0), 0.005);
	}
}
