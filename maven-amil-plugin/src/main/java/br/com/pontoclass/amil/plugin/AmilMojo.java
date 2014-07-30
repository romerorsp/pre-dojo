package br.com.pontoclass.amil.plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.codehaus.plexus.util.StringUtils;

import br.com.pontoclass.lab.LogInfoBuilder;
import br.com.pontoclass.reflection.ReflectionHelper;

/** 
 * This goal will process a ... 
 * 
 * @goal like-a-lol
 * @execute phase="test-compile" 
 * @author Rômero Ricardo
 * @aggregator
**/
@Mojo(name="like-a-lol", defaultPhase = LifecyclePhase.TEST_COMPILE)
public class AmilMojo extends AbstractMojo {

	@SuppressWarnings({"rawtypes", "unchecked"}) public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("begin.template")))
				.lines()
				.forEach((line) -> getLog().info(line));
			
			final List<Class<? extends LogInfoBuilder>> classes = new ArrayList<>();
			Arrays.asList(Package.getPackages())
			.parallelStream()
			.forEach((pack)->classes.addAll(ReflectionHelper.findClassesImplementing(LogInfoBuilder.class, pack)));
			Function<Class, Constructor<LogInfoBuilder>> constructors = (clazz)->{
				try{
					return clazz.getConstructor();
				} catch(Exception e){
					throw new RuntimeException(e);
				}
			};
			Function<Constructor<LogInfoBuilder>, LogInfoBuilder> instances = (constructor)->{
				try{
					return LogInfoBuilder.class.cast(constructor.newInstance());
				} catch(Exception e){
					throw new RuntimeException(e);
				}
			};
			List<? extends LogInfoBuilder> builders = classes.parallelStream()
			   .map(constructors)
			   .map(instances)
			   .map(b -> b.withLog(getLog()))
			   .collect(Collectors.toList());
			System.out.print("[INPUT] LOG DIRECTORY (/amil.log): ");
			
			try(final Scanner scan = new Scanner(System.in)) {
				String dir = scan.nextLine();
				getLog().info("");
				getLog().info("________________________________________________________________________");
				if(StringUtils.isEmpty(dir)) {
					dir = "/amil.log";
				}
				try (Stream<String> lines = Files.lines(Paths.get(dir), Charset.defaultCharset())) {
					List<String> lineList = lines.collect(Collectors.toList());
					for(String line: lineList) {
						for(LogInfoBuilder builder: builders) {
							 builder.withLine(line);
						 }
					}
					builders.stream().map(b->b.build()).collect(Collectors.toList()).stream().forEach(b -> b.process());
				} catch(IOException e) {
					getLog().error(String.format("IMPOSSIVEL LER ARQUIVO [%s]", dir), e);
				}
				getLog().info("");
				getLog().info("________________________________________________________________________");
				new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("end.template"))).lines().forEach((line)->getLog().info(line));
			}
		} catch(Throwable e) {
			getLog().warn(e.getMessage(), e);
		}
	}
}