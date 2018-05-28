package com.seetest.apps.eribank.pages;

import com.experitest.client.Client;
import com.seetest.framework.BaseTest;
import com.seetest.framework.annotations.FindBy;
import com.seetest.framework.annotations.FindByAndroid;
import com.seetest.framework.annotations.FindByIOS;
import com.seetest.framework.annotations.Index;
import com.seetest.framework.annotations.Zone;
import com.seetest.framework.core.Context;
import com.seetest.framework.core.MobileDeviceType;
import com.seetest.framework.core.MobileOS;
import com.seetest.framework.core.MobileObject;

public class BalancePage extends BaseEribankPage {

	@Zone(Context.WEB)
	@FindBy(xpath="xpath=//*[@nodeName='H1']")
	MobileObject balanceText;
	
	@FindBy(xpath="xpath=//*[@text='Make Payment']")
	MobileObject makePaymentButton;
	
	@FindByIOS(xpath="xpath=//*[@text='Logout']")
	@FindByAndroid(xpath="xpath=//*[@text='Logout']")
	MobileObject logoutButton;
	
	public BalancePage(BaseTest test) {
		super(test);
	}
	
	public BalancePage(Client client, MobileOS mobileOS, MobileDeviceType deviceType) {
		super(client, mobileOS, deviceType);
	}

	@Override
	public void verifyPage() {
		balanceText.isDisplayed();
	}
	
	public String getBalance() {
		return balanceText.getText().substring(18);
	}
	
	public void clickLogoutButton() {
		logoutButton.click();
	}

}