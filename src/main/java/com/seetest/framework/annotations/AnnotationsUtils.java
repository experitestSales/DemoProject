package com.seetest.framework.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import com.seetest.framework.BasePage;
import com.seetest.framework.core.Context;
import com.seetest.framework.core.MobileDeviceType;
import com.seetest.framework.core.MobileOS;
import com.seetest.framework.core.MobileObject;
import com.seetest.framework.core.MobileObjectImpl;

public class AnnotationsUtils {
	
	public static void initPageMobileObjects(BasePage page) {
		Field[] fields = page.getClass().getDeclaredFields();
		for (int i=0; i<fields.length; i++) {
			Field field = fields[i];
			if (!field.getType().equals(MobileObject.class)) {
				continue;
			}
			String xpath = null;
			String zone = page.getDefaultContext().asString();
			int index = 0;
			Annotation[] annotations = field.getAnnotations();
			HashMap<String,Annotation> asMap = new HashMap<String,Annotation>();
			for (int j=0; j<annotations.length; j++) {
				Annotation a = annotations[j];
				asMap.put(a.annotationType().getSimpleName(), a);    			
			}
			if (asMap.containsKey("Index")) {
				index = (int) getValueFromAnnotation(asMap.get("Index"), "value");
			}
			if (asMap.containsKey("Zone")) {
				zone = ((Context) getValueFromAnnotation(asMap.get("Zone"), "value")).asString();
			}
			for (String findBy:getFindByOrdered(page)) {
				if (asMap.containsKey(findBy)) {
					xpath = getValueFromAnnotation(asMap.get(findBy), "xpath").toString();
					break;
				}
			}
			field.setAccessible(true);
			try {
				field.set(page, new MobileObjectImpl(page.client, zone, xpath, index));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	private static ArrayList<String> getFindByOrdered(BasePage page) {
		ArrayList<String> list = new ArrayList<String>();
		if (page.deviceOS.equals(MobileOS.ANDROID) && page.deviceType.equals(MobileDeviceType.PHONE)) {
			list.add("FindByAndroidPhone");
			list.add("FindByAndroid");
		}
		if (page.deviceOS.equals(MobileOS.ANDROID) && page.deviceType.equals(MobileDeviceType.TABLET)) {
			list.add("FindByAndroidTablet");
			list.add("FindByAndroid");
		}
		if (page.deviceOS.equals(MobileOS.IOS) && page.deviceType.equals(MobileDeviceType.PHONE)) {
			list.add("FindByIOSPhone");
			list.add("FindByIOS");
		}
		if (page.deviceOS.equals(MobileOS.IOS) && page.deviceType.equals(MobileDeviceType.TABLET)) {
			list.add("FindByIOSTablet");
			list.add("FindByIOS");
		}
		list.add("FindBy");
		return list;
	}

	private static Object getValueFromAnnotation(Annotation a, String key) {
		Object value = null;
		Method m = null;
		try {
			m = a.getClass().getMethod(key, new Class<?>[]{});
			value = m.invoke(a, new Object[]{});
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return value;    	
	}

}
