package com.lssoftworks.u0068830.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lssoftworks.u0068830.bakingapp.Data.Recipe;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    ArrayList<Recipe> mRecipes;

    public RecipeAdapter(ArrayList<Recipe> recipes) {
        mRecipes = recipes;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recipe_item, parent, false);

        RecipeViewHolder recipeViewHolder = new RecipeViewHolder(view);

        return recipeViewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        holder.mRecipeName.setText(mRecipes.get(position).getName());
        holder.mRecipeServings.setText(String.valueOf(mRecipes.get(position).getServings()));
        holder.mRecipePicture.setVisibility(View.GONE);
        holder.mRecipeName.setTag(mRecipes.get(position).getId());
    }

    @Override
    public int getItemCount() {
        if(mRecipes != null) {
            return mRecipes.size();
        }
        return 0;
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_recipe_picture)
        ImageView mRecipePicture;

        @BindView(R.id.tv_recipe_name)
        TextView mRecipeName;

        @BindView(R.id.tv_recipe_servings)
        TextView mRecipeServings;

        public RecipeViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(MainActivity.viewHolderClickListener);
        }
    }

    public void setRecipeData(ArrayList<Recipe> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }
}
