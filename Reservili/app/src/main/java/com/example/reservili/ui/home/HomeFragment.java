package com.example.reservili.ui.home;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.Spinner;
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
import com.example.reservili.databinding.FragmentHomeBinding;
import com.example.reservili.model.Rubrique;
import com.example.reservili.model.Spectacle;
import com.example.reservili.model.SpectacleSummary;
import com.example.reservili.utils.SessionManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private SpectacleListAdapter adapter;
    private LinkedList<SpectacleSummary> spectacleList;
    private List<SpectacleSummary> allSpectacles;
    private SessionManager session;
    private PopupWindow popupWindow;
    private String currentSearch = "";
    private String filterCity    = "Toutes";
    private String filterDate    = "";        // format "dd/MM/yyyy"
    private String sortOption    = "Date ↑";
    private FrameLayout progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.d("HomeFragment", "Fragment is being created");
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        session = SessionManager.getInstance(requireContext());
        recyclerView = view.findViewById(R.id.spectacleRecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        progressBar = view.findViewById(R.id.loadingOverlayy);

        TextView btnMore = view.findViewById(R.id.btnMore);
        btnMore.setOnClickListener(v -> togglePopup(v));
        // Exemple de données fictives (tu peux remplacer par les vraies depuis la base ou API)
        Retrofit retrofit = new Retrofit.Builder()
               // .baseUrl("http://192.168.100.107:8080")
               // .baseUrl("http://172.18.0.149:8080")
                .baseUrl("http://10.0.2.2:8080") // Use 10.0.2.2 to access localhost from emulator
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SpectacleApiService apiService = retrofit.create(SpectacleApiService.class);

        spectacleList = new LinkedList<>();
        allSpectacles = new ArrayList<>();

        // ... d’autres spectacles ici
        Log.d("HomeFragment", "Spectacle list size: " + spectacleList.size());
        adapter = new SpectacleListAdapter(spectacleList, new SpectacleListAdapter.OnSpectacleClickListener() {
            @Override
            public void onSpectacleClick(SpectacleSummary spectacle) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("spectacle", spectacle);
                Navigation.findNavController(requireView()).navigate(R.id.action_navigation_home_to_detailSpectacleFragment, bundle);
            }

            @Override
            public void onReserveClick(SpectacleSummary spectacle) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("spectacle", spectacle);
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_navigation_home_to_reservationFragment, bundle);

            }
        });

        EditText etSearch = view.findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) { }
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) { }

            @Override
            public void afterTextChanged(Editable s) {
                currentSearch = s.toString();
                applyAllFilters();
            }

        });
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.VISIBLE);
        Call<List<SpectacleSummary>> call = apiService.getSpectacles();
        call.enqueue(new Callback<List<SpectacleSummary>>() {
            @Override
            public void onResponse(Call<List<SpectacleSummary>> call, Response<List<SpectacleSummary>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    allSpectacles.clear();
                    allSpectacles.addAll(response.body());
                    spectacleList.clear();
                    spectacleList.addAll(allSpectacles);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("HomeFragment", "API Response Error");
                }
            }

            @Override
            public void onFailure(Call<List<SpectacleSummary>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("HomeFragment", "API Call Failed: " + t.getMessage());
            }
        });


        return view;
    }

    private void togglePopup(View anchor) {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            View popupView = getLayoutInflater().inflate(R.layout.popup_filters, null);
            popupWindow = new PopupWindow(popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true);

            // Initialiser les spinners et le datePicker
            Spinner spinnerCity = popupView.findViewById(R.id.spinnerCity);
            Spinner spinnerSort = popupView.findViewById(R.id.spinnerSort);
            EditText etDate = popupView.findViewById(R.id.etDateFilter);

            List<String> cities = Arrays.asList(
                    "Toutes", "Tunis", "Sfax", "Sousse", "Bizerte", "Monastir"
            );
            List<String> sortOptions = Arrays.asList(
                    "Date ↑", "Date ↓", "Heure ↑", "Heure ↓"
            );

            // 2) Création des ArrayAdapter
            ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_spinner_item,
                    cities
            );
            cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_spinner_item,
                    sortOptions
            );
            sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // 3) Assignation aux Spinner
            spinnerCity.setAdapter(cityAdapter);
            spinnerCity.setPrompt("Choisir la ville");
            spinnerSort.setAdapter(sortAdapter);
            spinnerSort.setPrompt("Trier par");

            // Gérer le clic sur le champ de date
            etDate.setOnClickListener(d -> {
                // Utilisation de Calendar pour compatibilité API 24
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(),
                        (dp, y, m, dayOfMonth) -> {
                            String dateStr = String.format(Locale.FRANCE, "%02d/%02d/%04d", dayOfMonth, m + 1, y);
                            etDate.setText(dateStr);
                        },
                        year, month, day);
                dialog.show();
            });



            Button btnReset = popupView.findViewById(R.id.btnResetFilters);
            btnReset.setOnClickListener(v -> {

                filterCity = "Toutes";
                filterDate = "";
                sortOption = sortOptions.get(0);  // "Date ↑"

                // 2) Remise à zéro des UI
                spinnerCity.setSelection(0);
                etDate.setText("");
                spinnerSort.setSelection(0);

                // 3) Appliquer les filtres (tout restaurer)
                applyAllFilters();

                // 4) Fermer la popup (optionnel)
                popupWindow.dismiss();
            });


            // Pour fermer en cliquant à l'extérieur
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // ❗❗ Afficher la popup immédiatement, et PAS attendre le clic sur date
            popupWindow.showAsDropDown(anchor, 0, 0);



            // Quand la ville change
            spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                    filterCity = cities.get(pos);
                    applyAllFilters();
                }
                @Override public void onNothingSelected(AdapterView<?> p) { }
            });

