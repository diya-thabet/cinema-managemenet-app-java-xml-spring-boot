package com.example.reservili.ui.detailspectacle;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.reservili.R;
import com.example.reservili.SpectacleApiService;
import com.example.reservili.adapter.CrenauxListAdapter;
import com.example.reservili.adapter.RubriqueListAdapter;
import com.example.reservili.adapter.SpectacleListAdapter;
import com.example.reservili.model.Crenaux;
import com.example.reservili.model.Rubrique;
import com.example.reservili.model.Spectacle;
import com.example.reservili.model.SpectacleSummary;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailSpectacleFragment extends Fragment {
    SpectacleSummary spectacle;
    private Button btnReserve;
    private RubriqueListAdapter adapter;
    private CrenauxListAdapter adaptercrn;
    private ArrayList<Rubrique> rubriquesList;
    private ArrayList<Crenaux> crenauxList;
    private int idspec;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_spectacle_fragment, container, false);
        Context ctx =this.getContext();
        if (getArguments() != null) {
            spectacle = getArguments().getParcelable("spectacle");
            idspec=spectacle.getIdSpec();


            TextView title=view.findViewById(R.id.detailTitle);

            TextView dates=view.findViewById(R.id.detailInfo);
            title.setText(spectacle.getTitre());


            ImageView imgdetails=view.findViewById(R.id.detailImage);
            Glide.with(requireContext()).load(spectacle.getImageUrl()).into(imgdetails);



        }
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("http://192.168.100.107:8080")
                //.baseUrl("http://172.18.0.149:8080")
                .baseUrl("http://10.0.2.2:8080") // Use 10.0.2.2 to access localhost from emulator
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SpectacleApiService apiService = retrofit.create(SpectacleApiService.class);
        rubriquesList=new ArrayList<>();
        crenauxList=new ArrayList<>();
        adapter = new RubriqueListAdapter(rubriquesList);
       // adaptercrn = new CrenauxListAdapter(crenauxList);
        adaptercrn = new CrenauxListAdapter(crenauxList, new CrenauxListAdapter.OnCrenauxClickListener() {
            @Override
            public void onMapClick(Crenaux crn) {
                String address =crn.getNomlieu()+", "+ crn.getAdrss() + ", " + crn.getVille();
                // 2) Encoder pour URI
                String encodedAddress = Uri.encode(address);
                // 3) URI de navigation (mode voiture)
                String uri = "google.navigation:q=" + encodedAddress + "&mode=d";

                // 4) Créer l'intent
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");

                // 5) Vérifier que Maps est installé et lancer

                if (intent.resolveActivity(ctx.getPackageManager()) != null) {
                    ctx.startActivity(intent);
                } else {
                    Toast.makeText(ctx, "Veuillez installer Google Maps", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCrenauxSelected(Crenaux crn) {



            }
        },false);

        RecyclerView rubriqueRecyclerView = view.findViewById(R.id.rubriquesRecyclerView);
        RecyclerView crenauxRecyclerView = view.findViewById(R.id.crenauxRecyclerView);
        rubriqueRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        crenauxRecyclerView .setLayoutManager(new LinearLayoutManager(getContext()));
        rubriqueRecyclerView.setAdapter(adapter);
        crenauxRecyclerView.setAdapter(adaptercrn);
        Call<Spectacle> call = apiService.getSpectacleD(idspec);
        call.enqueue(new Callback<Spectacle>() {
            @Override
            public void onResponse(Call<Spectacle> call, Response<Spectacle> response) {
                if (response.isSuccessful() && response.body() != null) {
                    rubriquesList.clear();
                    rubriquesList.addAll(response.body().getRubriques());
                    crenauxList.clear();
                    crenauxList.addAll(response.body().getCrenaux());
                    TextView descr=view.findViewById(R.id.detailDescription);
                    descr.setText(response.body().getDescription());

                    adapter.notifyDataSetChanged();
                    adaptercrn.notifyDataSetChanged();
                } else {
                    Log.e("HomeFragment", "API Response Error");
                }
            }

            @Override
            public void onFailure(Call<Spectacle> call, Throwable t) {
                Log.e("HomeFragment", "API Call Failed: " + t.getMessage());
            }
        });





        btnReserve = view.findViewById(R.id.fabReservation);
        Animation pulse = AnimationUtils.loadAnimation(this.getContext(), R.anim.pulse);
        btnReserve.startAnimation(pulse);

        // Définir le listener pour le bouton "Réserver"
        btnReserve.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("spectacle", spectacle);
            Navigation.findNavController(v)
                    .navigate(R.id.action_detailSpectacleFragment_to_reservationFragment, bundle);
        });
        return view;
    }
}
