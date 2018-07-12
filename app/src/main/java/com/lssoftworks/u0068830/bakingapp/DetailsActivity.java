package com.lssoftworks.u0068830.bakingapp;

import android.support.v4.app.FragmentManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
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

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lssoftworks.u0068830.bakingapp.MainActivity.EXTRA_RECIPE_ID;
import static com.lssoftworks.u0068830.bakingapp.MainFragment.mRecipes;

public class DetailsActivity extends AppCompatActivity {

    private static Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();

        int id = intent.getIntExtra(EXTRA_RECIPE_ID, 1);

        mRecipe = mRecipes.get(id-1);

        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setRecipe(mRecipe);
        detailsFragment.setContext(this);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.details_container, detailsFragment)
                .commit();
    }

    public void RecipeToWidget(View v) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidget.class));
        //Now update all widgets
        RecipeWidget.updateRecipeWidgets(this, appWidgetManager, mRecipe, appWidgetIds);
    }

}
