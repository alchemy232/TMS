package com.example.tms;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import java.util.Calendar;
import java.util.Locale;

public class AddActivity extends Activity {
    private DatePickerDialog.OnDateSetListener mDatePicker;
    final Calendar myCalendar = Calendar.getInstance();
    private Button btSave,btCancel;
    private EditText etDate, etGamenum, etPoints, etNumofplayers, etFio, etCorp;
    private Context context;
    private long MyMatchID;
    ArrayList<Fio> fio;
    DataBase mDBConnector;

    String[] corp = {"Ecoline", "Terractor", "United Nations Mars Initiative", "Mining Guild", "Science", "Saturn Corp"};
//    String[] fio = {"Алексей", "Наталия", "Алиса", "Азия", "Шип", "Тархун"};

    private void updateLabel() {
        String myFormat = "YYYY-MM-dd"; //In which you need put here
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
        etNumofplayers=(EditText)findViewById(R.id.etNumofplayers);
//        etFio=(EditText)findViewById(R.id.etFio);
        final Spinner etFio = findViewById(R.id.spinnerFIO);

//        etCorp=(EditText)findViewById(R.id.etCorp);
        final Spinner etCorp=findViewById(R.id.spinner);
        btSave=(Button)findViewById(R.id.butSave);
        btCancel=(Button)findViewById(R.id.butCancel);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, corp);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etCorp.setAdapter(adapter);

        ArrayAdapter<Fio> adapterFio = new ArrayAdapter<Fio>(this, android.R.layout.simple_spinner_item, fio);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etFio.setAdapter(adapterFio);

        if(getIntent().hasExtra("Matches")){
            Games games=(Games) getIntent().getSerializableExtra("Matches");
            etDate.setText(games.getDate());
            etGamenum.setText(""+games.getGameNum());
            etPoints.setText(""+games.getPoints());
            etNumofplayers.setText(""+games.getNumOfPlayers());

            etFio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String selected = (String)adapterView.getItemAtPosition(i);
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Ваш выбор: " + selected, Toast.LENGTH_SHORT);
                    toast.show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Ничего не выбрано", Toast.LENGTH_SHORT);
                    toast.show();

                }
            });

//            etFio.setText(games.getFio());
//            etCorp.setText(games.getCorp());
            MyMatchID=games.getId();
        }
        else
        {
            MyMatchID=-1;
        }
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Games games=new Games(MyMatchID,etTeamHome.getText().toString(),etTeamGuest.getText().toString(),Integer.parseInt(etGoalsHome.getText().toString()),Integer.parseInt(etGoalsGuest.getText().toString()));
//                Games games=new Games(MyMatchID,etDate.getText().toString(),Integer.parseInt(etGamenum.getText().toString()),Integer.parseInt(etPoints.getText().toString()),Integer.parseInt(etNumofplayers.getText().toString()),etFio.getText().toString(),etCorp.getText().toString() );
                if (TextUtils.isEmpty(etPoints.getText().toString())){
                    Toast.makeText(context, "Fill the points!",
                    Toast.LENGTH_SHORT).show();
                }else {
                    Games games = new Games(MyMatchID, etDate.getText().toString(), Integer.parseInt(etGamenum.getText().toString()), Integer.parseInt(etPoints.getText().toString()), Integer.parseInt(etNumofplayers.getText().toString()), etFio.getSelectedItem().toString(), etCorp.getSelectedItem().toString());
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