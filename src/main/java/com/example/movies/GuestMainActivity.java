package com.example.movies;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class GuestMainActivity extends AppCompatActivity {
    private TextView username;
    private ListView list;
    private RequestQueue queue;
    private int myPosition;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_main);
        list = findViewById(R.id.genreList);
        username = findViewById(R.id.username);

        progressDialog = new ProgressDialog(GuestMainActivity.this);

        progressDialog.setMessage("Please Wait, We are Inserting Your Data on Server");
        progressDialog.show();

        username.setText("Welcome, Guest");

        final ArrayAdapter<String> genres = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Data.genres);

        list.setAdapter(genres);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myPosition = position;
                Intent myIntent = new Intent(GuestMainActivity.this, SecondActivity.class);
                myIntent.putExtra ("clickedItem", Data.genres[myPosition]);
                startActivity(myIntent);
            }
        });
        //------------------------------------------------------------------------------------------
        //Getting all movies and puting them in the static array list of movies

        //initialize the volley queue. we have only 1 queue in the project
        queue = Volley.newRequestQueue(this);

        //declare the url for the REST service that will return a json array
        String url = "https://seventh-paygrade.000webhostapp.com/getAllMovies.php";

        //create a json array request
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    //create a loop to iterate through the json array
                    for(int i = 0; i < response.length(); i++){
                        //now fetch a json object from the array at index i
                        JSONObject row = response.getJSONObject(i);
                        int id = row.getInt("ID");
                        String name = row.getString("Name");
                        String year = row.getString("Year");
                        String genre = row.getString("Genre");
                        String rating = row.getString("Rating");
                        Movie.movies.add(new Movie(id, name, year, genre, rating));

                        progressDialog.dismiss();
                    }
                    genres.notifyDataSetChanged();
                }
                catch (Exception ex){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GuestMainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
