package com.novequipo28.aiyachay.register;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.novequipo28.aiyachay.Login;
import com.novequipo28.aiyachay.R;
import com.novequipo28.aiyachay.Register;

import java.util.concurrent.Executor;

public class ask_password extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button continue_btn;

    FirebaseAuth mAuth;
    FirebaseUser user;

    EditText pass_input;

    DatabaseReference userReference;

    Context context;

    boolean finalResponce;

    private ProgressDialog progressDialog;

    public ask_password() {

    }

    public static ask_password newInstance(String param1, String param2) {
        ask_password fragment = new ask_password();
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
        View view = inflater.inflate(R.layout.fragment_ask_password, container, false);
        mAuth = FirebaseAuth.getInstance();
        context = getActivity().getApplicationContext();
        userReference = FirebaseDatabase.getInstance().getReference().child("users");
        progressDialog = new ProgressDialog(getActivity());
        pass_input = view.findViewById(R.id.pass_input);

        continue_btn = ((AppCompatActivity)getActivity()).findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterController.setNewUserPassword(pass_input.getText().toString());
                processNameFragment();
            }
        });


        return view;
    }

    private void processNameFragment(){

        String pass_obtain = pass_input.getText().toString().trim();

        if(pass_obtain.equals("")){
            Toast.makeText(context, "Ingrese una contraseña.", Toast.LENGTH_SHORT).show();
        } else {
            registerUser();
            RegisterController.fragmentPosition+=1;
        }
    }

    private void registerUser() {

        final String name = RegisterController.getNewUserName();
        final String email = RegisterController.getNewUserEmail();
        final int age = RegisterController.getNewUserAge();
        final String gender = RegisterController.getNewUserGender();
        final String country = RegisterController.getNewUserCountry();
        final String state = RegisterController.getNewUserState();
        final String city = RegisterController.getNewUserCity();
        final String pass = RegisterController.getNewUserPassword();

        if (name.isEmpty() || email.isEmpty() || gender.isEmpty() || country.isEmpty() ||
                state.isEmpty() || city.isEmpty() || pass.isEmpty()) {
            Toast.makeText(context,
                    "Alguno de los datos no fue capturado durante el registro. Intentalo nuevamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registrando nuevo usuario...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener( getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    user = mAuth.getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name).build();
                    user.updateProfile(profileUpdates);
                    DatabaseReference dbTemporalReference = FirebaseDatabase.getInstance().getReference()
                            .child("users").child(user.getUid());
                    dbTemporalReference.child("name").setValue(name);
                    dbTemporalReference.child("email").setValue(email);
                    dbTemporalReference.child("age").setValue(age);
                    dbTemporalReference.child("gender").setValue(gender);
                    dbTemporalReference.child("country").setValue(country);
                    dbTemporalReference.child("state").setValue(state);
                    dbTemporalReference.child("city").setValue(city);
                    dbTemporalReference.child("pass").setValue(pass);

                    Toast.makeText(context, "Ha sido registrado correctamente.",
                            Toast.LENGTH_SHORT).show();
                    mAuth.setLanguageCode("es");
                    user.sendEmailVerification();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setCancelable(false);
                    builder.setTitle("Registro exitoso.");
                    builder.setMessage("Se ha registrado exitosamente a AI YACHAY Melanoma Detector. \n\nSe ha enviado un correo electrónico a su cuenta para verificar el mismo. ¡ Gracias !");
                    builder.setNeutralButton("Entendido", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(getActivity().getApplicationContext(), Register.class);
                            getActivity().getApplicationContext().startActivity(intent);
                        }
                    });
                    builder.show();


                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(context, "Ya esta registrado una cuenta con este correo.",
                                Toast.LENGTH_SHORT).show();
                        task.getException().printStackTrace();
                        Intent intent = new Intent(getActivity().getApplicationContext(), Register.class);
                        getActivity().getApplicationContext().startActivity(intent);
                        progressDialog.dismiss();
                        return;
                    } else {
                        Toast.makeText(context, "Fallo al registrar esta cuenta.",
                                Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        return;
                    }

                }
                progressDialog.dismiss();

            }
        });

    }
}