package com.example.plantio;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class Plant_Statistics extends Fragment {

    private final String plant_id;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference mood_ref = db.collection("Plant_Mood_Statistics");
    BarChart barChart;
    ArrayList<BarEntry> best = new ArrayList<BarEntry>();
    ArrayList<BarEntry> neutral_l = new ArrayList<BarEntry>();
    ArrayList<BarEntry> bad_l = new ArrayList<BarEntry>();
    private AnimationDrawable animationDrawable;
    private ImageView mProgressBar;
    Plant_Statistics(String plant_id){
        this.plant_id = plant_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plant__statistics, container, false);
        barChart = view.findViewById(R.id.bar_chart);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(50);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(true);
        barChart.setGridBackgroundColor(R.color.transparent);
        barChart.getXAxis().setTextSize(10f);
        barChart.setBackgroundColor(getResources().getColor(R.color.WhiteTransparrent));

        mProgressBar = view.findViewById(R.id.main_progress);
        mProgressBar.setBackgroundResource(R.drawable.animation_progree);
        animationDrawable = (AnimationDrawable)mProgressBar.getBackground();




        LoadDataIntoBarChart();


        return view;
    }

    private void LoadDataIntoBarChart(){

        mProgressBar.setVisibility(View.VISIBLE);
        animationDrawable.start();
        mood_ref.whereEqualTo("plant_id", plant_id)
        .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int positive = 0;
                int neutral = 0;
                int bad = 0;
                for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                    if(documentSnapshot.get("mood").toString().equals("Отлично")){
                        positive += 1;
                    }
                    else if(documentSnapshot.get("mood").toString().equals("Нормально")){
                        neutral += 1;
                    }
                    else{
                        bad += 1;
                    }

                    best.add(new BarEntry(2, positive));
                    neutral_l.add(new BarEntry(3, neutral));
                    bad_l.add(new BarEntry(4, bad));

                    BarDataSet barDataSet_good = new BarDataSet(best, "Хорошо");
                    barDataSet_good.setColors(getResources().getColor(R.color.BarGreen));
                    barDataSet_good.setValueTextSize(13f);

                    BarDataSet bar_n = new BarDataSet(neutral_l, "Нормально");
                    bar_n.setColors(getResources().getColor(R.color.BarYellow));
                    bar_n.setValueTextSize(13f);

                    BarDataSet bar_b = new BarDataSet(bad_l, "Плохо");
                    bar_b.setColors(getResources().getColor(R.color.BarRed));
                    bar_b.setValueTextSize(13f);

                    BarData barData = new BarData();
                    barData.addDataSet(barDataSet_good);
                    barData.addDataSet(bar_n);
                    barData.addDataSet(bar_b);
                    barData.setBarWidth(0.9f);

                    barChart.setData(barData);
                    animationDrawable.stop();
                    mProgressBar.setVisibility(View.GONE);

                }


            }

        });

    }

}