package com.example.android.inventoryapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.inventoryapp.data.InventoryContract;

import static com.example.android.inventoryapp.data.InventoryContract.*;

/**
 * Created by klaudia on 11/08/18.
 */

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER_EDIT_PRODUCT_ID = 2;
    private Uri currentProductUri;

    private EditText nameEditText;
    private EditText priceEditText;
    private EditText quantityEditText;
    private EditText supplierNameEditText;
    private EditText supplierPhoneEditText;
    private Button plusButton;
    private Button minusButton;

    int quantity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        currentProductUri = intent.getData();

        findViewsById();
        plusAndMinusButttonsSetOnClickListeners();

        setAccordingTitle();

        if(isInEditorState()){
            getLoaderManager().initLoader(LOADER_EDIT_PRODUCT_ID, null, this);
        }
    }

    private void plusAndMinusButttonsSetOnClickListeners() {
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity += 1;
                quantityEditText.setText(Integer.toString(quantity));
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantity > 0){
                    quantity -= 1;
                    quantityEditText.setText(Integer.toString(quantity));
                }
            }
        });
    }

    private void findViewsById() {
        nameEditText = findViewById(R.id.edit_product_name);
        priceEditText = findViewById(R.id.edit_product_price);
        quantityEditText = findViewById(R.id.edit_product_quantity);
        supplierNameEditText = findViewById(R.id.edit_supplier_name);
        supplierPhoneEditText = findViewById(R.id.edit_supplier_phone);

        plusButton = findViewById(R.id.button_increase_quantity);
        minusButton = findViewById(R.id.button_decrease_quantity);
    }

    private void setAccordingTitle() {
        if(isInEditorState()){
            setTitle(R.string.edit_product_title);
        }
        else{
            setTitle(R.string.add_product_title);
        }
    }

    private boolean isInEditorState(){
        if(currentProductUri != null){
            return true;
        }
        return false;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE
        };
        return new CursorLoader(this, currentProductUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor.moveToFirst()) {
            nameEditText.setText(cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME)));
            priceEditText.setText(Integer.toString(cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE))));

            quantity = cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY));

            quantityEditText.setText(Integer.toString(quantity));
            supplierNameEditText.setText(cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME)));
            supplierPhoneEditText.setText(cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE)));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        nameEditText.setText(null);
        priceEditText.setText(null);
        quantityEditText.setText(null);
        supplierNameEditText.setText(null);
        supplierPhoneEditText.setText(null);
    }
}
