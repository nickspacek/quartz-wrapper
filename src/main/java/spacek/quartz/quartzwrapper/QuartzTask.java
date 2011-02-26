package spacek.quartz.quartzwrapper;

import java.util.Date;

public interface QuartzTask<T> {
    public QuartzResult<T> start();
    public QuartzTask<T> schedule(Date date);
    public QuartzTask<T> after(QuartzCallback<T> callback);
}
