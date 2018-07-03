package com.lssoftworks.u0068830.bakingapp.Data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecipeJsonParser {

    static final String TAG = RecipeJsonParser.class.getSimpleName();

    // Main JSON data
    private static final String RECIPE_ID = "id";
    private static final String RECIPE_NAME = "name";
    private static final String RECIPE_INGREDIENTS = "ingredients";
    private static final String RECIPE_STEPS = "steps";
    private static final String RECIPE_SERVINGS = "servings";
    private static final String RECIPE_IMAGE = "image";

    // Ingredients JSON data
    private static final String RECIPE_ING_QUANTITY = "quantity";
    private static final String RECIPE_ING_MEASURE = "measure";
    private static final String RECIPE_ING_INGREDIENT = "ingredient";

    // Steps JSON data
    private static final String RECIPE_STEPS_ID = "id";
    private static final String RECIPE_STEPS_SHORTDESCRIPTION = "shortDescription";
    private static final String RECIPE_STEPS_DESCRIPTION = "description";
    private static final String RECIPE_STEPS_VIDEOURL = "videoURL";
    private static final String RECIPE_STEPS_THUMBNAILURL = "thumbnailURL";

    public static Recipe getRecipes(String recipeJsonString) throws JSONException {

        JSONArray recipeArray = new JSONArray(recipeJsonString);

        Recipe[] recipes =  new Recipe[recipeArray.length()];

        for(int i = 0; i < recipeArray.length(); i++) {
            recipes[i] = new Recipe();
            JSONObject recipeObject = recipeArray.getJSONObject(i);
            recipes[i].setId(recipeObject.getInt(RECIPE_ID));

            Log.d(TAG, String.valueOf(recipes[i].getId()));
        }

        return null;
    }
}
