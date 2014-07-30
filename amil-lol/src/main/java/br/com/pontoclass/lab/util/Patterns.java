package br.com.pontoclass.lab.util;

public abstract class Patterns {
	
	private Patterns(){};
	
	public static final String KILLING = "^(([0-9]{2}\\/){2}[0-9]{4}\\ ([0-9]{2}\\:){2}[0-9]{2}\\ \\-\\ )((?!\\<WORLD\\>).*)(\\ killed){1}\\ ((?!\\<WORLD\\>).*)\\ (.*)\\ (.*)$";
}
