/*******************************************************************************
 * Copyright (c) 2016 Robert Koszewski
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package com.robertkoszewski.wui.utils;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;
//import eu.infomas.annotation.AnnotationDetector;
//import eu.infomas.annotation.AnnotationDetector.MethodReporter;

/**
 * Java Reflection Utilities
 * @author Robert Koszewski
 */
public class ReflectionUtils {
	
	/*
	 * Caching Methods
	 */
	
	private static boolean instance_cache_enabled = true;
	private static HashMap<String, Object> instance_cache = new HashMap<String, Object>();
	
	/**
	 * Add Class Instance to Cache
	 * @param instance
	 */
	public void addInstanceToCache(Object instance){
		//@DEBUG: System.out.println("Starting instance: "+instance.getClass().getName());
		instance_cache.put(instance.getClass().getName(), instance);
	}
	
	/**
	 * Instantiate Class
	 * @param class_name
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws MalformedURLException
	 */
	public Object getInstanceFromCache(String class_name) throws InstantiationException, IllegalAccessException, ClassNotFoundException, MalformedURLException{
		return getInstanceFromCache(null, class_name);
	}
	
	/**
	 * Instantiate Class from JAR File
	 * @param jar_file
	 * @param class_name
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws MalformedURLException
	 */
	public Object getInstanceFromCache(File jar_file, String class_name) throws InstantiationException, IllegalAccessException, ClassNotFoundException, MalformedURLException{
		//Object instance = instance_cache.get((jar_file!=null?jar_file.getAbsolutePath()+"@":"")+class_name);
		Object instance;
		if(instance_cache_enabled){
			instance = instance_cache.get(class_name);
			if(instance == null){
				instance = getClass(jar_file, class_name).newInstance();
				addInstanceToCache(instance);
				//@DEBUG: System.out.println("-- Cache Miss: "+class_name);
			}else{
				//@DEBUG: System.out.println("++ Cache Hit: "+class_name);
			}
		}else{
			instance = getClass(jar_file, class_name).newInstance();
		}
		return instance;
	}
	
	/**
	 * Instantiate Class from Jar File
	 * @param jarFile
	 * @param className
	 * @param methodName
	 * @return
	 * @throws ClassNotFoundException
	 * @throws MalformedURLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public Object instantiateClass(File jar_file, String class_name) throws ClassNotFoundException, MalformedURLException, InstantiationException, IllegalAccessException{
		return getInstanceFromCache(jar_file, class_name);
	}

	/**
	 * Instantiate Class
	 * @param className
	 * @return
	 * @throws ClassNotFoundException
	 * @throws MalformedURLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public Object instantiateClass(String className) throws ClassNotFoundException, MalformedURLException, InstantiationException, IllegalAccessException{
		return instantiateClass(null, className);
	}
	
	/**
	 * Clears Class Cache
	 */
	public void flushInstanceCache(){
		instance_cache.clear();
	}
	
	/**
	 * Removes Class from Cache
	 * @param class_name
	 */
	public void removeFromInstanceCache(String class_name){
		instance_cache.remove(class_name);
	}
	
	/**
	 * Removes Class from Cache
	 * @param class_name
	 */
	public void removeFromInstanceCache(Object object_class){
		instance_cache.remove(object_class.getClass().toString());
	}

