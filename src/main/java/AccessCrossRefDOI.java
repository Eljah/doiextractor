import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class AccessCrossRefDOI {

    public static void accessDOI(String doi) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet(
                    "http://www.crossref.org/openurl/?id=doi:" + doi + "&noredirect=true&pid=eljah@mail.ru&format=unixref");
            //HttpGet getRequest = new HttpGet("http://www.crossref.org/openurl/?id=doi:10.1143/JPSJ.67.1560&noredirect=true&pid=eljah@mail.ru&format=unixref");

            //getRequest.addHeader("accept", "application/json");

            HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());

            }
            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String output;
            //System.out.println("Output CrossRef XML: \n");
            httpClient.getConnectionManager().shutdown();
            DOI result = null;
            String toProcess = "";
            while ((output = br.readLine()) != null) {
                //System.out.println(output);
                toProcess = toProcess + output.trim();
            }
            //System.out.println(toProcess);
            result = getDOI(toProcess);
            //Gondela E., Walczak K. Z. Synthesis and preliminary bioactivity assays of 3,4-dichloro-5-(ω-hydroxyalkylamino)-2(5H)-furanones // Eur. J. Med. Chem. 2010. V. 45. P. 3993−3997.

            
            for (int i = 0; i < result.surname.size(); i++) {
                String secondName="";
                if (result.given_name.get(i).endsWith(".")&&result.given_name.get(i).length()>2)
                {
                   secondName=" "+result.given_name.get(i).substring(result.given_name.get(i).indexOf(" ")+1);
                }
                System.out.print(result.surname.get(i) + " " + result.given_name.get(i).charAt(0) + "."+secondName+", ");
            }
            ;
            System.out.print(result.title.trim());

            System.out.print(" // ");
            System.out.print(result.abbrev_title + ". " + result.year.get(0)+".");
            if (result.volume != null)
                System.out.print(" V. " + result.volume);
            if (result.issue != null) {
                if (result.volume == null) {
                    System.out.print(" V. " + result.issue);
                }
                else
                {
                    System.out.print(". № " + result.issue);
                }
            } ;
            System.out.print(".");
            if (result.item_number != null)
                System.out.print(" " + result.item_number + " ");
            if (result.first_page != null)
                System.out.print(" P. " + result.first_page);
            if (result.last_page != null)
                System.out.print("–" + result.last_page);

            System.out.println(".");
        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static DOI getDOI(String text) {
        Serializer serializer = new Persister();
        DOI result = null;
        try {
            result = serializer.read(DOI.class, text, false);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        result.title = text.substring(text.lastIndexOf("<title>") + 7, text.indexOf("</title>")).replaceAll("\\s+", " ").replaceAll("> <", "><");
        return result;
    }

}