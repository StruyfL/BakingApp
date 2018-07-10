package com.lssoftworks.u0068830.bakingapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.lssoftworks.u0068830.bakingapp.Data.Recipe;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, Recipe recipe,
                                int appWidgetId) {

        CharSequence widgetText = "";
        String allIngredients = "";
        if(recipe!= null) {
            widgetText = recipe.getName();

            String[] mQuantity = recipe.getQuantity();
            String[] mMeasure = recipe.getMeasure();
            String[] mIngredient = recipe.getIngredient();


            for(int i = 0; i < mQuantity.length; i++) {
                allIngredients += mQuantity[i] + " " + mMeasure[i] + " " + mIngredient[i] + "\n";
            }
        }
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        views.setTextViewText(R.id.tv_recipe_name, widgetText);
        views.setTextViewText(R.id.tv_recipe_ingredients, allIngredients);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, null, appWidgetId);
        }
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager, Recipe recipe, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipe, appWidgetId);
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

