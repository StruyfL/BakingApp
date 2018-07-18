package com.lssoftworks.u0068830.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.lssoftworks.u0068830.bakingapp.Data.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsStepFragment extends Fragment {

    private static Recipe.Step mStep;
    private Context mContext;

    private SimpleExoPlayer mExoPlayer;
    private MediaSource mMediaSource;
    private DefaultBandwidthMeter mBandwidthMeter;
    private DataSource.Factory mDataSourceFactory;

    @BindView(R.id.ep_instructionvideo)
    PlayerView mExoPlayerView;

    @BindView(R.id.tv_desc)
    TextView mDesc;

    public DetailsStepFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_details_step, container, false);

        ButterKnife.bind(DetailsStepFragment.this, rootView);

        if(mStep != null && mContext != null) {

            mDesc.setText(mStep.getDescription());

            if(!mStep.getVideoURL().equals("")) {

                initializeExoPlayer();

                mExoPlayerView.setVisibility(View.VISIBLE);
                mExoPlayerView.setPlayer(mExoPlayer);
                mExoPlayer.prepare(mMediaSource);
                mExoPlayer.setPlayWhenReady(false);
            }
        }

        return rootView;
    }

    public void setStep(Recipe.Step step) {
        mStep = step;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    void initializeExoPlayer() {
        if(mExoPlayer == null) {
            mBandwidthMeter = new DefaultBandwidthMeter();

            TrackSelection.Factory videoTrackSelection = new AdaptiveTrackSelection.Factory(mBandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelection);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);

            mDataSourceFactory = new DefaultDataSourceFactory(mContext,
                    Util.getUserAgent(mContext, "BakingApp"), mBandwidthMeter);

            mMediaSource = new ExtractorMediaSource.Factory(mDataSourceFactory).createMediaSource(Uri.parse(mStep.getVideoURL()));

        }
    }
}
