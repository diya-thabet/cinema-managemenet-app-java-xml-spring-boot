package com.example.reservili;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.reservili.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.reservili.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private BottomNavigationView navView;
    private SessionManager session;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        session = SessionManager.getInstance(this);

        // 4) on récupère navView **dans le champ**, pas en local**
        navView = binding.navView;



        navController = Navigation.findNavController(
                this, R.id.nav_host_fragment_activity_main
        );
        navController.addOnDestinationChangedListener((controller, destination, args) -> {
            // 1) Mise à jour de l’item Réservations
            navView.getMenu()
                    .findItem(R.id.navigation_reservations)
                    .setVisible(session.isLoggedIn());

            // 2) Forcer le rechargement de l’ActionBar (avatar)
            invalidateOptionsMenu();
        });

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_reservations)
                .build();
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
       NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
       // NavigationUI.setupWithNavController(binding.navView, navController);






        navView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            NavController navController =
                    Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

            if (id == R.id.navigation_home) {
                navController.popBackStack(R.id.navigation_home, false);
                navController.navigate(R.id.navigation_home);
                return true;

            } else if (id == R.id.navigation_reservations) {
                navController.popBackStack(R.id.navigation_reservations, false);
                navController.navigate(R.id.navigation_reservations);
                return true;

            } else if (id == R.id.navigation_profile) {
                if (session.isLoggedIn()) {
                    navController.popBackStack(R.id.profileFragment, false);
                    navController.navigate(R.id.profileFragment);
                } else {
                    navController.popBackStack(R.id.loginFragment, false);
                    navController.navigate(R.id.loginFragment);
                }
                return true;
            }

            return false;
        });





        Menu menu = navView.getMenu();
        menu.findItem(R.id.navigation_reservations)
                .setVisible(session.isLoggedIn());
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Re-vérifier après un éventuel login/logout
        navView.getMenu()
                .findItem(R.id.navigation_reservations)
                .setVisible(session.isLoggedIn());
        // On réaffiche ou masque aussi l’icône utilisateur (voir plus bas)
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Récupère la vue custom
        MenuItem userItem = menu.findItem(R.id.action_user);
        View actionView = userItem.getActionView();
        ImageView ivUser = actionView.findViewById(R.id.ivUserAvatar);


        // Affiche-le si connecté
        ivUser.setVisibility(session.isLoggedIn() ? View.VISIBLE : View.GONE);

        // Click → popup
        ivUser.setOnClickListener(v -> showUserPopup(v));

        return true;
    }


    private PopupWindow userPopup;

    private void showUserPopup(View anchor) {
        if (userPopup != null && userPopup.isShowing()) {
            userPopup.dismiss();
            return;
        }

        View popupView = getLayoutInflater().inflate(R.layout.popup_user, null);
        userPopup = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);

        TextView tvName = popupView.findViewById(R.id.tvUserName);
        String name=session.getFirstName()+" "+session.getLastName();
        tvName.setText(name);
        Button btnProfile = popupView.findViewById(R.id.btnProfile);
        Button btnLogout  = popupView.findViewById(R.id.btnLogout);

        // Remplir le nom depuis SessionManager ou un DAO
        int clientId = session.getClientId();
        // TODO: récupérer le nom/prénom depuis un stockage local ou API


        btnProfile.setOnClickListener(v -> {
            userPopup.dismiss();
            // Naviguer vers Fragment Profile
            Navigation.findNavController(findViewById(R.id.nav_host_fragment_activity_main))
                    .navigate(R.id.profileFragment);
        });

        btnLogout.setOnClickListener(v -> {
            userPopup.dismiss();
            session.logout();
            // Raffraîchir UI : cacher réservations, avatar
            navView.getMenu().findItem(R.id.navigation_reservations).setVisible(false);
            invalidateOptionsMenu();
            // Et retourner au Home
            Navigation.findNavController(findViewById(R.id.nav_host_fragment_activity_main))
                    .navigate(R.id.navigation_home);
        });

        userPopup.setOutsideTouchable(true);
        userPopup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        userPopup.showAsDropDown(anchor);
    }















}