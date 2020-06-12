package com.example.movies;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class postActivity extends AppCompatActivity {


    private EditText name, year, rating;
    private Spinner genre;
    private Button post;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    private ProgressBar progressBar;
    String n,y,g,r;
    String insert = "https://seventh-paygrade.000webhostapp.com/insertMovie.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        name = findViewById(R.id.name);
        year = findViewById(R.id.year);
        genre = findViewById(R.id.genre);
        rating = findViewById(R.id.rating);
        post = findViewById(R.id.post);
        progressBar = findViewById(R.id.progressBar);

        final Animation animguest = AnimationUtils.loadAnimation(postActivity.this, R.anim.bounce);
        final Animation anim = AnimationUtils.loadAnimation(postActivity.this, R.anim.lefttoright);
        final Animation animpost = AnimationUtils.loadAnimation(postActivity.this, R.anim.zoomin);

        name.startAnimation(anim);
        year.startAnimation(anim);
        rating.startAnimation(anim);

        // Creating Volley newRequestQueue .
        requestQueue = Volley.newRequestQueue(postActivity.this);
        progressDialog = new ProgressDialog(postActivity.this);

       final  ArrayAdapter<String> genreAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Data.genres) {

            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);

                ((TextView) v).setTextSize(16);
                ((TextView) v).setGravity(Gravity.CENTER);

                return v;

            }

            public View getDropDownView(int position, View convertView,ViewGroup parent) {

                View v = super.getDropDownView(position, convertView,parent);

                ((TextView) v).setGravity(Gravity.CENTER);

                return v;

            }

        };

        genre.setAdapter(genreAdapter);
        genre.startAnimation(animguest);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (name.getText().toString().isEmpty() || year.getText().toString().isEmpty() || rating.getText().toString().isEmpty()) {
                    Toast.makeText(postActivity.this, "Please Fill All Fields!", Toast.LENGTH_SHORT).show();

                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    // Showing progress dialog at user registration time.
                    progressDialog.setMessage("Please Wait, We are Inserting Your Data on Server");
                    progressDialog.show();

                    // Calling method to get value from EditText.
                    GetValueFromEditText();

                    // Creating string request with post method.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, insert,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String ServerResponse) {

                                    // Hiding the progress dialog after all task complete.
                                    progressDialog.dismiss();

                                    // Showing response message coming from server.
                                    Toast.makeText(postActivity.this, ServerResponse, Toast.LENGTH_LONG).show();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {

                                    // Hiding the progress dialog after all task complete.
                                    progressDialog.dismiss();

                                    // Showing error message if something goes wrong.
                                    Toast.makeText(postActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {

                            // Creating Map String Params.
                            Map<String, String> params = new HashMap<String, String>();

                            // Adding All values to Params.
                            params.put("Name", n);
                            params.put("Year", y);
                            params.put("Genre", g);
                            params.put("Rating", r);
                            params.put("key", "Sa2eL");

                            return params;
                        }
                    };

                    //Time out duration
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    // Creating RequestQueue.
                    RequestQueue requestQueue = Volley.newRequestQueue(postActivity.this);

                    // Adding the StringRequest object into requestQueue.
                    requestQueue.add(stringRequest);

                    recreate();
                }
                post.startAnimation(animpost);
                final long changeTime = 1000L;
                post.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        post.clearAnimation();
                    }
                }, changeTime);
            }
        });
    }

    public void GetValueFromEditText(){
        n = name.getText().toString();
        y = year.getText().toString();
        g = genre.getSelectedItem().toString();
        r = rating.getText().toString();
    }
}
