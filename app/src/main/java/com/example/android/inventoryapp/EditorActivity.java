package com.example.android.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.android.inventoryapp.data.InventoryContract.ProductEntry;

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
        else{
            quantityEditText.setText(String.valueOf(0));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_product:
                saveProduct();
                finish();
                return true;
            case R.id.action_delete_product:
                deleteProduct();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveProduct() {
        String name = null, priceString = null, quantityString = null;

        name = nameEditText.getText().toString().trim();

        priceString = priceEditText.getText().toString().trim();
        quantityString = quantityEditText.getText().toString().trim();

        String supplierName = supplierNameEditText.getText().toString().trim();
        String supplierPhone = supplierPhoneEditText.getText().toString().trim();

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(priceString) || TextUtils.isEmpty(quantityString)){
            Toast.makeText(this, R.string.toast_msg_cannot_save, Toast.LENGTH_LONG).show();
            return;
        }

        int price = Integer.parseInt(priceString);
        int quantity = Integer.parseInt(quantityString);

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, name);
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, price);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, supplierName);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE, supplierPhone);

        if(isInEditorState()){
            int rowsUpdated = getContentResolver().update(currentProductUri, values,null, null);
            if(rowsUpdated == 0){
                Toast.makeText(this, R.string.toast_msg_update_failed, Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, R.string.toast_msg_update_success, Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);
            if(newUri == null){
                Toast.makeText(this, R.string.toast_msg_insert_failed, Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, R.string.toast_msg_insert_success, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void deleteProduct() {
        int rowDeleted = getContentResolver().delete(currentProductUri, null, null);
        if(rowDeleted == 0){
            Toast.makeText(this, R.string.toast_msg_deletion_failed, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, R.string.toast_msg_deletion_success, Toast.LENGTH_SHORT).show();
        }
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
