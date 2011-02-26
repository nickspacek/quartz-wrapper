package spacek.quartz.quartzwrapper;

import java.lang.reflect.Method;
import java.util.UUID;

import net.sf.cglib.proxy.Enhancer;

import org.quartz.JobDetail;
import org.quartz.Scheduler;

import spacek.quartz.GenericJob;


public class QuartzUtil implements InvocationObserver {
	private Scheduler scheduler;
	private String jobGroup;
	private ThreadLocal<ServiceCallback> observed = new ThreadLocal<ServiceCallback>();

	public QuartzUtil() {
	}

	public QuartzUtil(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public <T> T wrap(Class<T> service) {
		ServiceCallback callback = new ServiceCallback(this);
		T serviceProxy = service.cast(Enhancer.create(service, callback));
		return serviceProxy;
	}

	public <T> QuartzTask<T> queue(T methodResult) {
		JobDetail jobDetail = buildJobDetail();
		QuartzTask<T> task = new DefaultQuartzTask<T>(scheduler, jobDetail);
		return task;
	}

	public <T> QuartzTask<T> queueLastNull() {
		JobDetail jobDetail = buildJobDetail();
		QuartzTask<T> task = new DefaultQuartzTask<T>(scheduler, jobDetail);
		return task;
	}

	private JobDetail buildJobDetail() {
		// TODO could determine if there is a custom job class here
		Method method = observed.get().getLastMethod();
		Object[] args = observed.get().getLastArgs();

		JobDetail jobDetail = new JobDetail(UUID.randomUUID().toString(),
				jobGroup, GenericJob.class, false, true, false);
		jobDetail.getJobDataMap().put("class", method.getDeclaringClass());
		jobDetail.getJobDataMap().put("method", method.getName());
		jobDetail.getJobDataMap().put("args", args);

		return jobDetail;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	@Override
	public void invoked(ServiceCallback callback) {
		observed.set(callback);
	}
}