// Quand le tri change
            spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                    sortOption = sortOptions.get(pos);
                    applyAllFilters();
                }
                @Override public void onNothingSelected(AdapterView<?> p) { }
            });

// Pour la date, après le DatePicker
            etDate.setOnClickListener(d -> {
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(getContext(),
                        (dp, y, m, day) -> {
                            filterDate = String.format(Locale.FRANCE, "%02d/%02d/%04d",
                                    day, m+1, y);
                            etDate.setText(filterDate);
                            applyAllFilters();
                        },
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH)
                ).show();
            });

        }








    }


    private void filterSpectacles(String query) {
        spectacleList.clear();

        if (query == null || query.trim().isEmpty()) {
            // pas de filtre → tout afficher
            spectacleList.addAll(allSpectacles);
        } else {
            String lower = query.toLowerCase(Locale.ROOT);
            for (SpectacleSummary sp : allSpectacles) {
                // compare sur tous les attributs texte
                boolean matches = false;
                if (sp.getTitre().toLowerCase(Locale.ROOT).contains(lower)) matches = true;
                else if (sp.getNomLieu().toLowerCase(Locale.ROOT).contains(lower)) matches = true;
                else if (sp.getVille().toLowerCase(Locale.ROOT).contains(lower)) matches = true;
                else if (sp.getDateS().toString().toLowerCase(Locale.ROOT).contains(lower)) matches = true;
                // ajoute d'autres champs si besoin…

                if (matches) {
                    spectacleList.add(sp);
                }
            }
        }


        adapter.notifyDataSetChanged();
    }



    private void applyAllFilters() {
        spectacleList.clear();

        String q = currentSearch.toLowerCase(Locale.ROOT);
        for (SpectacleSummary sp : allSpectacles) {
            // 1) Recherche textuelle
            boolean matchSearch = q.isEmpty() ||
                    sp.getTitre().toLowerCase(Locale.ROOT).contains(q) ||
                    sp.getNomLieu().toLowerCase(Locale.ROOT).contains(q) ||
                    sp.getVille().toLowerCase(Locale.ROOT).contains(q) ||
                    sp.getDateS().toString().contains(q);

            if (!matchSearch) continue;

            // 2) Filtre ville
            if (!filterCity.equals("Toutes")
                    && !sp.getVille().equalsIgnoreCase(filterCity)) {
                continue;
            }

            // 3) Filtre date
            if (!filterDate.isEmpty()) {
                // sp.getDateS() est une String ISO, ex. "2025-01-10"
                String iso = sp.getDateS();
                String[] parts = iso.split("-");
                // on s’assure qu’on a bien 3 morceaux (année, mois, jour)
                if (parts.length != 3) {
                    // format inattendu, on considère que ce n’est pas un match
                    continue;
                }
                // reconstitue au format "dd/MM/yyyy"
                String formatted = parts[2] + "/" + parts[1] + "/" + parts[0];
                if (!formatted.equals(filterDate)) {
                    continue;
                }
            }

            // Tout est passé → on garde
            spectacleList.add(sp);
        }

        // 4) Tri
        Comparator<SpectacleSummary> cmp;
        switch (sortOption) {
            case "Date ↑":
                // Comparaison lexique sur ISO "yyyy-MM-dd" ⇒ ordre chronologique
                cmp = Comparator.comparing(SpectacleSummary::getDateS);
                break;
            case "Date ↓":
                cmp = Comparator.comparing(SpectacleSummary::getDateS).reversed();
                break;
            case "Heure ↑":
                // On parse en double pour trier numériquement (ex. "18.3" → 18.3)
                cmp = Comparator.comparingDouble(sp ->
                        Double.parseDouble(sp.getH_debut())
                );
                break;
            case "Heure ↓":
                cmp = Comparator.comparingDouble(
                                (SpectacleSummary sp) -> Double.parseDouble(sp.getH_debut())
                        )
                        .reversed();
                break;
            default:
                cmp = Comparator.comparing(SpectacleSummary::getDateS);
        }


        Collections.sort(spectacleList, cmp);

        // 5) Mise à jour de l’adapter
        adapter.notifyDataSetChanged();
    }




}