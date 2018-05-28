package other;
//package <set your test package>;
import com.experitest.client.*;
import org.testng.annotations.*;
/**
 *
*/
public class SimulateCaptureBarcodeAndroid {
    protected Client client = null;
    protected GridClient grid = null;
    private String accessKey = "eyJ4cC51Ijo4NiwieHAucCI6MiwieHAubSI6Ik1UVXlNVEUxTURBNU56ZzFPQSIsImFsZyI6IkhTMjU2In0.eyJleHAiOjE4MzkzMjk3MDksImlzcyI6ImNvbS5leHBlcml0ZXN0In0.UWcmBdI24xN3nOXhQ-A14xK2PSI5pZre1MX1dv8U_B8";

    @BeforeMethod
    public void setUp(){
        grid = new GridClient(accessKey, "sales.experitest.com",443, true);
        client = grid.lockDeviceForExecution("EHI","@serialnumber='81fbdb60549592832dd2766472689bee900822a6'", 10, 50000);
        client.setReporter("xml", "reports", "QRCode");
    }

    @Test(groups = {"seetest"})
    public void testEHI(){
//        client.install("cloud:com.ensenta.custommisnap", true, false);
        client.launch("com.ensenta.custommisnap", true, true);
        client.setProperty("ios.auto.accept.alerts", "true");
        client.waitForElement("NATIVE", "xpath=//*[@placeholder='Username']", 0, 10000);
        client.elementSendText("NATIVE", "xpath=//*[@placeholder='Username']", 0, "a");
        client.elementSendText("NATIVE", "xpath=//*[@placeholder='Password']", 0, "a");
        client.click("NATIVE", "xpath=//*[@accessibilityLabel='Log In' and @class='UIButton' and @enabled='true']", 0, 1);
        if(client.waitForElement("NATIVE", "xpath=//*[@accessibilityLabel='Deposit' and @class='UIButton']", 0, 10000)){
        	client.click("NATIVE", "xpath=//*[@accessibilityLabel='Deposit' and @class='UIButton']", 0, 1);
        }
        if(client.waitForElement("NATIVE", "xpath=//*[@class='UITextField']", 0, 10000)) {
        	client.elementSendText("NATIVE", "xpath=//*[@class='UITextField']", 0, "100");
        }
        client.simulateCapture("C:\\Users\\amit.nahum\\Downloads\\Checks\\Checkimage.jpg");
        client.click("NATIVE", "xpath=//*[@accessibilityIdentifier='CheckImageButtonFront']", 0, 1);
        client.waitForElement("NATIVE", "xpath=//*[@class='UIView' and ./*[@accessibilityLabel='Continue']]", 0, 10000);
        client.click("NATIVE", "xpath=//*[@class='UIView' and ./*[@accessibilityLabel='Continue']]", 0, 1);
        client.sleep(4000);
        client.waitForElement("NATIVE", "xpath=(//*[@class='UIView' and ./parent::*[@class='UIView']]/*/*[@text='Continue'])[1]", 0, 30000);
        client.click("NATIVE", "xpath=(//*[@class='UIView' and ./parent::*[@class='UIView']]/*/*[@text='Continue'])[1]", 0, 1);
        client.sleep(15000);
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
