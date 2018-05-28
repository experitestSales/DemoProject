package other;
//package <set your test package>;
import com.experitest.client.*;
import org.testng.annotations.*;
/**
 *
*/
public class SimulateCaptureCheckDepositIOS {
    protected Client client = null;
    protected GridClient grid = null;
    private String accessKey = "eyJ4cC51Ijo4NiwieHAucCI6MiwieHAubSI6Ik1UVXlNVEUxTURBNU56ZzFPQSIsImFsZyI6IkhTMjU2In0.eyJleHAiOjE4MzkzMjk3MDksImlzcyI6ImNvbS5leHBlcml0ZXN0In0.UWcmBdI24xN3nOXhQ-A14xK2PSI5pZre1MX1dv8U_B8";

    @BeforeMethod
    public void setUp(){
        grid = new GridClient(accessKey, "sales.experitest.com",443, true);
        client = grid.lockDeviceForExecution("EHI","@os='android'", 10, 50000);
        client.setReporter("xml", "reports", "QRCode");
    }

    @Test(groups = {"seetest"})
    public void testEHI(){
        client.install("cloud:me.scan.android.client/.ui.ScanActivity", true, false);
        client.launch("me.scan.android.client/.ui.ScanActivity", true, true);
        if(client.waitForElement("NATIVE", "xpath=//*[@text='Allow']", 0, 10000)){
        	client.click("NATIVE", "xpath=//*[@text='Allow']", 0, 1);
        }
        client.sleep(1000);
        client.simulateCapture("C:\\Users\\amit.nahum\\Downloads\\Checks\\QR\\Capture1.JPG");
        client.sleep(2000);
        client.click("NATIVE", "xpath=//*[@text='OK']", 0, 1);
        client.waitForElement("WEB", "//*[@text='LOGIN' and @class='login-btn']", 0, 20000);
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
