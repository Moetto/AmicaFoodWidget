package com.moetto.amicafoodwidget;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;

/**
 * Created by moetto on 21/02/16.
 */
public class PreferenceManager {
    private static final String PREFERENCES = "preferences",
            PREFERENCES_TIME = "time";

    protected static Calendar getCalendar(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(preferences.getLong(PREFERENCES_TIME, calendar.getTimeInMillis()));
        return calendar;
    }

    protected static void setCalendar(Context context, Calendar calendar) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(PREFERENCES_TIME, calendar.getTimeInMillis());
        editor.apply();
    }
}
