package com.example.android.inventoryapp.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.android.inventoryapp.data.InventoryContract.ProductEntry;

import java.sql.SQLInput;

/**
 * Created by klaudia on 09/08/18.
 */

public class InventoryDbUtils {
    private InventoryDbUtils(){}

    public static String readFromDb(InventoryDbHelper dbHelper){
        StringBuilder builder = new StringBuilder();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(ProductEntry.TABLE_NAME, null, null, null, null, null, null);

        builder.append(ProductEntry._ID + " - " +
                ProductEntry.COLUMN_PRODUCT_NAME + " - " +
                ProductEntry.COLUMN_PRODUCT_PRICE + " - " +
                ProductEntry.COLUMN_PRODUCT_QUANTITY + " - " +
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME + " - " +
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE + "\n");

        try{

            int idColumnIndex = cursor.getColumnIndex(ProductEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE);

            while(cursor.moveToNext()){
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                String currentSupplierPhone = cursor.getString(supplierPhoneColumnIndex);

                builder.append("\n" + currentID + " - " +
                        currentName + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentSupplierName + " - " +
                        currentSupplierPhone);
            }
        } finally {
            cursor.close();
            return builder.toString();
        }

    }

    public static long insertDummyDataToDb(InventoryDbHelper dbHelper){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Headphones");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, 100);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 2);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, "Tommy");
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE, "555999000");

        long newRowId = db.insert(ProductEntry.TABLE_NAME, null, values);
        return newRowId;
    }

}
