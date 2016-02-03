package com.moetto.amicafoodwidget;

import android.os.AsyncTask;
import android.util.Log;

import com.Ostermiller.util.CGIParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Calendar;

/**
 * Created by moetto on 2/3/16.
 */
public class DownloadJSONTask extends AsyncTask<String, Void, JSONObject> {
    public static final String TAG = "DownloadJSONTask";
    JSONResultListener networkResultListener;

    public DownloadJSONTask(JSONResultListener networkResultListener) {
        this.networkResultListener = networkResultListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        URL url;
        CGIParser cgiParser;
        try {
            cgiParser = new CGIParser("", "UTF-8");
            cgiParser.addParameter("costNumber", "0273");
            cgiParser.addParameter("language", "fi");
            Calendar now = Calendar.getInstance();
            cgiParser.addParameter("firstDay", now.get(Calendar.YEAR) + "-" + (1 + now.get(Calendar.MONTH)) + "-" + now.get(Calendar.DAY_OF_MONTH));
            url = new URL("http://www.amica.fi/modules/json/json/Index?" + cgiParser.toString());
            Log.d(TAG, url.toString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), Charset.forName("UTF-8")));
            return new JSONObject(readAll(reader));
        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, "UTF-8 is not supported");
            return new JSONObject();
        } catch (MalformedURLException e) {
            Log.d(TAG, "Malformed URL");
            return new JSONObject();
        } catch (IOException e) {
            Log.d(TAG, "Error fetching menu");
            return new JSONObject();
        } catch (JSONException e) {
            Log.d(TAG, "Error in JSON");
            return new JSONObject();
        }
    }

    @Override
    protected void onPostExecute(JSONObject s) {
        super.onPostExecute(s);
        networkResultListener.getResult(s);
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
