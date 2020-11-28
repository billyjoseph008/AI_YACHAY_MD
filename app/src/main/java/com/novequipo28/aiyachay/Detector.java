package com.novequipo28.aiyachay;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.custom.FirebaseCustomLocalModel;
import com.google.firebase.ml.custom.FirebaseModelDataType;
import com.google.firebase.ml.custom.FirebaseModelInputOutputOptions;
import com.google.firebase.ml.custom.FirebaseModelInputs;
import com.google.firebase.ml.custom.FirebaseModelInterpreter;
import com.google.firebase.ml.custom.FirebaseModelInterpreterOptions;
import com.google.firebase.ml.custom.FirebaseModelOutputs;
import com.novequipo28.aiyachay.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.app.Activity.RESULT_OK;

public class Detector extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageButton capture_btn;
    ImageView capture_view;
    ImageButton analyze_btn;
    ImageButton save_btn;
    Bitmap capture;

    private ProgressDialog progressDialog;

    FirebaseCustomLocalModel localModel;
    FirebaseModelInterpreterOptions options;
    FirebaseModelInputOutputOptions inputOutputOptions;
    FirebaseModelInterpreter interpreter;
    float[][] output;
    float[] probabilities;

    public Detector() {

    }

    public static Detector newInstance(String param1, String param2) {
        Detector fragment = new Detector();
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
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Detector");
        View view  = inflater.inflate(R.layout.fragment_detector, container, false);
        progressDialog = new ProgressDialog(getActivity());
        loadModel();
        capture_view = view.findViewById(R.id.capture_view);
        analyze_btn = view.findViewById(R.id.analyze_btn);
        analyze_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    obtainResults();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        save_btn = view.findViewById(R.id.save_btn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        capture_btn = view.findViewById(R.id.picture_btn);
        capture_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        return view;
    }

    private void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getActivity().getPackageManager()) != null){
            startActivityForResult(intent, 1);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imgBitmap = (Bitmap) extras.get("data");
            capture_view.setImageBitmap(imgBitmap);
            capture = imgBitmap;
        }
    }


    private void obtainResults() throws IOException {

        if(capture==null){
            Toast.makeText(getActivity().getApplicationContext(), "Primero debe realizar una captura", Toast.LENGTH_SHORT).show();
            return;
        }

        Bitmap bitmap = capture;
        bitmap = Bitmap.createScaledBitmap(bitmap, 256, 256, true);

        progressDialog.setMessage("Analizando imagen...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        int batchNum = 0;
        float[][][][] input = new float[1][256][256][3];
        for (int x = 0; x < 256; x++) {
            for (int y = 0; y < 256; y++) {
                int pixel = bitmap.getPixel(x, y);
                // Normalize channel values to [-1.0, 1.0]. This requirement varies by
                // model. For example, some models might require values to be normalized
                // to the range [0.0, 1.0] instead.
                input[batchNum][x][y][0] = (Color.red(pixel)) / 255.0f;
                input[batchNum][x][y][1] = (Color.green(pixel)) / 255.0f;
                input[batchNum][x][y][2] = (Color.blue(pixel)) / 255.0f;
            }
        }

        FirebaseModelInputs inputs = null;
        try {
            inputs = new FirebaseModelInputs.Builder()
                    .add(input)  // add() as many input arrays as your model requires
                    .build();
        } catch (FirebaseMLException e) {
            e.printStackTrace();
        }
        assert inputs != null;
        interpreter.run(inputs, inputOutputOptions).addOnSuccessListener(
                new OnSuccessListener<FirebaseModelOutputs>() {
                    @Override
                    public void onSuccess(FirebaseModelOutputs result) {
                        output = result.getOutput(0);
                        probabilities = output[0];
                        progressDialog.dismiss();
                        try {
                            showResults();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }
                        });
    }

    public void showResults() throws IOException {

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(getActivity().getAssets().open("retrained_labels.txt")));
        for (int i = 0; i < probabilities.length; i++) {
            String label = reader.readLine();
            Log.i("MLKit", String.format("%s: %1.4f", label, probabilities[i]));
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(false);
            builder.setTitle("Resultados");
            builder.setMessage("La muestra detectada muestra un porcentaje de probabilidad de melanoma de... " +
                    "\n\n Probabilidad: "+  probabilities[i]*100 + "%");
            builder.setNeutralButton("Entendido", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.show();
        }

    }

    private void loadModel(){

        localModel = new FirebaseCustomLocalModel.Builder()
                .setAssetFilePath("melanoma_detector.tflite").build();
        try {
            options = new FirebaseModelInterpreterOptions.Builder(localModel).build();
            interpreter = FirebaseModelInterpreter.getInstance(options);

        } catch (FirebaseMLException e) {
            e.printStackTrace();
        }

        try {
            inputOutputOptions = new FirebaseModelInputOutputOptions.Builder()
                    .setInputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1, 256, 256, 3})
                    .setOutputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1, 1})
                    .build();
        } catch (FirebaseMLException e) {
            e.printStackTrace();
        }

    }
}