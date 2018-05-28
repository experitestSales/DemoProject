package com.seetest.framework;

import com.experitest.client.Client;
import com.seetest.apps.eribank.pages.BalancePage;
import com.seetest.apps.eribank.pages.LoginPage;
import com.seetest.framework.annotations.AnnotationsUtils;
import com.seetest.framework.core.Context;
import com.seetest.framework.core.MobileDeviceType;
import com.seetest.framework.core.MobileOS;

public abstract class BasePage {
	public final Client client;
	public final MobileOS deviceOS;
	public final MobileDeviceType deviceType;

    protected BasePage(BaseTest test) {
        this.client = test.client;
        this.deviceOS = test.deviceOS;
        this.deviceType = test.deviceType;
        AnnotationsUtils.initPageMobileObjects(this);
    }
    
    protected BasePage(Client client, MobileOS mobileOS, MobileDeviceType deviceType) {
		this.client = client;
		this.deviceOS = mobileOS;
		this.deviceType = deviceType;
		AnnotationsUtils.initPageMobileObjects(this);
	}
    
    public abstract Context getDefaultContext();
    
    public abstract void verifyPage();
    
    protected void seeTestAssert(boolean condition, String reportLine) {
    	client.report(reportLine, condition);
    	org.testng.Assert.assertTrue(condition, reportLine);
    }

    public static void main(String...args) {
    	Client c = new Client("localhost",8889,true);
    	c.setDevice("ios_app:IPhone6a");
    	LoginPage loginPage = new LoginPage(c, MobileOS.IOS, MobileDeviceType.PHONE);
    	loginPage.enterCredentials("company", "company");
    	loginPage.clickLoginButton();
    	BalancePage balancePage = new BalancePage(c, MobileOS.IOS, MobileDeviceType.PHONE);
    	System.out.println(balancePage.getBalance());
    }
    
}