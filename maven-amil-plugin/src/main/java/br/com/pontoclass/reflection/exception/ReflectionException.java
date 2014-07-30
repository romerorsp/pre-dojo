package br.com.pontoclass.reflection.exception;

public class ReflectionException extends RuntimeException {

	private static final long serialVersionUID = -3256635633292635187L;

	public ReflectionException(String message) {
		super(message);
	}

	public ReflectionException(String message, Throwable e) {
		super(message, e);
	}
}
