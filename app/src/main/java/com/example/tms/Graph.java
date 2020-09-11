package com.example.tms;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class Graph extends AppCompatActivity {
    int mCounter; //для проверки многопоточности
    private Button btCorpUpdate,btFioUpdate;
    Context mContext;
    DataBase mDBConnector;
    private DatePickerDialog.OnDateSetListener mDatePickerDateFrom , mDatePickerDateTo ;
    final Calendar myCalendar = Calendar.getInstance();
    private EditText etDateFrom,etDateTo;

    private void updateLabelDateFrom() {
        String myFormat = "YYYY-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etDateFrom.setText(sdf.format(myCalendar.getTime()));
    }
    private void updateLabelDateTo() {
        String myFormat = "YYYY-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etDateTo.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);
        btCorpUpdate=(Button)findViewById(R.id.corporationsUpdate);
        btFioUpdate=(Button)findViewById(R.id.fioUpdate);

        etDateFrom=(EditText)findViewById(R.id.editTextDateFrom);
        etDateTo=(EditText)findViewById(R.id.editTextDateTo);

        mDBConnector=new DataBase(this);


        btCorpUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ///// пилим пногопоточность
                if (TextUtils.isEmpty(etDateFrom.getText().toString()) || TextUtils.isEmpty(etDateTo.getText().toString()) ){
                    Toast.makeText(mContext, "Заполните все поля !",
                            Toast.LENGTH_SHORT).show();
                }else {
                final BarChart chart = (BarChart) findViewById(R.id.chart);
                Runnable runnable = new Runnable() {
                    public void run() {


                        List<BarEntry> corpsBar = new ArrayList<BarEntry>();

                        for (int i = 0; i < Corporations.corp.length; i++) {
                            corpsBar.add(new BarEntry(i, mDBConnector.selectCorpMaxPoints(Corporations.corp[i], etDateFrom.getText().toString(), etDateTo.getText().toString())));
                        }


                        BarDataSet setComp1 = new BarDataSet(corpsBar, "Corporations");
                        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
                        setComp1.setColors(ColorTemplate.COLORFUL_COLORS);

                        List<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                        dataSets.add(setComp1);

                        ValueFormatter formatter = new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {
                                return Corporations.corp[(int) value];
                            }
                        };
                        XAxis xAxis = chart.getXAxis();
                        xAxis.setGranularity(1f); // шаг
                        xAxis.setValueFormatter(formatter);

                        BarData data = new BarData(dataSets);
                        Description description = new Description();
                        description.setText("Corporates MAX scores");
                        chart.setDescription(description);
                        chart.setData(data);
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();
                chart.animateY(2000);
                chart.invalidate(); // refresh

                ///// пилим пногопоточность
            }
            }
        });

        btFioUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etDateFrom.getText().toString()) || TextUtils.isEmpty(etDateTo.getText().toString()) ){
                    Toast.makeText(mContext, "Заполните все поля !",
                            Toast.LENGTH_SHORT).show();
                }else {
                final BarChart chart2 = (BarChart) findViewById(R.id.chart2);
                Runnable runnable = new Runnable() {
                    public void run() {

                        final ArrayList<String> fiokeys = new ArrayList<>();

                        List<BarEntry> fioBar = new ArrayList<BarEntry>();
                        Map arr = mDBConnector.selectFioMaxPoints(etDateFrom.getText().toString(), etDateTo.getText().toString());

                        Set<String> keys = mDBConnector.selectFioMaxPoints(etDateFrom.getText().toString(), etDateTo.getText().toString()).keySet();

                        for (String key : keys) {
                            fiokeys.add(key);
                        }
                        for (int i = 0; i < fiokeys.size(); i++) {
                            int buffer = (Integer) arr.get(fiokeys.get(i));
                            fioBar.add(new BarEntry(i, buffer));
                        }
                        BarDataSet setFioData = new BarDataSet(fioBar, "Fio");
                        setFioData.setAxisDependency(YAxis.AxisDependency.LEFT);
                        setFioData.setColors(ColorTemplate.COLORFUL_COLORS);

                        List<IBarDataSet> dataSetsFio = new ArrayList<IBarDataSet>();
                        dataSetsFio.add(setFioData);

                        ValueFormatter formatterFio = new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {
                                return fiokeys.get((int) value);
                            }
                        };

                        XAxis xAxisFio = chart2.getXAxis();
                        xAxisFio.setGranularity(1f); // шаг
                        xAxisFio.setValueFormatter(formatterFio);

                        BarData dataFio = new BarData(dataSetsFio);
                        Description descriptionFio = new Description();
                        descriptionFio.setText("Players MAX scores");
                        chart2.setDescription(descriptionFio);
                        chart2.setData(dataFio);
                    }
                };
                Thread thread2 = new Thread(runnable);
                thread2.start();
                chart2.animateY(2000);
                chart2.invalidate(); // refresh
            }
            }
        });

        mDatePickerDateFrom=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelDateFrom();
            }
        };
        mDatePickerDateTo=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelDateTo();
            }
        };

        etDateFrom=(EditText)findViewById(R.id.editTextDateFrom);
        etDateTo=(EditText)findViewById(R.id.editTextDateTo);

        etDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(mContext, mDatePickerDateFrom,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        etDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(mContext, mDatePickerDateTo,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        // График по игрокам


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
        getMenuInflater().inflate(R.menu.menu_graph, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main:
                Intent i = new Intent(mContext, MainActivity.class);
                startActivity(i);
                return true;
            case R.id.exit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
