import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsingHTMLforDOI {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("file.txt"));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            //System.out.println("Processing line: " + line);
            String htmlGet;
            //System.out.println("Resource: " + htmlGet);
            String doi=null;
            //AccessCrossRefDOI.accessDOI(doi);

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                //System.out.println("Processing line: " + line);
                if (!line.startsWith("//"))
                {
                if (line.startsWith("doi:"))
                {
                    //System.out.println(doi);
                    doi = line.substring(line.indexOf(":")+1).trim();
                    //System.out.println(doi);
                }
                else
                if (line.startsWith("http://dx.doi.org/"))
                {
                    doi = line.substring(line.indexOf("10.")).trim();
                    //System.out.println(doi);
                }
                else
                if (line.startsWith("http:"))
                {
                    htmlGet = accessGet(line.trim());
                    doi = resourceToDOI(htmlGet);
                }
                else
                if (line.startsWith("https:"))
                {
                    htmlGet = accessGet(line.trim());
                    doi = resourceToDOI(htmlGet);
                }
                else

                {
                    doi=line.trim();
                }


                //System.out.println("Resource: " + htmlGet);
                AccessCrossRefDOI.accessDOI(doi);
                }
                line = br.readLine();

            }

        } finally {
            br.close();
        }
    }

    public static String accessGet(String resourceURL) {
        String toReturn = "";
        String output = null;
        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet(resourceURL);
            HttpResponse response=null;
            try
            {
            response = httpClient.execute(getRequest);
            }
            catch (ClientProtocolException e)
            {
                String message=e.getCause().getMessage();
                String URL=message.substring(message.indexOf("\'")+1,message.length()-1);
                //System.out.println(URL);
                response = httpClient.execute(new HttpGet(URL));
         //e.printStackTrace();
            }

            //System.out.println(response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            //System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                //   System.out.println(output);
                toReturn = toReturn + output;
            }

            httpClient.getConnectionManager().shutdown();

        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }
        return toReturn;
    }


    public static String resourceToDOI(String resource) {
        String toReturn;
        //System.out.println(resource);
        Pattern pattern = Pattern.compile("\\b(10[.][0-9]{4,}(?:[.][0-9]+)*/(?:(?![\"&\\'<>])\\S)+)\\b");
        //System.out.println(pattern);
        //int doiIndex = resource.indexOf("DOI:");
        //String doiRegion = resource.substring(doiIndex, doiIndex + 30);
        //Matcher matcher = pattern.matcher(doiRegion);
        Matcher matcher = pattern.matcher(resource);
        //System.out.println(matcher);
        if (matcher.find()) {
            toReturn = matcher.group(0);
            //System.out.println(toReturn); //prints /{item}/
            //Todo several dois on page
            return toReturn;
        } else {
            System.out.println("Match not found");
            return null;
        }

    }

}

