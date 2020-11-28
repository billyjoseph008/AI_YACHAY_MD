package com.novequipo28.aiyachay.register;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.novequipo28.aiyachay.R;
import com.novequipo28.aiyachay.Register;

public class ask_email extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText email_input;
    Button continue_btn;

    public ask_email() {

    }

    public static ask_email newInstance(String param1, String param2) {
        ask_email fragment = new ask_email();
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
        View view = inflater.inflate(R.layout.fragment_ask_email, container, false);

        email_input = view.findViewById(R.id.email_input);
        if(!RegisterController.getNewUserName().isEmpty()){
            email_input.setText(RegisterController.getNewUserName());
        }
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
        String obtain_mail = email_input.getText().toString().trim();
        if(obtain_mail.isEmpty()){
            Toast.makeText(getActivity().getApplicationContext(), "Campos vacios", Toast.LENGTH_SHORT).show();
        } else {
            RegisterController.fragmentPosition+=1;
            RegisterController.setNewUserEmail(obtain_mail);
            Intent intent = new Intent(getActivity().getApplicationContext(), Register.class);
            getActivity().getApplicationContext().startActivity(intent);
        }
    }
}