package com.example.fema_botapp.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fema_botapp.R;
import com.example.fema_botapp.languageSelection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseAppLifecycleListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login_fragment extends Fragment {

    TextView login_button;
    EditText user_email, user_password;

    //FirebaseAuth
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_form_fragment, container, false);

        login_button = view.findViewById(R.id.login_button);
        user_email = view.findViewById(R.id.user_email);
        user_password = view.findViewById(R.id.user_password);


        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Firebase login functionality here...
                if (!user_email.getText().toString().isEmpty() && !user_password.getText().toString().isEmpty()) {

                    ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("feaching your info...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    firebaseAuth.signInWithEmailAndPassword(user_email.getText().toString().trim(), user_password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(getContext(), languageSelection.class));
                                        getActivity().finish();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                } else {
                    Toast.makeText(getContext(), "Field cant be empty!!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }
}