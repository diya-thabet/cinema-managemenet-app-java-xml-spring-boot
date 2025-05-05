package com.example.reservili.ui.reservation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reservili.R;
import com.example.reservili.ReservationApiService;
import com.example.reservili.SpectacleApiService;
import com.example.reservili.adapter.CrenauxListAdapter;
import com.example.reservili.adapter.RubriqueListAdapter;
import com.example.reservili.model.Crenaux;
import com.example.reservili.model.ReservationRequest;
import com.example.reservili.model.Rubrique;
import com.example.reservili.model.Spectacle;
import com.example.reservili.model.SpectacleSummary;
import com.example.reservili.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReservationFragment extends Fragment {

    private SessionManager session;
    private SpectacleSummary spectacle;
    private float unitPrice = 0;
    private int quantity  = 1;

    // UI
    private MaterialButtonToggleGroup ticketTypeGroup;
    private TextView unitPriceTextView, totalPriceTextView,specTitleTextview;
    private EditText quantityEditText;
    private Button btnMinus, btnPlus, reserveButton;
    private EditText firstNameEditText, lastNameEditText, emailEditText, phoneEditText;
    private RadioGroup paymentMethodGroup;
    private CrenauxListAdapter adaptercrn;
    private ArrayList<Crenaux> crenauxList;
    Long selectedId;
    private Crenaux selectedCrenaux;


    private ReservationApiService reservationApiService;
    private FrameLayout progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservation, container, false);

        session  = SessionManager.getInstance(requireContext());
        spectacle = getArguments() != null
                ? getArguments().getParcelable("spectacle")
                : null;

        Context ctx =this.getContext();
        // find views...
        ticketTypeGroup    = view.findViewById(R.id.ticketTypeGroup);
        unitPriceTextView  = view.findViewById(R.id.unitPriceTextView);
        totalPriceTextView = view.findViewById(R.id.totalPriceTextView);
        quantityEditText   = view.findViewById(R.id.quantityEditText);
        btnMinus           = view.findViewById(R.id.btnMinus);
        btnPlus            = view.findViewById(R.id.btnPlus);

        firstNameEditText = view.findViewById(R.id.firstNameEditText);
        lastNameEditText  = view.findViewById(R.id.lastNameEditText);
        emailEditText     = view.findViewById(R.id.emailEditText);
        phoneEditText     = view.findViewById(R.id.phoneEditText);

        paymentMethodGroup = view.findViewById(R.id.paymentMethodGroup);
        reserveButton      = view.findViewById(R.id.reserveButton);
        specTitleTextview=view.findViewById(R.id.spectacleTitleTextView);
        specTitleTextview.setText(spectacle.getTitre());

        progressBar = view.findViewById(R.id.loadingOverlay);

        // Préremplissage si connecté
        if (session.isLoggedIn()) {
            // Ces getters supposés exister dans SessionManager :
            firstNameEditText.setText(session.getFirstName());
            lastNameEditText.setText(session.getLastName());
            emailEditText.setText(session.getEmail());
            phoneEditText.setText(session.getPhone());

            // désactiver la modification
            for (EditText e : new EditText[]{ firstNameEditText, lastNameEditText, emailEditText, phoneEditText}) {
                e.setEnabled(false);
            }
        }


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();



        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("http://192.168.100.107:8080")
                //.baseUrl("http://172.18.0.149:8080")
                .baseUrl("http://10.0.2.2:8080") // Use 10.0.2.2 to access localhost from emulator
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        reservationApiService = retrofit.create(ReservationApiService.class);



        // Ticket price mapping
        ticketTypeGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) return;
            if (selectedCrenaux == null) {
                Toast.makeText(requireContext(),
                        "Réservation enregistrée !", Toast.LENGTH_SHORT).show();
                group.clearChecked();
                return;
            }
            // on ne peut plus faire switch(checkedId) avec des R.id non-final
            if (checkedId == R.id.toggleBalcon) {
                unitPrice = selectedCrenaux.getPrixbalcon();
            } else if (checkedId == R.id.toggleOrchestre) {
                unitPrice = selectedCrenaux.getPrixorchestre();
            } else if (checkedId == R.id.toggleGalerie) {
                unitPrice = selectedCrenaux.getPrixgalerie();
            }
            unitPriceTextView.setText("Prix unitaire : " + unitPrice + " DT");
            recalcTotal();
        });



        // quantity buttons
        btnMinus.setOnClickListener(v -> {
            try {
                quantity = Math.max(1,
                        Integer.parseInt(quantityEditText.getText().toString()));
            } catch (Exception ignored) { quantity = 1; }
            if (quantity > 1) quantity--;
            quantityEditText.setText(String.valueOf(quantity));
            recalcTotal();
        });
        btnPlus.setOnClickListener(v -> {
            try {
                quantity = Integer.parseInt(quantityEditText.getText().toString());
            } catch (Exception ignored) { quantity = 1; }
            quantity++;
            quantityEditText.setText(String.valueOf(quantity));
            recalcTotal();
        });



        reserveButton.setOnClickListener(v -> {
            if (selectedId == null) {
                Toast.makeText(requireContext(),
                        "Réservation enregistrée !",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            // 2) Catégorie de ticket sélectionnée ?
            int checkedTicketId = ticketTypeGroup.getCheckedButtonId();
            if (checkedTicketId == View.NO_ID) {
                Toast.makeText(requireContext(),
                        "Veuillez choisir un type de ticket",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            String categorie = ((MaterialButton) ticketTypeGroup.findViewById(checkedTicketId))
                    .getText().toString();

            // 3) Mode de paiement choisi ?
            int checkedPayment = paymentMethodGroup.getCheckedRadioButtonId();
            if (checkedPayment == -1) {
                Toast.makeText(requireContext(),
                        "Veuillez choisir un mode de paiement",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            String payment = (checkedPayment == R.id.radioCard) ? "Carte Bancaire" : "Solde Telephonique";

            // 4) Quantité valide ?
            String qtyStr = quantityEditText.getText().toString().trim();
            if (qtyStr.isEmpty()) {
                Toast.makeText(requireContext(),
                        "Veuillez saisir la quantité",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            int quantity;
            try {
                quantity = Integer.parseInt(qtyStr);
                if (quantity < 1) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(),
                        "Quantité invalide", Toast.LENGTH_SHORT).show();
                return;
            }

            float prixPaye = unitPrice * quantity;

            // 5) Si invité, vérifier ses infos personnelles non vides
            if (!session.isLoggedIn()) {
                if (firstNameEditText.getText().toString().trim().isEmpty()) {
                    firstNameEditText.setError("Prénom requis");
                    firstNameEditText.requestFocus();
                    return;
                }
                if (lastNameEditText.getText().toString().trim().isEmpty()) {
                    lastNameEditText.setError("Nom requis");
                    lastNameEditText.requestFocus();
                    return;
                }
                String mail = emailEditText.getText().toString().trim();
                if (mail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                    emailEditText.setError("Email invalide");
                    emailEditText.requestFocus();
                    return;
                }
                if (phoneEditText.getText().toString().trim().isEmpty()) {
                    phoneEditText.setError("Téléphone requis");
                    phoneEditText.requestFocus();
                    return;
                }
            }


            // 3) Confirmation avant envoi
            new AlertDialog.Builder(requireContext())
                    .setTitle("Confirmation")
                    .setMessage("Voulez-vous vraiment confirmer votre réservation ?")
                    .setPositiveButton("Confirmer", (dialog, which) -> {
                        progressBar.setVisibility(View.VISIBLE);
                        ReservationRequest req;
                        if (session.isLoggedIn()) {
                            // --- Utilisateur déjà connu, on n'envoie que idClt ---
                            req = new ReservationRequest(
                                    /* idClt */           session.getClientId(),
                                    /* idSpec */          spectacle.getIdSpec(),
                                    /* idDateLieu */      selectedId,
                                    /* categorieTckt */   categorie,
                                    /* qte */             quantity,
                                    /* prixpaye */        prixPaye,
                                    /* paymentMethod */   payment,
                                    /* email */           emailEditText.getText().toString().trim(),
                                                          lastNameEditText.getText().toString().trim(),
                                    /* prenomClt */       firstNameEditText.getText().toString().trim()

                            );
                        } else {
                            // --- Invité, on crée d'abord le client en fournissant nom/prenom/tel/email ---
                            req = new ReservationRequest(
                                    /* nomClt */          lastNameEditText.getText().toString().trim(),
                                    /* prenomClt */       firstNameEditText.getText().toString().trim(),
                                    /* tel */             phoneEditText.getText().toString().trim(),
                                    /* email */           emailEditText.getText().toString().trim(),
                                    /* idSpec */          spectacle.getIdSpec(),
                                    /* idDateLieu */      selectedId,
                                    /* categorieTckt */   categorie,
                                    /* qte */             quantity,
                                    /* prixpaye */        prixPaye,
                                    /* paymentMethod */   payment
                            );
                        }

                        // 4) On appelle l’API
                        reservationApiService.createReservation(req)
                                .enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> c, Response<ResponseBody> r) {
                                        progressBar.setVisibility(View.GONE);
                                        if (!r.isSuccessful()) {
                                            try {
                                                String error = r.errorBody().string();
                                                Log.e("API_ERROR", "Erreur: " + error);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            Toast.makeText(requireContext(),
                                                    "Erreur serveur, veuillez réessayer", Toast.LENGTH_LONG).show();
                                            return;
                                        }

                                        if (r.isSuccessful()) {
                                            Toast.makeText(requireContext(),
                                                            "Réservation enregistrée !", Toast.LENGTH_LONG)
                                                    .show();
                                            // retour au fragment précédent
                                            Navigation.findNavController(view).popBackStack();
                                        } else {
                                            Toast.makeText(requireContext(),
                                                            "Erreur serveur, veuillez réessayer", Toast.LENGTH_LONG)
                                                    .show();
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<ResponseBody> c, Throwable t) {
                                        progressBar.setVisibility(View.GONE);
                                        Log.e("ReservationError", "Erreur : " + t.getMessage(), t);
                                        Toast.makeText(requireContext(),
                                                        "Échec réseau : " + t.getMessage(), Toast.LENGTH_LONG)
                                                .show();

                                    }
                                });
                    })
                    .setNegativeButton("Annuler", null)
                    .show();
        });







        SpectacleApiService apiService = retrofit.create(SpectacleApiService.class);

        crenauxList=new ArrayList<>();


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

                selectedId = crn.getId();
                selectedCrenaux = crn;

            }
        },true);





        RecyclerView crenauxRecyclerView = view.findViewById(R.id.crenauxRecyclerView);
        crenauxRecyclerView .setLayoutManager(new LinearLayoutManager(getContext()));

        crenauxRecyclerView.setAdapter(adaptercrn);
        Call<Spectacle> call = apiService.getSpectacleD(spectacle.getIdSpec());
        call.enqueue(new Callback<Spectacle>() {
            @Override
            public void onResponse(Call<Spectacle> call, Response<Spectacle> response) {
                if (response.isSuccessful() && response.body() != null) {

                    crenauxList.clear();
                    crenauxList.addAll(response.body().getCrenaux());

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











        return view;
    }

    private void recalcTotal() {
        totalPriceTextView.setText(
                "Total : " + (unitPrice * quantity) + " DT"
        );
    }
}
