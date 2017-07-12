package com.jhp.banseok.parser;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
public class DOMParser {

    private RSSFeed _feed = new RSSFeed();

    public RSSFeed parseXml(String xml) {

        URL url = null;
        try {
            url = new URL(xml);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }

        try {
            // Create required instances
            DocumentBuilderFactory dbf;
            dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            // Parse the xml
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();

            // Get all <item> tags.
            NodeList nl = doc.getElementsByTagName("item");
            int length = nl.getLength();

            for (int i = 0; i < length; i++) {
                Node currentNode = nl.item(i);
                RSSItem _item = new RSSItem();

                NodeList nchild = currentNode.getChildNodes();
                int clength = nchild.getLength();

                // Get the required elements from each Item
                for (int j = 1; j < clength; j = j + 2) {

                    Node thisNode = nchild.item(j);
                    String theString = null;
                    String nodeName = thisNode.getNodeName();

                    theString = nchild.item(j).getFirstChild().getNodeValue();

                    if (theString != null) {
                        if ("title".equals(nodeName)) {
                            // Node name is equals to 'title' so set the Node
                            // value to the Title in the RSSItem.
                            _item.setTitle(theString);
                        }

                        else if ("description".equals(nodeName)) {
                            _item.setDescription(theString);

                            // Parse the html description to get the image url
                            String html = theString;
                            org.jsoup.nodes.Document docHtml = Jsoup
                                    .parse(html);
                            Elements imgEle = docHtml.select("img");
                            _item.setImage(imgEle.attr("src"));
                        }

                        else if ("pubDate".equals(nodeName)) {
                            String pubDate = (theString);

                            SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US);

                            try {

                                Date date = format.parse(pubDate);

                                pubDate = new SimpleDateFormat("yyyy년 MM월 dd일 E요일").format(date);

                            }finally {



                            }
                            // We replace the plus and zero's in the date with
                            // empty string

                            _item.setDate(pubDate);
                        }
                        else if ("dc:creator".equals(nodeName)) {

                            // We replace the plus and zero's in the date with
                            // empty string
                            _item.setWriter(theString);
                        }
                    }
                }

                // add item to the list
                _feed.addItem(_item);
            }

        } catch (Exception e) {
        }

        // Return the final feed once all the Items are added to the RSSFeed
        // Object(_feed).
        return _feed;
    }
}
