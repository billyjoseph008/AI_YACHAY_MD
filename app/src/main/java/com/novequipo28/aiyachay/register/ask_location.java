package com.novequipo28.aiyachay.register;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.novequipo28.aiyachay.R;
import com.novequipo28.aiyachay.Register;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class ask_location extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button continue_btn;

    EditText country_input;
    EditText state_input;
    EditText city_input;

    public ask_location() {

    }

    public static ask_location newInstance(String param1, String param2) {
        ask_location fragment = new ask_location();
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
        View view = inflater.inflate(R.layout.fragment_ask_ubicacion, container, false);


        country_input = view.findViewById(R.id.country_input);
        state_input = view.findViewById(R.id.state_input);
        city_input = view.findViewById(R.id.city_input);
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

        String country_obtain = country_input.getText().toString().trim();
        String state_obtain = state_input.getText().toString().trim();
        String city_obtain = city_input.getText().toString().trim();

        if(country_obtain.isEmpty() && state_obtain.isEmpty() && city_obtain.isEmpty()){
            Toast.makeText(getActivity().getApplicationContext(), "No deben de haber campos vacios", Toast.LENGTH_SHORT).show();
        } else {
            RegisterController.fragmentPosition+=1;
            RegisterController.setNewUserCountry(country_obtain);
            RegisterController.setNewUserState(state_obtain);
            RegisterController.setNewUserCity(city_obtain);
            Intent intent = new Intent(getActivity().getApplicationContext(), Register.class);
            getActivity().getApplicationContext().startActivity(intent);
        }
    }

}