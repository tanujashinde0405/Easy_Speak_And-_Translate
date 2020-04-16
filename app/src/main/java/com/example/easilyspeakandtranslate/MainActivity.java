package com.example.easilyspeakandtranslate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ImageView speechbutton;
    EditText speechtext;
    Button translate;
    TextView textview;
    private String sourceText;
   // private String targetlang;
    private int sourcecode = FirebaseTranslateLanguage.EN;
    private int targetcode = FirebaseTranslateLanguage.HI;
    private static final int record_result=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speechbutton= findViewById(R.id.voicelogo);
        speechtext=findViewById(R.id.voicetext);
        translate= findViewById(R.id.translate_btn);
        textview= findViewById(R.id.textView2);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        speechbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent speechintent= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechintent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                speechintent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please Speak something");
                startActivityForResult(speechintent, record_result);
            }
        });

        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translateText(targetcode);
            }
        });


    }


    private void identifyLanguage() {
        sourceText = speechtext.getText().toString();

        FirebaseLanguageIdentification identifier = FirebaseNaturalLanguage.getInstance()
                .getLanguageIdentification();

       // mSourceLang.setText("Detecting..");
        identifier.identifyLanguage(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (s.equals("und")){
                    Toast.makeText(getApplicationContext(),"Language Not Identified",Toast.LENGTH_SHORT).show();

                }
                else {
                   getLanguageCode(s);
                }
            }
        });
    }



   private void getLanguageCode(String language) {
        //int langCode;
        switch (language){
            case "hi":
                sourcecode = FirebaseTranslateLanguage.HI;
                //mSourceLang.setText("Hindi");
                break;
            case "ar":
                sourcecode = FirebaseTranslateLanguage.EN;
                //mSourceLang.setText("Arabic");

                break;
            case "ur":
                sourcecode = FirebaseTranslateLanguage.UR;
                //mSourceLang.setText("Urdu");

                break;
            default:
                sourcecode = 0;
        }
    }


    private void translateText(int langCode) {
        sourceText = speechtext.getText().toString();
        identifyLanguage();
        textview.setText("Translating..");
        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                //from language
                .setSourceLanguage(sourcecode)
                // to language
                .setTargetLanguage(langCode)
                .build();

       // textview.setText("Target language set");

        final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance()
                .getTranslator(options);

        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                .build();


        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                       // textview.setText("converted");

                        textview.setText(s);
                    }
                });
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == record_result && resultCode== RESULT_OK)
        {
            ArrayList<String> matches= data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            speechtext.setText(matches.get(0).toString());
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String targetlang= parent.getItemAtPosition(position).toString();

        switch (targetlang){

            case "Arabic":
                targetcode = FirebaseTranslateLanguage.AR;
                break;

            case "Bengali":
                targetcode = FirebaseTranslateLanguage.BN;
                break;
            case "German":
                targetcode = FirebaseTranslateLanguage.DE;
                break;
            case "Spanish":
                targetcode = FirebaseTranslateLanguage.ES;
                break;
            case "Filipino":
                targetcode = FirebaseTranslateLanguage.FI;
                break;
            case "French":
                targetcode = FirebaseTranslateLanguage.FR;
                break;
            case "Gujarati":
                targetcode = FirebaseTranslateLanguage.GU;
                break;
            case "Hindi":
                targetcode = FirebaseTranslateLanguage.HI;
                break;
            case "Indonesian":
                targetcode = FirebaseTranslateLanguage.ID;
                break;
            case "Italian":
                targetcode = FirebaseTranslateLanguage.IT;
                break;
            case "Japanese":
                targetcode = FirebaseTranslateLanguage.JA;
                break;
            case "Kannada":
                targetcode = FirebaseTranslateLanguage.KN;
                break;
            case "Korean":
                targetcode = FirebaseTranslateLanguage.KO;
                break;
            case "Marathi":
                targetcode = FirebaseTranslateLanguage.MR;
                break;
            case "Portuguese":
                targetcode = FirebaseTranslateLanguage.PT;
                break;
            case "Russian":
                targetcode = FirebaseTranslateLanguage.RU;
                break;
            case "Swedish":
                targetcode = FirebaseTranslateLanguage.SV;
                break;
            case "Tamil":
                targetcode = FirebaseTranslateLanguage.TA;
                break;
            case "Telugu":
                targetcode = FirebaseTranslateLanguage.TE;
                break;
            case "Turkish":
                targetcode = FirebaseTranslateLanguage.TR;
                break;
            case "Chinese":
                targetcode = FirebaseTranslateLanguage.ZH;
                break;
            case "Urdu":
                targetcode = FirebaseTranslateLanguage.UR;
                break;
            default:
                targetcode = FirebaseTranslateLanguage.HI;
                break;
        }
        //Toast.makeText(MainActivity.this,lang, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(MainActivity.this,"Error", Toast.LENGTH_SHORT).show();
    }
}
