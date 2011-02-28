package spacek.quartz.quartzwrapper;

import java.util.Date;
import java.util.UUID;

import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;

public class DefaultQuartzTask<T> implements QuartzTask<T> {

	private QuartzUtil quartzUtil;
	private JobDetail jobDetail;
	private Date scheduled;
	private QuartzCallback<T> callback;

	public DefaultQuartzTask(QuartzUtil quartzUtil, JobDetail jobDetail) {
		this.quartzUtil = quartzUtil;
		this.jobDetail = jobDetail;
	}

	@Override
	public QuartzResult<T> start() {
		// TODO schedule job
		Date startTime = scheduled == null ? new Date() : scheduled;
		
		Trigger trigger = new SimpleTrigger(UUID.randomUUID().toString(), startTime);
		return quartzUtil.schedule(jobDetail, trigger, callback);
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
