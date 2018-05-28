package com.seetest.framework;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;

import com.experitest.client.Client;
import com.experitest.client.GridClient;
import com.seetest.framework.core.MobileOS;
import com.seetest.framework.core.MobileDeviceType;
import com.seetest.framework.utils.SeeTestHelpers;

//@Listeners(ManagerITestListener.class)
public abstract class BaseTest {
	
	//General
	protected static String buildId;
	private static HashMap<String,String> devicesNamesMap;
	private static Properties dataProps;
	private static boolean logDevice;
    private static File deviceLogsLocalFolder;  
	
	//Grid
	private static boolean useGrid;
    private static final String GRID_ACCESS_KEY = "eyJ4cC51Ijo4NiwieHAucCI6MiwieHAubSI6Ik1UVXlNVEUxTURBNU56ZzFPQSIsImFsZyI6IkhTMjU2In0.eyJleHAiOjE4MzkzMjk3MDksImlzcyI6ImNvbS5leHBlcml0ZXN0In0.UWcmBdI24xN3nOXhQ-A14xK2PSI5pZre1MX1dv8U_B8";
    private static final String GRID_HOST = "sales.experitest.com";
    private static final int GRID_PORT = 443;
    private static final boolean GRID_SECURED = true;
    
    //Client and Device
    protected Client client;
    protected String deviceName;
    protected MobileOS deviceOS;
    protected MobileDeviceType deviceType;
    
    private static synchronized void storeDeviceName(String testId, String deviceName) {
    	if (devicesNamesMap == null) {
    		devicesNamesMap = new HashMap<String,String>();
    	}
    	devicesNamesMap.put(testId, deviceName);
    }
    
    private Client createLocalClient(String deviceQuery) {
    	Client client = new Client("localhost", 8889, true);
        client.setProjectBaseDirectory("C:\\Users\\amit.nahum\\workspace\\project6");
        client.setReporter("xml", "reports", "123");
    	client.waitForDevice(deviceQuery, 30000);
    	return client;
    }
    
    private Client createGridClient(String deviceQuery) {
    	GridClient grid = new GridClient(GRID_ACCESS_KEY, GRID_HOST, GRID_PORT, GRID_SECURED);
    	return grid.lockDeviceForExecution("BeforeTest Setup", deviceQuery, 8, 30000);
    }
    
    protected static String getDataProperty(String key) {
    	return dataProps.getProperty(key);
    }
    
    private static String getTestNGParam(ITestContext context, String key) {
    	return context.getCurrentXmlTest().getParameter(key);
    }
    
    @BeforeSuite
    public void beforeSuite(ITestContext context) {
    	//reporter
    	//System.setProperty("manager.url", "192.168.4.23:8081");
    	//buildId = SeeTestHelpers.getBuildId();
    	//grid
        useGrid = Boolean.parseBoolean(getTestNGParam(context,"run.on.grid"));
    	//data properties file
        initDataFile(getTestNGParam(context,"data.file.name"));
        //device log
        initDeviceLogsFolder(context);
    }
    
