package com.example.plantio;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class Journal_fragment extends Fragment {

    private String plant_id;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference journal_ref = db.collection("Plant_journal");
    RecyclerView recyclerView;
    private AnimationDrawable animationDrawable;
    private ImageView mProgressBar;
    Journal_fragment(String plant_id){
        this.plant_id = plant_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journal_fragment, container, false);
        recyclerView = view.findViewById(R.id.recycler);

        mProgressBar = view.findViewById(R.id.main_progress);
        mProgressBar.setBackgroundResource(R.drawable.animation_progree);
        animationDrawable = (AnimationDrawable)mProgressBar.getBackground();

        LoadDataIntoRecycler();


        return view;
    }

    private void LoadDataIntoRecycler(){
        ArrayList<JournalNote> journalArrayList = new ArrayList<JournalNote>();

        animationDrawable.start();
        journal_ref.whereEqualTo("plant_id", plant_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                JournalNote journalNote = new JournalNote(
                                        documentSnapshot.get("action").toString(),
                                        documentSnapshot.get("date").toString(),
                                        documentSnapshot.get("plant_id").toString(),
                                        documentSnapshot.get("user_commet").toString(),
                                        getTime()
                                );

                                journalArrayList.add(journalNote);
                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setAdapter(new JournalRecycler(getContext(), journalArrayList, getCurrentHour()));
                            animationDrawable.stop();
                            mProgressBar.setVisibility(View.GONE);
                        }
                        else{
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private String getTime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat time_formatter = new SimpleDateFormat("HH:mm");
        String time;

        time = time_formatter.format(calendar.getTime());
        return time;
    }

    private int getCurrentHour(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
}