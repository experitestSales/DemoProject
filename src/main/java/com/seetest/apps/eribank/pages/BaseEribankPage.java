package com.seetest.apps.eribank.pages;

import com.experitest.client.Client;
import com.seetest.framework.BasePage;
import com.seetest.framework.BaseTest;
import com.seetest.framework.core.Context;
import com.seetest.framework.core.MobileDeviceType;
import com.seetest.framework.core.MobileOS;

public abstract class BaseEribankPage extends BasePage {

	protected BaseEribankPage(BaseTest test) {
		super(test);
	}
	
	protected BaseEribankPage(Client client, MobileOS mobileOS, MobileDeviceType deviceType) {
		super(client, mobileOS, deviceType);
	}

	@Override
	public Context getDefaultContext() {
		return Context.NATIVE;
	}

}