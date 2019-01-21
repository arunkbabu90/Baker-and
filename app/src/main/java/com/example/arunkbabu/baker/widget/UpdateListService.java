package com.example.arunkbabu.baker.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.arunkbabu.baker.data.Ingredient;
import com.example.arunkbabu.baker.ui.MainActivity;

import java.util.ArrayList;

public class UpdateListService extends IntentService
{
    public static final String ACTION_UPDATE_WIDGET = "android.appwidget.action.APPWIDGET_UPDATE";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public UpdateListService() {
        super("APPWIDGET_UPDATE");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ArrayList<Ingredient> ingredientList;
        if (intent != null && intent.getAction().equals(ACTION_UPDATE_WIDGET)) {
            ingredientList = intent.getParcelableArrayListExtra(MainActivity.WIDGET_INGREDIENT_LIST);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakerWidgetProvider.class));
            BakerWidgetProvider.updateAppWidget(this, appWidgetManager, appWidgetIds, ingredientList);
        }
    }
}
