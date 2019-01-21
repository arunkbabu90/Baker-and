package com.example.arunkbabu.baker.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arunkbabu.baker.R;
import com.example.arunkbabu.baker.data.Ingredient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsFragment extends Fragment
{
    @BindView(R.id.rv_ingredients) RecyclerView mIngredientRecyclerView;
    private ArrayList<Ingredient> mIngredients;

    private static final String SAV_INGREDIENTS = "ingr_da_sav";

    public IngredientsFragment() {
        // Required public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mIngredients = savedInstanceState.getParcelableArrayList(SAV_INGREDIENTS);
        }

        View view = inflater.inflate(R.layout.fragment_ingredients, container, false);
        ButterKnife.bind(this, view);

        mIngredientRecyclerView.setHasFixedSize(true);
        mIngredientRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        IngredientAdapter mIngredientAdapter = new IngredientAdapter();
        mIngredientAdapter.setIngredientList(mIngredients);
        mIngredientRecyclerView.setAdapter(mIngredientAdapter);

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (mIngredients != null) {
            outState.putParcelableArrayList(SAV_INGREDIENTS, mIngredients);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mIngredients = savedInstanceState.getParcelableArrayList(SAV_INGREDIENTS);
        }
        super.onViewStateRestored(savedInstanceState);
    }


    /**
     * Set the List of ingredients to the calling fragment
     * @param ingredientList The ingredient list
     */
    public void setIngredients(ArrayList<Ingredient> ingredientList) {
        mIngredients = ingredientList;
    }
}