    private void initDataFile(String dataFileName) {
    	dataProps = new Properties();
        try {
			dataProps.load(this.getClass().getResourceAsStream("/" + dataFileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void initDeviceLogsFolder(ITestContext context) {
    	logDevice = Boolean.parseBoolean(getTestNGParam(context,"capture.device.log"));
        if (logDevice) {
        	deviceLogsLocalFolder = new File(System.getProperty("user.home") + File.separator + "temp_seetest_logs");
        	if (!deviceLogsLocalFolder.isDirectory()) {
        		deviceLogsLocalFolder.mkdir();
        	}
        }
    }

    @BeforeTest
    public void beforeTest(ITestContext context) {
    	Client beforeTestClient = null;
    	String deviceQuery = getTestNGParam(context,"device.query");
    	beforeTestClient = useGrid ? createGridClient(deviceQuery) : createLocalClient(deviceQuery);
    	storeDeviceName(getTestNGParam(context,"test.id"), beforeTestClient.getProperty("device.name"));
    	setDeviceFields(getTestNGParam(context,"test.id"));
    	if (getTestNGParam(context,"app.type").equals("native") && Boolean.parseBoolean(getTestNGParam(context,"install.before.test"))) {
    		runBeforeTestInstall(beforeTestClient,context);
    	}
    	beforeTestClient.releaseClient();
    }
    
    private void setDeviceFields(String testId) {
    	deviceName = devicesNamesMap.get(testId);
    	deviceOS = deviceName.contains("adb") ? MobileOS.ANDROID : MobileOS.IOS;
    	deviceType = MobileDeviceType.PHONE;
    }
    
    private void runBeforeTestInstall(Client client, ITestContext context) {
    	String buildPath = null;
		boolean instrument = false;
		String appName = null;
		if (deviceOS.equals(MobileOS.IOS)) {
			buildPath = getTestNGParam(context,"ios.build.path");
			instrument = Boolean.parseBoolean(getTestNGParam(context,"ios.app.instrumentation"));
			appName = getTestNGParam(context,"ios.app.bundle.id");
		}
		if (deviceOS.equals(MobileOS.ANDROID)) {
			buildPath = getTestNGParam(context,"android.build.path");
			instrument = Boolean.parseBoolean(getTestNGParam(context,"android.app.instrumentation"));
			appName = getTestNGParam(context,"android.app.package");
		}
		client.uninstall(appName);
		client.install(System.getProperty("user.dir") + buildPath, instrument, false);
    }
    
    private String getQueryFromName() {
    	return "@name='" + deviceName.split(  ":")[1] + "'";
    }

    @BeforeClass
    public void beforeClass(ITestContext context) {
    }
    
    @BeforeMethod
    public void beforeMethod(ITestContext context, Method method) {
    	if (useGrid) {
    		client = this.createGridClient(getQueryFromName());
    	} else {
    		client = this.createLocalClient(getQueryFromName());
    	}
    	addMobileListeners(client);
    	if (logDevice) {
    		startDeviceLogging();
    	}
    	String buildPath = null;
		boolean instrument = false;
		String appName = null;
		String activityName = null;
    	setDeviceFields(getTestNGParam(context,"test.id"));
		if (deviceOS.equals(MobileOS.IOS)) {
			buildPath = getTestNGParam(context,"ios.build.path");
			instrument = Boolean.parseBoolean(getTestNGParam(context,"ios.app.instrumentation"));
			appName = getTestNGParam(context,"ios.app.bundle.id");
		}
		if (deviceOS.equals(MobileOS.ANDROID)) {
			buildPath = getTestNGParam(context,"android.build.path");
			instrument = Boolean.parseBoolean(getTestNGParam(context,"android.app.instrumentation"));
			activityName = getTestNGParam(context,"android.app.launch.activity");
			appName = getTestNGParam(context,"android.app.package") +"/"+ activityName;
		}
		client.launch(appName, instrument, true);
    }
    
    private void startDeviceLogging() {
    	client.startLoggingDevice(deviceLogsLocalFolder + File.separator + System.currentTimeMillis() + "_" + deviceName.split(":")[1] + ".txt");
    }
    
    private void finalizeDeviceLogging() {
    	File deviceLogFile = new File(client.stopLoggingDevice());
    	//PManager.getInstance().addLogFile(deviceLogFile);
    }
    
    //Override to add mobile listeners
    protected void addMobileListeners(Client client) {
    	client.report("No listneres added", true);
    }
    
    @AfterMethod
    public void afterMethod() {
    	if (logDevice) {
    		finalizeDeviceLogging();
    	}
    	//PManager.getInstance().addReportFolder(client.generateReport(false));
    	client.generateReport(false);
    	client.releaseClient();
    }

    @AfterClass
    public void afterClass() {

    }

    @AfterTest
    public void afterTest() {

    }
    
}