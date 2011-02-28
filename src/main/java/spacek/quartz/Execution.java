package spacek.quartz;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;

public class Execution {
	private JobDetail jobDetail;
	private Trigger trigger;

	public Execution() {
	}

	public Execution(JobDetail jobDetail, Trigger trigger) {
		setJobDetail(jobDetail);
		setTrigger(trigger);
	}

	// TODO hashCode equals builders
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Execution)) {
			return false;
		}
		Execution other = (Execution) obj;
		
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(jobDetail.getName(), other.jobDetail.getName());
		builder.append(jobDetail.getGroup(), other.jobDetail.getGroup());
		builder.append(trigger.getName(), other.trigger.getName());
		
		return builder.isEquals();
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		builder.append(jobDetail.getName());
		builder.append(jobDetail.getGroup());
		builder.append(trigger.getName());
		
		return builder.toHashCode();
	}

	public JobDetail getJobDetail() {
		return jobDetail;
	}

	public void setJobDetail(JobDetail jobDetail) {
		if (jobDetail == null) {
			throw new NullPointerException("jobDetail");
		}
		this.jobDetail = jobDetail;
	}

	public Trigger getTrigger() {
		return trigger;
	}

	public void setTrigger(Trigger trigger) {
		if (trigger == null) {
			throw new NullPointerException("trigger");
		}
		this.trigger = trigger;
	}
}
