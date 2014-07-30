package br.com.pontoclass.lab;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.pontoclass.lab.util.Patterns;

public class PatternTest {
	private Pattern	pattern;

	public @Before void setup() {
		this.pattern = Pattern.compile(Patterns.KILLING);
	}
	
	public @Test void testPatternGroups() {
		String line = "23/04/2013 15:36:04 - Roman killed Nick using M16";
		Matcher matcher = pattern.matcher(line);
		Assert.assertTrue(matcher.find());
		Assert.assertEquals("Roman", matcher.group(4));
		Assert.assertEquals("Nick", matcher.group(6));
	}
	
	public @Test void testFailPatternGroups() {
		String line = "23/04/2013 15:36:33 - <WORLD> killed Nick by DROWN";
		Matcher matcher = pattern.matcher(line);
		Assert.assertFalse(matcher.find());
	}
}