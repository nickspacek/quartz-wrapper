package spacek.quartz.quartzwrapper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class ServiceCallback implements MethodInterceptor {

	private Method lastMethod;
	private Object[] lastArgs;
	private List<InvocationObserver> observers = new ArrayList<InvocationObserver>();

	public ServiceCallback(InvocationObserver observer) {
		observers.add(observer);
	}

	@Override
	public Object intercept(Object object, Method method, Object[] args,
			MethodProxy methodProxy) throws Throwable {
		lastMethod = method;
		lastArgs = args;

		notifyObservers();
		return null;
	}

	private void notifyObservers() {
		for (InvocationObserver observer : observers) {
			observer.invoked(this);
		}
	}

	public Method getLastMethod() {
		return lastMethod;
	}

	public Object[] getLastArgs() {
		return lastArgs;
	}

}
