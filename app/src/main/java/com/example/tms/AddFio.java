package com.example.tms;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddFio extends Activity {
    private Button btSave,btCancel;
    private EditText etFio;
    private Context context;
    private long elemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fio);

        etFio=(EditText)findViewById(R.id.fio);
        btSave=(Button)findViewById(R.id.butSave);
        btCancel=(Button)findViewById(R.id.butCancel);

        if(getIntent().hasExtra("Matches")){
            Fio fio=(Fio) getIntent().getSerializableExtra("Matches");
            etFio.setText(fio.getFio());
//            etFio.setText(games.getFio());
//            etCorp.setText(games.getCorp());
            elemID=fio.getId();
        }
        else
        {
            elemID=-1;
        }
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etFio.getText().toString()) ){
                    Toast.makeText(context, "Заполните поле ФИО !",
                            Toast.LENGTH_SHORT).show();
                }else {
                    Fio fio = new Fio(elemID, etFio.getText().toString());
                    Intent intent = getIntent();
                    intent.putExtra("Matches", fio);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}