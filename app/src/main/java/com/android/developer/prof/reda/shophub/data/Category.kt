package com.android.developer.prof.reda.shophub.data

sealed class Category(val category: String) {
    object Chair: Category("Chairs")
    object Cupboard: Category("Cupboard")
    object Table: Category("Table")
    object Furniture: Category("Furniture")
    object Accessory: Category("Accessory")
    object Shoes: Category("Shoes")
    object Clothes: Category("Clothes")
}