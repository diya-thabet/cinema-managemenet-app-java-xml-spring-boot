package com.example.reservili.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reservili.R;
import com.example.reservili.model.Crenaux;
import com.example.reservili.model.Rubrique;
import com.example.reservili.model.SpectacleSummary;

import java.util.ArrayList;
import java.util.List;

public class CrenauxListAdapter extends RecyclerView.Adapter<CrenauxListAdapter.CrenauxViewHolder>{

    private ArrayList<Crenaux> crenauxList;

    private final boolean enableSelection;
    private OnCrenauxClickListener listener;
    // **Position sélectionnée** (aucune au départ)
    private int selectedPosition = RecyclerView.NO_POSITION;
    public interface OnCrenauxClickListener {
        void onCrenauxSelected(Crenaux spectacle);
        void onMapClick(Crenaux spectacle);
    }


    // **Nouveau constructeur** avec flag
    public CrenauxListAdapter(ArrayList<Crenaux> crenauxList,
                              OnCrenauxClickListener listener,
                              boolean enableSelection) {
        this.crenauxList     = crenauxList;
        this.listener        = listener;
        this.enableSelection = enableSelection;
    }

    @NonNull
    @Override
    public CrenauxListAdapter.CrenauxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_crenaux, parent, false);
        return new CrenauxListAdapter.CrenauxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CrenauxListAdapter.CrenauxViewHolder holder, int position) {
        Crenaux crn = crenauxList.get(position);
        holder. DateTextView .setText(crn.getDatecrn());

        holder.nomLieuTextView.setText(crn.getNomlieu());
        holder.adresseTextView.setText(crn.getAdrss());
        holder.villeView.setText(crn.getVille());
        holder.mapView.setOnClickListener(v -> listener.onMapClick(crn));

        // 1) Afficher ou non la surcouche + tick
        if (enableSelection) {
            boolean isSelected = position == selectedPosition;
            holder.selectionOverlay.setVisibility(isSelected ? View.VISIBLE : View.GONE);
            holder.selectionTick .  setVisibility(isSelected ? View.VISIBLE : View.GONE);

            holder.itemView.setOnClickListener(v -> {
                int old = selectedPosition;
                selectedPosition = position;
                if (old != RecyclerView.NO_POSITION)
                    notifyItemChanged(old);
                notifyItemChanged(selectedPosition);
                listener.onCrenauxSelected(crn);
                notifyDataSetChanged();
            });
        } else {
            // si désactivé → tout masqué et pas de click de sélection
            holder.selectionOverlay.setVisibility(View.GONE);
            holder.selectionTick .setVisibility(View.GONE);
            holder.itemView.setOnClickListener(null);
        }




    }

    @Override
    public int getItemCount() {
        return crenauxList.size();
    }

    /** Si besoin, expose l’id du créneau sélectionné */
    public Long getSelectedCrenauxId() {
        return selectedPosition != RecyclerView.NO_POSITION
                ? crenauxList.get(selectedPosition).getId()
                : null;
    }

    public static class CrenauxViewHolder extends RecyclerView.ViewHolder {
        TextView DateTextView, nomLieuTextView, adresseTextView, villeView ;
        ImageView mapView, selectionTick;
        View      selectionOverlay;

        public CrenauxViewHolder(@NonNull View itemView) {
            super(itemView);
            DateTextView = itemView.findViewById(R.id.tvDateCrenau);
            nomLieuTextView = itemView.findViewById(R.id.tvNomLieu);
           adresseTextView = itemView.findViewById(R.id.tvAdresse);
            villeView = itemView.findViewById(R.id.tvVille);
            mapView = itemView.findViewById(R.id.ivLocation);
            selectionOverlay  = itemView.findViewById(R.id.selectionOverlay);
            selectionTick     = itemView.findViewById(R.id.selectionTick);
        }
    }















}
