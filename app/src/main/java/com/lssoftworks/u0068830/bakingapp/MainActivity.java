package com.lssoftworks.u0068830.bakingapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.lssoftworks.u0068830.bakingapp.Data.Recipe;
import com.lssoftworks.u0068830.bakingapp.Network.NetworkUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;

import static com.lssoftworks.u0068830.bakingapp.MainFragment.mRecipes;

public class MainActivity extends AppCompatActivity {


    private String mRecipeJson;
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String EXTRA_RECIPE_ID = "recipe_id";
    private static final String RECIPE_ID = "recipe_id";
    private static final String DETAILS_FRAGMENT_STATE = "details_fragment_state";

    public static DetailsFragment mDetailsFragment;
    public static FragmentManager mFragmentManager;

    private boolean mTwoPane;
    private Recipe mRecipe;
    public static int mRecipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.ll_recipe_two_pane) != null) {
            mTwoPane = true;

            if(savedInstanceState == null) {
                mDetailsFragment = new DetailsFragment();
            } else {
                mRecipeId = savedInstanceState.getInt(RECIPE_ID);
                mDetailsFragment = (DetailsFragment) getSupportFragmentManager().getFragment(savedInstanceState, DETAILS_FRAGMENT_STATE);
            }

            mRecipe = mRecipes.get(mRecipeId);

            Log.d(TAG, String.valueOf(mRecipeId));

            mDetailsFragment.setRecipe(mRecipe);
            mDetailsFragment.setContext(this);

            mFragmentManager = getSupportFragmentManager();

            mFragmentManager.beginTransaction()
                    .replace(R.id.details_container, mDetailsFragment)
                    .commit();

        } else {
            mTwoPane = false;
        }

        MainFragment.setTwoPane(mTwoPane);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        getSupportFragmentManager().putFragment(outState, DETAILS_FRAGMENT_STATE, mDetailsFragment);
        outState.putInt(RECIPE_ID, mRecipeId);
    }

    class FetchRecipeTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                Socket sock = new Socket();
                sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
                sock.close();

            } catch (IOException e) {
                return null;
            }

            URL recipeRequestUrl = NetworkUtils.buildUrl();

            try {
                String jsonRecipeResponse = NetworkUtils.getResponseFromHttpUrl(recipeRequestUrl);

                return jsonRecipeResponse;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String jsonRecipeData) {
            if(jsonRecipeData != null) {
                mRecipeJson = jsonRecipeData;
                Log.d(TAG, mRecipeJson);
            }
        }
    }
}
