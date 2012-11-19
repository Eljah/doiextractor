import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.util.List;

@Root(strict = false)
public class DOI {
    //@Element
    //String doi_records;
    @Element(required=false)
    String doi_record;
    @Element(required=false)
    String crossref;
    @Element(required=false)
    String journal;
    @Element(required=false)
    String journal_metadata;
    @Element(required=false)
    String journal_issue;
    @ElementList(entry = "publication_date", required = false, inline = true)
    List<String> publication_date;
    @Element(required=false)
    String journal_volume;
    @Element(required=false)
    String journal_article;
    @Element(required=false)
    String titles;
    @Element(required=false)
    String contributors;
    @ElementList(entry = "person_name", required = false, inline = true)
    List<String> person_name;
    @Element(required=false)
    String pages;
    /*
    */
    @Element(required=false)
    String doi_data;
    @Element(required=false)
    String doi;
    @Element(required=false)
    String resource;
    @Element(required=false)
    String first_page;
    @Element(required=false)
    String last_page;
    @Element(required=false)
    String publisher_item;
    @Element(required=false)
    String item_number;
    @ElementList(entry = "month", required = false, inline = true)
    List<String> month;
    @ElementList(entry = "year", required = false, inline = true)
    List<String> year;
    @ElementList(entry = "given_name", required = false, inline = true)
    List<String> given_name;
    @ElementList(entry = "surname", required = false, inline = true)
    List<String> surname;
    @Element(name = "title",required=false)
    String title;
    @Element(required=false)
    String full_title;
    @Element(required=false)
    String abbrev_title;
    @ElementList(entry = "issn", required = false, inline = true)
    List<String> issn;
    @Element(required=false)
    String coden;
    @Element(required=false)
    String volume;
    @Element(required=false)
    String issue;
}

