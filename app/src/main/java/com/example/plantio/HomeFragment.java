package com.example.plantio;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class HomeFragment extends Fragment {

    TextView text_name, text2, advise_tv;
    Button btn , btn_to_plants;
    SharedPreferences sharedPreferences;
    CardView card;
    ImageView iv;
    int name_text_color;
    private AnimationDrawable animationDrawable;
    private ImageView mProgressBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        text_name = view.findViewById(R.id.text_name);
        text2 = view.findViewById(R.id.text2);
        advise_tv = view.findViewById(R.id.advise_tv);
        btn = view.findViewById(R.id.btn_plants);
        card = view.findViewById(R.id.card);
        btn_to_plants = view.findViewById(R.id.btn_goto_plants);
        iv = view.findViewById(R.id.logo_view);
        mProgressBar = view.findViewById(R.id.main_progress);

        mProgressBar.setBackgroundResource(R.drawable.animation_progree);
        animationDrawable = (AnimationDrawable)mProgressBar.getBackground();


        if(getCurrentHour() >= 6 && getCurrentHour() < 17){
            text_name.setTextColor(getResources().getColor(R.color.black));
            text2.setTextColor(getResources().getColor(R.color.black));
            btn.setBackgroundColor(getResources().getColor(R.color.ColorButtonDay));
            advise_tv.setTextColor(getResources().getColor(R.color.black));
            name_text_color = getResources().getColor(R.color.NameMorningColor);
            card.setCardBackgroundColor(getResources().getColor(R.color.CardColorMorning));
            btn_to_plants.setBackgroundColor(getResources().getColor(R.color.ColorButtonDay));

        } else if (getCurrentHour() >= 17 && getCurrentHour() < 21) {
            text_name.setTextColor(getResources().getColor(R.color.black));
            text2.setTextColor(getResources().getColor(R.color.black));
            btn.setBackgroundColor(getResources().getColor(R.color.EveningWhite));
            advise_tv.setTextColor(getResources().getColor(R.color.black));
            name_text_color = getResources().getColor(R.color.NameEveningColor);
            card.setCardBackgroundColor(getResources().getColor(R.color.CardColorEvening));
            btn_to_plants.setBackgroundColor(getResources().getColor(R.color.EveningWhite));

        }
        else {
            text_name.setTextColor(getResources().getColor(R.color.white));
            text2.setTextColor(getResources().getColor(R.color.white));
            btn.setBackgroundColor(getResources().getColor(R.color.CardButtonColorNight));
            advise_tv.setTextColor(getResources().getColor(R.color.white));
            name_text_color = getResources().getColor(R.color.CardButtonColorNight);
            card.setCardBackgroundColor(getResources().getColor(R.color.CardColorNight));
            advise_tv.setTextColor(getResources().getColor(R.color.white));
            btn_to_plants.setBackgroundColor(getResources().getColor(R.color.CardButtonColorNight));
            iv.setImageDrawable(getResources().getDrawable(R.drawable.without_text_small_logo_night));



        }
        animationDrawable.start();
        sharedPreferences = this.getActivity().getSharedPreferences("SETTINGS", MODE_PRIVATE);
        if(!sharedPreferences.getString("Name", "Name").equals("Name")){

            String name = sharedPreferences.getString("Name", "Name");
            int name_length = name.length();

            Spannable text = new SpannableString(text_name.getText().toString() + " " + name + "!");
            text.setSpan(new ForegroundColorSpan(name_text_color),
                    7, 8 + name_length,  1);
            text_name.setText(text, TextView.BufferType.SPANNABLE);
            animationDrawable.stop();
            mProgressBar.setVisibility(View.GONE);
        }


        btn_to_plants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PlantsForAddActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainUserWindow.class);
                intent.putExtra("mode", "myplants");
                view.getContext().startActivity(intent);
            }
        });

        return view;

    }

    private int getCurrentHour(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

}