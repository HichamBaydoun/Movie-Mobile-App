package com.example.movies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    private ListView list;
    private TextView genreName;
    private ImageView image;
    private int myPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        list = findViewById(R.id.movies);
        genreName = findViewById(R.id.genreName);
        image = findViewById(R.id.imageg);

        genreName.setText(getIntent().getStringExtra("clickedItem"));

        //getting clicked genre
        final String clickedGenre = getIntent().getStringExtra("clickedItem");

        switch (clickedGenre){
            case "Action": image.setImageResource(R.drawable.action);
                break;
            case "Adventure": image.setImageResource(R.drawable.adventure);
                break;
            case "Comedy": image.setImageResource(R.drawable.comedy);
                break;
            case "Fantasy": image.setImageResource(R.drawable.fantasy);
                break;
            case "Horror": image.setImageResource(R.drawable.horror);
                break;
            case "Romance": image.setImageResource(R.drawable.romance);
                break;
            case "Sci-Fi": image.setImageResource(R.drawable.sci);
                break;
            case "Drama": image.setImageResource(R.drawable.drama);
                break;
        }

        final ArrayList<Movie> array = new ArrayList<>();
        final ArrayList<String> names = new ArrayList<>();

        for(int i = 0; i < Movie.movies.size(); i++){
            if(Movie.movies.get(i).getGenre().equalsIgnoreCase(clickedGenre)) {
                array.add(Movie.movies.get(i));
                names.add(Movie.movies.get(i).getName());
            }
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, names);

        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myPosition = position;
                Intent myIntent = new Intent(SecondActivity.this, ThirdActivity.class);
                myIntent.putExtra("id", array.get(myPosition).getId());
                startActivity(myIntent);
            }
        });
    }
}
