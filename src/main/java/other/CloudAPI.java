package other;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
public class CloudAPI{
      
    private static final String APPLICATIONS_URL = "/applications";
    private static final String ACTION = "/new";
    private String host = "sales.experitest.com";//TODO: host name or ip goes here
    private String port = "443";//TODO: open port goes here
    private String webPage= "https://" + host + ":" + port + "/api/v1";
    private String authStringEnc;
    private String AccessKey;
    
    @Before
    public void setup() {
//        String name = "admin";//TODO: admin user name is here
//        String password = "va1idPassword";//TODO: admin password is here
        AccessKey = "eyJ4cC51Ijo4NiwieHAucCI6MiwieHAubSI6Ik1UVXlNVEUxTURBNU56ZzFPQSIsImFsZyI6IkhTMjU2In0.eyJleHAiOjE4MzkzMjk3MDksImlzcyI6ImNvbS5leHBlcml0ZXN0In0.UWcmBdI24xN3nOXhQ-A14xK2PSI5pZre1MX1dv8U_B8";  
//        String authString = name + ":" + password;
    }
    
    
    @Test
    public void testPostNewApplication() throws IOException {
          
//        String postURL = prepareURL();
//        uploadFile("C:\\Users\\amit.nahum\\AppData\\Roaming\\seetest\\original-apks\\com.experitest.simplebrowser.WebViewActivity.2.apk", postURL);//TODO: filePath in local PC
        doGet(APPLICATIONS_URL, webPage, AccessKey);
            
    }
    private String prepareURL() {
        String postURL = webPage + APPLICATIONS_URL + ACTION;
        return postURL;
    }
    protected void printGet(URL url, HttpURLConnection httpURLConnection, String result) throws IOException {
        int responseCode = httpURLConnection.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        System.out.println(result);
    }
    
    protected String doGet(String entity, String webPage, String authStringEnc) throws IOException {
        URL url = new URL(webPage+entity);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("Authorization", "Basic " + AccessKey);
        InputStream is = urlConnection.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        int numCharsRead;
        char[] charArray = new char[1024];
        StringBuffer sb = new StringBuffer();
        while ((numCharsRead = isr.read(charArray)) > 0) {
            sb.append(charArray, 0, numCharsRead);
        }
        String result = sb.toString();
        printGet(url, (HttpURLConnection) urlConnection, result);
        boolean isResponseValid = ((HttpURLConnection)urlConnection).getResponseCode() < 300;
        Assert.assertTrue("Did not get valid response", isResponseValid);
        return result;
    }
      
    String uploadFile(String pathToFile, String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost uploadFile = new HttpPost(url.toString());
         
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        /*
        *  This attaches the file to the POST:
        */
        File f = new File(pathToFile);
        builder.addTextBody(f.getName(), "yes", ContentType.TEXT_PLAIN);
        builder.addBinaryBody(
            "file",
            new FileInputStream(f),
            ContentType.APPLICATION_OCTET_STREAM,
            f.getName()
        );
        HttpEntity multipart = builder.build();
        uploadFile.setEntity(multipart);
        uploadFile.addHeader("Authorization", "Basic " + authStringEnc);
        System.out.println("\nSending 'POST' request to URL : " + url + " file: " + pathToFile);
        CloseableHttpResponse response = httpClient.execute(uploadFile);
        HttpEntity responseEntity = response.getEntity();
        java.io.InputStream stream = responseEntity.getContent();
        BufferedReader in = new BufferedReader(new InputStreamReader(stream));
        String inputLine;
        StringBuffer responseBuffer = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
                        responseBuffer.append(inputLine);
        }
        in.close();
        String finalString = responseBuffer.toString();
        System.out.println(finalString);
         
        System.out.println(String.format("Got response buffer: %s", responseBuffer.toString()));
        Assert.assertTrue("Did not get Success Status", responseBuffer.toString().contains("\"status\":\"SUCCESS\""));
        return finalString;
    }
    protected void printPost(URL url, HttpURLConnection httpURLConnection, String query) throws IOException {
        int responseCode = httpURLConnection.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Sending Query : " + query);
        System.out.println("Response Code : " + responseCode);
    }
    /**
     * @param entity can be "/users" / "/projects" / "/devices" etc
     * String query = String.format("param1=%s&param2=%s", URLEncoder.encode(param1, charset), URLEncoder.encode(param2, charset));
     */
    protected String doPost(String entity , String query, String webPage, String authStringEnc) throws IOException {
        URL url = new URL(webPage+entity);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + StandardCharsets.UTF_8.name());
        urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
    
        OutputStream output = urlConnection.getOutputStream();
        output.write(query.getBytes(StandardCharsets.UTF_8.name()));
            
        HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
           
        printPost(url, httpURLConnection, query);
   
        InputStream stream = null;
           
        if (httpURLConnection.getResponseCode() >= 400) {
            stream = httpURLConnection.getErrorStream();
               
        } else {
            stream = httpURLConnection.getInputStream();     
        }
           
        BufferedReader in = new BufferedReader(new InputStreamReader(stream));
        String inputLine;
        StringBuffer responseBuffer = new StringBuffer();
   
        while ((inputLine = in.readLine()) != null) {
            responseBuffer.append(inputLine);
        }
        in.close();
           
        //print result
        System.out.println(responseBuffer.toString());
        boolean isResponseValid = httpURLConnection.getResponseCode() < 300;
        Assert.assertTrue("Did not get valid response", isResponseValid);
        return responseBuffer.toString();
            
    }
}