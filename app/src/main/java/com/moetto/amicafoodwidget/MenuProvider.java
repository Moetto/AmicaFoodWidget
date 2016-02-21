package com.moetto.amicafoodwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * Created by moetto on 02/02/16.
 */
public class MenuProvider extends AppWidgetProvider {
    private static final String CLICK_UPDATE = "click_update",
            CLICK_PREVIOUS = "previous",
            CLICK_NEXT = "next";
    private static final int update_code = 1, left_code = 2, right_code = 3;
    private static final String TAG = "AmicaFood:MenuProvider";

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        PreferenceManager.setCalendar(context, Calendar.getInstance());
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.menu_layout);

        Intent update = new Intent(context, MenuProvider.class);
        update.setAction(CLICK_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, update_code, update, 0);
        remoteViews.setOnClickPendingIntent(R.id.update, pendingIntent);

        Intent nextDate = new Intent(context, MenuProvider.class);
        nextDate.setAction(CLICK_NEXT);
        pendingIntent = PendingIntent.getBroadcast(context, right_code, nextDate, 0);
        remoteViews.setOnClickPendingIntent(R.id.arrow_right, pendingIntent);

        Intent previousDate = new Intent(context, MenuProvider.class);
        previousDate.setAction(CLICK_PREVIOUS);
        pendingIntent = PendingIntent.getBroadcast(context, left_code, previousDate, 0);
        remoteViews.setOnClickPendingIntent(R.id.arrow_left, pendingIntent);

        remoteViews.setRemoteAdapter(R.id.dish_list, new Intent(context, DishListService.class));
        updateDate(remoteViews, R.id.date_text, PreferenceManager.getCalendar(context));
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(TAG, "Received intent");
        Log.d(TAG, intent.getAction());

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                new ComponentName(context, MenuProvider.class));
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.menu_layout);
        switch (intent.getAction()) {
            case CLICK_UPDATE:
                Log.d(TAG, "Update intent");
                remoteViews.setImageViewResource(R.id.update, R.drawable.refresh_active);
                appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.dish_list);
                break;

            case CLICK_PREVIOUS: {
                Log.d(TAG, "Previous date");
                Calendar calendar = PreferenceManager.getCalendar(context);
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
                PreferenceManager.setCalendar(context, calendar);
                updateDate(remoteViews, R.id.date_text, calendar);
                appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
                break;
            }
            case CLICK_NEXT: {
                Log.d(TAG, "Next date");
                Calendar calendar = PreferenceManager.getCalendar(context);
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
                PreferenceManager.setCalendar(context, calendar);
                updateDate(remoteViews, R.id.date_text, calendar);
                appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
                break;
            }
        }
    }

    private void updateDate(RemoteViews remoteViews, int textViewId, Calendar calendar) {
        remoteViews.setTextViewText(textViewId, new SimpleDateFormat("E dd.MM.", Locale.getDefault()).format(calendar.getTime()));
    }
}


