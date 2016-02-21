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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by moetto on 02/02/16.
 */
public class MenuProvider extends AppWidgetProvider {
    private static final String CLICK_UPDATE = "click_update",
            CLICK_PREVIOUS = "previous",
            CLICK_NEXT = "next";
    private static final int requestCode = 1;
    private static final String TAG = "AmicaFood:MenuProvider";

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.menu_layout);

        Intent update = new Intent(context, MenuProvider.class);
        update.setAction(CLICK_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, update, 0);
        remoteViews.setOnClickPendingIntent(R.id.update, pendingIntent);

        Intent nextDate = new Intent(context, MenuProvider.class);
        nextDate.setAction(CLICK_NEXT);
        pendingIntent = PendingIntent.getBroadcast(context, requestCode, nextDate, 0);
        remoteViews.setOnClickPendingIntent(R.id.arrow_right, pendingIntent);

        Intent previousDate = new Intent(context, MenuProvider.class);
        previousDate.setAction(CLICK_PREVIOUS);
        pendingIntent = PendingIntent.getBroadcast(context, requestCode, previousDate, 0);
        remoteViews.setOnClickPendingIntent(R.id.arrow_left, pendingIntent);

        remoteViews.setRemoteAdapter(R.id.dish_list, new Intent(context, DishListService.class));
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

        remoteViews.setTextViewText(R.id.date_text, new SimpleDateFormat("E dd.MM.", Locale.getDefault()).format(new Date()));
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(TAG, "Received intent");
        Log.d(TAG, intent.getAction());
        switch (intent.getAction()) {
            case CLICK_UPDATE:
                Log.d(TAG, "Update intent");
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                        new ComponentName(context, MenuProvider.class));
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.dish_list);
                break;

            case CLICK_PREVIOUS:
                Log.d(TAG, "Previous date");
                break;

            case CLICK_NEXT:
                Log.d(TAG, "Next date");
                break;
        }
    }
}
