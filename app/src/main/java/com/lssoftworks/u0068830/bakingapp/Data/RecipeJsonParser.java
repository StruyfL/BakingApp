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

    public static Recipe[] getRecipes(String recipeJsonString) throws JSONException {

        JSONArray recipeArray = new JSONArray(recipeJsonString);

        Recipe[] recipes =  new Recipe[recipeArray.length()];

        for(int i = 0; i < recipeArray.length(); i++) {
            recipes[i] = new Recipe();
            JSONObject recipeObject = recipeArray.getJSONObject(i);
            recipes[i].setId(recipeObject.getInt(RECIPE_ID));
            recipes[i].setName(recipeObject.getString(RECIPE_NAME));
            recipes[i].setServings(recipeObject.getInt(RECIPE_SERVINGS));
            recipes[i].setImage(recipeObject.getString(RECIPE_IMAGE));

            JSONArray recipeIngredients = recipeObject.getJSONArray(RECIPE_INGREDIENTS);

            String[] quantity = new String[recipeIngredients.length()];
            String[] measure = new String[recipeIngredients.length()];
            String[] ingredient = new String[recipeIngredients.length()];

            for(int j = 0; j < recipeIngredients.length(); j++) {
                JSONObject recipeIngredient = recipeIngredients.getJSONObject(j);
                quantity[j] = recipeIngredient.getString(RECIPE_ING_QUANTITY);
                measure[j] = recipeIngredient.getString(RECIPE_ING_MEASURE);
                ingredient[j] = recipeIngredient.getString(RECIPE_ING_INGREDIENT);
            }

            JSONArray recipeSteps = recipeObject.getJSONArray(RECIPE_STEPS);

            Recipe.Step[] steps = new Recipe.Step[recipeSteps.length()];

            for(int j = 0; j < recipeSteps.length(); j++) {
                steps[j] = new Recipe.Step();
                JSONObject recipeStep = recipeSteps.getJSONObject(j);
                id[j] = recipeStep.getInt(RECIPE_STEPS_ID);
                shortDescription[j] = recipeStep.getString(RECIPE_STEPS_SHORTDESCRIPTION);
                description[j] = recipeStep.getString(RECIPE_STEPS_DESCRIPTION);
                videoURL[j] = recipeStep.getString(RECIPE_STEPS_VIDEOURL);
                thumbnailURL[j] = recipeStep.getString(RECIPE_STEPS_THUMBNAILURL);
            }

            recipes[i].setQuantity(quantity);
            recipes[i].setMeasure(measure);
            recipes[i].setIngredient(ingredient);

            Log.d(TAG, String.valueOf(recipes[i].getId()));
        }

        return recipes;
    }
}
