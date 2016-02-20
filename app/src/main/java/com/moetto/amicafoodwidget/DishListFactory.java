package com.moetto.amicafoodwidget;

import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import org.json.JSONException;
import java.util.ArrayList;

/**
 * Created by moetto on 03/02/16.
 */
public class DishListFactory implements RemoteViewsService.RemoteViewsFactory {
    ArrayList<RemoteViews> dishes;
    ArrayList<String> dishList;
    private static final String TAG = "AmicaFood:DishListFact";

    public DishListFactory() {
        dishList = new ArrayList<>();
        dishList.add("Updating");
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "Creating DishListFactory");
        Log.d(TAG, dishList.toString());
        updateRemoteViews();
    }

    private void updateRemoteViews() {
        dishes = new ArrayList<>();
        for (int i = 0; i < dishList.size(); i++) {
            String dish = dishList.get(i);
            if (dish.trim().length() > 0) {
                RemoteViews remoteViews = new RemoteViews("com.moetto.amicafoodwidget", R.layout.dish);
                remoteViews.setTextViewText(R.id.single_dish, dish);
                int color;
                if (i % 2 == 0) {
                    color = Color.DKGRAY;
                } else {
                    color = Color.GRAY;
                }
                remoteViews.setInt(R.id.single_dish, "setBackgroundColor", color);
                dishes.add(remoteViews);
            }
        }
        RemoteViews remoteViews = new RemoteViews("com.moetto.amicafoodwidget", R.layout.dish);
        dishes.add(remoteViews);
    }

    @Override
    public void onDataSetChanged() {
        Log.d(TAG, "Update requested");
        try {
            dishList = new JSONToMenu(URLDownloader.downloadURL()).getDishList(0);
            updateRemoteViews();
        } catch (JSONException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return dishes.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        return dishes.get(position);
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}
