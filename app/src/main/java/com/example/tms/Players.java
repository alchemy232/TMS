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
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent i = new Intent(pContext, AddFio.class);
                startActivityForResult (i, ADD_ACTIVITY);
                updateList();
//                Log.d("Alchemy", "Обновление таблицы игроков на листвью активити");
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.edit:
                Intent i = new Intent(pContext, AddFio.class); //сменить класс активности на редактирование players
                Games games = mDBConnector.select(info.id);
                i.putExtra("Matches", games);
                startActivityForResult(i, UPDATE_ACTIVITY);
                updateList();
                return true;
            case R.id.delete:
                mDBConnector.delete(info.id);
                updateList();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    private void updateList () {
        myAdapter.setArrayMyData(mDBConnector.selectAllFio());
        myAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Fio fio = (Fio) data.getExtras().getSerializable("Matches");
            if (requestCode == UPDATE_ACTIVITY) {
                mDBConnector.updateFio(fio);
//                Log.d("Alchemy", "if " + requestCode); // не понятно когда срабатывает
            }
            else {
                mDBConnector.insertFIO(fio.getFio());
//                Log.d("Alchemy", "else " + requestCode);
            }
            updateList();

        }
    }
    //разобрать этот класс
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
            Log.d("Alchemy","GetView пашет");
            if (convertView == null)
                convertView = mLayoutInflater.inflate(R.layout.itemfio, null); // To create itemfio.xml textView Fio !!

            TextView vFio= (TextView)convertView.findViewById(R.id.itemfiotextview);
            TextView vPos= (TextView)convertView.findViewById(R.id.numitem);


            Fio fio = arrayFio.get(position);
            vPos.setText(""+ (position+1));
//            Log.d("Alchemy","оно : " + position);
            vFio.setText(fio.getFio());
//            Log.d("Alchemy","Players " + fio.getFio()); // не доходит до вызова этого метода
            return convertView;
        }
    }
}
