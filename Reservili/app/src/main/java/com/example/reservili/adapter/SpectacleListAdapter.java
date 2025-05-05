package com.example.reservili.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.reservili.R;
import com.example.reservili.model.Spectacle;
import com.example.reservili.model.SpectacleSummary;

import java.util.LinkedList;

public class SpectacleListAdapter extends RecyclerView.Adapter<SpectacleListAdapter.SpectacleViewHolder> {

    private LinkedList<SpectacleSummary> spectacleList;
    private OnSpectacleClickListener listener;

    public interface OnSpectacleClickListener {
        void onSpectacleClick(SpectacleSummary spectacle);
        void onReserveClick(SpectacleSummary spectacle);
    }

    public SpectacleListAdapter(LinkedList<SpectacleSummary> spectacleList, OnSpectacleClickListener listener) {
        this.spectacleList = spectacleList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SpectacleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.spectaclelist_item, parent, false); // Assure-toi d’avoir ce layout
        return new SpectacleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SpectacleViewHolder holder, int position) {
        SpectacleSummary spectacle = spectacleList.get(position);
        holder.titleText.setText(spectacle.getTitre());
        String t="A Partir du "+spectacle.getDateS();
        holder.dateText.setText(t);
        String heure="A  "+spectacle.getH_debut()+"H";
        holder.heureText.setText(heure);
        Log.d("SpectacleAdapter", "nomLieu: " + spectacle.getNomLieu());
        t="Au "+spectacle.getNomLieu();
        holder.lieuText.setText(t);
        holder.villeText.setText(spectacle.getVille());
        Log.d("IMAGE_URL", "Loading image: " + spectacle.getImageUrl());
        // Charger l’image si tu veux (si imageUrl est une URL ou un chemin local)
        Glide.with(holder.itemView.getContext()).load(spectacle.getImageUrl()).into(holder.imageView);
        boolean isFull = spectacle.getNbrSpect() >= spectacle.getCapacite();
        holder.reserveButton.setEnabled(!isFull);
        holder.tvComplet.setVisibility(isFull ? View.VISIBLE : View.GONE);

        holder.reserveButton.setOnClickListener(v -> listener.onReserveClick(spectacle));
        holder.itemView.setOnClickListener(v -> listener.onSpectacleClick(spectacle));
    }

    @Override
    public int getItemCount() {
        return spectacleList.size();
    }

    public static class SpectacleViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, dateText,heureText,lieuText,villeText,tvComplet;
        ImageView imageView;
        Button reserveButton;


        public SpectacleViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titreSpectacle);
            dateText = itemView.findViewById(R.id.dateSpectacle);
            heureText=itemView.findViewById(R.id.heureS);
            lieuText=itemView.findViewById(R.id.nomlieu);
            villeText=itemView.findViewById(R.id.ville);
            imageView = itemView.findViewById(R.id.imageSpectacle); // si tu as une image
            reserveButton = itemView.findViewById(R.id.btnReserver);
            tvComplet      = itemView.findViewById(R.id.tvComplet);
        }
    }
    public void updateList(LinkedList<SpectacleSummary> newList) {
        this.spectacleList = new LinkedList<>(newList); // Assure la mise à jour
        notifyDataSetChanged();
    }
}