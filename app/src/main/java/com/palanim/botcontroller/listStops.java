package com.palanim.botcontroller;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class listStops extends AppCompatActivity {
    ListView stops;
    TextView targetView;
    String finalTarget;
    String countryList[] = {"Master Bedroom", "BedRoom2", "Lavatory", "Kitchen", "Store Room", "Balcony", "Verandah", "Dining Hall", "Hall"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_stops);
        targetView=(TextView)findViewById(R.id.target);
        stops = (ListView)findViewById(R.id.stops);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , countryList);
        stops.setAdapter(arrayAdapter);

        stops.setOnItemClickListener(this::onItemClick);

        };

    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        Log.i("Target",      parent.getItemAtPosition(position).toString()   );
        targetView.setText(parent.getItemAtPosition(position).toString());
    }
    }
