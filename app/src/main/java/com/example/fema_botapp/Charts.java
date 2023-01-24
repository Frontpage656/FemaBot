package com.example.fema_botapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fema_botapp.Adapters.AutoAdapter;
import com.example.fema_botapp.Adapters.ChartsAdapter;
import com.example.fema_botapp.ApiClass.RetrofitApi;
import com.example.fema_botapp.PojoClass.ChartModeClass;
import com.example.fema_botapp.PojoClass.MessageModeClass;
import com.example.fema_botapp.PojoClass.RecommendedModeClass;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentifier;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressLint("UseSwitchCompatOrMaterialCode")
public class Charts extends AppCompatActivity {

    //firebase instance..
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    //All constants
    private static final String USER_KEY = "user";
    private static final String BOT_KEY = "bot";
    private static final int REQUEST_CODE = 10;
    private static int SWITCH_CHEQUE = 0;


    TextToSpeech textToSpeech;
    BottomSheetDialog bottomSheetDialog;

    RecyclerView chart_recycle, auto_texts;
    EditText message_text;
    ImageView mic_button;
    TextView send_btn;
    ImageView setting_button;
    ChartsAdapter chartsAdapter;
    RecyclerView.LayoutManager linearLayout;
    ArrayList<ChartModeClass> modeClassesArrayList = new ArrayList<>();

    AutoAdapter autoAdapter;
    RecyclerView.LayoutManager horizontal;
    List<RecommendedModeClass> textsList = new ArrayList();
    MessageModeClass messageModeClass;

    View view;
    SwitchMaterial voice_switch;
    SwitchMaterial dark_theme_switch;
    TextView logout_btn, language_select;

    String lang = "";

    Calendar calendar;
    String formattedDate, strDate;


