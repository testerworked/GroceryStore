package com.homework.grocerystore

import android.net.Uri
import android.widget.ImageView


data class Product(
    val name: String,
    val price: String,
    val imageUri: Uri
)