package com.example.sdppetugas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lvKendaraan;
    ArrayList<String> arrInfo;
    ArrayList<String> arrOnGoing,arrPlat;
    ArrayAdapter<String> infoAdapter;
    EditText etSearch;
    TextWatcher twSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvKendaraan = findViewById(R.id.lvKendaraan);
        etSearch = findViewById(R.id.etSearch);
        arrInfo =  new ArrayList<String>();
        arrOnGoing = new ArrayList<String>();
        arrPlat = new ArrayList<String>();
        infoAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrInfo);
        arrInfo.add("Dicky");
        arrInfo.add("Aucky");
        arrInfo.add("Adrian");
        arrInfo.add("Albert");
        arrInfo.add("Adam");
        arrInfo.add("Gilbert");
        arrInfo.add("Sion");
        arrInfo.add("Farrell");
        arrInfo.add("Cosmas");
        arrInfo.add("Edwin");
        arrInfo.add("Ferdinan");
        arrInfo.add("Alfon");
        arrInfo.add("Yoel");
        arrInfo.add("Daniel");
        arrInfo.add("Leo");
        lvKendaraan.setAdapter(infoAdapter);
        if(getIntent()!=null){
            String plat = getIntent().getStringExtra("plat");
            for (int a = 0; a < arrInfo.size(); a++) {
                if(arrInfo.get(a).equals(getIntent().getStringExtra("nama"))){
                    arrOnGoing.add(arrInfo.get(a));
                    arrPlat.add(plat);
                    arrInfo.remove(a);
                }
            }
            infoAdapter.notifyDataSetChanged();
        }
        twSearch = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                infoAdapter.getFilter().filter(charSequence);
                infoAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        etSearch.addTextChangedListener(twSearch);
        lvKendaraan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,statusKendaraan.class);
                intent.putExtra("nama",arrInfo.get(i));
                intent.putStringArrayListExtra("info",arrInfo);
                finish();
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.optOnGoing){
            Intent i = new Intent(this,cekActivity.class);
            i.putStringArrayListExtra("ongoing",arrOnGoing);
            i.putStringArrayListExtra("platno",arrPlat);
            finish();
            startActivity(i);
        }
        else if(item.getItemId()==R.id.optLogout){
            Intent i = new Intent(this,LoginActivity.class);
            finish();
            startActivity(i);
        }
        return true;
    }
}
