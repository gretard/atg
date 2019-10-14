package edu.ktu.atg.common.helpers;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public final class DumpUtilities {
	private DumpUtilities() {
	}

	public static String dump(Object object) {
		return dump(object, 1);
	}

	public static String dump(Object object, int maxRecursionDeep, String... parameterArray) {
		if (object == null) {
			return "null";
		}

		Class<? extends Object> objectClass = object.getClass();

		if (isInArray(objectClass,
				new Class<?>[] { Long.class, Integer.class, Boolean.class, Double.class, Short.class, Byte.class })
				|| objectClass.isPrimitive()) {
			return object.toString();
		}

		try {
			boolean onlyFilled = booleanParameter(parameterArray, "onlyFilled");
			boolean newline = booleanParameter(parameterArray, "newline");
			String indent = duplicate(" ", toInt(parameter(parameterArray, "indent")));
			String excludeType = parameter(parameterArray, "excludeType");
			String excludeName = parameter(parameterArray, "excludeName");
			String includeType = parameter(parameterArray, "includeType");
			String includeName = parameter(parameterArray, "includeName");

			if (objectClass == java.lang.String.class) {
				String s = object.toString();
				return onlyFilled && s.length() == 0 ? "" : "\"" + s + "\"";
			}

			StringBuffer buffer = new StringBuffer();

			if (objectClass.isArray()) {
				for (int i = 0; i < Array.getLength(object) && maxRecursionDeep > 0; i++) {
					String value = dump(Array.get(object, i), maxRecursionDeep - 1, parameterArray);

					if (onlyFilled && (value.length() == 0 || "null".equals(value))) {
						continue;
					}
					buffer.append((i == 0 ? "" : ",") + value.replace("\n", "\n" + indent) + (newline ? "\n" : ""));
				}

				if ((onlyFilled && buffer.length() > 0) || !onlyFilled) {
					buffer.insert(0, "[");
					buffer.append("]");
				}
			} else {
				while (objectClass != null) {
					Field[] fieldArray = objectClass.getDeclaredFields();

					for (int i = 0; i < fieldArray.length && maxRecursionDeep > 0; i++) {
						String className;
						String value;
						Field field = fieldArray[i];
						String name = field.getName();

						if ((excludeName != null && name.matches(excludeName))
								|| (includeName != null && !name.matches(includeName))) {
							continue;
						}

						try {
							field.setAccessible(true);

							Object fieldObject = field.get(object);
							if (fieldObject != null && fieldObject.getClass().isArray()) {
								String s = field.toString();
								int index = s.indexOf("[");
								className = s.substring(0, index >= 0 ? index : s.length());
							} else {
								className = field.getType().getName();
							}

							if ((excludeType != null && className.matches(excludeType))
									|| (includeType != null && !className.matches(includeType))) {
								continue;
							}

							if (fieldObject != null && fieldObject.getClass().isArray()) {
								className += "[" + Array.getLength(fieldObject) + "]";
							}

							value = dump(fieldObject, maxRecursionDeep - 1, parameterArray);
						} catch (IllegalAccessException e) {
							value = e.getMessage();
							className = field.getType().getName();
						}

						if ((onlyFilled && value.length() > 0 && !"null".equals(value)) || !onlyFilled) {
							buffer.append(indent);

							if (objectClass != object.getClass()) {
								buffer.append("/* " + objectClass.getSimpleName() + " */ ");
							}
							buffer.append(
									(className.equals("java.lang.String") ? "String" : className) + " " + name + "=");
							buffer.append(value.replace("\n", "\n" + indent) + ";" + (newline ? "\n" : ""));
						}
					}

					objectClass = objectClass.getSuperclass();
				}

				if ((onlyFilled && buffer.length() > 0) || !onlyFilled) {
					buffer.insert(0, "{" + ((newline && buffer.length() > 0) ? "\n" : ""));
					buffer.append("}");
				}
			}

			return buffer.toString();
		} catch (Throwable t) {
			return t + "\n" + stackTrace2String(t);
		}
	}

	/**
	 * @param regExArray array of regular expressions
	 * @param string
	 * @return true if any array member matches v
	 */
	public static boolean matchesInArray(final String[] regExArray, final String string) {
		if (regExArray == null || string == null) {
			return false;
		}

		for (final String regEx : regExArray) {
			if (string.matches(regEx)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @param array
	 * @param object
	 * @return true if array contains v
	 */
	public static <T> boolean contains(final T[] array, final T object) {
		if (array == null || object == null) {
			return false;
		}

		for (final T element : array) {
			if (element == object || object.equals(element)) {
				return true;
			}
		}

		return false;
	}

	public static Method[] methodsWithAnnotation(Class c, Class annotation) {
		try {
			Method[] methods = c.getMethods();
			List<Method> list = new ArrayList<Method>();

			for (int i = 0; i < methods.length; i++) {
				if (methods[i].isAnnotationPresent(annotation)) {
					list.add(methods[i]);
				}
			}

			methods = new Method[list.size()];
			return list.toArray(methods);
		} catch (Throwable t) {
			// ignore
		}

		return new Method[0];
	}

	public static Method[] methodsWithPrefix(Class c, String prefix) {
		try {
			Method[] methods = c.getMethods();
			List<Method> list = new ArrayList<Method>();

			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getName().startsWith(prefix)) {
					list.add(methods[i]);
				}
			}

			methods = new Method[list.size()];
			return list.toArray(methods);
		} catch (Throwable t) {
			// ignore
		}

		return new Method[0];
	}

	public static String dumpBean(Object beanObject) {
		String name = null;

		try {
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("{\n");

			BeanInfo beanInfo = Introspector.getBeanInfo(beanObject.getClass());
			for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
				Method method = propertyDescriptor.getReadMethod();

				Type[] parameterTypes = method.getGenericParameterTypes();
				if (parameterTypes.length > 0) {
					continue;
				}

				name = method.getName();

				if ("getClass".equals(name)) {
					continue;
				}

				Object value = method.invoke(beanObject);

				if (value != null) {
					name = name.startsWith("get") ? (name.substring(3, 4).toLowerCase() + name.substring(4))
							: name.startsWith("is") ? (name.substring(2, 3).toLowerCase() + name.substring(3)) : name;
					stringBuffer.append("  /* " + method.getDeclaringClass().getSimpleName() + " */ "
							+ method.getReturnType().getSimpleName() + " " + name + "=" + value + "\n");
				}
			}

			stringBuffer.append("}\n");
			return stringBuffer.toString();
		} catch (Throwable t) {
			return "error by " + name;
		}
	}

	public static Object getField(Object object, String fieldName)
			throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field field = null;
		for (Class<?> c = object.getClass(); c != null && field == null; c = c.getSuperclass()) {
			try {
				field = c.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				// ignore
			}
		}

		if (field == null) {
			throw new NoSuchFieldException();
		}

		boolean isAccessible = field.isAccessible();
		field.setAccessible(true);
		Object objectReturn = field.get(object);
		field.setAccessible(isAccessible);
		return objectReturn;
	}

	/**
	 * @param object
	 * @param memberName
	 * @return child of object named memberName (also private member)
	 */
	public static Object getMember(Object object, String memberName) {
		Object objectReturn = null;
		try {
			Field field = object.getClass().getDeclaredField(memberName);
			boolean isAccessible = field.isAccessible();
			field.setAccessible(true);

			objectReturn = field.get(object);
			field.setAccessible(isAccessible);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return objectReturn;
	}

	public static boolean isInArray(Object object, Object[] array) {
		return Arrays.asList(array).indexOf(object) != -1;
	}

	public static boolean isInArray(Class<?> c, Class<?>[] array) {
		return Arrays.asList(array).indexOf(c) != -1;
	}

	public static String stackTrace2String(Throwable t) {
		return t + "\n"
				+ Arrays.toString(t.getStackTrace()).replace(", ", "\n  ").replace("[", "\n  ").replace("]", "\n");
	}

	/**
	 * @param parameterList
	 * @param name
	 * @return hello for name b from parameterList "a=1,b=hello"
	 */
	public static String parameter(String parameterList, String name) {
		return parameter(csv2arry(parameterList), name);
	}

	/**
	 * @param parameterList
	 * @param name
	 * @return true for name a or b from parameterList "a,b=true"
	 */
	public static boolean booleanParameter(String parameterList, String name) {
		return booleanParameter(csv2arry(parameterList), name);
	}

	/**
	 * @param parameterArray
	 * @param name
	 * @return true for name a or b from parameterArray ["a","b=true"]
	 */
	public static boolean booleanParameter(String[] parameterArray, String name) {
		String result = parameter(parameterArray, name);
		return result != null && (result.length() == 0 || Boolean.valueOf(result));
	}

	/**
	 * @param parameterArray
	 * @param name
	 * @return null if name not found, "" for name a or "hello" for name b from
	 *         parameterArray ["a","b=hello"]
	 */
	public static String parameter(String[] parameterArray, String name) {
		if (parameterArray != null && name != null) {
			for (String parameter : parameterArray) {
				parameter = parameter.trim();
				if (parameter.contains("=") && parameter.startsWith(name)) {
					String rest = parameter.substring(name.length()).trim();
					String argument;
					if (rest.startsWith("=")) {
						argument = rest.substring(1).trim();
					} else {
						argument = "";
					}
					return argument;
				}
			}
		}

		return null;
	}

	public static String duplicate(String string, int count) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < count; i++) {
			stringBuffer.append(string);
		}
		return stringBuffer.toString();
	}

	public static int toInt(String string) {
		try {
			return Integer.decode(string).intValue();
		} catch (NullPointerException e) {
			return 0;
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * @param parameterList
	 * @return array ["a=1","b=hello"] from parameterList "a=1,b=hello"
	 */
	public static String[] csv2arry(String parameterList) {
		if (parameterList == null) {
			return null;
		}

		ArrayList<String> list = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(parameterList, ",");

		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken().trim();
			list.add(token);
		}

		return list.toArray(new String[list.size()]);
	}
}