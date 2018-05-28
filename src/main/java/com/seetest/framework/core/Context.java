package com.seetest.framework.core;

public enum Context {
	NATIVE("NATIVE"), 
	NAITVE_NI("NATIVE"), 
	WEB("WEB");

	private final String asString;
	
	private Context(final String asString) {
		this.asString = asString; 
	}
	
	public String asString() {
		return asString;
	}
	
}