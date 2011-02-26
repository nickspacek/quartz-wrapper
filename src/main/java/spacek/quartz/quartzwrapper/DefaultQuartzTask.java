package spacek.quartz.quartzwrapper;

import java.util.Date;
import java.util.UUID;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;

public class DefaultQuartzTask<T> implements QuartzTask<T> {

	private Scheduler scheduler;
	private JobDetail jobDetail;
	private Date scheduled;
	private QuartzCallback<T> callback;

	public DefaultQuartzTask(Scheduler scheduler, JobDetail jobDetail) {
		this.scheduler = scheduler;
		this.jobDetail = jobDetail;
	}

	@Override
	public QuartzResult<T> start() {
		// TODO schedule job
		Date startTime = scheduled == null ? new Date() : scheduled;
		try {
			scheduler.scheduleJob(jobDetail, new SimpleTrigger(UUID
					.randomUUID().toString(), startTime));
		} catch (SchedulerException e) {
			// TODO something more useful
			throw new RuntimeException(e);
		}

		return null;
	}

	@Override
	public QuartzTask<T> schedule(Date date) {
		this.scheduled = date;
		return this;
	}

	@Override
	public QuartzTask<T> after(QuartzCallback<T> callback) {
		this.callback = callback;
		return this;
	}

}
