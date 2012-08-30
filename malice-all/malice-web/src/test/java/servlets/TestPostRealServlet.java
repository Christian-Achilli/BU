package servlets;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class TestPostRealServlet {

    private static final String URL_UNDER_TEST = "https://live.portale-gar.it/malice/xml/receiver"; //PRODUZIONE
//    private static final String URL_UNDER_TEST = "http://127.0.0.1:8888/malice/xml/receiver"; //LOCALE JETTY
//    private static final String URL_UNDER_TEST = "http://localhost:8080/portale-gar-2.5.2/malice/xml/receiver"; //LOCALE TOMCAT
//    private static final String URL_UNDER_TEST = "http://pgar-test2.elasticbeanstalk.com/malice/xml/receiver"; //TEST

    private static final String ENCODING = "UTF-8";
    private final String INPUT_XML_EXAMPLE_FILE_NAME = "src/test/resources/A7RCA06338D.xml";
    private File input_xml;

    @Test
    public void postToServlet() throws Exception {
        try {
            input_xml = new File(INPUT_XML_EXAMPLE_FILE_NAME);
            String data = IOUtils.toString(new FileInputStream(input_xml), ENCODING);
            // Send data
            URL url = new URL(URL_UNDER_TEST);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept-Charset", ENCODING);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + ENCODING);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), ENCODING);
            wr.write(data);
            wr.flush();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }
            wr.close();
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
