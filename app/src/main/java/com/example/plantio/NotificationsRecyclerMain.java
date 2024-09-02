package com.example.plantio;

import android.content.Context;
import android.net.Uri;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;

public class NotificationsRecyclerMain extends RecyclerView.Adapter<NotificationsRecyclerMain.ViewHolder>{
    private static Context context;
    private ArrayList<Notification> notifications_list;
    private ArrayList<String> names;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    PlantDictionary plantDictionary = new PlantDictionary();
    int Hour;

    public NotificationsRecyclerMain(Context context, ArrayList<Notification> notifications_list, ArrayList<String> names, int Hour) {
        this.context = context;
        this.notifications_list = notifications_list;
        this.names = names;
        this.Hour = Hour;
        plantDictionary.load_data();
    }

    @NonNull
    @Override
    public NotificationsRecyclerMain.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notif_item_main,  parent, false);
        return new NotificationsRecyclerMain.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsRecyclerMain.ViewHolder holder, int position) {
        if(Hour >= 6 && Hour < 17){
            holder.cardView.setBackground(context.getResources().getDrawable(R.drawable.gradient_item));
            holder.constraint.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        } else if (Hour >= 17 && Hour < 21) {
            holder.cardView.setBackground(context.getResources().getDrawable(R.drawable.gradient_item));
            holder.constraint.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        }
        else {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.CardColorNight));
            holder.action.setTextColor(context.getResources().getColor(R.color.white));
            holder.day_time.setTextColor(context.getResources().getColor(R.color.white));
        }

        Notification notification = notifications_list.get(position);
        holder.action.setText(names.get(position) + ": " + notification.getName());
        String one = notification.getWeek_day() + " в " + notification.getTime();
        holder.day_time.setText(one);

        String path = plantDictionary.map.get(notification.getName());

        StorageReference img_ref = storageReference.child(path);
        img_ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .into(holder.img);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return notifications_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView action, day_time;
        CardView cardView;
        View constraint;
        ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_v);
            action = itemView.findViewById(R.id.action);
            day_time = itemView.findViewById(R.id.day_time);
            cardView = itemView.findViewById(R.id.outer_view);
            constraint = itemView.findViewById(R.id.constraint);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
