package com.homework.grocerystore

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class StoreActivity : AppCompatActivity() {

    private val btnSelectImage by lazy { findViewById<Button>(R.id.btnSelectImage) }
    private val btnAddProduct by lazy { findViewById<Button>(R.id.btnAddProduct) }
    private val etProductName by lazy { findViewById<EditText>(R.id.etProductName) }
    private val etProductPrice by lazy { findViewById<EditText>(R.id.etProductPrice) }
    private val lvProducts by lazy { findViewById<ListView>(R.id.lvProducts) }

    private lateinit var imageView: ImageView

    private val productList = mutableListOf<Product>()
    private lateinit var productAdapter: ProductAdapter

    private val GALLERY_REQUEST_CODE = 1001
    private var selectedImageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        productAdapter = ProductAdapter(this, productList)
        lvProducts.adapter = productAdapter

        imageView = findViewById(R.id.imageViewStore)
        btnSelectImage.setOnClickListener {
            openGallery()
        }

        btnAddProduct.setOnClickListener {
            val name = etProductName.text.toString()
            val price = etProductPrice.text.toString()

            if (name.isNotEmpty() && price.isNotEmpty() && selectedImageUri != null) {
                val product = Product(name, price, selectedImageUri!!)
                productList.add(product)
                productAdapter.notifyDataSetChanged()
                clearFields()
            } else {
                Toast.makeText(this, "Заполните все поля и выберите изображение", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            selectedImageUri?.let {
                imageView.setImageURI(selectedImageUri)
            }
        }
    }

    private fun clearFields() {
        etProductName.text.clear()
        etProductPrice.text.clear()
        selectedImageUri = null
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_exit -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}