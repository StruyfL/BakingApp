package com.lssoftworks.u0068830.bakingapp;

import android.support.v4.app.FragmentManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lssoftworks.u0068830.bakingapp.Data.Recipe;

import static com.lssoftworks.u0068830.bakingapp.MainActivity.EXTRA_RECIPE_ID;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = DetailsActivity.class.getSimpleName();
    private static final String STEP_ID = "step_id";
    private static final String RECIPE_ID = "recipe_id";
    private static Recipe mRecipe;
    private int recipeId;
    private int mStepId;
    private static boolean mTwoPane;
    public static OnStepViewHolderClickListener stepViewHolderClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if(savedInstanceState == null) {
            Intent intent = getIntent();

            recipeId = intent.getIntExtra(EXTRA_RECIPE_ID, 0);
            mStepId = 0;
        } else {
            mStepId = savedInstanceState.getInt(STEP_ID);
            recipeId = savedInstanceState.getInt(RECIPE_ID);
        }
        mRecipe = MainActivity.mRecipes.get(recipeId);

        if(findViewById(R.id.ll_recipe_two_pane) != null) {
            mTwoPane = true;

            if(savedInstanceState == null) {
                FragmentManager fragmentManager = getSupportFragmentManager();

                DetailsStepFragment detailsStepFragment = new DetailsStepFragment();
                DetailsStepsFragment detailsStepsFragment = new DetailsStepsFragment();


                detailsStepsFragment.setRecipe(mRecipe);
                detailsStepsFragment.setContext(this);
                detailsStepsFragment.setRecipeId(recipeId);

                detailsStepFragment.setStep(mRecipe.getSteps()[mStepId]);
                detailsStepFragment.setContext(this);

                fragmentManager.beginTransaction()
                        .add(R.id.main_recipe_fragment, detailsStepsFragment)
                        .add(R.id.step_fragment, detailsStepFragment)
                        .commit();

                stepViewHolderClickListener = new OnStepViewHolderClickListener();
            }

        } else {
            mTwoPane = false;

            if(savedInstanceState == null) {
                FragmentManager fragmentManager = getSupportFragmentManager();

                DetailsFragment detailsFragment = new DetailsFragment();

                detailsFragment.setRecipe(mRecipe);
                detailsFragment.setContext(this);
                detailsFragment.setRecipeId(recipeId);

                fragmentManager.beginTransaction()
                        .add(R.id.details_container, detailsFragment)
                        .commit();
            }
        }
    }

    class OnStepViewHolderClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            TextView tvShortDesc = v.findViewById(R.id.tv_short_desc);
            mStepId = Integer.parseInt(tvShortDesc.getTag().toString());

            DetailsStepFragment detailsStepFragment = new DetailsStepFragment();

            detailsStepFragment.setContext(DetailsActivity.this);
            detailsStepFragment.setStep(mRecipe.getSteps()[mStepId]);

            Log.d(TAG, "ID: " + mStepId);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.step_fragment, detailsStepFragment)
                    .commit();
        }
    }

    public void RecipeToWidget(View v) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidget.class));
        //Now update all widgets
        RecipeWidget.updateRecipeWidgets(this, appWidgetManager, mRecipe, appWidgetIds);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STEP_ID, mStepId);
        outState.putInt(RECIPE_ID, recipeId);

        super.onSaveInstanceState(outState);
    }

}
