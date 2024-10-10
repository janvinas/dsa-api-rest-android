package com.janvinas.dsa_api_rest_android;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    Retrofit retrofit;
    GithubService githubService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.github.com/")
                .build();
        githubService = retrofit.create(GithubService.class);
    }

    public void onClick(View v){
        String username = ((TextView) findViewById(R.id.username)).getText().toString();
        githubService.listRepos(username).enqueue(new ResponseCallback());
    }

    public class ResponseCallback implements Callback<List<Repo>>{

        @Override
        public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
            TextView result = findViewById(R.id.result);
            if(response.body() == null){
                result.setText("Error performing request");
            }
            result.setText("");
            response.body().forEach(repo -> {
                result.append(repo.name + "\n  " + repo.description + "\n  " + repo.stargazers_count + " stars\n");
            });
        }

        @Override
        public void onFailure(Call<List<Repo>> call, Throwable t) {
            TextView result = findViewById(R.id.result);
            result.setText("Error performing request");
        }
    }
}