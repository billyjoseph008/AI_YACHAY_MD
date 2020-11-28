package com.novequipo28.aiyachay.register;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.novequipo28.aiyachay.R;
import com.novequipo28.aiyachay.Register;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Formatter;

public class ask_age extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button continue_btn;
    DatePicker datePicker;
    LocalDate calendarDate;
    LocalDate nowDate;
    int age;

    public ask_age() {

    }

    public static ask_age newInstance(String param1, String param2) {
        ask_age fragment = new ask_age();
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

        View view = inflater.inflate(R.layout.fragment_ask_age, container, false);
        datePicker = view.findViewById(R.id.datePicker);
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                final String selectedDate = twoDigits(dayOfMonth) + "/" + twoDigits(monthOfYear+1) + "/" + year;
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                calendarDate = LocalDate.parse(selectedDate, fmt);
                nowDate = LocalDate.now();
                Period period = Period.between(calendarDate, nowDate);
                age = period.getYears();
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
        if(age>130 || age <= 0){
            Toast.makeText(getActivity().getApplicationContext(), "Seleccione una fecha valida.", Toast.LENGTH_SHORT).show();
        } else {
            RegisterController.fragmentPosition+=1;
            RegisterController.setNewUserAge(age);
            Intent intent = new Intent(getActivity().getApplicationContext(), Register.class);
            getActivity().getApplicationContext().startActivity(intent);
        }
    }

    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }
}