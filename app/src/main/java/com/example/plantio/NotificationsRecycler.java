package com.example.plantio;
import android.content.Context;
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

public class NotificationsRecycler extends RecyclerView.Adapter<NotificationsRecycler.ViewHolder>{

    private static Context context;
    private ArrayList<Notification> notifications_list;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    PlantDictionary plantDictionary = new PlantDictionary();
    int hour;

    public NotificationsRecycler(Context context, ArrayList<Notification> notifications_list, int hour) {
        this.context = context;
        this.notifications_list = notifications_list;
        this.hour = hour;
        plantDictionary.load_data();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_item,  parent, false);
        return new NotificationsRecycler.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(hour >= 6 && hour < 17){
            holder.cardView.setBackground(context.getResources().getDrawable(R.drawable.gradient_item));
            holder.constraint.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        } else if (hour >= 17 && hour < 21) {
            holder.cardView.setBackground(context.getResources().getDrawable(R.drawable.gradient_item));
            holder.constraint.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        }
        else {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.CardColorNight));
            holder.action.setTextColor(context.getResources().getColor(R.color.white));
            holder.day_time.setTextColor(context.getResources().getColor(R.color.white));
        }

        Notification notification = notifications_list.get(position);
        holder.action.setText(notification.getName());
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
