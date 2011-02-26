package spacek.quartz;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class GenericJob implements Job {

    /*
     * TODO determine best way to allow child classes to override creation of
     * instance
     */

    @Override
    public void execute(JobExecutionContext context)
	    throws JobExecutionException {
	Object instance = getJobClassInstance(context);
	String methodName = context.getMergedJobDataMap().getString("method");
	Object[] args = context.getMergedJobDataMap().containsKey("args") ? (Object[]) context
		.getMergedJobDataMap().get("args") : null;

	Method method = getClassMethod(instance, methodName, args);
	context.setResult(invokeMethod(instance, method, args));
    }

    private Object invokeMethod(Object instance, Method method, Object[] args)
	    throws GenericJobException {
	try {
	    return method.invoke(instance, args);
	} catch (IllegalArgumentException e) {
	    throw new GenericJobException("Couldn't invoke method", e);
	} catch (IllegalAccessException e) {
	    throw new GenericJobException("Couldn't invoke method", e);
	} catch (InvocationTargetException e) {
	    throw new GenericJobException("Couldn't invoke method", e);
	}
    }

    private Method getClassMethod(Object instance, String methodName,
	    Object[] args) throws GenericJobException {
	Class<?>[] parameterTypes = new Class<?>[args.length];
	for (int i = 0; i < args.length; i++) {
	    parameterTypes[i] = args[i].getClass();
	}

	try {
	    return instance.getClass().getMethod(methodName, parameterTypes);
	} catch (SecurityException e) {
	    throw new GenericJobException("Couldn't get method from class", e);
	} catch (NoSuchMethodException e) {
	    throw new GenericJobException("Couldn't get method from class", e);
	}
    }

    private Object getJobClassInstance(JobExecutionContext context)
	    throws GenericJobException {
	Class<?> clazz = (Class<?>) context.getMergedJobDataMap().get("class");
	if (clazz == null) {
	    throw new GenericJobException(
		    "'class' was missing from job data map, or else was not of the Class<?> type");
	}

	try {
	    return clazz.newInstance();
	} catch (InstantiationException e) {
	    throw new GenericJobException("Couldn't create instance of class",
		    e);
	} catch (IllegalAccessException e) {
	    throw new GenericJobException("Couldn't create instance of class",
		    e);
	}
    }

}
