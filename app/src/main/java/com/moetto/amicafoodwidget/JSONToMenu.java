package com.moetto.amicafoodwidget;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by moetto on 03/02/16.
 */
public class JSONToMenu {
    private final static String TAG = "AmicaFood:JSONToMenu";
    JSONObject menu;

    public JSONToMenu(String menu) throws JSONException{
        this.menu = new JSONObject(menu);
        Log.d(TAG, "Created new menu");
    }

    public ArrayList<String> getDishList(int daysFromFirst) {
        try {
            ArrayList<String> dishList = new ArrayList<>();
            JSONArray dishes = getMenusForDay(daysFromFirst);
            if (dishes == null) {
                return null;
            }
            for (int i = 0; i < dishes.length(); i++) {
                JSONObject dish = dishes.getJSONObject(i);
                JSONArray components = (JSONArray) dish.get("Components");
                String dishComponents = "";
                for (int x = 0; x < components.length(); x++) {
                    dishComponents += components.get(x) + "\n";
                }
                dishComponents = dishComponents.trim();
                if(dishComponents.length() > 0) {
                    dishList.add(dishComponents);
                }
            }
            return dishList;
        } catch (JSONException e) {
            Log.e(TAG, Log.getStackTraceString(e));
            return null;
        }
    }

    public JSONArray getMenusForDay(int daysFromFirst) {
        try {
            return menu.getJSONArray("MenusForDays").getJSONObject(daysFromFirst).getJSONArray("SetMenus");
        } catch (JSONException e) {
            Log.e(TAG, Log.getStackTraceString(e));
            return null;
        }
    }

    public static String getRestaurantName(JSONObject menu) {
        try {
            return menu.getString("RestaurantName");
        } catch (JSONException e) {
            Log.e(TAG, Log.getStackTraceString(e));
            return null;
        }
    }
}
