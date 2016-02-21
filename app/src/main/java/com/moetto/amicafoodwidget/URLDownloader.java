package com.moetto.amicafoodwidget;

import android.content.Context;
import android.util.Log;

import com.Ostermiller.util.CGIParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Calendar;


/**
 * Created by moetto on 03/02/16.
 */
public class URLDownloader {
    private final static String TAG = "AmicaFood:URLDownloader";

    public static String downloadURL(Context context) {
        URL url;
        CGIParser cgiParser;
        try {
            cgiParser = new CGIParser("", "UTF-8");
            cgiParser.addParameter("costNumber", "0273");
            cgiParser.addParameter("language", "fi");
            Calendar calendar = PreferenceManager.getCalendar(context);
            cgiParser.addParameter("firstDay", calendar.get(Calendar.YEAR) + "-" + (1 + calendar.get(Calendar.MONTH)) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
            url = new URL("http://www.amica.fi/modules/json/json/Index?" + cgiParser.toString());
            Log.d(TAG, url.toString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), Charset.forName("UTF-8")));
            return readAll(reader);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "UTF-8 is not supported");
        } catch (MalformedURLException e) {
            Log.e(TAG, "Malformed URL");
        } catch (IOException e) {
            Log.e(TAG, "Error fetching menu");
        }
        return null;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

}
