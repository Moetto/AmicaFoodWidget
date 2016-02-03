package com.moetto.amicafoodwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by moetto on 02/02/16.
 */
public class MenuProvider extends AppWidgetProvider implements JSONResultListener {
    private static final String CLICK_UPDATE = "click_update";
    private static final int requestCode = 1;
    private static final String TAG = "MenuProvider";
    private Context context;

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Intent intent = new Intent(context, MenuProvider.class);
        intent.setAction(CLICK_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.menu_layout);
        remoteViews.setOnClickPendingIntent(R.id.update, pendingIntent);
        remoteViews.setTextViewText(R.id.menu_text, "Hello world again!");
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(TAG, "Received intent");
        Log.d(TAG, intent.getAction());
        if (intent.getAction().equals(CLICK_UPDATE)) {
            Log.d(TAG, "Update intent");
            this.context = context;
            new DownloadJSONTask(this).execute();
            updateMenu(context, "Update");
        }
    }

    private void updateMenu(Context context, String menu) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.menu_layout);
        remoteViews.setTextViewText(R.id.menu_text, menu);
        AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, MenuProvider.class), remoteViews);
    }

    public void getResult(JSONObject menu) {
        Log.d(TAG, menu.toString());
        String result = "";
        try {
            JSONArray menusForDays = (JSONArray) menu.get("MenusForDays");
            for (int y = 0; y < menusForDays.length(); y++) {
                JSONObject menuOfDay = (JSONObject)  menusForDays.get(y);
                JSONArray setMenus = (JSONArray)menuOfDay.get("SetMenus");
                for (int i = 0; i < setMenus.length(); i++) {
                    JSONObject dish = setMenus.getJSONObject(i);
                    JSONArray components = (JSONArray) dish.get("Components");
                    for (int x = 0; x < components.length(); x++) {
                        result += components.get(x)+"\n";
                    }
                    result += "------\n";
                }
                updateMenu(context, result);
            }
        } catch (JSONException e) {
            Log.d(TAG, Log.getStackTraceString(e));
            updateMenu(context, "Error in JSON");
        }
    }
}