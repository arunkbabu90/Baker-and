package com.example.arunkbabu.baker.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.arunkbabu.baker.R;
import com.example.arunkbabu.baker.data.Ingredient;
import com.example.arunkbabu.baker.ui.MainActivity;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class BakerWidgetProvider extends AppWidgetProvider {

    private static ArrayList<Ingredient> mIngredientList;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int[] appWidgetIds, ArrayList<Ingredient> ingredientList) {

        mIngredientList = ingredientList;

        for (int appWidgetId : appWidgetIds) {

            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baker_widget_provider);

            // Launch the MainActivity when the user presses on the Widget
            Intent listUpdateIntent = new Intent(context, ListWidgetService.class);
            Intent launchMainActivityIntent = new Intent(context, MainActivity.class);
            views.setRemoteAdapter(R.id.lv_widget_ingredients, listUpdateIntent);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchMainActivityIntent, 0);
            views.setOnClickPendingIntent(R.id.widget_ingredients_tv, pendingIntent);

            ComponentName componentName = new ComponentName(context, BakerWidgetProvider.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_widget_ingredients);
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(componentName, views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    /**
     * Returns the Ingredient list available in the widget provider
     * @return ArrayList of Ingredients
     */
    public static ArrayList<Ingredient> getIngredientList() {
        return mIngredientList;
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

