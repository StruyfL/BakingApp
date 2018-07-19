package com.lssoftworks.u0068830.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.lssoftworks.u0068830.bakingapp.Data.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsStepFragment extends Fragment {

    private final static String TAG = DetailsStepFragment.class.getSimpleName();
    private static final String CURRENT_POSITION = "current_position";
    private static final String PLAY_WHEN_READY = "play_when_ready";
    private static final String CURRENT_WINDOW = "current_window";
    private static final String VIDEO_URL = "video_url";
    private static final String DESCRIPTION = "description";

    private static Recipe.Step mStep;
    private Context mContext;
    private boolean mPlayReady;
    private long mCurPos;
    private int mCurWindow;

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

        if(savedInstanceState != null) {
            mCurWindow = savedInstanceState.getInt(CURRENT_WINDOW);
            mCurPos = savedInstanceState.getLong(CURRENT_POSITION);
            mPlayReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
            mStep.setDescription(savedInstanceState.getString(DESCRIPTION));
            mStep.setVideoURL(savedInstanceState.getString(VIDEO_URL));
            mContext = getContext();
        }

        Log.d(TAG, "Reloading previous state 1: " + mCurWindow + "  " + mCurPos);

        if(mStep != null) {

            Log.d(TAG, "Reloading previous state 2: " + mCurWindow + "  " + mCurPos);

            mDesc.setText(mStep.getDescription());

            if(!mStep.getVideoURL().equals("")) {

                initializeExoPlayer();

                mExoPlayerView.setVisibility(View.VISIBLE);
                mExoPlayerView.setPlayer(mExoPlayer);


                mExoPlayer.seekTo(mCurWindow, mCurPos);
                mExoPlayer.prepare(mMediaSource, false, false);
                mExoPlayer.setPlayWhenReady(mPlayReady);
                Log.d(TAG, "Reloading previous state 3: " + mCurWindow + "  " + mCurPos);



            }
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(Util.SDK_INT > 23) {
            initializeExoPlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Util.SDK_INT <= 23) {
            initializeExoPlayer();
        }
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

    @Override
    public void onPause() {
        super.onPause();

        if (mExoPlayer != null) {
            mCurPos = mExoPlayer.getCurrentPosition();
            mPlayReady = mExoPlayer.getPlayWhenReady();
            mCurWindow = mExoPlayer.getCurrentWindowIndex();
        }

        if(mExoPlayer != null && Util.SDK_INT <= 23) {
            mExoPlayer.release();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if(mExoPlayer != null && Util.SDK_INT > 23) {
            mExoPlayer.release();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(CURRENT_POSITION, mCurPos);
        outState.putBoolean(PLAY_WHEN_READY, mPlayReady);
        outState.putInt(CURRENT_WINDOW, mCurWindow);
        outState.putString(VIDEO_URL, mStep.getVideoURL());
        outState.putString(DESCRIPTION, mStep.getDescription());
    }
}
