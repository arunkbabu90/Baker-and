package com.example.arunkbabu.baker.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.arunkbabu.baker.R;
import com.example.arunkbabu.baker.data.Ingredient;
import com.example.arunkbabu.baker.utils.BakerUtils;

import java.util.ArrayList;

public class ListWidgetService extends RemoteViewsService
{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory
{
    private Context mContext;
    private ArrayList<Ingredient> mIngredients;

    public ListRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() { }

    @Override
    public void onDataSetChanged() {
        mIngredients = BakerWidgetProvider.getIngredientList();
    }

    @Override
    public void onDestroy() { }

    @Override
    public int getCount() {
        if (mIngredients == null) return 0;
        return mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.item_widget_ingredient);

        String ingredientName = mIngredients.get(position).getIngredient();
        String quantity = String.valueOf(mIngredients.get(position).getQuantity());
        String measure = BakerUtils.capitalizeFirstLetter(mIngredients.get(position).getMeasure());

        String formattedIngredient = ingredientName + "     " + quantity + " " + measure;
        views.setTextViewText(R.id.widget_ingredients_tv, formattedIngredient);

        return views;
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
        return true;
    }
}
