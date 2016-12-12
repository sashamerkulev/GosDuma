package ru.merkulyevsasha.gosduma.http;


import android.text.Html;
import android.util.Xml;

import com.google.firebase.crash.FirebaseCrash;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.merkulyevsasha.gosduma.models.Article;

public class RssParser {


    private boolean tryParseDateFormat(String date, String formatDate, Article item){
        try{
            SimpleDateFormat format = new SimpleDateFormat(formatDate, Locale.ENGLISH);
            item.PubDate = format.parse(date);
            return true;
        }
        catch(ParseException e){
            FirebaseCrash.report(e);
            e.printStackTrace();
            return false;
        }
    }
    private List<Article> parseXML(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Article> items = new ArrayList<Article>();
        int eventType = parser.getEventType();
        Article item = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equals("item")){
                        item = new Article();
                    } else if (item != null){
                        switch (name) {
                            case "title":
                                item.Title = parser.nextText();
                                break;
                            case "description":
                                String description = parser.nextText();
                                if (description != null) {
                                    description = Html.fromHtml(description).toString();
                                }
                                item.Description = description;
                                break;
                            case "link":
                                item.Link = parser.nextText();
                                break;
                            case "pubDate":
                                String pubDate = parser.nextText();
                                if (!tryParseDateFormat(pubDate, "E, dd MMM yyyy HH:mm:ss z", item))
                                    tryParseDateFormat(pubDate, "dd MMM yyyy HH:mm:ss z", item);
                                break;
                            case "category":
                                item.Category = parser.nextText();
                                break;
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("item") && item != null && item.PubDate != null){
                        String description = item.Description == null ? "" :item.Description.toLowerCase();
                        String title = item.Title == null ? "" :item.Title.toLowerCase();
                        String category = item.Category == null ? "" :item.Category.toLowerCase();
                        item.SearchText =  title + category + description;
                        items.add(item);
                    }
            }
            eventType = parser.next();
        }

        return items;

    }

    public List<Article> parseXml(String xml)  throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        InputStream stream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        parser.setInput(stream, "UTF-8");
        return parseXML(parser);
    }

}
