package com.moetto.amicafoodwidget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by moetto on 03/02/16.
 */
public class DishListService extends RemoteViewsService {
    private final static String TAG = "AmicaFood:DishListServ";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(TAG, "Creating new factory");

        return new DishListFactory(this);
    }
}
