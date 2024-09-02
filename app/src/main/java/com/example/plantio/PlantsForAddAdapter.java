package com.example.plantio;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class PlantsForAddAdapter extends RecyclerView.Adapter<PlantsForAddAdapter.ViewHolder>{
    private static Context context;
    private ArrayList<Plant> plant_list;
    int hour;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    PlantDictionary plantDictionary = new PlantDictionary();

    public PlantsForAddAdapter(Context context, ArrayList<Plant> plant_list, int hour) {
        this.context = context;
        this.plant_list = plant_list;
        this.hour = hour;
        plantDictionary.load_data();
    }

    @NonNull
    @Override
    public PlantsForAddAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_item_card,  parent, false);
        return new PlantsForAddAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantsForAddAdapter.ViewHolder holder, int position) {
        if(hour >= 6 && hour < 17){
            holder.cardView.setBackground(context.getResources().getDrawable(R.drawable.gradient_item));
            holder.constraint.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        } else if (hour >= 17 && hour < 21) {
            holder.cardView.setBackground(context.getResources().getDrawable(R.drawable.gradient_item));
            holder.constraint.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        }
        else {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.CardColorNight));
            holder.name.setTextColor(context.getResources().getColor(R.color.white));
        }

        Plant plant = plant_list.get(position);
        holder.name.setText(plant.getName());
        holder.plant = plant;

        String path = plantDictionary.map.get(plant.getName());
        holder.path = path;
        StorageReference img_ref = storageReference.child(path);
        img_ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .into(holder.img_v);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return plant_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_v;
        TextView name;
        CardView cardView;

        Plant plant;
        String path;
        View constraint;

        ViewHolder(View view) {
            super(view);

            img_v = view.findViewById(R.id.item_img_view_for_add);
            name = view.findViewById(R.id.item_text_view_for_add);
            cardView = view.findViewById(R.id.outer_view);
            constraint = view.findViewById(R.id.constraint);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AddPlant_Activity.class);
                    intent.putExtra("water_amount", plant.getWatering_amount());
                    intent.putExtra("sun_amount", plant.getSun_amount());
                    intent.putExtra("danger", plant.getDanger());
                    intent.putExtra("about_water", plant.getAll_about_watering());
                    intent.putExtra("about_sun", plant.getAll_about_sun());
                    intent.putExtra("about_spray", plant.getAll_about_spraying());
                    intent.putExtra("about_ferlilize", plant.getAll_about_fertilizing());
                    intent.putExtra("about_earth", plant.getAll_about_earth());
                    intent.putExtra("about_transfer", plant.getAll_about_transfer());
                    intent.putExtra("about_cutting", plant.getAll_about_cutting());
                    intent.putExtra("about_danger", plant.getAll_about_danger());
                    intent.putExtra("plant_name", plant.getName());
                    intent.putExtra("path", path);
                    context.startActivity(intent);
                }
            });
        }
    }
}
