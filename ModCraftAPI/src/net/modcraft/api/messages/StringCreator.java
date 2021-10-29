package net.modcraft.api.messages;

public class StringCreator<C> {

	private Callback<String, C> callback;
	
	public StringCreator(Callback<String, C> callback) {
		this.callback=callback;
	}
	
	public String str(C c) {
		return callback.callback(c);
	}
	
	@FunctionalInterface
	public static interface Callback<T,E> {
		T callback(E e);
	}
	
}
