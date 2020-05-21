package com.myapplicationdev.android.databasecrud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnAdd, btnEdit, btnRetrieve, btnSearch;
    TextView tvDBContent;
    EditText etContent, etSearch;
    ArrayList<Note> al;
    int requestCode = 1;
    ListView lv;
    ArrayAdapter aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize the variables with UI here
        btnAdd = (Button) findViewById(R.id.buttonInsert);
        btnEdit = (Button) findViewById(R.id.buttonEdit);
        btnRetrieve = (Button) findViewById(R.id.buttonRetrieve);
        btnSearch = (Button) findViewById(R.id.buttonSearch);
        tvDBContent = (TextView) findViewById(R.id.tvDBContent);
        etContent = (EditText) findViewById(R.id.etContent);
        etSearch = (EditText) findViewById(R.id.etSearch);
        lv = (ListView) findViewById(R.id.lv);

        al = new ArrayList<Note>();
        aa = new ArrayAdapter<Note>(this, android.R.layout.simple_list_item_1, al);
        lv.setAdapter(aa);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = etContent.getText().toString();
                DBHelper dbh = new DBHelper(MainActivity.this);
                long inserted_id = dbh.insertNote(data);
                dbh.close();

                if (inserted_id != -1){
                    Toast.makeText(MainActivity.this, "Insert successful",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnRetrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbh = new DBHelper(MainActivity.this);
                al.clear();
                al.addAll(dbh.getAllNotes());
                dbh.close();

                String txt = "";
                for (int i = 0; i< al.size(); i++){
                    Note tmp = al.get(i);
                    txt += "ID:" + tmp.getId() + ", " +
                            tmp.getNoteContent() + "\n";
                }
                tvDBContent.setText(txt);
                aa.notifyDataSetChanged();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper dbh = new DBHelper(MainActivity.this);
                al.clear();
                al.addAll(dbh.searchDB(etSearch.getText().toString()));
                dbh.close();

                String txt = "";
                for (int i = 0; i< al.size(); i++){
                    Note tmp = al.get(i);
                    txt += "ID:" + tmp.getId() + ", " +
                            tmp.getNoteContent() + "\n";
                }
                tvDBContent.setText(txt);
                aa.notifyDataSetChanged();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note target = al.get(0);
                Intent i = new Intent(MainActivity.this,
                        EditActivity.class);
                i.putExtra("data", target);
                startActivityForResult(i, requestCode);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int
                    position, long identity) {
                Note data = al.get(position);
                Intent i = new Intent(MainActivity.this,
                        EditActivity.class);
                i.putExtra("data", data);
                startActivityForResult(i, requestCode);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Only handle when 2nd activity closed normally
        //  and data contains something
        if(resultCode == RESULT_OK){
            if (data != null) {
                if(requestCode == requestCode) {
                    btnRetrieve.performClick();
                    Toast.makeText(MainActivity.this, "Reload test",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}