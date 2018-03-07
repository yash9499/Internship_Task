package com.example.pablo.mycarpet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements CountryAdapter.ListItemClickListener {
    private RecyclerView recyclerView;
    CountryPopulationRank list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        getData();
    }

    public void getData() {
        Call<CountryPopulationRank> countryPopulationRankCall = APIAccess.getApiService().getCountryList();
        countryPopulationRankCall.enqueue(new Callback<CountryPopulationRank>() {
            @Override
            public void onResponse(Call<CountryPopulationRank> call, Response<CountryPopulationRank> response) {
                list = response.body();
                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                recyclerView.setAdapter(new CountryAdapter(MainActivity.this, list.getWorldpopulation(), MainActivity.this));
            }

            @Override
            public void onFailure(Call<CountryPopulationRank> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onListItemClick(int clickedItemIndex) throws IOException {
        Worldpopulation worldpopulation = list.getWorldpopulation().get(clickedItemIndex);
        Intent intent = new Intent(MainActivity.this, Detail_Activity.class);
        intent.putExtra("image_URL", worldpopulation.getFlag());
        intent.putExtra("country_name", worldpopulation.getCountry());
        startActivity(intent);
    }
}
