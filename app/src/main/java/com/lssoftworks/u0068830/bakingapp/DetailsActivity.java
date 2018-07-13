package com.lssoftworks.u0068830.bakingapp;

import android.support.v4.app.FragmentManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.lssoftworks.u0068830.bakingapp.Data.Recipe;

import static com.lssoftworks.u0068830.bakingapp.MainActivity.EXTRA_RECIPE_ID;
import static com.lssoftworks.u0068830.bakingapp.MainFragment.mRecipes;

public class DetailsActivity extends AppCompatActivity {

    private static final String RECIPE_ID = "recipe_id";
    private static Recipe mRecipe;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if(savedInstanceState == null) {
            Intent intent = getIntent();

            id = intent.getIntExtra(EXTRA_RECIPE_ID, 1);
        } else {
            id = savedInstanceState.getInt(RECIPE_ID);
        }

        mRecipe = mRecipes.get(id-1);

        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setRecipe(mRecipe);
        detailsFragment.setContext(this);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.details_container, detailsFragment)
                .commit();
    }

    public void RecipeToWidget(View v) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidget.class));
        //Now update all widgets
        RecipeWidget.updateRecipeWidgets(this, appWidgetManager, mRecipe, appWidgetIds);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(RECIPE_ID, id);

        super.onSaveInstanceState(outState);
    }

}
