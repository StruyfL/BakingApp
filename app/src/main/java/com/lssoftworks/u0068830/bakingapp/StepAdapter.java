package com.lssoftworks.u0068830.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.lssoftworks.u0068830.bakingapp.Data.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private ArrayList<Recipe.Step> mStepsList;
    private SimpleExoPlayer mExoPlayer;
    private Context mContext;

    StepAdapter(Context context, ArrayList<Recipe.Step> steps, SimpleExoPlayer exoPlayer) {
        mStepsList = steps;
        mContext = context;
        mExoPlayer = exoPlayer;
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
        String videoUrl = mStepsList.get(position).getVideoURL();

        if(!videoUrl.equals("")) {
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext,
                    Util.getUserAgent(mContext, "BakingApp"), bandwidthMeter);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(videoUrl));
            holder.mExoPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(false);
        } else {
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

    void initializeExoPlayer() {
        if(mExoPlayer == null) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

            TrackSelection.Factory videoTrackSelection = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelection);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
        }
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

        }
    }
}
