package br.com.pontoclass.lab;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.logging.Log;

import br.com.pontoclass.lab.util.Patterns;

public class KillingLogInfoBuilder implements LogInfoBuilder {
    
	private Log	log;
	private List<String> lines = new ArrayList<>();
	private static final String PATTERN = Patterns.KILLING;

	public LogInfoBuilder withLog(Log log) {
		this.log = log;
		return this;
	}

	public LogInfoBuilder withLine(String line) {
		if(line.matches(PATTERN)) {
			this.lines.add(line);
		}
		return this;
	}

	public LogInfo build() {
		return new LogInfoImpl(log, lines);
	}
}