package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {
        Sandwich sandwich = new Sandwich();
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONObject name = jsonObject.getJSONObject("name");

            String mainName = name.getString("mainName");
            JSONArray alsoKnownForJSON = name.getJSONArray("alsoKnownAs");
            List<String> alsoKnownFor = new ArrayList<>();
            for(int i = 0; i < alsoKnownForJSON.length(); i++){
                alsoKnownFor.add(alsoKnownForJSON.getString(i));
            }

            String placeOfOrigin = jsonObject.getString("placeOfOrigin");

            String description = jsonObject.getString("description");

            String imageUrl = jsonObject.getString("image");

            JSONArray ingredientsJSON = jsonObject.getJSONArray("ingredients");
            List<String>  ingredients = new ArrayList<>();
            for(int i = 0; i < ingredientsJSON.length(); i++){
                ingredients.add(ingredientsJSON.getString(i));
            }


            sandwich.setMainName(mainName);
            sandwich.setAlsoKnownAs(alsoKnownFor);
            sandwich.setDescription(description);
            sandwich.setImage(imageUrl);
            sandwich.setIngredients(ingredients);
            sandwich.setPlaceOfOrigin(placeOfOrigin);

        } catch (JSONException e) {
            e.printStackTrace();
            sandwich = null;
        }
        return sandwich;
    }
}
