package com.example.android.inventoryapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.InventoryDbHelper;
import com.example.android.inventoryapp.data.InventoryDbUtils;

public class MainActivity extends AppCompatActivity {

    private InventoryDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new InventoryDbHelper(this);

        long newRowId = InventoryDbUtils.insertDummyDataToDb(dbHelper);
        if(newRowId == -1){
            Toast.makeText(this, "Error writing data to databe", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Data added", Toast.LENGTH_SHORT).show();
        }

        String dbResult = InventoryDbUtils.readFromDb(dbHelper);
        TextView textView = findViewById(R.id.text_view);
        textView.setText(dbResult);
    }

}
