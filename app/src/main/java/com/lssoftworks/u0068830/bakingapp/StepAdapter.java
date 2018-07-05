package com.lssoftworks.u0068830.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lssoftworks.u0068830.bakingapp.Data.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    ArrayList<Recipe.Step> mStepsList;

    public StepAdapter(ArrayList<Recipe.Step> steps) {
        mStepsList = steps;
        Log.d("StepAdapter", String.valueOf(mStepsList.size()));
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.step_item, parent, false);

        StepViewHolder stepViewHolder = new StepViewHolder(view);

        return stepViewHolder;
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        holder.mStepId.setText(String.valueOf(mStepsList.get(position).getStepId()));
        holder.mShortDesc.setText(mStepsList.get(position).getShortDescription());
        holder.mDesc.setText(mStepsList.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        if(mStepsList != null) {
            return mStepsList.size();
        }

        return 0;
    }

    class StepViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_stepid)
        TextView mStepId;

        @BindView(R.id.tv_short_desc)
        TextView mShortDesc;

        @BindView(R.id.tv_desc)
        TextView mDesc;

        public StepViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
