package com.example.movies;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class loginActivity extends AppCompatActivity {

    EditText edUsername, edPassword;
    Button btLogin, btSignUp, guest;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    String username, password;
    ImageView image;
    String insert = "https://seventh-paygrade.000webhostapp.com/insertAccount.php";
    String users = "https://seventh-paygrade.000webhostapp.com/getUsers.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edUsername = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edPassword);
        btLogin = findViewById(R.id.btLogin);
        btSignUp = findViewById(R.id.btSignUp);
        guest = findViewById(R.id.guest);
        image = findViewById(R.id.imageView);

        MediaPlayer ring= MediaPlayer.create(loginActivity.this, R.raw.start);
        ring.start();

        final Animation animguest = AnimationUtils.loadAnimation(loginActivity.this, R.anim.fadeout);
        final Animation animlogin = AnimationUtils.loadAnimation(loginActivity.this, R.anim.bounce);
        final Animation animsignup = AnimationUtils.loadAnimation(loginActivity.this, R.anim.rotate);
        final Animation panda = AnimationUtils.loadAnimation(loginActivity.this, R.anim.panda);

        image.startAnimation(animlogin);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.startAnimation(panda);
            }
        });

        // Creating Volley newRequestQueue .
        requestQueue = Volley.newRequestQueue(loginActivity.this);
        progressDialog = new ProgressDialog(loginActivity.this);

        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guest.startAnimation(animguest);
                final long changeTime = 500L;
                guest.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(loginActivity.this, GuestMainActivity.class));
                        Toast.makeText(loginActivity.this, "Sign up to be able to upload movies.", Toast.LENGTH_SHORT).show();
                    }
                }, changeTime);
            }
        });

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btSignUp.startAnimation(animsignup);
                final long changeTime = 600L;
                btSignUp.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(edPassword.getText().toString().isEmpty() || edUsername.getText().toString().isEmpty()) {
                            Toast.makeText(loginActivity.this, "Fields cant be Empty!", Toast.LENGTH_SHORT).show();
                        }else {
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
                                            Toast.makeText(loginActivity.this, ServerResponse, Toast.LENGTH_LONG).show();
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError volleyError) {

                                            // Hiding the progress dialog after all task complete.
                                            progressDialog.dismiss();

                                            // Showing error message if something goes wrong.
                                            Toast.makeText(loginActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                                        }
                                    }) {
                                @Override
                                protected Map<String, String> getParams() {

                                    // Creating Map String Params.
                                    Map<String, String> params = new HashMap<String, String>();

                                    // Adding All values to Params.
                                    params.put("Username", username);
                                    params.put("Password", password);

                                    return params;
                                }

                            };

                            stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                            // Creating RequestQueue.
                            RequestQueue requestQueue = Volley.newRequestQueue(loginActivity.this);

                            // Adding the StringRequest object into requestQueue.
                            requestQueue.add(stringRequest);
                        }
                        btSignUp.clearAnimation();
                    }
                }, changeTime);
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Showing progress dialog at user registration time.
                progressDialog.setMessage("Please Wait, Logging In");
                progressDialog.show();

                btLogin.startAnimation(animlogin);
                final long changeTime = 500L;
                btLogin.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        JsonArrayRequest request = new JsonArrayRequest(users, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                boolean successful = true;
                                try {
                                    if(edUsername.getText().toString().equals("") && edPassword.getText().toString().equals("")) {
                                        successful = false;
                                        progressDialog.dismiss();
                                        Toast.makeText(loginActivity.this, "All Fields Must be Filled!", Toast.LENGTH_SHORT).show();
                                    }
                                    if(successful == false){
                                        // Hiding the progress dialog after all task complete.
                                        successful = false;
                                        progressDialog.dismiss();
                                        Toast.makeText(loginActivity.this, "Not a user, Please Sign up!", Toast.LENGTH_SHORT).show();
                                    }
                                    //create a loop to iterate through the json array
                                    for (int i = 0; i < response.length(); i++) {
                                        //now fetch a json object from the array at index i
                                        JSONObject row = response.getJSONObject(i);
                                        String username = row.getString("Username");
                                        String password = row.getString("Password");

                                        if(edUsername.getText().toString().equals(username) && edPassword.getText().toString().equals(password) && successful == true) {
                                            Intent myIntent = new Intent(loginActivity.this, MainActivity.class);
                                            myIntent.putExtra("name", username);
                                            // Hiding the progress dialog after all task complete.
                                            progressDialog.dismiss();
                                            successful = true;
                                            startActivity(myIntent);
                                        }
                                    }
                                } catch (Exception ex) {
                                    // Hiding the progress dialog after all task complete.
                                    progressDialog.dismiss();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(loginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        requestQueue.add(request);
                    }
                }, changeTime);
            }
        });
    }

    public void GetValueFromEditText(){
        username = edUsername.getText().toString();
        password = edPassword.getText().toString();
    }
}