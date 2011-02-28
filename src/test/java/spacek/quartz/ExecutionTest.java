package spacek.quartz;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;

public class ExecutionTest {

	@Test
	public void sameDetailsTest() {
		List<Execution> list = new ArrayList<Execution>();

		JobDetail jobDetail = new JobDetail("jobDetail", "group",
				GenericJob.class);
		Trigger trigger = new SimpleTrigger("trigger");
		
		list.add(new Execution(jobDetail, trigger));
		assertThat(list, hasItem(new Execution(jobDetail, trigger)));
	}
	
	@Test
	public void differentDetailsTest() {
		List<Execution> list = new ArrayList<Execution>();

		JobDetail jobDetail = new JobDetail("jobDetail", "group",
				GenericJob.class);
		Trigger trigger = new SimpleTrigger("trigger");
		Execution original = new Execution(jobDetail, trigger);
		
		JobDetail secondJobDetail = new JobDetail("jobDetail", "group",
				GenericJob.class);
		Trigger secondTrigger = new SimpleTrigger("trigger");
		Execution second = new Execution(secondJobDetail, secondTrigger);
		
		list.add(original);
		assertThat(list, hasItem(second));
	}
	
	@Test
	public void hashMapTest() {
		HashMap<Execution, Boolean> map = new HashMap<Execution, Boolean>();

		JobDetail jobDetail = new JobDetail("jobDetail", "group",
				GenericJob.class);
		Trigger trigger = new SimpleTrigger("trigger");
		Execution original = new Execution(jobDetail, trigger);
		
		JobDetail secondJobDetail = new JobDetail("jobDetail", "group",
				GenericJob.class);
		Trigger secondTrigger = new SimpleTrigger("trigger");
		Execution second = new Execution(secondJobDetail, secondTrigger);
		
		map.put(original, true);
		assertThat(map.containsKey(second), equalTo(true));
	}
}
