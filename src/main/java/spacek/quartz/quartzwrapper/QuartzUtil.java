package spacek.quartz.quartzwrapper;

import java.lang.reflect.Method;
import java.util.UUID;

import net.sf.cglib.proxy.Enhancer;

import org.quartz.JobDetail;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import spacek.quartz.Execution;
import spacek.quartz.GenericJob;
import spacek.quartz.result.ResultStoringJobListener;

public class QuartzUtil implements InvocationObserver {

	private Scheduler scheduler;
	private ResultStoringJobListener resultListener;
	private String jobGroup;
	private ThreadLocal<ServiceCallback> observed = new ThreadLocal<ServiceCallback>();

	public QuartzUtil() {
	}

	public QuartzUtil(Scheduler scheduler) {
		setScheduler(scheduler);
	}

	public <T> T wrap(Class<T> service) {
		ServiceCallback callback = new ServiceCallback(this);
		T serviceProxy = service.cast(Enhancer.create(service, callback));
		return serviceProxy;
	}

	public <T> QuartzTask<T> queue(T methodResult) {
		JobDetail jobDetail = buildJobDetail();
		QuartzTask<T> task = new DefaultQuartzTask<T>(this, jobDetail);
		return task;
	}

	public <T> QuartzTask<T> queueLastNull() {
		JobDetail jobDetail = buildJobDetail();
		QuartzTask<T> task = new DefaultQuartzTask<T>(this, jobDetail);
		return task;
	}

	// TODO separate this out into another class; QuartzHelper?
	public <T> QuartzResult<T> schedule(JobDetail jobDetail, Trigger trigger,
			QuartzCallback<T> callback) {
		try {
			Execution execution = new Execution(jobDetail, trigger);

			// TODO figure out how to add some type checks
			@SuppressWarnings("unchecked")
			Class<T> resultClass = (Class<T>) getResultClass(jobDetail);
			QuartzResult<T> quartzResult = new QuartzResult<T>(resultClass,
					execution, scheduler, resultListener);

			if (callback != null) {
				resultListener.addObserver(execution,
						new QuartzCallbackWrapper<T>(callback, resultClass));
			}

			scheduler.scheduleJob(jobDetail, trigger);

			return quartzResult;
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

	// TODO figure out how to add some type checks
	@SuppressWarnings("unchecked")
	private <T> Class<T> getResultClass(JobDetail jobDetail) {
		return (Class<T>) jobDetail.getJobDataMap().get("returnType");
	}

	private JobDetail buildJobDetail() {
		// TODO could determine if there is a custom job class here
		Method method = observed.get().getLastMethod();
		Object[] args = observed.get().getLastArgs();

		JobDetail jobDetail = new JobDetail(UUID.randomUUID().toString(),
				jobGroup, GenericJob.class, false, true, false);
		jobDetail.getJobDataMap().put("class", method.getDeclaringClass());
		jobDetail.getJobDataMap().put("method", method.getName());
		jobDetail.getJobDataMap().put("returnType", method.getReturnType());
		jobDetail.getJobDataMap().put("args", args);

		return jobDetail;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		if (scheduler == null) {
			throw new NullPointerException("scheduler");
		}

		JobListener resultListener;
		try {
			resultListener = scheduler
					.getGlobalJobListener(ResultStoringJobListener.class
							.getName());
		} catch (SchedulerException e) {
			// TODO something nicer
			throw new RuntimeException(e);
		}

		if (resultListener == null
				|| !(resultListener instanceof ResultStoringJobListener)) {
			// TODO some sort of real exception?
			throw new RuntimeException(
					"Scheduler must have a job listener implementing ResultStoringJobListener named ResultStoringJobListener.class.getName()");
		}

		this.resultListener = (ResultStoringJobListener) resultListener;
		this.scheduler = scheduler;
	}

	@Override
	public void invoked(ServiceCallback callback) {
		observed.set(callback);
	}
}
