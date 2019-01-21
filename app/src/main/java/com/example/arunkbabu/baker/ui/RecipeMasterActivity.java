package com.example.arunkbabu.baker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.arunkbabu.baker.R;
import com.example.arunkbabu.baker.data.Ingredient;
import com.example.arunkbabu.baker.data.Step;
import com.example.arunkbabu.baker.widget.UpdateListService;

import java.util.ArrayList;

import static com.example.arunkbabu.baker.ui.MainActivity.WIDGET_INGREDIENT_LIST;

public class RecipeMasterActivity extends AppCompatActivity
{
    private int mId;
    private int mServings;
    public String mName;
    private ArrayList<Ingredient> mIngredients;
    private ArrayList<Step> mSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_recipe);

        mIngredients = getIntent().getParcelableArrayListExtra(MainActivity.INGREDIENTS_ARRAYLIST_KEY);
        mSteps = getIntent().getParcelableArrayListExtra(MainActivity.STEPS_ARRAYLIST_KEY);

        mId = getIntent().getIntExtra(MainActivity.RECIPE_ID_KEY, -1);
        mServings = getIntent().getIntExtra(MainActivity.RECIPE_SERVING_KEY, -1);
        mName = getIntent().getStringExtra(MainActivity.RECIPE_NAME_KEY);

        // Intent to update the widget with the ingredient list
        Intent widgetUpdateIntent = new Intent(this, UpdateListService.class);
        widgetUpdateIntent.setAction(UpdateListService.ACTION_UPDATE_WIDGET);
        widgetUpdateIntent.putExtra(WIDGET_INGREDIENT_LIST, mIngredients);
        startService(widgetUpdateIntent);


        if (savedInstanceState == null) {
            RecipeMasterListFragment fragment = new RecipeMasterListFragment();
            fragment.setRecipeData(mIngredients, mSteps, mName, mId, mServings);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_master_options_container, fragment)
                    .commit();

            // If the device is tablet, the detail view is visible so also inflate contents into it
            // Here we inflate the contents of IngredientsFragment which displays Ingredients
            //  which is also the first element in our list
            if (findViewById(R.id.activity_sw600_master_recipe) != null) {
                IngredientsFragment ingrFragment = new IngredientsFragment();
                ingrFragment.setIngredients(mIngredients);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_detail_container, ingrFragment)
                        .commit();
            }
        }
    }
}
