package com.homework.grocerystore

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class StoreActivity : AppCompatActivity() {

    private lateinit var etProductName: EditText
    private lateinit var etProductPrice: EditText
    private lateinit var ivProductImage: ImageView
    private lateinit var btnAddProduct: Button
    private lateinit var lvProducts: ListView
    private lateinit var btnExit: Button

    private val productListAdapter = ProductListAdapter(this, mutableListOf())

    private val PICK_IMAGE = 100
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        etProductName = findViewById(R.id.etProductName)
        etProductPrice = findViewById(R.id.etProductPrice)
        ivProductImage = findViewById(R.id.ivProductImage)
        btnAddProduct = findViewById(R.id.btnAddProduct)
        lvProducts = findViewById(R.id.lvProducts)
        btnExit = findViewById(R.id.btnExit)

        lvProducts.adapter = productListAdapter

        ivProductImage.setOnClickListener {
            openGallery()
        }

        btnAddProduct.setOnClickListener {
            addProduct()
        }

        btnExit.setOnClickListener {
            finish()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            ivProductImage.setImageURI(selectedImageUri)
        }
    }

    private fun addProduct() {
        val name = etProductName.text.toString()
        val price = etProductPrice.text.toString()
        if (name.isNotEmpty() && price.isNotEmpty() && selectedImageUri != null) {
            val product = Product(name, price, selectedImageUri.toString())
            productListAdapter.addProduct(product)

            // Очищаем поля ввода
            etProductName.text.clear()
            etProductPrice.text.clear()
            ivProductImage.setImageResource(0)
            selectedImageUri = null
        } else {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
        }
    }
}