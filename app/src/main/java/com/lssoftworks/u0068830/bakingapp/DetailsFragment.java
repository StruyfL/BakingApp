package com.lssoftworks.u0068830.bakingapp;

import android.content.Context;
import android.net.Uri;
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

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.lssoftworks.u0068830.bakingapp.Data.Recipe;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsFragment extends Fragment {

    private static Recipe mRecipe;
    private int mRecipeId;
    private String[] mQuantity;
    private String[] mMeasure;
    private String[] mIngredient;
    private StepAdapter mAdapter;
    private static ArrayList<Recipe.Step> mSteps;
    private SimpleExoPlayer mExoPlayer;
    private DefaultBandwidthMeter mBandwidthMeter;
    private DataSource.Factory mDataSourceFactory;
    private ArrayList<MediaSource> mMediaSource = new ArrayList<>();
    private Context mContext;
    private RecyclerView.LayoutManager mLinearLayoutManager;
    private Parcelable layoutManagerSavedState;

    private static final String STEPSLIST_POSITION = "stepslist_position";
    private static final String RECIPE_ID = "recipe_id";
    private static final String TAG = DetailsFragment.class.getSimpleName();
    private static final String CURRENT_POSITION = "current_position";
    private static final String PLAY_WHEN_READY = "play_when_ready";
    private static final String CURRENT_WINDOW = "current_window";

    private boolean mPlayReady;
    private long mCurPos;
    private int mCurWindow;

    @BindView(R.id.tv_recipe_name)
    TextView mRecipeName;

    @BindView(R.id.ll_ingredients)
    LinearLayout mIngredientList;

    @BindView(R.id.rv_steps)
    RecyclerView mStepList;

    public DetailsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        ButterKnife.bind(DetailsFragment.this, rootView);

        if(savedInstanceState != null) {
            mCurWindow = savedInstanceState.getInt(CURRENT_WINDOW);
            mCurPos = savedInstanceState.getLong(CURRENT_POSITION);
            mPlayReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
            mRecipeId = savedInstanceState.getInt(RECIPE_ID);
            mRecipe = MainActivity.mRecipes.get(mRecipeId);
            mContext = getContext();
        }

        if(mRecipe != null && mContext != null) {

            mRecipeName.setText(mRecipe.getName());
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

            mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            mStepList.setLayoutManager(mLinearLayoutManager);
            mStepList.setHasFixedSize(true);

            if(savedInstanceState != null) {
                mStepList.getLayoutManager().onRestoreInstanceState(layoutManagerSavedState);
            }

            initializeExoPlayer();

            mAdapter = new StepAdapter(mSteps, mExoPlayer, mMediaSource, mCurWindow, mCurPos);
            mStepList.setAdapter(mAdapter);
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

    void initializeExoPlayer() {
        if(mExoPlayer == null) {
            mBandwidthMeter = new DefaultBandwidthMeter();

            TrackSelection.Factory videoTrackSelection = new AdaptiveTrackSelection.Factory(mBandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelection);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);

            mDataSourceFactory = new DefaultDataSourceFactory(mContext,
                    Util.getUserAgent(mContext, "BakingApp"), mBandwidthMeter);

            for(int i = 0; i < mSteps.size(); i++) {
                mMediaSource.add(new ExtractorMediaSource.Factory(mDataSourceFactory).createMediaSource(Uri.parse(mSteps.get(i).getVideoURL())));
            }
        }
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

        Log.d(TAG, RECIPE_ID + " " + mRecipeId);

        outState.putParcelable(STEPSLIST_POSITION, mStepList.getLayoutManager().onSaveInstanceState());
        outState.putInt(RECIPE_ID, mRecipeId);
        outState.putLong(CURRENT_POSITION, mCurPos);
        outState.putBoolean(PLAY_WHEN_READY, mPlayReady);
        outState.putInt(CURRENT_WINDOW, mCurWindow);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null) {
            layoutManagerSavedState = savedInstanceState.getParcelable(STEPSLIST_POSITION);
        }
    }

}
