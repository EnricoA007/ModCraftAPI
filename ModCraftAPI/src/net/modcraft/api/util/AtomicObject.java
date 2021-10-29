package net.modcraft.api.util;

public class AtomicObject<E> {

	private E e;
	
	public AtomicObject(E e) {
		this.e=e;
	}
	
	public AtomicObject() {}
	
	public void setElement(E e) {
		this.e = e;
	}
	
	public E getElement() {
		return e == null ? null : e;
	}
	
	public boolean hasElement() {
		return e != null;
	}
	
}
