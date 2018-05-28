package com.seetest.apps.eribank.pages;

import com.experitest.client.Client;
import com.seetest.framework.BaseTest;
import com.seetest.framework.annotations.FindBy;
import com.seetest.framework.annotations.FindByAndroid;
import com.seetest.framework.annotations.FindByIOS;
import com.seetest.framework.core.MobileDeviceType;
import com.seetest.framework.core.MobileOS;
import com.seetest.framework.core.MobileObject;

public class LoginPage extends BaseEribankPage{
	
	@FindByIOS(xpath="//*[@accessibilityIdentifier='logo.png']")
	@FindByAndroid(xpath="xpath=//*[@class='android.widget.ImageView']")
	private MobileObject logo;
	
	@FindByIOS(xpath="//*[@placeholder='Username']")
	@FindByAndroid(xpath="xpath=//*[@hint='Username']")
	private MobileObject usernameTextField;
	
	@FindByIOS(xpath="//*[@placeholder='Password']")
	@FindByAndroid(xpath="xpath=//*[@hint='Password']")
	private MobileObject passwordTextField;
	
	@FindByIOS(xpath="//*[@accessibilityLabel='loginButton' and @class='UIButton']")
	@FindByAndroid(xpath="xpath=//*[@text='Login']")
	private MobileObject loginButton;
	
	public LoginPage(BaseTest test) {
		super(test);
	}
	
	public LoginPage(Client client, MobileOS mobileOS, MobileDeviceType deviceType) {
		super(client, mobileOS, deviceType);
	}

	@Override
	public void verifyPage() {
		logo.isDisplayed();
	}
	
	public void enterCredentials(String username, String password) {
		usernameTextField.sendText(username);
		passwordTextField.sendText(password);
	}
	
	public void clickLoginButton() {
		loginButton.click();
	}

}