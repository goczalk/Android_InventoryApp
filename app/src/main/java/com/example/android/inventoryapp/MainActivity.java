package com.example.android.inventoryapp;

import android.content.ContentUris;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

        ListView inventoryListView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        inventoryListView.setEmptyView(emptyView);

        InventoryCursorAdapter inventoryCursorAdapter = new InventoryCursorAdapter(this, null);
        inventoryListView.setAdapter(inventoryCursorAdapter);

//        getLoaderManager().initLoader(PET_LOADER, null, this);

    }

}
