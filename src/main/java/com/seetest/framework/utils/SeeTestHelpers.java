package com.seetest.framework.utils;

import java.io.*;
import java.util.Properties;

public class SeeTestHelpers {
	
	public synchronized static String getBuildId() {
		Properties props = new Properties();
		File file = null;
		try {
			file = new File("buildid.properties");
			props.load(new FileInputStream(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String buildId = props.getProperty("build.id");
		Integer updated = Integer.parseInt(buildId) + 1;
		props.setProperty("build.id", updated.toString());
		OutputStream out;
		try {
			out = new FileOutputStream(file);
			props.store(out,null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buildId;
	}
	
	public static void threadSleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
