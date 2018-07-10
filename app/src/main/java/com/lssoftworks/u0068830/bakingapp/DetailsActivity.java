package com.lssoftworks.u0068830.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

import static com.lssoftworks.u0068830.bakingapp.MainActivity.EXTRA_RECIPE_ID;
import static com.lssoftworks.u0068830.bakingapp.MainActivity.mRecipes;

public class DetailsActivity extends AppCompatActivity {

    private static Recipe mRecipe;
    private String[] mQuantity;
    private String[] mMeasure;
    private String[] mIngredient;
    private StepAdapter mAdapter;
    private static ArrayList<Recipe.Step> mSteps;
    private SimpleExoPlayer mExoPlayer;
    private DefaultBandwidthMeter mBandwidthMeter;
    private DataSource.Factory mDataSourceFactory;
    private ArrayList<MediaSource> mMediaSource = new ArrayList<>();

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

        initializeExoPlayer();

        mAdapter = new StepAdapter(mSteps, mExoPlayer, mMediaSource);
        mStepList.setAdapter(mAdapter);

    }

    void initializeExoPlayer() {
        if(mExoPlayer == null) {
            mBandwidthMeter = new DefaultBandwidthMeter();

            TrackSelection.Factory videoTrackSelection = new AdaptiveTrackSelection.Factory(mBandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelection);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

            mDataSourceFactory = new DefaultDataSourceFactory(this,
                    Util.getUserAgent(this, "BakingApp"), mBandwidthMeter);

            for(int i = 0; i < mSteps.size(); i++) {
                mMediaSource.add(new ExtractorMediaSource.Factory(mDataSourceFactory).createMediaSource(Uri.parse(mSteps.get(i).getVideoURL())));
            }
        }
    }

    public void RecipeToWidget(View v) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidget.class));
        //Now update all widgets
        RecipeWidget.updateRecipeWidgets(this, appWidgetManager, mRecipe, appWidgetIds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mExoPlayer.release();
    }
}
