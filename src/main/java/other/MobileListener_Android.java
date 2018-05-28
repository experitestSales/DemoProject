package other;
import com.experitest.client.*;
import org.testng.annotations.*;
/**
 *
 */
public class MobileListener_Android {
    private String host = "localhost";
    private int port = 8889;
    protected Client client = null;

    @BeforeMethod
    public void setUp(){
        client = new Client(host, port, true);
        client.setReporter("xml", "reports", "Untitled");
        client.addMobileListener("NATIVE", "xpath=//*[@text='Incoming call']",
                new MobileListener() {
                    @Override
                    public boolean recover(String type, String xpath) {
                        client.dragDrop("NATIVE", "xpath=//*[@id='reject']", 0, "xpath=//*[@id='receive']", 0);
                        client.report("A Alarm pop-up has appeared and handled in android device",true);
                        System.out.println("MobileListener invoked and handled");
                        return true;

                    }
                }
        );
    }

    @Test
    public void testUntitled(){
        client.waitForDevice("@os='android'",30000);
        if(client.install("com.popularapp.fakecall/.AdActivity", true, false)){
            // If statement
        }
        client.launch("com.popularapp.fakecall/.AdActivity", true, false);
        if(client.waitForElement("NATIVE", "xpath=//*[@text='Call Assistant']", 0, 10000)){
            // If statement
        }
        client.click("NATIVE", "xpath=//*[@text='Save & Make Call']", 0, 1);
        if(client.waitForElement("NATIVE", "xpath=//*[@text='Make a call after']", 0, 10000)){
            // If statement
        }
        client.click("NATIVE", "xpath=//*[@text='Make a call after']", 0, 1);
        client.click("NATIVE", "xpath=//*[@text='10s']", 0, 1);
        client.deviceAction("Home");
        client.waitForElement("NATIVE","hint=Username",0,20000);

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
