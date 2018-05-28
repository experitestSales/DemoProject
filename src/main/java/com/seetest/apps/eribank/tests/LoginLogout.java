package com.seetest.apps.eribank.tests;

import org.testng.annotations.Test;

import com.seetest.apps.eribank.pages.BalancePage;
import com.seetest.apps.eribank.pages.LoginPage;
import com.seetest.framework.BaseTest;

public class LoginLogout extends BaseTest {
	
	@Test
	public void LoginLogoutValidCreds() {
		LoginPage loginPage = new LoginPage(this);
		loginPage.verifyPage();
		loginPage.enterCredentials(getDataProperty("valid.username"),getDataProperty("valid.password"));
		loginPage.clickLoginButton();
		BalancePage balancePage = new BalancePage(this);
		balancePage.verifyPage();
		balancePage.clickLogoutButton();
		loginPage.verifyPage();
	}
	
	@Test
	public void LoginLogoutInvalidCreds() {
		LoginPage loginPage = new LoginPage(this);
		loginPage.verifyPage();
		loginPage.enterCredentials(getDataProperty("invalid.username"),getDataProperty("invalid.password"));
		loginPage.clickLoginButton();
		BalancePage balancePage = new BalancePage(this);
		balancePage.verifyPage();
		balancePage.clickLogoutButton();
		loginPage.verifyPage();
	}

}
