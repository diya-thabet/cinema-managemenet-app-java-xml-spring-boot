package com.example.reservili.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reservili.R;
import com.example.reservili.ReservationApiService;
import com.example.reservili.adapter.CrenauxListAdapter;
import com.example.reservili.databinding.FragmentNotificationsBinding;
import com.example.reservili.model.Crenaux;
import com.example.reservili.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationsFragment extends Fragment {

    private RecyclerView rv;
    private TextView tvEmpty;
    private CrenauxListAdapter adapter;
    private ArrayList<Crenaux> crenauxList;
    private SessionManager session;
    private ReservationApiService api;
    private Context ctx;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // 1) Inflater + views
        View v = inflater.inflate(R.layout.fragment_notifications, container, false);
        rv      = v.findViewById(R.id.rvReservations);
        tvEmpty = v.findViewById(R.id.tvEmpty);
        ctx     = requireContext();
        session = SessionManager.getInstance(ctx);

        // 2) Préparez Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ReservationApiService.class);

        // 3) Préparez la liste & l’adapter
        crenauxList = new ArrayList<>();
        adapter = new CrenauxListAdapter(crenauxList,
                new CrenauxListAdapter.OnCrenauxClickListener() {
                    @Override
                    public void onMapClick(Crenaux crn) {
                        String address = crn.getNomlieu() + ", " + crn.getAdrss() + ", " + crn.getVille();
                        String encoded = Uri.encode(address);
                        String uri = "google.navigation:q=" + encoded + "&mode=d";
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        i.setPackage("com.google.android.apps.maps");
                        if (i.resolveActivity(ctx.getPackageManager()) != null) {
                            ctx.startActivity(i);
                        } else {
                            Toast.makeText(ctx, "Veuillez installer Google Maps", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCrenauxSelected(Crenaux crn) {
                        // pas utilisé ici
                    }
                }, false);

        rv.setLayoutManager(new LinearLayoutManager(ctx));
        rv.setAdapter(adapter);

        // 4) Chargez directement les réservations pour ce client
        int clientId = session.getClientId();
        api.getReservationsForClient(clientId)
                .enqueue(new Callback<List<Crenaux>>() {
                    @Override
                    public void onResponse(Call<List<Crenaux>> call,
                                           Response<List<Crenaux>> resp) {
                        if (resp.isSuccessful() && resp.body() != null && !resp.body().isEmpty()) {
                            // remplit la liste et notifie l’adapter
                            crenauxList.clear();
                            crenauxList.addAll(resp.body());
                            adapter.notifyDataSetChanged();
                            rv.setVisibility(View.VISIBLE);
                            tvEmpty.setVisibility(View.GONE);
                        } else {
                            // pas de réservations : affiche message vide
                            rv.setVisibility(View.GONE);
                            tvEmpty.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Crenaux>> call, Throwable t) {
                        Toast.makeText(ctx,
                                "Échec de chargement : " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

        return v;
    }
}
