package com.lssoftworks.u0068830.bakingapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lssoftworks.u0068830.bakingapp.Data.Recipe;
import com.lssoftworks.u0068830.bakingapp.Network.NetworkUtils;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lssoftworks.u0068830.bakingapp.Data.RecipeJsonParser.getRecipes;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_baking_recipes)
    RecyclerView mBakingRecipes;

    private RecipeAdapter mAdapter;
    public static ArrayList<Recipe> mRecipes;
    private String mRecipeJson;
    public static OnViewHolderClickListener viewHolderClickListener;
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String EXTRA_RECIPES = "all_recipes";
    public static final String EXTRA_RECIPE_ID = "recipe_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        InputStream is = getResources().openRawResource(R.raw.baking);
        String bakingData = iStreamToString(is);

        try {
            mRecipes = new ArrayList<>(Arrays.asList(getRecipes(bakingData)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mBakingRecipes.setLayoutManager(linearLayoutManager);
        mBakingRecipes.setHasFixedSize(true);

        mAdapter = new RecipeAdapter(mRecipes);
        mBakingRecipes.setAdapter(mAdapter);

        viewHolderClickListener = new OnViewHolderClickListener();
    }

    public String iStreamToString(InputStream is)
    {
        BufferedReader rd = new BufferedReader(new InputStreamReader(is), 4096);
        String line;
        StringBuilder sb =  new StringBuilder();
        try {
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        String contentOfMyInputStream = sb.toString();
        return contentOfMyInputStream;
    }

    class OnViewHolderClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
            TextView tvName = v.findViewById(R.id.tv_recipe_name);
            int id = Integer.parseInt(tvName.getTag().toString());
            intent.putExtra(EXTRA_RECIPE_ID, id);

            startActivity(intent);
        }
    }


    public class FetchRecipeTask extends AsyncTask<Void, Void, String> {

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
