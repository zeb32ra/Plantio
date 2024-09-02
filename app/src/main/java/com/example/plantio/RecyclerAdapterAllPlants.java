package com.example.plantio;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class RecyclerAdapterAllPlants extends RecyclerView.Adapter<RecyclerAdapterAllPlants.ViewHolder> {

    private static Context context;
    private ArrayList<UserPlant> plant_list;

    private ArrayList<String> plants_id;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    PlantDictionary plantDictionary = new PlantDictionary();
    int hour;

    public RecyclerAdapterAllPlants(Context context, ArrayList<UserPlant> plant_list, ArrayList<String> plants_id, int hour){
        this.context = context;
        this.plant_list = plant_list;
        plantDictionary.load_data();
        this.plants_id = plants_id;
        this.hour = hour;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card,  parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(hour >= 6 && hour < 17){
            holder.cardView.setBackground(context.getResources().getDrawable(R.drawable.gradient_item));
            holder.constraint.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        } else if (hour >= 17 && hour < 21) {
            holder.cardView.setBackground(context.getResources().getDrawable(R.drawable.gradient_item));
            holder.constraint.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            holder.name.setTextColor(context.getResources().getColor(R.color.black));
            holder.watering.setTextColor(context.getResources().getColor(R.color.Grey));
        }
        else {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.CardColorNight));
            holder.name.setTextColor(context.getResources().getColor(R.color.white));
            holder.watering.setTextColor(context.getResources().getColor(R.color.white));
        }
        UserPlant plant = plant_list.get(position);
        holder.plant_name = plant.getPlant_name();
        holder.name.setText(plant.getPlant_name());
        holder.watering.setText("Последний полив: " + plant.getLast_watering_date());
        holder.plant_id = plants_id.get(position);

        String path = plantDictionary.map.get(plant.getPlant_type());
        holder.img_path = path;
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


    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img_v;
        TextView name;
        TextView watering;
        String plant_id;
        UserPlant userPlant;
        String img_path;
        CardView cardView;
        View constraint;

        String plant_name;

        ViewHolder(View view){
            super(view);

            img_v = view.findViewById(R.id.item_img_view);
            name = view.findViewById(R.id.item_text_view);
            watering = view.findViewById(R.id.item_text_view_2);
            cardView = view.findViewById(R.id.outer_view);
            constraint = view.findViewById(R.id.constraint);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, UserPlantDetailActivity.class);
                    intent.putExtra("id", plant_id);
                    intent.putExtra("path", img_path);
                    intent.putExtra("name", plant_name);
                    context.startActivity(intent);
                }
            });
        }
    }
}








