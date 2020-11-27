package com.novequipo28.aiyachay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.novequipo28.aiyachay.R;
import com.novequipo28.aiyachay.register.RegisterController;
import com.novequipo28.aiyachay.session.SessionManager;

import java.util.Objects;

public class Login extends AppCompatActivity {

    Button login;
    Button register;

    EditText email_txt;
    EditText pass_txt;

    ProgressDialog progressDialog;

    SessionManager sessionManager;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        setDefaultRegisterValues();

        sessionManager = new SessionManager(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        progressDialog = new ProgressDialog(this);

        email_txt = findViewById(R.id.email_input);
        pass_txt = findViewById(R.id.pass_input);
        login = findViewById(R.id.login_btn);
        register = findViewById(R.id.register_btn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }


    private void login() {

        final String email = email_txt.getText().toString().trim();
        final String pass = pass_txt.getText().toString().trim();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getApplicationContext(), "Datos incorrectos.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Contraseña vacia.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Iniciando sesion...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    assert user != null;
                    if (user.isEmailVerified()) {
                        Toast.makeText(getApplicationContext(), "Inicio de sesión exitoso. Bienvenido:" + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Este usuario no ha sido verificado.", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        return;
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Inicio de sesión erroneo.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }

                progressDialog.dismiss();
                sessionManager.createLoginSession(mAuth.getCurrentUser().getDisplayName(),
                        mAuth.getCurrentUser().getEmail());
                sessionManager.setName(mAuth.getCurrentUser().getDisplayName());
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
    }

    private void register(){
        Intent intent = new Intent(getApplicationContext(), Register.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void setDefaultRegisterValues(){
        RegisterController.fragmentPosition=1;
        RegisterController.setNewUserName("");
        RegisterController.setNewUserEmail("");
        RegisterController.setNewUserGender("");
        RegisterController.setNewUserPassword("");
        RegisterController.setNewUserCountry("");
        RegisterController.setNewUserState("");
        RegisterController.setNewUserCity("");
        RegisterController.setNewUserAge(0);
    }

    @Override
    public void onBackPressed() {

    }
}