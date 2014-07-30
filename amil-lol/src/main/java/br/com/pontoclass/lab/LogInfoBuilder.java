package br.com.pontoclass.lab;

import org.apache.maven.plugin.logging.Log;

public interface LogInfoBuilder {
	public LogInfoBuilder withLog(Log log);

	public LogInfoBuilder withLine(String line);
	
	public LogInfo build();
}
