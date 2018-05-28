package other;

//package <set your test package>;
import com.experitest.client.*;
import org.junit.*;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
*
*/
public class CSVLoop {
  private String host = "localhost";
  private int port = 8889;
  private String projectBaseDirectory = "C:\\Users\\amit.nahum\\workspace\\project8";
  protected Client client = null;
  private String[] loginData = null;
  private boolean iOS;

  @BeforeTest
  public void setUp(){
      client = new Client(host, port, true);
      client.setProjectBaseDirectory(projectBaseDirectory);
      client.setReporter("xml", "reports", "Untitled");
      loginInit();
      client.setDevice("ios_app:iPhone 7 Plus");
  }

  @Test
  public void testUntitled(){
          client.install("com.experitest.ExperiBank", true, true);
          client.launch("com.experitest.ExperiBank", true, true);
      for (int i = 0; i < loginData.length; i++) {
          client.elementSendText("NATIVE", "xpath=//*[@placeholder='Username']", 0, "");
          client.click("NATIVE", "xpath=//*[@placeholder='Username']", 0, 1);
          client.sendText(loginData[i].substring(0, loginData[i].indexOf(',')));
          client.click("NATIVE", "xpath=//*[@placeholder='Password']", 0, 1);
          client.elementSendText("NATIVE", "xpath=//*[@placeholder='Password']", 0, loginData[i].substring(loginData[i].indexOf(',') + 1, loginData[i].length()));
          client.click("NATIVE", "xpath=//*[@text='Login']", 0, 1);
          if (!client.isElementFound("NATIVE", "xpath=//*[@text='Logout']") & i < 7)
              client.click("NATIVE", "xpath=//*[@text='Dismiss']", 0, 1);
      }
  }

  @AfterTest
  public void tearDown(){
      // Generates a report of the test case.
      // For more information - https://docs.experitest.com/display/public/SA/Report+Of+Executed+Test
      client.generateReport(false);
      // Releases the client so that other clients can approach the agent in the near future.
      client.releaseClient();
  }

  public void loginInit() {
      try {
          FileReader fileReader = new FileReader("login.csv");
          StringBuilder sb = new StringBuilder();
          BufferedReader br = new BufferedReader(fileReader);
          String temp;
          while ((temp = br.readLine()) != null) {
              sb.append(temp);
              sb.append('\n');
          }
          loginData = sb.toString().split("\n");
      } catch (FileNotFoundException e) {

          e.printStackTrace();
      } catch (
              IOException e) {
          e.printStackTrace();
      }
  }
}
