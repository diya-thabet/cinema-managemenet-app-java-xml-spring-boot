package com.example.reservili;// SpectacleApiService.java
import com.example.reservili.model.Spectacle;
import com.example.reservili.model.SpectacleSummary;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface SpectacleApiService {
    @GET("/spectacles")
    Call<List<SpectacleSummary>> getSpectacles();
    @GET("/spectacles/{id}")
    Call<Spectacle> getSpectacleD(@Path("id") int id);
    @GET("/spectacles/search")
    Call<List<SpectacleSummary>> searchSpectacles(
            @Query("titre") String titre,
            @Query("lieu") String lieu,
            @Query("ville") String ville,
            @Query("dateS") String dateS,
            @Query("h_debut") String h_debut
    );
}