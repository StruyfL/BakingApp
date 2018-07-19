package com.lssoftworks.u0068830.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lssoftworks.u0068830.bakingapp.Data.Recipe;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsStepsFragment extends Fragment {

    private static final String TAG = DetailsStepsFragment.class.getSimpleName();
    private static final String STEPLIST = "step_list";
    private static final String RECIPE_ID = "recipe_id";

    private static Recipe mRecipe;
    private int mRecipeId;
    private String[] mQuantity;
    private String[] mMeasure;
    private String[] mIngredient;
    private StepAdapter mAdapter;
    private static ArrayList<Recipe.Step> mSteps;
    private Context mContext;
    private Parcelable layoutManagerSavedState;

    @BindView(R.id.ll_ingredients)
    LinearLayout mIngredientList;

    @BindView(R.id.rv_step)
    RecyclerView mStepList;

    public DetailsStepsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        //Log.d(TAG, "Starting onCreateView");

        final View rootView = inflater.inflate(R.layout.fragment_details_steps, container, false);

        ButterKnife.bind(DetailsStepsFragment.this, rootView);

        if(savedInstanceState != null) {
            mRecipeId = savedInstanceState.getInt(RECIPE_ID);
            mRecipe = MainActivity.mRecipes.get(mRecipeId);
            mContext = getContext();
        }


        if (mRecipe != null && mContext != null) {
            //Log.d(TAG, "Recipe and Context are set");

            mQuantity = mRecipe.getQuantity();
            mMeasure = mRecipe.getMeasure();
            mIngredient = mRecipe.getIngredient();
            mSteps = new ArrayList<>(Arrays.asList(mRecipe.getSteps()));

            for (int i = 0; i < mQuantity.length; i++) {
                LinearLayout ingredientLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.ingredient, mIngredientList, false);
                TextView tvQuantity = (TextView) ingredientLayout.getChildAt(0);
                TextView tvMeasure = (TextView) ingredientLayout.getChildAt(1);
                TextView tvIngredient = (TextView) ingredientLayout.getChildAt(2);

                tvQuantity.setText(mQuantity[i]);
                tvMeasure.setText(mMeasure[i]);
                tvIngredient.setText(mIngredient[i]);

                mIngredientList.addView(ingredientLayout);
            }

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            mStepList.setLayoutManager(linearLayoutManager);
            mStepList.setHasFixedSize(true);

            if(savedInstanceState != null) {
                mStepList.getLayoutManager().onRestoreInstanceState(layoutManagerSavedState);
            }

            mAdapter = new StepAdapter(mSteps, null, null, 0, 0);
            mStepList.setAdapter(mAdapter);
            mAdapter.setStepData(mSteps);

        }

        return rootView;

    }

    public void setRecipe(Recipe recipe) {
        mRecipe = recipe;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public void setRecipeId(int id) {
        mRecipeId = id;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(STEPLIST, mStepList.getLayoutManager().onSaveInstanceState());
        outState.putInt(RECIPE_ID, mRecipeId);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null) {
            layoutManagerSavedState = savedInstanceState.getParcelable(STEPLIST);
        }
    }
}
