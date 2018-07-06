package com.lssoftworks.u0068830.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lssoftworks.u0068830.bakingapp.Data.Recipe;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lssoftworks.u0068830.bakingapp.MainActivity.EXTRA_RECIPES;
import static com.lssoftworks.u0068830.bakingapp.MainActivity.EXTRA_RECIPE_ID;
import static com.lssoftworks.u0068830.bakingapp.MainActivity.mRecipes;

public class DetailsActivity extends AppCompatActivity {

    private static Recipe mRecipe;
    private String[] mQuantity;
    private String[] mMeasure;
    private String[] mIngredient;
    private StepAdapter mAdapter;
    private static ArrayList<Recipe.Step> mSteps;

    @BindView(R.id.tv_recipe_name)
    TextView mRecipeName;

    @BindView(R.id.ll_ingredients)
    LinearLayout mIngredientList;

    @BindView(R.id.rv_steps)
    RecyclerView mStepList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(DetailsActivity.this);

        Intent intent = getIntent();

        int id = intent.getIntExtra(EXTRA_RECIPE_ID, 1);

        mRecipe = mRecipes.get(id-1);

        mRecipeName.setText(mRecipe.getName());
        mQuantity = mRecipe.getQuantity();
        mMeasure = mRecipe.getMeasure();
        mIngredient = mRecipe.getIngredient();
        mSteps = new ArrayList<>(Arrays.asList(mRecipe.getSteps()));

        for(int i = 0; i < mQuantity.length; i++) {
            LinearLayout ingredientLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.ingredient, mIngredientList, false);
            TextView tvQuantity = (TextView) ingredientLayout.getChildAt(0);
            TextView tvMeasure = (TextView) ingredientLayout.getChildAt(1);
            TextView tvIngredient = (TextView) ingredientLayout.getChildAt(2);

            tvQuantity.setText(mQuantity[i]);
            tvMeasure.setText(mMeasure[i]);
            tvIngredient.setText(mIngredient[i]);

            mIngredientList.addView(ingredientLayout);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mStepList.setLayoutManager(linearLayoutManager);
        mStepList.setHasFixedSize(true);

        mAdapter = new StepAdapter(this, mSteps);
        mStepList.setAdapter(mAdapter);

    }
}
