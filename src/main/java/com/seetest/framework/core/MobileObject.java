package com.seetest.framework.core;

public interface MobileObject {
	
	public boolean waitFor(int timeout);
	
	public void click();
	
	public void sendText(String text);
	
	public boolean isDisplayed();
	
	public String getText();
	
	public boolean swipeTo(String direction, int offset, int swipeTime, int delay, int rounds, boolean click);
	
	public String runAPI(String script);

}