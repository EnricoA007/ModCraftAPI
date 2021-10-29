package net.modcraft.api.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class MethodCallable<CallableType> {
	
	private Class<?> clazz;
	private Object obj;
	private Object[] arguments;
	
	public MethodCallable(Object obj, Object... methodArgs) {
		this.clazz=obj.getClass();
		this.obj=obj;
		this.arguments=methodArgs.clone();
	}
	
	public Callable<CallableType> call(Method m) {
		Callable<CallableType> callable = new Callable<CallableType>() {
			@SuppressWarnings("unchecked")
			@Override
			public CallableType call() throws Exception {
				return (CallableType) m.invoke(clazz, arguments);
			}
		};
		
		return callable;
	}
	
	public NoThrowableCallable<CallableType> call(String methodname, Class<?>... parameters) {
		NoThrowableCallable<CallableType> callable = new NoThrowableCallable<CallableType>() {
			@SuppressWarnings("unchecked")
			@Override
			public CallableType call() {
				try {
					return (CallableType) clazz.getMethod(methodname, parameters).invoke(obj, arguments);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
					return null;
				}
			}
		};
		
		if(callable.call() == null) {
			System.out.println("NoThrowableCallable wasn't created! call(" + methodname + ", " + Arrays.asList(parameters).toString() + ")");
			return null;
		}
		
		return callable;
	}

}