	/**
	 * Returns Filed With Annotation
	 * @param annotation
	 * @param src
	 * @return
	 */
	public static Map<String, Object> getFieldsWithAnnotation(Class<? extends Annotation> annotation, Object src){
		
		Map<String, Object> fields_map = new HashMap<String, Object>();
		
		Field[] fieldList = src.getClass().getDeclaredFields();

        for (Field field : fieldList) {
        	
        	if(field.isAnnotationPresent(annotation)){
        		try {
					fields_map.put(field.getName(), field.get(src));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
        	}
        }

		return fields_map;
	}
	
	/**
	 * Get Annotation from ClassMethod
	 * @param cm
	 * @throws ClassNotFoundException 
	 * @throws SecurityException 
	 */
	public Annotation getAnnotation(ClassMethod cm, Class<? extends Annotation> annotation_class) throws SecurityException, ClassNotFoundException{
		
		Method[] method_list = Class.forName(cm.class_name).getMethods();
		
		for(Method method: method_list){

			if(method.getName().toLowerCase().equals(cm.method_name.toLowerCase())){
				
				if(method.isAnnotationPresent(annotation_class)){
					// TODO: There was some way to force the return statement to be cast to the provided class from the argument..
					return method.getAnnotation(annotation_class);
				}

			}
		}

		return null;
	}
	
	/**
	 * Find Methods by Annotation
	 * @param annotation_class
	 * @param filesOrFolders
	 * @return
	 * @throws IOException
	 */
	public ArrayList<ClassMethod> findMethodsByAnnotation(final Class<? extends Annotation> annotation_class) throws IOException{
		return findMethodsByAnnotation(annotation_class, null, null);
	}
	
	/**
	 * Find Methods by Annotation filtered by Files or Folders
	 * @param annotation_class
	 * @return String (Class Name, Method Name)
	 * @throws IOException
	 */
	public ArrayList<ClassMethod> findMethodsByAnnotation(final Class<? extends Annotation> annotation_class, File... filesOrFolders) throws IOException{
		return findMethodsByAnnotation(annotation_class, filesOrFolders, null);
	}
	
	/**
	 * Find Methods by Annotation filtered by package names
	 * @param annotation_class
	 * @param filesOrFolders
	 * @return
	 * @throws IOException
	 */
	public ArrayList<ClassMethod> findMethodsByAnnotation(final Class<? extends Annotation> annotation_class, String... packageNames) throws IOException{
		return findMethodsByAnnotation(annotation_class, null, packageNames);
	}
	
	/**
	 * Find Methods by Annotation (Inner Method)
	 * @param annotation_class
	 * @param packageNames
	 * @return
	 * @throws IOException
	 */
	private ArrayList<ClassMethod> findMethodsByAnnotation(final Class<? extends Annotation> annotation_class, File[] filesOrFolders, String[] packageNames) throws IOException{
		/*
		final ArrayList<ClassMethod> class_list = new ArrayList<ClassMethod>();
		
		final MethodReporter reporter = new MethodReporter() {

		    @SuppressWarnings("unchecked")
		    @Override
		    public Class<? extends Annotation>[] annotations() {
		        return new Class[]{annotation_class};
		    }

		    @Override
		    public void reportMethodAnnotation(Class<? extends Annotation> annotation, String className, String methodName) {
		    	class_list.add(new ClassMethod(className, methodName)); // Add class and methods to list
		    }

		};
		
		if(packageNames != null &&  packageNames.length != 0)
			(new AnnotationDetector(reporter)).detect(packageNames); // Run Scanner on Files and Folders
		else if(filesOrFolders != null &&  filesOrFolders.length != 0)
			(new AnnotationDetector(reporter)).detect(filesOrFolders); // Run Scanner on Files and Folders
		else
			(new AnnotationDetector(reporter)).detect(); // Run Scanner
		
		return class_list;
		*/
		return null;
		
	}
	
	
	/**
	 * Call Method that used the specified annotation
	 * @param annotation_class
	 * @param filesOrFolders
	 * @return
	 * @throws IOException
	 */
/*	public static Object callMethodWithAnnotation(Class<? extends Annotation> annotation_class, File filesOrFolders, Map<String, Object> parameter_map, boolean cache) throws IOException {
		
		ArrayList<String[]> methods_list = ReflectionUtils.findMethodsByAnnotation(annotation_class, filesOrFolders);
		Iterator<String[]> methods_iterator = methods_list.iterator();
		
		String[] method;
		while(methods_iterator.hasNext()){
			method = methods_iterator.next();
			
			try{
				if(cache){
					// Use Cache
					Object obj;
					if((obj = class_cache.get(method[0]))==null){
						obj = instantiateClass(filesOrFolders, method[0], method[1]);
						class_cache.put(method[0], obj);
					}
					if(parameter_map==null)
						return ReflectionUtils.callMethod(filesOrFolders, obj, method[1]);
					else
						return ReflectionUtils.callMethod(obj, annotation_class, parameter_map);
					
				}else{
					// Without Cache
					if(parameter_map==null)
						return ReflectionUtils.callMethod(filesOrFolders, method[0], method[1]);
					else
						// TODO: This is wrong. Cannot handle static calls right now
						return ReflectionUtils.callMethod(null, annotation_class, parameter_map);
					
				}
				
			}catch(Exception e){
				System.err.println("ERROR: Couldn't load method '"+method[1]+"' from class '"+method[0]+"'");
			}
			
		}
		
		return null;
		
	}
*/	
	

	/**
	 * Call Method using Reflection
	 * @param className
	 * @param methodName
	 * @param parameters
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws MalformedURLException
	 */
	/* TODO: This is ambiguous
	public Object callMethod(String className, String methodName, Object... parameters) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, MalformedURLException{	
		return callMethod(null, className, methodName, parameters);
	}
	*/
	
	/**
	 * Returns Class from an External JAR file
	 * @param jarFile
	 * @param class_name
	 * @return
	 * @throws ClassNotFoundException
	 * @throws MalformedURLException 
	 */
	public Class<?> getClass(File jar_file, String class_name) throws ClassNotFoundException, MalformedURLException{
		// Locate Class
		Class<?> cls;
		if(jar_file == null){
			cls = Class.forName(class_name);
			
		}else{
			URLClassLoader cl = URLClassLoader.newInstance(new URL[]{jar_file.toURI().toURL()});
			cls = cl.loadClass(class_name);
			
		}
		return cls;
	}

	/**
	 * Call Method using Reflection
	 * @param classmethod
	 * @param parameters
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws MalformedURLException
	 */
	public Object callMethod(ClassMethod classmethod, Object... parameters) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, MalformedURLException{	
		return callMethodInFile(null, classmethod.class_name, classmethod.method_name, parameters);
	}
	
	/*
	public static Object callMethod(String class_name, String method_name, Object... parameters) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, MalformedURLException{	
		return callMethod(null, class_name, method_name, parameters);
	}
	*/
	
	/**
	 * Call method from File using Reflection
	 * @param jarFile
	 * @param className
	 * @param methodName
	 * @param (Class<?>) parameters
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws MalformedURLException
	 */
	public Object callMethodInFile(File jarFile, String className, String methodName, Object... parameters) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, MalformedURLException{
		
		// Call Static Method
		Class<?> cls = getClass(jarFile, className);

		Method method = cls.getDeclaredMethod(methodName, getObjectClassArray(parameters));
		if (Modifier.isStatic(method.getModifiers())) {
			return method.invoke(cls, parameters); // Execute Static Function
        }else{
        	return method.invoke(instantiateClass(jarFile, className), parameters); // Execute Instantiated Function
        }

	}
	
	/**
	 * Generate a Class representation of an Object
	 * @param obj
	 * @return
	 */
	private Class<?>[] getObjectClassArray(Object[] obj){
		Class<?>[] obj_class = new Class<?>[obj.length];
		int i = 0;
		for(Object o: obj)
			obj_class[i++] = o.getClass();
		return obj_class;
	}
	
	/*
	 * object.getClass().isPrimitive()
	private Map<String,Class> builtInMap = new HashMap<String,Class>();
	{
	       builtInMap("int", Integer.TYPE );
	       builtInMap("long", Long.TYPE );
	       builtInMap("double", Double.TYPE );
	       builtInMap("float", Float.TYPE );
	       builtInMap("bool", Boolean.TYPE );
	       builtInMap("char", Character.TYPE );
	       builtInMap("byte", Byte.TYPE );
	       builtInMap("void", Void.TYPE );
	       builtInMap("short", Short.TYPE );
	}*/
	
	/**
	 * Call method from File using Reflection
	 * @param jarFile
	 * @param classmethod
	 * @param parameters
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws MalformedURLException
	 */
	public Object callMethodInFile(File jarFile, ClassMethod classmethod, Object... parameters) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, MalformedURLException{
		
		// Call Static Method
		Class<?> cls = getClass(jarFile, classmethod.class_name);
		Method method = cls.getDeclaredMethod(classmethod.method_name, getObjectClassArray(parameters));
		if (Modifier.isStatic(method.getModifiers())) {
			return method.invoke(cls, parameters); // Execute Static Function
        }else{
        	return method.invoke(instantiateClass(jarFile, classmethod.class_name), parameters); // Execute Instantiated Function
        }

	}
	
	/**
	 * Call Method from Instance 
	 * @param jarFile
	 * @param instance
	 * @param methodName
	 * @param parameters
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws MalformedURLException
	 */
	public Object callMethod(Object class_or_instance, String methodName, Object... parameters) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, MalformedURLException{
		
		// Call Initialzied Method
		Class<?> cls = class_or_instance.getClass();

		Method method = cls.getDeclaredMethod(methodName, getObjectClassArray(parameters));
	
		if (Modifier.isStatic(method.getModifiers())) {
			return method.invoke(cls, parameters); // Execute Static Function
        }else{
        	return method.invoke(class_or_instance, parameters); // Execute Instantiated Function
        }
		
	}

	/**
	 * Call Method with Permissive Injection (With auto instantiation)
	 * @param classmethod
	 * @param instantiate
	 * @param parameter_map
	 * @return
	 * @throws Exception
	 */
	public Object callMethodWithInjection(ClassMethod classmethod, boolean instantiate, ParameterMap parameter_map) throws Exception{
		return callMethodWithAnnotationUsingInjection(
				(instantiate?
						instantiateClass(null, classmethod.class_name):
							Class.forName(classmethod.class_name)), 
				classmethod.method_name, null, parameter_map);
	}
	
	/**
	 * Call Method with Permissive Injection
	 * @param classmethod
	 * @param parameter_map
	 * @return
	 * @throws Exception
	 */
	public Object callMethodWithInjection(ClassMethod classmethod, ParameterMap parameter_map) throws Exception{
		return callMethodWithAnnotationUsingInjection(Class.forName(classmethod.class_name), classmethod.method_name, null, parameter_map);
	}
	
	/**
	 * Call Method with Permissive Injection
	 * @param cm
	 * @param annotation_class
	 * @param parameter_map
	 * @return
	 * @throws Exception
	 */
	public Object callMethodWithAnnotationUsingInjection(ClassMethod cm, 
			final Class<? extends Annotation> annotation_class, ParameterMap parameter_map) throws Exception{
		return callMethodWithAnnotationUsingInjection(getInstanceFromCache(cm.class_name), cm.method_name, annotation_class, parameter_map);
	}
	
	/**
	 * Call Method with Permissive Injection
	 * @param instance
	 * @param annotation
	 * @param parameter_map
	 * @return
	 * @throws Exception
	 */
	public Object callMethodWithAnnotationUsingInjection(Object class_or_instance, String method_name, 
			final Class<? extends Annotation> annotation_class, ParameterMap parameter_map) throws Exception{
		// Initialize required components
		if(parameter_map==null)
			parameter_map = new ParameterMap();
		
		// Call Initialized Method
		Class<?> cls;
		if(class_or_instance instanceof Class)
			cls = (Class<?>) class_or_instance;
		else
			cls = class_or_instance.getClass();

		// Search for Method with Annotation
		Method[] method_list = cls.getMethods();
		
		for(Method method: method_list){

			if(method.getName().toLowerCase().equals(method_name.toLowerCase())){
				
				if(annotation_class != null && !method.isAnnotationPresent(annotation_class)){
					continue;
				}
				
				// Found the Method (First Found)
				Object[] params = new Object[method.getParameterCount()];

				int i = 0;
				//System.out.println(method.getParameters()[0].getType());
				for(Parameter param:  method.getParameters()){
					params[i++] = parameter_map.get(param.getType());
				}
				
				//try{
				if (Modifier.isStatic(method.getModifiers())) {
					return method.invoke(cls, params); // Execute Static Function
		        }else{
		        	if(class_or_instance instanceof Class){
		        		Object instance = getInstanceFromCache(((Class<?>) class_or_instance).getName());
		        		return method.invoke(instance, params); // Execute Instantiated Function
		        	}else{	
		        		return method.invoke(class_or_instance, params); // Execute Instantiated Function
		        	}
		        }
				/*}catch(Exception e){
					System.err.println("ERROR: Could not execute '"+method_name+"' in '"+class_or_instance+"");
					e.printStackTrace();
				}*/

			}
		}
		
		// Method not Found
		throw new NoSuchMethodException();
	}
	
	/**
	 * Class Method
	 */
	public static class ClassMethod{
		
		public final String class_name;
		public final String method_name;
		
		/**
		 * Class-Method with Annotation
		 * @param class_name
		 * @param method_name
		 * @param annotation
		 */
		public ClassMethod(String class_name, String method_name){
			this.class_name = class_name;
			this.method_name = method_name;
		}
		
		public String toString(){
			return "ClassMethod: " + class_name + " @ " + method_name;
		}
		
	}
	
	/**
	 * Parameter Map
	 */
	public static class ParameterMap{
		
		private Map<Class<?>, Object> map = new HashMap<Class<?>, Object>();

		public int size() {
			return map.size();
		}

		public boolean isEmpty() {
			return map.isEmpty();
		}

		public boolean containsKey(Object key) {
			return map.containsKey(key);
		}

		public boolean containsValue(Object value) {
			return map.containsValue(value);
		}

		public Object get(Class<?> key) {
			Iterator<Object> i = map.values().iterator();
			while(i.hasNext()){
				Object obj = i.next();
				if(key.isAssignableFrom(obj.getClass())){
					return obj;
				}
			}
			return null;
		}

		public Object put(Object value) {
			return map.put(value.getClass(), value);
		}

		public Object remove(Object key) {
			return map.remove(key);
		}

		public void clear() {
			map.clear();
		}

		public Collection<Object> values() {
			return map.values();
		}

		public Set<?> entrySet() {
			return map.entrySet();
		}
	}
	
}
