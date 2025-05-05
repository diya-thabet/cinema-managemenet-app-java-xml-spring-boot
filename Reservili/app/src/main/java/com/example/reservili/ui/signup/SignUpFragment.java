package com.example.reservili.ui.signup;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.reservili.AuthService;
import com.example.reservili.R;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpFragment extends Fragment {

    private EditText etEmail, etPassword, etConfirmPassword, etNom, etPrenom, etPhone;
    private Button btnSignUp;
    private TextView tvLogin;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        etNom = view.findViewById(R.id.etNom);
        etPrenom = view.findViewById(R.id.etPrenom);
        etPhone = view.findViewById(R.id.etNumtel);
        btnSignUp = view.findViewById(R.id.btnSignup);
        tvLogin = view.findViewById(R.id.tvLoginLink);

        btnSignUp.setOnClickListener(v -> {
            if (!validateInputs()) return;

            // 1) Construire le SignupRequest
            SignupRequest req = new SignupRequest(



                    etEmail.getText().toString().trim(),
                    etNom.getText().toString().trim(),
                    etPassword.getText().toString().trim(),
                    etPrenom.getText().toString().trim(),
                    etPhone.getText().toString().trim()

            );

            // 2) Instancier Retrofit + AuthService
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/")  // ou votre URL
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            AuthService authService = retrofit.create(AuthService.class);

            // 3) Appeler signup
            authService.signup(req).enqueue(new Callback<Map<String, Integer>>() {
                @Override
                public void onResponse(Call<Map<String, Integer>> call,
                                       Response<Map<String, Integer>> response) {
                    if (response.code() == 201 && response.body() != null) {
                        int newId = response.body().get("clientId");
                        Toast.makeText(getContext(),
                                "Inscription réussie (id=" + newId + ")",
                                Toast.LENGTH_SHORT).show();
                        // 4) Naviguer vers LoginFragment
                        NavHostFragment.findNavController(SignUpFragment.this)
                                .navigate(R.id.action_signupFragment_to_loginFragment);

                    } else if (response.code() == 409) {
                        // compte déjà existant
                        String errorMsg = "Un compte existe déjà pour cet email";
                        if (response.errorBody() != null) {
                            try {
                                // si le back renvoie { "error": "..."}
                                JSONObject obj = new JSONObject(response.errorBody().string());
                                errorMsg = obj.optString("error", errorMsg);
                            } catch (Exception ignored){ }
                        }
                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getContext(),
                                "Erreur serveur (" + response.code() + ")", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Map<String, Integer>> call, Throwable t) {
                    Toast.makeText(getContext(),
                            "Échec réseau : " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        });

        tvLogin.setOnClickListener(v ->
                NavHostFragment.findNavController(SignUpFragment.this)
                        .navigate(R.id.action_signupFragment_to_loginFragment));
    }

    private boolean validateInputs() {
        if (TextUtils.isEmpty(etEmail.getText()) ||
                TextUtils.isEmpty(etPassword.getText()) ||
                TextUtils.isEmpty(etConfirmPassword.getText()) ||
                TextUtils.isEmpty(etNom.getText()) ||
                TextUtils.isEmpty(etPrenom.getText()) ||
                TextUtils.isEmpty(etPhone.getText())) {
            Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etPassword.getText().length() < 3) {
            Toast.makeText(getContext(), "Le mot de passe doit contenir au moins 4 caractères", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
            Toast.makeText(getContext(), "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }



}
