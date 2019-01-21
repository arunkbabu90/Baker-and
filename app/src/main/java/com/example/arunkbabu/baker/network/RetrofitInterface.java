package com.example.arunkbabu.baker.network;

import com.example.arunkbabu.baker.data.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface RetrofitInterface
{
    @Headers("Content-Type: application/json")
    @GET("baking.json")
    Call<ArrayList<Recipe>> getAllRecipes();
}
