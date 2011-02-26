package spacek.quartz.quartzwrapper;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import spacek.quartz.quartzwrapper.QuartzUtil;

public class QuartzUtilTest {
	private Scheduler scheduler;
	private QuartzUtil util;
	
	@Before
	public void setUp() throws SchedulerException {
		scheduler = StdSchedulerFactory.getDefaultScheduler();
		util = new QuartzUtil(scheduler);
		
		scheduler.start();
	}
	
	@After
	public void tearDown() throws SchedulerException {
		scheduler.shutdown(true);
	}

	@Test
	public void test() {
		SampleService wrapped = util.wrap(SampleService.class);
		assertThat(wrapped, notNullValue());
		wrapped.print();
		util.queueLastNull().start();
	}
	
	@Test
	public void after5Seconds() throws InterruptedException {
		SampleService wrapped = util.wrap(SampleService.class);
		assertThat(wrapped, notNullValue());
		
		Date after5Seconds = new Date();
		after5Seconds.setTime(after5Seconds.getTime() + 5000);
		
		wrapped.print();
		util.queueLastNull().schedule(after5Seconds).start();
		Thread.sleep(7000);
	}
}
