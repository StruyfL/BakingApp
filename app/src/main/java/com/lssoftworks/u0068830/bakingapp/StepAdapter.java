package com.lssoftworks.u0068830.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.lssoftworks.u0068830.bakingapp.Data.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private static final String TAG = StepAdapter.class.getSimpleName();
    private ArrayList<Recipe.Step> mStepsList;
    private SimpleExoPlayer mExoPlayer;
    private ArrayList<MediaSource> mMediaSource;
    private long mCurPos;
    private int mCurWindow;


    StepAdapter(ArrayList<Recipe.Step> steps, SimpleExoPlayer exoPlayer, ArrayList<MediaSource> mediaSource, int curWindow, long curPos) {
        mStepsList = steps;
        if(exoPlayer != null && mediaSource != null) {
            mExoPlayer = exoPlayer;
            mMediaSource = mediaSource;
            mCurWindow = curWindow;
            mCurPos = curPos;
            Log.d(TAG, "Binding pos and window: " + mCurPos + " " + mCurWindow);

        }
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
        holder.mShortDesc.setText(mStepsList.get(position).getShortDescription());
        holder.mShortDesc.setTag(mStepsList.get(position).getStepId());
        String videoUrl = mStepsList.get(position).getVideoURL();

        if(mExoPlayer != null && mMediaSource != null) {
            holder.mStepId.setText(String.valueOf(mStepsList.get(position).getStepId()));
            holder.mStepId.setVisibility(View.VISIBLE);
            holder.mDesc.setText(mStepsList.get(position).getDescription());
            holder.mDesc.setVisibility(View.VISIBLE);
            if (!videoUrl.equals("")) {
                holder.mExoPlayerView.setVisibility(View.VISIBLE);
                holder.mExoPlayerView.setPlayer(mExoPlayer);
                mExoPlayer.seekTo(mCurWindow, mCurPos);
                mExoPlayer.prepare(mMediaSource.get(position), false, false);
                mExoPlayer.setPlayWhenReady(false);
            } else {
                holder.mExoPlayerView.setVisibility(View.GONE);
            }
        } else {
            holder.mStepId.setVisibility(View.GONE);
            holder.mDesc.setVisibility(View.GONE);
            holder.mExoPlayerView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if(mStepsList != null) {
            return mStepsList.size();

        }
        return 0;
    }

    class StepViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ep_instructionvideo)
        PlayerView mExoPlayerView;

        @BindView(R.id.tv_stepid)
        TextView mStepId;

        @BindView(R.id.tv_short_desc)
        TextView mShortDesc;

        @BindView(R.id.tv_desc)
        TextView mDesc;

        StepViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(DetailsActivity.stepViewHolderClickListener);
        }
    }

    public void setStepData(ArrayList<Recipe.Step> steps) {
        mStepsList = steps;
        notifyDataSetChanged();
    }
}
