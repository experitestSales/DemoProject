package other;

import com.experitest.client.*;
import org.testng.annotations.*;
import com.experitest.client.Utils.*;

/**
 * Created by Arundoss on 5/28/2018.
 */
public class MobileListener_iOS{

    private String host = "localhost";
    private int port = 8889;
    protected Client client = null;

    @BeforeMethod
    public void setUp(){
        client = new Client(host, port, true);
        client.setReporter("xml", "reports", "MobileListener-iOS");
        client.addMobileListener("NATIVE", "xpath=//*[contains (@text,'CLOCK') and contains(@text,'Alarm')]",
                new MobileListener() {
                    @Override
                    public boolean recover(String type, String xpath) {
                        client.elementSwipe("NATIVE", "xpath=//*[contains (@text,'CLOCK') and contains(@text,'Alarm')]", 0, "Down", 0, 2000);
                        client.report("A call has appeared and handled in iOS device",true);
                        System.out.println("MobileListener invoked and handled");
                        return true;

                    }
                }
        );

    }

    @Test
    public void testMobileListener_iOS(){
        client.setDevice("ios_app:iPhone 8plus A11");
        client.launch("com.apple.mobiletimer", false, false);
        if(client.waitForElement("NATIVE", "xpath=//*[@text='Alarm' and parent::*[@class='UIATabBar']]", 0, 10000)){
            client.click("NATIVE", "xpath=//*[@text='Alarm' and parent::*[@class='UIATabBar']]", 0, 1);
        }
        client.waitForElement("NATIVE", "xpath=//*[@value='Add' and preceding::*[@text='Alarm']]", 0, 10000);
        client.click("NATIVE", "xpath=//*[@value='Add' and preceding::*[@text='Alarm']]", 0, 1);
        client.setPickerValues("NATIVE", "xpath=//*[@XCElementType='XCUIElementTypeDatePicker']", 0, 1, "up:1");
        client.click("NATIVE", "xpath=//*[@value='Save' and @XCElementType='XCUIElementTypeButton']", 0, 1);
        client.deviceAction("Home");
        client.waitForElement("NATIVE","//*[@placeholder='Username']",0,60000);
    }

    @AfterMethod
    public void tearDown(){
        // Generates a report of the test case.
        // For more information - https://docs.experitest.com/display/public/SA/Report+Of+Executed+Test
        client.generateReport(false);
        // Releases the client so that other clients can approach the agent in the near future.
        client.releaseClient();
    }
}
