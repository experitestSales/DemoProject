package com.seetest.framework.core;

import com.experitest.client.Client;

public class MobileObjectImpl implements MobileObject {
	private Client client;
	private String zone;
	private String xpath;
	private int index;
	
	public MobileObjectImpl(Client client, String zone, String xpath, int index) {
		this.client = client;
		this.zone = zone;
		this.xpath = xpath;
		this.index = index;
		//System.out.println("NEW OBJECT!!!!! " + zone + " , " + xpath + " , " + index);
	}

	@Override
	public boolean waitFor(int timeout) {
		return client.waitForElement(zone, xpath, index, timeout);
	}

	@Override
	public void click() {
		client.click(zone, xpath, index, 1);
	}

	@Override
	public void sendText(String text) {
		client.elementSendText(zone, xpath, index, text);		
	}

	@Override
	public boolean isDisplayed() {
		return Boolean.parseBoolean(client.elementGetProperty(zone, xpath, index, "onScreen"));
	}

	@Override
	public String getText() {
		return client.elementGetProperty(zone, xpath, index, "text");
	}

	@Override
	public boolean swipeTo(String direction, int offset, int swipeTime, int delay, int rounds, boolean click) {
		return client.swipeWhileNotFound(direction, offset, swipeTime, zone, xpath, index, delay, rounds, click);
	}

	@Override
	public String runAPI(String script) {
		return client.runNativeAPICall(zone, xpath, index, script);
	}

}