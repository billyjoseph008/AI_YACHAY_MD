package com.novequipo28.aiyachay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.novequipo28.aiyachay.register.RegisterController;
import com.novequipo28.aiyachay.register.ask_age;
import com.novequipo28.aiyachay.register.ask_email;
import com.novequipo28.aiyachay.register.ask_gender;
import com.novequipo28.aiyachay.register.ask_name;
import com.novequipo28.aiyachay.register.ask_password;
import com.novequipo28.aiyachay.register.ask_location;

public class Register extends AppCompatActivity {

    Button continue_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        chanceStage(false);

        continue_btn = findViewById(R.id.continue_btn);

        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction().setCustomAnimations(R.anim.move_right, R.anim.move_left)
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private boolean chanceStage(Boolean direction){

        Fragment fragment = null;
        switch ( RegisterController.fragmentPosition) {
            case 0:
                goLogin();
            case 1:
                fragment = new ask_name();
                break;
            case 2:
                fragment = new ask_email();
                break;
            case 3:
                fragment = new ask_age();
                break;
            case 4:
                fragment = new ask_gender();
                break;
            case 5:
                fragment = new ask_location();
                break;
            case 6:
                fragment = new ask_password();
                break;
            case 7:
                goLogin();
        }
        return loadFragment(fragment);
    }

    private void goLogin(){
        Intent intent = new Intent(getApplicationContext(), Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        RegisterController.fragmentPosition-=1;
        chanceStage(true);
    }

}