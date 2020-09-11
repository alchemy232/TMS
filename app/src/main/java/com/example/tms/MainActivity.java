package com.example.tms;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DataBase mDBConnector;
    Context mContext;
    ListView mListView;
//    SimpleCursorAdapter scAdapter;
//    Cursor cursor;
    myListAdapter myAdapter;

    int ADD_ACTIVITY = 0;
    int UPDATE_ACTIVITY = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext=this;
        mDBConnector=new DataBase(this);
        mListView=(ListView)findViewById(R.id.list);
        myAdapter=new myListAdapter(mContext,mDBConnector.selectAll());
        mListView.setAdapter(myAdapter);
        registerForContextMenu(mListView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent i = new Intent(mContext, AddActivity.class);
                startActivityForResult (i, ADD_ACTIVITY);
                updateList();
                return true;
            case R.id.deleteAll:
                mDBConnector.deleteAll();
                updateList();
                return true;
            case R.id.exit:
                finish();
                return true;
            case R.id.changeActivity:
//                Intent intent = new Intent(mContext, Players.class);
                Intent intent = new Intent(mContext, Players.class);
                startActivity(intent);
                return true;
            case R.id.graphActivity:
                Intent intentGraph = new Intent(mContext, Graph.class);
                startActivity(intentGraph);
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
                Log.d("Alchemy","onContextItemSelected" +  info + " - " + info.id);
                Intent i = new Intent(mContext, AddActivity.class);
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
    Runnable runnable = new Runnable() {
        public void run() {
            myAdapter.setArrayMyData(mDBConnector.selectAll());
        }
    };
            private void updateList () {
                Thread thread = new Thread(runnable);
                thread.start();
//        myAdapter.setArrayMyData(mDBConnector.selectAll());
        myAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Games games = (Games) data.getExtras().getSerializable("Matches");
            if (requestCode == UPDATE_ACTIVITY)
                mDBConnector.update(games);
            else
                mDBConnector.insertGames(games.getDate(), games.getGameNum(), games.getPoints(), games.getFio(), games.getCorp(), games.getNumOfPlayers());
            updateList();
        }
    }

    class myListAdapter extends BaseAdapter {
        private LayoutInflater mLayoutInflater;
        private ArrayList<Games> arrayMyMatches;

        public myListAdapter (Context ctx, ArrayList<Games> arr) {
            mLayoutInflater = LayoutInflater.from(ctx);
            setArrayMyData(arr);
        }

        public ArrayList<Games> getArrayMyData() {
            return arrayMyMatches;
        }

        public void setArrayMyData(ArrayList<Games> arrayMyData) {
            this.arrayMyMatches = arrayMyData;
        }

        public int getCount () {
            return arrayMyMatches.size();
        }

        public Object getItem (int position) {

            return position;
        }

        public long getItemId (int position) {
            Games games = arrayMyMatches.get(position);
            if (games != null) {
                return games.getId();
            }
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = mLayoutInflater.inflate(R.layout.item, null);

            TextView vDate= (TextView)convertView.findViewById(R.id.date);
            TextView vGamenum = (TextView)convertView.findViewById(R.id.gamenum);
            TextView vPoints=(TextView)convertView.findViewById(R.id.points);
            TextView vNumofplayers=(TextView)convertView.findViewById(R.id.numofplayers);
            TextView vFIO=(TextView)convertView.findViewById(R.id.fio);
            TextView vCorp=(TextView)convertView.findViewById(R.id.corp);


            Games games = arrayMyMatches.get(position);
            vDate.setText(games.getDate());
            vGamenum.setText(""+ games.getGameNum());
            vPoints.setText(""+ games.getPoints());
            vNumofplayers.setText(""+ games.getNumOfPlayers());
            vFIO.setText(games.getFio());
            vCorp.setText(games.getCorp());
//            Log.d("Alchemy","mainactivity" + games.getCorp());
            return convertView;
        }
    } // end myAdapter
}