package com.example.fema_botapp.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fema_botapp.R;
import com.example.fema_botapp.languageSelection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class sign_up_fragment extends Fragment {
    EditText new_user_email, new_user_first_password, new_user_comf_password;
    TextView login_button;
    ImageView sign_with_google, sign_with_facebook, sign_with_github;
    FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up_fragment, container, false);

        new_user_email = view.findViewById(R.id.new_user_email);
        new_user_first_password = view.findViewById(R.id.new_user_first_password);
        new_user_comf_password = view.findViewById(R.id.new_user_comf_password);
        login_button = view.findViewById(R.id.login_button);
        sign_with_google = view.findViewById(R.id.sign_with_google);
        sign_with_facebook = view.findViewById(R.id.sign_with_facebook);
        sign_with_github = view.findViewById(R.id.sign_with_github);



        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (new_user_email.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Email field can't be empty", Toast.LENGTH_SHORT).show();
                } else {
                    if (new_user_first_password.getText().toString().isEmpty() || new_user_comf_password.getText().toString().isEmpty()) {
                        Toast.makeText(getContext(), "password fields can't be empty", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!new_user_first_password.getText().toString().equals(new_user_comf_password.getText().toString())) {
                            Toast.makeText(getContext(), "Password are not equal", Toast.LENGTH_SHORT).show();
                        } else {
                            ProgressDialog progressDialog = new ProgressDialog(getContext());
                            progressDialog.setMessage("uploading data...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            auth.createUserWithEmailAndPassword(new_user_email.getText().toString().trim(), new_user_first_password.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getContext(), "User added", Toast.LENGTH_SHORT).show();
                                                new_user_email.setText("");
                                                new_user_comf_password.setText("");
                                                new_user_first_password.setText("");
                                                startActivity(new Intent(getContext(), languageSelection.class));
                                                getActivity().finish();
                                            } else {
                                                progressDialog.dismiss();
//                                                Toast.makeText(getContext(), "Fail to add user", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "The email address is already in use by another account.", Toast.LENGTH_SHORT).show();
                                            if (e.getMessage().equals("The email address is already in use by another account.")) {
                                                new_user_comf_password.setText("");
                                                new_user_first_password.setText("");
                                            }
                                        }
                                    });
                        }
                    }
                }
            }
        });

        return view;

    }
}
