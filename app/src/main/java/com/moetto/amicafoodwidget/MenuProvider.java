package com.moetto.amicafoodwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;
import android.widget.TextView;

/**
 * Created by moetto on 02/02/16.
 */
public class MenuProvider extends AppWidgetProvider {
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        new RemoteViews(context.getPackageName(), R.layout.menu_layout).setOnClickPendingIntent(R.id.menu_text);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.menu_layout);
        views.setTextViewText(R.id.menu_text, "Hello world again!");
    }
    public void updateMenu (){

    }
}
