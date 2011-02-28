package spacek.quartz.quartzwrapper;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import spacek.quartz.quartzwrapper.QuartzUtil;
import spacek.quartz.result.ExecutionResult;
import spacek.quartz.result.MemoryResultStoringJobListener;

public class QuartzUtilTest {
	private Scheduler scheduler;
	private QuartzUtil util;

	@Before
	public void setUp() throws SchedulerException {
		scheduler = StdSchedulerFactory.getDefaultScheduler();
		scheduler.addGlobalJobListener(new MemoryResultStoringJobListener());
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
	public void after5Seconds() throws InterruptedException, ExecutionException {
		SampleService wrapped = util.wrap(SampleService.class);
		assertThat(wrapped, notNullValue());

		Date after5Seconds = new Date();
		after5Seconds.setTime(after5Seconds.getTime() + 5000);

		wrapped.print();
		util.queueLastNull().schedule(after5Seconds).start().get();
	}

	@Test
	public void resultTest() throws InterruptedException, ExecutionException {
		SampleService wrapped = util.wrap(SampleService.class);
		assertThat(wrapped, notNullValue());

		Date after5Seconds = new Date();
		after5Seconds.setTime(after5Seconds.getTime() + 5000);

		assertThat(util.queue(wrapped.toString()).start().get(), notNullValue());
	}

	@Test
	public void callbackTest() throws InterruptedException, ExecutionException {
		SampleService wrapped = util.wrap(SampleService.class);
		assertThat(wrapped, notNullValue());

		Date after5Seconds = new Date();
		after5Seconds.setTime(after5Seconds.getTime() + 5000);

		System.out.println(util.queue(wrapped.message())
				.after(new QuartzCallback<String>() {
					@Override
					public void call(ExecutionResult<String> result) {
						if (result.getError() != null) {
							System.out.println("Got error: "
									+ result.getError().toString());
						} else {
							System.out.println("Result: " + result.getResult());
						}
					}
				}).start().get());
	}
}
