package com.example.reservili.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.reservili.R;
import com.example.reservili.model.Rubrique;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RubriqueListAdapter extends RecyclerView.Adapter<RubriqueListAdapter.RubriqueViewHolder> {

    private ArrayList<Rubrique> rubriqueList;

    public RubriqueListAdapter(ArrayList<Rubrique> rubriqueList) {
        this.rubriqueList = rubriqueList;
    }

    @NonNull
    @Override
    public RubriqueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rubrique, parent, false);
        return new RubriqueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RubriqueViewHolder holder, int position) {
        Rubrique rubrique = rubriqueList.get(position);

        String res=rubrique.getPnomart()+" "+rubrique.getNomart();
        holder.nomArtTextView.setText(res);
        res="Debut a "+rubrique.getH_debutR()+"H";
        holder.startTimeTextView.setText(res);
        res="Duree de "+rubrique.getDureeR()+"H";
        holder.durationTextView.setText(res);
        holder.typeTextView.setText(rubrique.getTypeR());
    }

    @Override
    public int getItemCount() {
        return rubriqueList.size();
    }

    public static class RubriqueViewHolder extends RecyclerView.ViewHolder {
        TextView nomArtTextView, startTimeTextView, durationTextView, typeTextView;

        public RubriqueViewHolder(@NonNull View itemView) {
            super(itemView);
            nomArtTextView = itemView.findViewById(R.id.textViewNomArt);
            startTimeTextView = itemView.findViewById(R.id.textViewStartTime);
            durationTextView = itemView.findViewById(R.id.textViewDuration);
            typeTextView = itemView.findViewById(R.id.textViewType);
        }
    }
}