    @SuppressLint({"InflateParams", "ObsoleteSdkInt"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_hole);

        calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        formattedDate = df.format(calendar.getTime());

        //inflate all from pref_layout
        view = getLayoutInflater().inflate(R.layout.pref_layout, null, false);
        voice_switch = view.findViewById(R.id.voice_switch);
        language_select = view.findViewById(R.id.language_select);
        dark_theme_switch = view.findViewById(R.id.dark_theme_switch);
        logout_btn = view.findViewById(R.id.logout_btn);

        //Bottom sheet instance
        bottomSheetDialog = new BottomSheetDialog(Charts.this, R.style.BottomSheetDialogTheme);

        message_text = findViewById(R.id.message_text);
        send_btn = findViewById(R.id.send_btn);
        mic_button = findViewById(R.id.mic_btn);
        setting_button = findViewById(R.id.setting_button);

        chart_recycle = findViewById(R.id.chart_recycle);
        chart_recycle.setHasFixedSize(true);

        auto_texts = findViewById(R.id.auto_texts);
        auto_texts.setHasFixedSize(true);

        linearLayout = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        chart_recycle.setLayoutManager(linearLayout);

        //Recommended text recycle view
        horizontal = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        auto_texts.setLayoutManager(horizontal);

        chartsAdapter = new ChartsAdapter(modeClassesArrayList, Charts.this);
        chart_recycle.setAdapter(chartsAdapter);

        //Recommended text adapter
        autoAdapter = new AutoAdapter(getApplicationContext(), textsList);

        autoAdapter.setCallback(new AutoAdapter.AutoAdapterCallback() {
            @Override
            public void onItemClick(RecommendedModeClass texts) {

                returnResponse(texts.getText());

            }
        });

        auto_texts.setAdapter(autoAdapter);
        addInputs();

        //Text to speech instance
        textToSpeech = new TextToSpeech(getApplicationContext(), i -> {
            if (i != TextToSpeech.ERROR) {
                // To Choose language of speech
                textToSpeech.setLanguage(Locale.UK);
            }
        });

        //Button to show bottom sheet
        setting_button.setOnClickListener(view -> {
            createDialog();
            bottomSheetDialog.show();

        });


        mic_button.setOnClickListener(view -> {

            if (Build.VERSION.SDK_INT >= 23) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    String[] permission = {Manifest.permission.RECORD_AUDIO};
                    ActivityCompat.requestPermissions(Charts.this, permission, REQUEST_CODE);
                } else {
                    catchSpeech();
                }
            } else {
                catchSpeech();
            }

        });


        message_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() == 0) {
                    mic_button.setVisibility(View.VISIBLE);
                    send_btn.setVisibility(View.GONE);
                } else {
                    send_btn.setVisibility(View.VISIBLE);
                    mic_button.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (charSequence.toString().trim().isEmpty()) {
                    mic_button.setVisibility(View.VISIBLE);
                    send_btn.setVisibility(View.GONE);
                } else {
                    send_btn.setVisibility(View.VISIBLE);
                    mic_button.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().length() == 0) {
                    mic_button.setVisibility(View.VISIBLE);
                    send_btn.setVisibility(View.GONE);
                }
            }
        });


        send_btn.setOnClickListener(view -> {
            if (message_text.getText().toString().isEmpty()) {
                Toast.makeText(Charts.this, "Please enter text..", Toast.LENGTH_SHORT).show();
            }

            if (Build.VERSION.SDK_INT >= 23) {
                returnResponse(message_text.getText().toString());
            }
            message_text.setText("");
        });

    }

    //Logout button
    private void createDialog() {
        logout_btn.setOnClickListener(view -> {

            //logout here...
            firebaseAuth.signOut();
            startActivity(new Intent(Charts.this, MainActivity.class));
            finish();
        });

        //select languages from setSingleChoiceItems dialog
        language_select.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(Charts.this)
                    .setCancelable(true)
                    .setTitle("Select language");

            String[] languages = {"English", "Swahili"};
            int item = 0;
            builder.setSingleChoiceItems(languages, item, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch (i) {
                        case 0:
                            language_select.setText("English");
                            if (language_select.getText().toString().equals("English")) {
                                Toast.makeText(Charts.this, "Language is English", Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(Charts.this, "Language changed to English", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            language_select.setText("Swahili");
                            if (language_select.getText().toString().equals("Swahili")) {
                                Toast.makeText(Charts.this, "Language is SWAHILI", Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(Charts.this, "Language changed to Swahili", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        });

        // Switch functionality
        voice_switch.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {

                SWITCH_CHEQUE = 1;

            } else {

                Toast.makeText(Charts.this, "The voice text is disabled", Toast.LENGTH_SHORT).show();
                SWITCH_CHEQUE = 0;
            }
        });
        if (SWITCH_CHEQUE == 1) {
            voice_switch.setChecked(true);
            Toast.makeText(Charts.this, "The voice text is active", Toast.LENGTH_SHORT).show();
        } else {
            voice_switch.setChecked(false);
        }

        bottomSheetDialog.setContentView(view);
    }

    //permission results is when user react to the permission dialog here is the functionality
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    catchSpeech();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }
    }

    // All about Api requests here/ Full retrofit
    @SuppressLint("NotifyDataSetChanged")
    private void returnResponse(String message) {


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("hh:mm a");
        String strDate = mdformat.format(calendar.getTime());


        modeClassesArrayList.add(new ChartModeClass(message, USER_KEY, strDate));
        chartsAdapter.notifyDataSetChanged();
        chart_recycle.scrollToPosition(modeClassesArrayList.size() - 1);


        String url = "http://api.brainshop.ai/get?bid=164756&key=dPq4fP24Q3OlD71n&uid=" + firebaseAuth.getUid() + "&msg=" + message;
        String BASE_URL = "https://brainshop.ai/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
        Call<MessageModeClass> call = retrofitApi.getMessage(url);
        call.enqueue(new Callback<MessageModeClass>() {
            @Override
            public void onResponse(Call<MessageModeClass> call, Response<MessageModeClass> response) {

                if (response.isSuccessful()) {
                    messageModeClass = response.body();
                    if (messageModeClass != null) {
//
////                              /////////////////////////////////  Lets know the language used  ///////////////////////////////////////////
                        LanguageIdentifier languageIdentifier = LanguageIdentification.getClient();
                        languageIdentifier.identifyLanguage(message)
                                .addOnSuccessListener(
                                        new OnSuccessListener<String>() {
                                            @Override
                                            public void onSuccess(@Nullable String languageCode) {
                                                Toast.makeText(Charts.this, languageCode, Toast.LENGTH_SHORT).show();


                                                if (SWITCH_CHEQUE == 1) {
                                                    textToSpeech.speak(messageModeClass.getCnt(), TextToSpeech.QUEUE_FLUSH, null);
                                                }

                                                modeClassesArrayList.add(new ChartModeClass(messageModeClass.getCnt(), BOT_KEY, strDate));
                                                chartsAdapter.notifyDataSetChanged();
                                                chart_recycle.scrollToPosition(modeClassesArrayList.size() - 1);

                                            }

                                        });

                    } else {
                        if (SWITCH_CHEQUE == 1) {
                            textToSpeech.speak(messageModeClass.getCnt(), TextToSpeech.QUEUE_FLUSH, null);
                        }
                        modeClassesArrayList.add(new ChartModeClass(messageModeClass.getCnt(), BOT_KEY,strDate));
                        chartsAdapter.notifyDataSetChanged();
                        chart_recycle.scrollToPosition(modeClassesArrayList.size() - 1);

                    }

                } else {
                    Toast.makeText(Charts.this, "Failed to load...", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<MessageModeClass> call, Throwable t) {
                Toast.makeText(Charts.this, "Failed to load...", Toast.LENGTH_SHORT).show();
            }
        });


//        translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
//            @Override
//            public void onSuccess(String translatedText) {
//
//                String url = "http://api.brainshop.ai/get?bid=164756&key=dPq4fP24Q3OlD71n&uid=" + firebaseAuth.getUid() + "&msg=" + message;
//                String BASE_URL = "https://brainshop.ai/";
//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl(BASE_URL)
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
//                RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
//                Call<MessageModeClass> call = retrofitApi.getMessage(url);
//
//                call.enqueue(new Callback<MessageModeClass>() {
//                    @Override
//                    public void onResponse(@NonNull Call<MessageModeClass> call, @NonNull Response<MessageModeClass> response) {
//                        if (response.isSuccessful()) {
//                            messageModeClass = response.body();
//                            if (messageModeClass != null) {
//
////                              /////////////////////////////////  Lets know the language used  ///////////////////////////////////////////
//                                LanguageIdentifier languageIdentifier = LanguageIdentification.getClient();
//                                languageIdentifier.identifyLanguage(message)
//                                        .addOnSuccessListener(
//                                                new OnSuccessListener<String>() {
//                                                    @Override
//                                                    public void onSuccess(@Nullable String languageCode) {
//                                                        Toast.makeText(Charts.this, languageCode, Toast.LENGTH_SHORT).show();
//
////                                                        if (languageCode.equals("sw")) {
//                                                        translateAPI = new TranslateAPI(
//                                                                Language.ENGLISH,
//                                                                Language.SWAHILI,
//                                                                messageModeClass.getCnt()
//                                                        );
//
//                                                        translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
//                                                            @Override
//                                                            public void onSuccess(String translatedText) {
//                                                                if (SWITCH_CHEQUE == 1) {
//                                                                    textToSpeech.speak(translatedText, TextToSpeech.QUEUE_FLUSH, null);
//                                                                }
//
//                                                                modeClassesArrayList.add(new ChartModeClass(translatedText, BOT_KEY));
//                                                                chartsAdapter.notifyDataSetChanged();
//                                                                chart_recycle.scrollToPosition(modeClassesArrayList.size() - 1);
//
//                                                            }
//
//                                                            @Override
//                                                            public void onFailure(String ErrorText) {
//                                                                Toast.makeText(Charts.this, "Failure to translate", Toast.LENGTH_SHORT).show();
//                                                            }
//                                                        });
//
////                                                        } else {
////                                                            if (SWITCH_CHEQUE == 1) {
////                                                                textToSpeech.speak(messageModeClass.getCnt(), TextToSpeech.QUEUE_FLUSH, null);
////                                                            }
////                                                            modeClassesArrayList.add(new ChartModeClass(messageModeClass.getCnt(), BOT_KEY));
////                                                            chartsAdapter.notifyDataSetChanged();
////                                                            chart_recycle.scrollToPosition(modeClassesArrayList.size() - 1);
////
////                                                        }
//
//                                                    }
//                                                })
//                                        .addOnFailureListener(
//                                                new OnFailureListener() {
//                                                    @Override
//                                                    public void onFailure(@NonNull Exception e) {
//                                                        Toast.makeText(Charts.this, "failed to recognise language", Toast.LENGTH_SHORT).show();
//                                                    }
//                                                });
//                            }
//
//                        } else {
//                            Toast.makeText(Charts.this, "response is null", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call<MessageModeClass> call, @NonNull Throwable t) {
//                        String failure_message = "No response check your network connection!";
//
//                        if (SWITCH_CHEQUE == 1) {
//                            textToSpeech.speak(failure_message, TextToSpeech.QUEUE_FLUSH, null);
//                        }
//                        modeClassesArrayList.add(new ChartModeClass(failure_message, BOT_KEY));
//                        chartsAdapter.notifyDataSetChanged();
//                        chart_recycle.scrollToPosition(modeClassesArrayList.size() - 1);
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(String ErrorText) {
//                Toast.makeText(Charts.this, "Failed in translate" + ErrorText, Toast.LENGTH_SHORT).show();
//            }
//        });

    }


    //on start functionality
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onStart() {
        super.onStart();

        lang = getIntent().getExtras().getString("lang");
        if (lang.equals("english")) {

            modeClassesArrayList.add(new ChartModeClass("Hey welcome back am fema bot ask anything lol!", BOT_KEY, strDate));

        } else {
            modeClassesArrayList.add(new ChartModeClass("Nafurahi kukutana na wewe tena niulize chochote", BOT_KEY,strDate));
        }
        chartsAdapter.notifyDataSetChanged();
        chart_recycle.scrollToPosition(modeClassesArrayList.size() - 1);

    }

    // All recommended text are added here so i will add firebase functionality here....
    private void addInputs() {

        firebaseFirestore.collection("recommended").orderBy("text", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(Charts.this, "Error occurred!" + error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                textsList.add(documentChange.getDocument().toObject(RecommendedModeClass.class));
                            }
                            autoAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }


    // All about speech recognition here
    private void catchSpeech() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    // Results about speech recognition....
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    // Add the output to return response for retrofit response
                    returnResponse(result.get(0));

                }
                break;
        }
    }
}