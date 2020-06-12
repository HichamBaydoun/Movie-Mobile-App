package com.example.movies;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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


public class MainActivity extends AppCompatActivity {
    private TextView username;
    private ListView list;
    private RequestQueue queue;
    private int myPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.genreList);
        username = findViewById(R.id.username);

        final Animation animupload = AnimationUtils.loadAnimation(MainActivity.this, R.anim.mixed_anim);

        username.setText("Welcome, " + getIntent().getStringExtra("name"));

        final ArrayAdapter<String> genres = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Data.genres);

        list.setAdapter(genres);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myPosition = position;
                Intent myIntent = new Intent(MainActivity.this, SecondActivity.class);
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
                    }
                    genres.notifyDataSetChanged();
                }
                catch (Exception ex){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);

        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.upload:
                startActivity(new Intent(MainActivity.this, postActivity.class));
                return true;
            case R.id.about:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Welcome Movie Lovers")
                        .setMessage("This our new app that helps you choose a movie to watch and lets you add new movies.")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setIcon(R.drawable.info)
                        .show();
                    return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
