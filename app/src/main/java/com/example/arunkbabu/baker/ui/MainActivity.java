package com.example.arunkbabu.baker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.arunkbabu.baker.IdlingResources.BakerIdlingResource;
import com.example.arunkbabu.baker.R;
import com.example.arunkbabu.baker.data.Ingredient;
import com.example.arunkbabu.baker.data.Recipe;
import com.example.arunkbabu.baker.data.Step;
import com.example.arunkbabu.baker.network.RetrofitInterface;
import com.example.arunkbabu.baker.utils.BakerUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements MainRecipeAdapter.ItemClickListener
{
    @BindView(R.id.rv_main) RecyclerView mRecyclerView;
    private MainRecipeAdapter mMainRecipeAdapter;
    private final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    public static final String RECIPE_NAME_KEY = "recipe_name_key";
    public static final String RECIPE_ID_KEY = "recipe_id_key";
    public static final String RECIPE_SERVING_KEY = "recipe_serv_key";
    public static final String INGREDIENTS_ARRAYLIST_KEY = "ingr_array_list_key";
    public static final String STEPS_ARRAYLIST_KEY = "steps_array_list_key";
    public static final String WIDGET_INGREDIENT_LIST = "widget_ingredient_list_array_key";

    private final long TEST_DELAY = 7000;

    private static final String RECIPE_SAV = "recipe_sav";

    private ArrayList<Recipe> mRecipeList;
    @Nullable private BakerIdlingResource mIdlingResource;

    /**
     * Creates a new IdlingResource object and is only for tests
     * @return Returns the IdlingResource
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new BakerIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // Instantiate the Idling Resource
        getIdlingResource();

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }

        mRecyclerView.setHasFixedSize(true);
        // Check whether the device is a Tablet and update the UI accordingly
        if (BakerUtils.isTablet(this)) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                    false));
        }
        mMainRecipeAdapter = new MainRecipeAdapter();
        mMainRecipeAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mMainRecipeAdapter);

        if (savedInstanceState != null) {
            mRecipeList = savedInstanceState.getParcelableArrayList(RECIPE_SAV);
            mMainRecipeAdapter.setRecipeList(this, mRecipeList);
            return;
        }

        // Get the Recipe JSON from Internet
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<ArrayList<Recipe>> call = retrofitInterface.getAllRecipes();
        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Recipe>> call, @NonNull Response<ArrayList<Recipe>> response) {
                if (!response.isSuccessful() || Objects.requireNonNull(response.body()).isEmpty()) {
                    Toast.makeText(MainActivity.this, R.string.err_no_internet, Toast.LENGTH_LONG).show();
                    return;
                }
                mRecipeList = response.body();
                mMainRecipeAdapter.setRecipeList(MainActivity.this, response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Recipe>> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, R.string.err_no_internet, Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mRecipeList != null) {
            outState.putParcelableArrayList(RECIPE_SAV, mRecipeList);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemClick(View view, List<Recipe> recipes, int position) {

        String recipeName = recipes.get(position).getRecipeName();
        int recipeId = recipes.get(position).getId();
        int servings = recipes.get(position).getServings();

        int ingredientSize = recipes.get(position).getIngredients().size();
        int stepSize = recipes.get(position).getSteps().size();

        ArrayList<Ingredient> ingredientsArrayList = new ArrayList<>();
        for (int i = 0; i < ingredientSize; i++) {
            ingredientsArrayList.add(recipes.get(position).getIngredients().get(i));
        }

        ArrayList<Step> stepArrayList = new ArrayList<>();
        for (int i = 0; i < stepSize; i++) {
            stepArrayList.add(recipes.get(position).getSteps().get(i));
        }


        // Intent to launch Recipe Activity
        Intent i = new Intent(MainActivity.this, RecipeMasterActivity.class);
        i.putExtra(RECIPE_NAME_KEY, recipeName);
        i.putExtra(RECIPE_ID_KEY, recipeId);
        i.putExtra(RECIPE_SERVING_KEY, servings);
        i.putParcelableArrayListExtra(STEPS_ARRAYLIST_KEY, stepArrayList);
        i.putParcelableArrayListExtra(INGREDIENTS_ARRAYLIST_KEY, ingredientsArrayList);
        startActivity(i);
    }


    @Override
    protected void onStart() {
        super.onStart();

        delayTest(TEST_DELAY);
    }



    /**
     * Delays the test for the given amount of time in milliseconds
     * @param delayInMillis The time delay in milliseconds
     */
    private void delayTest(long delayInMillis) {
        if (mIdlingResource != null) {
            Handler delayHandler = new Handler();
            delayHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mIdlingResource.setIdleState(true);
                }
            }, delayInMillis);
        }
    }
}
