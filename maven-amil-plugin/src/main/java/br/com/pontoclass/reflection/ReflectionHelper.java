package br.com.pontoclass.reflection;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import br.com.pontoclass.reflection.exception.ReflectionException;

public class ReflectionHelper {
	@SuppressWarnings("unchecked")
	public static <T> List<Class<T>> findClassesImplementing(final Class<T> interfaceClass, final Package fromPackage) throws ReflectionException {
		if (interfaceClass == null) {
			throw new ReflectionException("Unknown subclass.");
		}
		if (fromPackage == null) {
			throw new ReflectionException("Unknown package.");
		}
		Reflections reflections = new Reflections(
				  new ConfigurationBuilder()
				  	.setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
				  	.setUrls(ClasspathHelper.forPackage(fromPackage.getName()))
				  	.filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(fromPackage.getName()))));
		List<Class<T>> result = List.class.cast(reflections.getSubTypesOf(interfaceClass).stream().collect(Collectors.toList()));
		
		result = List.class.cast(result.parallelStream()
			  .filter((clazz)->!(Modifier.isAbstract(clazz.getModifiers()) || clazz.isInterface()))
			  .collect(Collectors.toList()));
		return result;
	}
}