package com.example.arunkbabu.baker.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.arunkbabu.baker.R;
import com.example.arunkbabu.baker.data.Ingredient;
import com.example.arunkbabu.baker.data.Step;
import com.example.arunkbabu.baker.utils.BakerUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragment containing all the Recipe Option's short Description
 * A simple {@link Fragment} subclass.
 */
public class RecipeMasterListFragment extends Fragment implements StepsAdapter.ItemClickListener
{
    @BindView(R.id.tv_ingredients) TextView ingredientsTextView;
    @BindView(R.id.rv_recipe_options) RecyclerView mRecyclerView;
    @BindView(R.id.iv_frag_recipe_image) ImageView mRecipeImageView;
    @BindView(R.id.tv_frag_servings) TextView mServingsTextView;
    @BindView(R.id.tv_frag_recipe_name) TextView mRecipeNameTextView;

    private static final String INGREDIENTS_STATE = "state_ingredients";
    private static final String STEP_STATE = "step_state";
    private static final String ID_STATE = "state_id";
    private static final String SERVINGS_STATE = "state_servings";
    private static final String NAME_STATE = "state_name";

    public static final String INGREDIENTS_LIST_KEY = "ingredient_list_key_rmlf";
    public static final String STEP_DETAIL_KEY = "step_detail_key_rmlf";
    public static final String STEP_DETAIL_POSITION_KEY = "step_pos_key";

    private int mId;
    private int mServings;
    private String mName;
    private ArrayList<Ingredient> mIngredients;
    private ArrayList<Step> mSteps;
    private StepsAdapter mStepsAdapter;
    private Context mContext;


    public RecipeMasterListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mId = savedInstanceState.getInt(ID_STATE);
            mServings = savedInstanceState.getInt(SERVINGS_STATE);
            mName = savedInstanceState.getString(NAME_STATE);
            mSteps = savedInstanceState.getParcelableArrayList(STEP_STATE);
            mIngredients = savedInstanceState.getParcelableArrayList(INGREDIENTS_STATE);
        }

        View view = inflater.inflate(R.layout.fragment_recipe_options, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mStepsAdapter = new StepsAdapter();
        mStepsAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mStepsAdapter);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mStepsAdapter.setStepsList(mSteps);

        String formattedServing = "Servings: " + String.valueOf(mServings);

        Glide.with(mContext).load(BakerUtils.supplyCorrectImage(mName)).into(mRecipeImageView);
        mServingsTextView.setText(formattedServing);
        mRecipeNameTextView.setText(mName);

        ingredientsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIngredients != null) {
                    if (BakerUtils.isTablet(mContext)) {
                        IngredientsFragment ingredientsFragment = new IngredientsFragment();
                        ingredientsFragment.setIngredients(mIngredients);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_detail_container, ingredientsFragment)
                                .commit();
                    } else {
                        Intent i = new Intent(mContext, IngredientsActivity.class);
                        i.putParcelableArrayListExtra(INGREDIENTS_LIST_KEY, mIngredients);
                        startActivity(i);
                    }
                }
            }
        });


    }

    public void setRecipeData(ArrayList<Ingredient> ingredientList,
                              ArrayList<Step> stepList, String recipeName, int recipeId, int servings) {
        mId = recipeId;
        mName = recipeName;
        mServings = servings;
        mIngredients = ingredientList;
        mSteps = stepList;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (mIngredients != null && mSteps != null  && mName != null) {
            outState.putParcelableArrayList(INGREDIENTS_STATE, mIngredients);
            outState.putParcelableArrayList(STEP_STATE, mSteps);
            outState.putInt(ID_STATE, mId);
            outState.putInt(SERVINGS_STATE, mServings);
            outState.putString(NAME_STATE, mName);
        }
    }


    @Override
    public void onStepItemClick(View view, ArrayList<Step> stepList, int position) {
        if (BakerUtils.isTablet(mContext)) {
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setStepList(getContext(), stepList, position);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_detail_container, stepDetailFragment)
                    .commit();
        } else {
            Intent stepActivityIntent = new Intent(mContext, StepsActivity.class);
            stepActivityIntent.putParcelableArrayListExtra(STEP_DETAIL_KEY, stepList);
            stepActivityIntent.putExtra(STEP_DETAIL_POSITION_KEY, position);
            startActivity(stepActivityIntent);
       }

    }
}
