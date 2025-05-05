package com.example.reservili.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reservili.R;
import com.example.reservili.SpectacleApiService;
import com.example.reservili.adapter.SpectacleListAdapter;
import com.example.reservili.databinding.FragmentDashboardBinding;
import com.example.reservili.model.Rubrique;
import com.example.reservili.model.Spectacle;
import com.example.reservili.model.SpectacleSummary;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DashboardFragment extends Fragment {

    private LinkedList<SpectacleSummary> allSpectacles; // Liste complète
    private LinkedList<SpectacleSummary> filteredSpectacles = new LinkedList<>();
    private SpectacleListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        EditText titreInput = view.findViewById(R.id.editTitre);
        EditText dateInput = view.findViewById(R.id.editDate);
        EditText heureInput = view.findViewById(R.id.editHeure);
        //EditText lieuInput = view.findViewById(R.id.editLieu);
        EditText villeInput = view.findViewById(R.id.editVille);
        Button btnSearch = view.findViewById(R.id.btnChercher);
        RecyclerView recyclerView = view.findViewById(R.id.resultRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SpectacleListAdapter(filteredSpectacles, new SpectacleListAdapter.OnSpectacleClickListener() {
            @Override
            public void onSpectacleClick(SpectacleSummary spectacle) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("spectacle", spectacle);
                Navigation.findNavController(requireView()).navigate(R.id.action_searchFragment_to_detailSpectacleFragment, bundle);
            }

            @Override
            public void onReserveClick(SpectacleSummary spectacle) {
                // Création du bundle pour passer l'objet spectacle au fragment de réservation
                Bundle bundle = new Bundle();
                bundle.putParcelable("spectacle", spectacle);
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_searchFragment_to_reservationFragment, bundle);
            }
        });

        recyclerView.setAdapter(adapter);

        // TODO: Remplace ceci avec ta vraie source (API ou DB)
        allSpectacles = getFakeSpectacles();

        btnSearch.setOnClickListener(v -> {
            String titre = titreInput.getText().toString().trim().toLowerCase();
            String date = dateInput.getText().toString().trim();
            String heure = heureInput.getText().toString().trim();
            //String lieu = lieuInput.getText().toString().trim().toLowerCase();
            String lieu = "";
            String ville = villeInput.getText().toString().trim().toLowerCase();
            if (titre.isEmpty()) {
                titre = null;
            }
            /*if (lieu.isEmpty()) {
                lieu = null;
            }*/
            if (ville.isEmpty()) {
                ville = null;
            }
            if (date.isEmpty()) {
                date = null;
            }
            if (heure.isEmpty()) {
                heure = null;
            }


            Retrofit retrofit = new Retrofit.Builder()
                   // .baseUrl("http://192.168.100.107:8080")
                    // .baseUrl("http://172.18.0.149:8080")
                   .baseUrl("http://10.0.2.2:8080") // Use 10.0.2.2 to access localhost from emulator
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            SpectacleApiService apiService = retrofit.create(SpectacleApiService.class);
            Call<List<SpectacleSummary>> call = apiService.searchSpectacles(titre,lieu,ville,date,heure);
            call.enqueue(new Callback<List<SpectacleSummary>>() {
                @Override
                public void onResponse(Call<List<SpectacleSummary>> call, Response<List<SpectacleSummary>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        filteredSpectacles.clear();
                        filteredSpectacles.addAll(response.body());
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("HomeFragment", "API Response Error");
                    }
                }

                @Override
                public void onFailure(Call<List<SpectacleSummary>> call, Throwable t) {
                    Log.e("HomeFragment", "API Call Failed: " + t.getMessage());
                }
            });


            adapter.notifyDataSetChanged();
        });

        return view;
    }

    private LinkedList<SpectacleSummary> getFakeSpectacles() {
        // Réutilise le code de HomeFragment pour des données factices ici
        LinkedList<SpectacleSummary> spectacleList = new LinkedList<>();
        // Ajoute quelques objets Spectacle ici...
        spectacleList.add(new SpectacleSummary(111,"Concert Jaz","20-5-2025","https://woody.cloudly.space/app/uploads/cotedazur-france/2022/06/thumbs/istock-465732100-640x640.jpg"));
        spectacleList.add(new SpectacleSummary(111,"Concert Jaz","20-5-2025","https://woody.cloudly.space/app/uploads/cotedazur-france/2022/06/thumbs/istock-465732100-640x640.jpg"));
        spectacleList.add(new SpectacleSummary(111,"Concert Jaz","20-5-2025","https://woody.cloudly.space/app/uploads/cotedazur-france/2022/06/thumbs/istock-465732100-640x640.jpg"));
        spectacleList.add(new SpectacleSummary(111,"Concert Jaz","20-5-2025","https://woody.cloudly.space/app/uploads/cotedazur-france/2022/06/thumbs/istock-465732100-640x640.jpg"));

        return spectacleList;
    }
}
