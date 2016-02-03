package com.moetto.amicafoodwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by moetto on 02/02/16.
 */
public class MenuProvider extends AppWidgetProvider {
    private static final String CLICK_UPDATE = "click_update";
    private static final int requestCode = 1;
    private static final String TAG = "AmicaFood:MenuProvider";
    private Context context;

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        this.context = context;
        Intent updateIntent = new Intent(context, MenuProvider.class);
        updateIntent.setAction(CLICK_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, updateIntent, 0);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.menu_layout);
        remoteViews.setRemoteAdapter(R.id.dish_list, new Intent(context, DishListService.class));
        remoteViews.setOnClickPendingIntent(R.id.update, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(TAG, "Received intent");
        Log.d(TAG, intent.getAction());
        if (intent.getAction().equals(CLICK_UPDATE)) {
            Log.d(TAG, "Update intent");
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, MenuProvider.class));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.dish_list);
        }
    }
}
