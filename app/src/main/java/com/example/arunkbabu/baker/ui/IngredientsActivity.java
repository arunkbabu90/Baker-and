package com.example.arunkbabu.baker.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.arunkbabu.baker.R;
import com.example.arunkbabu.baker.data.Ingredient;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class IngredientsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        ButterKnife.bind(this);

        ArrayList<Ingredient> ingredients = getIntent().getParcelableArrayListExtra(RecipeMasterListFragment.INGREDIENTS_LIST_KEY);

        IngredientsFragment ingredientsFragment = new IngredientsFragment();
        ingredientsFragment.setIngredients(ingredients);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.ingredients_frag_container, ingredientsFragment)
                .commit();
    }
}
