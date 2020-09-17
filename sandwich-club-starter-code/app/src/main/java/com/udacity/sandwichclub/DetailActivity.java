package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {

    private TextView mDescriptionTextView;
    private TextView mAlsoKnownForTextView;
    private TextView mIngredientsTextView;
    private TextView mPlaceOfOriginTextView;

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {

        mAlsoKnownForTextView = findViewById(R.id.also_known_tv);
        mDescriptionTextView = findViewById(R.id.description_tv);
        mIngredientsTextView = findViewById(R.id.ingredients_tv);
        mPlaceOfOriginTextView = findViewById(R.id.place_of_origin_tv);

        if(sandwich.getAlsoKnownAs().size() == 0){
            mAlsoKnownForTextView.setVisibility(View.GONE);
        }else{
            String alsoKnownFor = "";
            for(String knownFor : sandwich.getAlsoKnownAs()){
                alsoKnownFor += knownFor + "\n";
            }
            mAlsoKnownForTextView.setText(alsoKnownFor);
        }

        mDescriptionTextView.setText(sandwich.getDescription());


        if(sandwich.getIngredients().size() == 0){
            mIngredientsTextView.setVisibility(View.GONE);
        }else{
            String ingredients = "";
            for(String knownFor : sandwich.getIngredients()){
                ingredients += knownFor + "\n";
            }
            mIngredientsTextView.setText(ingredients);
        }


        mPlaceOfOriginTextView.setText(sandwich.getPlaceOfOrigin());

    }
}
