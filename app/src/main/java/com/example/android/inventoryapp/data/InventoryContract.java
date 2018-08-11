package com.example.android.inventoryapp.data;

import android.provider.BaseColumns;

/**
 * Created by klaudia on 09/08/18.
 */

public final class InventoryContract {
  private InventoryContract() {
    throw new AssertionError("No InventoryContractinstances for you!");
  }

  public static class ProductEntry implements BaseColumns{
    public static final String TABLE_NAME = "products";
    public static final String _ID = "_id";
    public static final String COLUMN_PRODUCT_NAME = "name";
    public static final String COLUMN_PRODUCT_PRICE = "price";
    public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
    public static final String COLUMN_PRODUCT_SUPPLIER_NAME = "supplier_name";
    public static final String COLUMN_PRODUCT_SUPPLIER_PHONE = "supplier_phone";
  }
}