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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddActivity extends Activity {
    private DatePickerDialog.OnDateSetListener mDatePicker;
    final Calendar myCalendar = Calendar.getInstance();
    private Button btSave,btCancel;
    private EditText etDate, etGamenum, etPoints; // etNumofplayers;
    private Context context;
    private long elemID;
    private Spinner etFio, etCorp,etNumofplayers;
    ArrayList<Fio> fio;
    DataBase mDBConnector;


    private void updateLabel() {
        String myFormat = "YYYY-MM-dd"; //формат выбран для работоспособности оператора between в запросе бд sqlite
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etDate.setText(sdf.format(myCalendar.getTime()));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context=this;
        mDBConnector=new DataBase(this);
        fio = mDBConnector.selectAllFio();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        List possibleNumberOfPlayers = Arrays.asList(Games.possiblePlayers);

        mDatePicker=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        etDate=(EditText)findViewById(R.id.etDate);
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(context, mDatePicker,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        etGamenum=(EditText)findViewById(R.id.etGameNum);
        etPoints=(EditText)findViewById(R.id.etPoints);
//        etNumofplayers=(EditText)findViewById(R.id.etNumofplayers);
        etNumofplayers=findViewById(R.id.etNumofplayers);
        etFio = findViewById(R.id.spinnerFIO);
        etCorp=findViewById(R.id.spinner);
        btSave=(Button)findViewById(R.id.butSave);
        btCancel=(Button)findViewById(R.id.butCancel);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Corporations.corp);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etCorp.setAdapter(adapter);

        ArrayAdapter<Fio> adapterFio = new ArrayAdapter<Fio>(this, android.R.layout.simple_spinner_item, fio);
        adapterFio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etFio.setAdapter(adapterFio);

        ArrayAdapter<Integer> numberOfPlayers = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, Games.possiblePlayers);
        numberOfPlayers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etNumofplayers.setAdapter(numberOfPlayers);

        if(getIntent().hasExtra("Matches")){
            Games games=(Games) getIntent().getSerializableExtra("Matches");
            etDate.setText(games.getDate());
            etGamenum.setText(""+games.getGameNum());
            etPoints.setText(""+games.getPoints());
            elemID=games.getId();
        }
        else
        {
            elemID=-1;
        }
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etPoints.getText().toString()) || TextUtils.isEmpty(etDate.getText().toString()) ||TextUtils.isEmpty(etGamenum.getText().toString())){
                    Toast.makeText(context, "Заполните все поля !",
                    Toast.LENGTH_SHORT).show();
                }else {
                    Games games = new Games(elemID, etDate.getText().toString(), Integer.parseInt(etGamenum.getText().toString()), Integer.parseInt(etPoints.getText().toString()), Integer.parseInt(etNumofplayers.getSelectedItem().toString()), etFio.getSelectedItem().toString(), etCorp.getSelectedItem().toString());
//                    Log.d("Alchemy","tre" + games.getPoints() + " - "+ games.getGameNum() );
                    Intent intent = getIntent();
                    intent.putExtra("Matches", games);
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