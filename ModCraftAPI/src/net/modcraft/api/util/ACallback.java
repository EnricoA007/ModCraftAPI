package net.modcraft.api.util;

@FunctionalInterface
public interface ACallback<Return,Parameter> {

	Return callback(Parameter parameter);
	
}
