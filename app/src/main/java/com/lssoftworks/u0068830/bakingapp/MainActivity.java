package com.lssoftworks.u0068830.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.lssoftworks.u0068830.bakingapp.Data.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_baking_recipes)
    RecyclerView mBakingRecipes;

    public RecipeAdapter mAdapter;
    public ArrayList<Recipe> mRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mAdapter = new RecipeAdapter(mRecipes);
        mBakingRecipes.setAdapter(mAdapter);
    }
}
