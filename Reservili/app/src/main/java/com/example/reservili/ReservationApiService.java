package com.example.reservili;

import com.example.reservili.model.Crenaux;
import com.example.reservili.model.ReservationRequest;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReservationApiService {
    @POST("/reservations")
    Call<ResponseBody> createReservation(@Body ReservationRequest request);

    @GET("reservations/{clientId}")
    Call<List<Crenaux>> getReservationsForClient(@Path("clientId") int clientId);
}

