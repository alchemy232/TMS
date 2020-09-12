package com.example.tms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;


public class Players extends AppCompatActivity {
    Context pContext;
    DataBase mDBConnector;
    ListView mListView;
    myListAdapter myAdapter;
    int ADD_ACTIVITY = 0;
    int UPDATE_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.players);
        pContext=this;
        mDBConnector=new DataBase(this);
        mListView=(ListView)findViewById(R.id.listplayers);
        myAdapter=new myListAdapter(pContext,mDBConnector.selectAllFio()); // переписать метод selectAll на таблицу ФИО
        mListView.setAdapter(myAdapter);
        registerForContextMenu(mListView);
        Log.d("Alchemy"," пашет");
    }
    // создаем меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    // описываем функционал меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent i = new Intent(pContext, AddFio.class);
                startActivityForResult (i, ADD_ACTIVITY);
                updateList();
                return true;
            case R.id.deleteAll:
                mDBConnector.deleteAllFio();
                updateList();
                return true;
            case R.id.exit:
                finish();
                return true;
            case R.id.changeActivity:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.graphActivity:
                Intent intentGraph = new Intent(this, Graph.class);
                startActivity(intentGraph);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // создаем меню долгого нажатия
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }
    // описываем функуионал меню долгого нажатия
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.edit:
                Intent i = new Intent(pContext, AddFio.class); //сменить класс активности на редактирование players
                Fio fio = mDBConnector.selectFio(info.id);
                i.putExtra("Matches", fio);
                startActivityForResult(i, UPDATE_ACTIVITY);
                updateList();
                return true;
            case R.id.delete:
                mDBConnector.deleteFio(info.id);
                updateList();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    // вытаскиваем в отдельный поток обновление списка игр
    Runnable runnable = new Runnable() {
        public void run() {
            myAdapter.setArrayMyData(mDBConnector.selectAllFio());
        }
    };
    // обновляем список
    private void updateList () {
        Thread thread = new Thread(runnable);
        thread.start();
        myAdapter.notifyDataSetChanged();
    }
    // при возвращении интента в зависимости от метки или обновляем список на players или добавляем данные в БД
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Fio fio = (Fio) data.getExtras().getSerializable("Matches");
            if (requestCode == UPDATE_ACTIVITY) {
                mDBConnector.updateFio(fio);
            }
            else {
                mDBConnector.insertFIO(fio.getFio());
            }
            updateList();

        }
    }
    // создаем новый класс - адаптер для формарования списка list на activity_main
    class myListAdapter extends BaseAdapter {
        private LayoutInflater mLayoutInflater;
        private ArrayList<Fio> arrayFio;

        public myListAdapter (Context ctx, ArrayList<Fio> arr) {
            mLayoutInflater = LayoutInflater.from(ctx);
            setArrayMyData(arr);
        }

        public ArrayList<Fio> getArrayMyData() {
            return arrayFio;
        }

        public void setArrayMyData(ArrayList<Fio> arrayMyData) {
            this.arrayFio = arrayMyData;
        }

        public int getCount () {
            return arrayFio.size();
        }

        public Object getItem (int position) {

            return position;
        }

        public long getItemId (int position) {
            Fio fio = arrayFio.get(position);
            if (fio != null) {
                return fio.getId();
            }
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = mLayoutInflater.inflate(R.layout.itemfio, null);

            TextView vFio= (TextView)convertView.findViewById(R.id.itemfiotextview);
            TextView vPos= (TextView)convertView.findViewById(R.id.numitem);


            Fio fio = arrayFio.get(position);
            vPos.setText(""+ (position+1));
            vFio.setText(fio.getFio());
            return convertView;
        }
    }
}
