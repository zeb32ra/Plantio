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

public class JournalRecycler extends RecyclerView.Adapter<JournalRecycler.ViewHolder> {

    private final Context context;
    private final ArrayList<JournalNote> journal_list;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    PlantDictionary plantDictionary = new PlantDictionary();
    int hour;

    public JournalRecycler(Context context, ArrayList<JournalNote> journal_list, int hour) {
        this.context = context;
        this.journal_list = journal_list;
        this.hour = hour;
        plantDictionary.load_data();
    }

    @NonNull
    @Override
    public JournalRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.journal_item,  parent, false);
        return new JournalRecycler.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalRecycler.ViewHolder holder, int position) {
        if(hour >= 6 && hour < 17){
            holder.cardView.setBackground(context.getResources().getDrawable(R.drawable.gradient_item));
            holder.constraint.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        } else if (hour >= 17 && hour < 21) {
            holder.cardView.setBackground(context.getResources().getDrawable(R.drawable.gradient_item));
            holder.constraint.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        }
        else {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.CardColorNight));
            holder.tv_action_date.setTextColor(context.getResources().getColor(R.color.white));
            holder.tv_comment.setTextColor(context.getResources().getColor(R.color.white));
        }

        JournalNote journalNote = journal_list.get(position);
        holder.tv_action_date.setText(journalNote.getAction() + " " + journalNote.getDate());
        holder.tv_comment.setText(journalNote.getUser_commet());

        String path = plantDictionary.map.get(journalNote.getAction());

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
        return journal_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img_v;
        TextView tv_action_date, tv_comment;
        CardView cardView;
        View constraint;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_v = itemView.findViewById(R.id.item_img_view);
            tv_action_date = itemView.findViewById(R.id.item_text_view);
            tv_comment = itemView.findViewById(R.id.item_text_view_2);
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
