package com.example.reservili.ui.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.reservili.AuthService;
import com.example.reservili.R;
import com.example.reservili.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginFragment extends Fragment {

    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private SessionManager session;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        etEmail    = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin   = view.findViewById(R.id.btnLogin);
        session    = SessionManager.getInstance(requireContext());

        // Lien vers SignUp
        TextView tvSignUp = view.findViewById(R.id.tvSignUp);
        tvSignUp.setOnClickListener(v ->
                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.action_loginFragment_to_signupFragment)
        );

        btnLogin.setOnClickListener(v -> attemptLogin(v));
        return view;
    }

    private void attemptLogin(View v) {
        String email    = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validation basique
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Veuillez saisir votre email");
            etEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Veuillez saisir votre mot de passe");
            etPassword.requestFocus();
            return;
        }

        // Préparer Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AuthService authService = retrofit.create(AuthService.class);

        // Appel login
        LoginRequest req = new LoginRequest(email, password);
        authService.login(req).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call,
                                   Response<LoginResponse> response) {
                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().getClientId() > 0) {
                    // 1) Sauvegarde de la session

                    session.login(
                            response.body().getClientId(),
                            response.body().getFirstName(),
                            response.body().getLastName(),
                            response.body().getEmail(),
                            response.body().getPhone()
                    );

                    // 2) Message de succès
                    Toast.makeText(requireContext(),
                            "Connexion réussie",
                            Toast.LENGTH_SHORT).show();
                    // 3) Navigation vers Home
                    NavHostFragment.findNavController(LoginFragment.this)
                            .navigate(R.id.action_loginFragment_to_navigation_home);
                } else {
                    Toast.makeText(requireContext(),
                            "Email ou mot de passe incorrect",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(requireContext(),
                        "Erreur réseau : " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
