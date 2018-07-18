package com.lssoftworks.u0068830.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lssoftworks.u0068830.bakingapp.Data.Recipe;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lssoftworks.u0068830.bakingapp.Data.RecipeJsonParser.getRecipes;
import static com.lssoftworks.u0068830.bakingapp.MainActivity.mDetailsFragment;
import static com.lssoftworks.u0068830.bakingapp.MainActivity.mRecipeId;
import static com.lssoftworks.u0068830.bakingapp.MainActivity.mRecipes;

public class MainFragment extends Fragment {

    private RecipeAdapter mAdapter;

    public static MainFragment.OnViewHolderClickListener viewHolderClickListener;
    private static boolean mTwoPane;
    private Recipe mRecipe;

    public static final String EXTRA_RECIPE_ID = "recipe_id";

    @BindView(R.id.rv_baking_recipes)
    RecyclerView mBakingRecipes;

    public MainFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, rootView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mBakingRecipes.setLayoutManager(linearLayoutManager);
        mBakingRecipes.setHasFixedSize(true);

        mAdapter = new RecipeAdapter(mRecipes);
        mBakingRecipes.setAdapter(mAdapter);

        viewHolderClickListener = new OnViewHolderClickListener();

        return rootView;
    }

    public static void setTwoPane(boolean twoPane) {
        mTwoPane = twoPane;
    }

    class OnViewHolderClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            TextView tvName = v.findViewById(R.id.tv_recipe_name);
            MainActivity.mRecipeId = Integer.parseInt(tvName.getTag().toString());
            if(!mTwoPane) {
                Intent intent = new Intent(getContext(), DetailsActivity.class);
                intent.putExtra(EXTRA_RECIPE_ID, mRecipeId);
                startActivity(intent);
            } else {
                mRecipe = mRecipes.get(MainActivity.mRecipeId);

                mDetailsFragment = new DetailsFragment();
                mDetailsFragment.setRecipe(mRecipe);
                mDetailsFragment.setContext(getContext());
                //MainActivity.mFragmentManager.beginTransaction()
                //        .replace(R.id.details_container, mDetailsFragment)
                //        .commit();
            }
        }
    }
}
