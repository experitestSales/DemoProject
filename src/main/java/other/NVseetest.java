package other;
//package <set your test package>;
import com.experitest.client.*;

import org.testng.annotations.*;
/**
 * 
*/
public class NVseetest {
    private String host = "localhost";
    private int port = 8889;
    private String projectBaseDirectory = "C:\\Users\\amit.nahum\\workspace\\project8";
    protected Client client = null;
    private String accessKey = "eyJ4cC51Ijo4NiwieHAucCI6MiwieHAubSI6Ik1UVXlNVEUxTURBNU56ZzFPQSIsImFsZyI6IkhTMjU2In0.eyJleHAiOjE4MzcxMjAyMjksImlzcyI6ImNvbS5leHBlcml0ZXN0In0.CzLk-x0MDzFevzJlWCLv0FoB1LM_tJQMgHhwd_7cpJo";
    protected GridClient grid = null;

    
    
    @BeforeMethod
    public void setUp(){
        grid = new GridClient(accessKey, "sales.experitest.com",443, true);
        client = grid.lockDeviceForExecution("EHI","@serialNumber='81fbdb60549592832dd2766472689bee900822a6'", 10, 50000);
        client.setProjectBaseDirectory(projectBaseDirectory);
        client.setReporter("xml", "reports", "NVdemo");
    }

    @Test(groups = {"seetest"})
    public void testEriBank(){
        client.launch("com.ookla.speedtest", false, true);
        client.click("NATIVE", "xpath=//*[@value='GO']", 0, 1);
        if(client.waitForElement("NATIVE", "xpath=//*[@text='RATE YOUR PROVIDER']", 0, 90000)){
            // If statement
        }
        client.sleep(5000);
        client.setNetworkConditions("300K", 120000);
        client.sleep(3000);
        client.click("NATIVE", "xpath=//*[@value='GO']", 0, 1);
        if(client.waitForElement("NATIVE", "xpath=//*[@text='RATE YOUR PROVIDER']", 0, 90000)){
            // If statement
        }
        client.sleep(5000);
        client.setNetworkConditions("Poor Europe", 120000);
        client.sleep(3000);
        client.click("NATIVE", "xpath=//*[@value='GO']", 0, 1);
        if(client.waitForElement("NATIVE", "xpath=//*[@text='RATE YOUR PROVIDER']", 0, 90000)){
            // If statement
        }



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
