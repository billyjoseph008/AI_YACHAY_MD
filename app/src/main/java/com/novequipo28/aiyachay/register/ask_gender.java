package com.novequipo28.aiyachay.register;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.novequipo28.aiyachay.R;
import com.novequipo28.aiyachay.Register;

public class ask_gender extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button continue_btn;
    ImageButton female_btn;
    ImageButton male_btn;

    boolean leftGenderSelected;
    String gender = "";

    public ask_gender() {

    }

    public static ask_gender newInstance(String param1, String param2) {
        ask_gender fragment = new ask_gender();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ask_genero, container, false);

        female_btn = view.findViewById(R.id.female_btn);
        male_btn = view.findViewById(R.id.male_btn);

        male_btn.setBackgroundColor(getResources().getColor(R.color.colorComplementary));
        female_btn.setBackgroundColor(getResources().getColor(R.color.transparent));
        leftGenderSelected = true;
        gender = "male";

        female_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(leftGenderSelected){
                    female_btn.setBackgroundColor(getResources().getColor(R.color.colorComplementary));
                    male_btn.setBackgroundColor(getResources().getColor(R.color.transparent));
                    leftGenderSelected = false;
                    gender = "female";
                }

            }
        });

        male_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!leftGenderSelected){
                    male_btn.setBackgroundColor(getResources().getColor(R.color.colorComplementary));
                    female_btn.setBackgroundColor(getResources().getColor(R.color.transparent));
                    leftGenderSelected = true;
                    gender = "male";
                }

            }
        });

        continue_btn = ((AppCompatActivity)getActivity()).findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processNameFragment();
            }
        });
        return view;
    }

    private void processNameFragment(){
        if(gender.equals("")){
            Toast.makeText(getActivity().getApplicationContext(), "Campos vacios", Toast.LENGTH_SHORT).show();
        } else {
            RegisterController.fragmentPosition+=1;
            RegisterController.setNewUserGender(gender);
            Intent intent = new Intent(getActivity().getApplicationContext(), Register.class);
            getActivity().getApplicationContext().startActivity(intent);
        }
    }
}