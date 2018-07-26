package com.kandara.medicalapp.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

/**
 * Created by abina on 7/25/2018.
 */

public class HtmlCleaner {

    public static String cleanThis(String text){

        try {
            Document doc = Jsoup.parse(text);

            for (Element element : doc.select("*")) {
                if (!element.hasText() && element.isBlock()) {
                    element.remove();
                }
            }
            text = doc.body().html();
            text = Jsoup.clean(text, Whitelist.relaxed());
        }catch(Exception e){

        }
        return text;
    }
}
