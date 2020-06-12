package com.example.movies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ThirdActivity extends AppCompatActivity {

    private TextView tv;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        tv = (TextView)findViewById(R.id.tv);

        int id = getIntent().getIntExtra("id", 0);

        for(int i = 0; i < Movie.movies.size(); i++){
            if(Movie.movies.get(i).getId() == id){
                movie = Movie.movies.get(i);
                break;
            }
        }
        tv.setText(movie.toString());
    }
}
