package com.example.reservili.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.reservili.R;
import com.example.reservili.utils.SessionManager;

public class ProfileFragment extends Fragment {

    private SessionManager session;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        session = SessionManager.getInstance(requireContext());

        ImageView ivAvatar   = view.findViewById(R.id.ivAvatar);
        TextView tvFullName  = view.findViewById(R.id.tvFullName);
        TextView tvEmail     = view.findViewById(R.id.tvEmail);
        Button btnLogout     = view.findViewById(R.id.btnLogout);

        // TODO : récupérer les vraies infos (nom, email) de la session ou d'une API
        // Exemple temporaire : on lit l'email de la session
        int clientId = session.getClientId();
        // Vous pourriez avoir stocké nom+prenom dans SharedPreferences, ou faire un appel réseau ici


        String name=session.getFirstName()+" "+session.getLastName();
        tvFullName.setText(name);
        tvEmail.setText(session.getEmail());

        btnLogout.setOnClickListener(v -> {
            session.logout();
            // Retour au home non-connecté
            Navigation.findNavController(requireView())
                    .navigate(R.id.navigation_home);
        });

        return view;
    }
}
