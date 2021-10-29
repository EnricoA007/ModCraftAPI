package net.modcraft.api.util;

@FunctionalInterface
public interface NoThrowableCallable<K> {

	K call();
	
	
}
