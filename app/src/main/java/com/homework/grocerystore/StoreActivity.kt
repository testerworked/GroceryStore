package com.homework.grocerystore

import android.content.Context
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider

class StoreActivity : AppCompatActivity() {

    private val btnSelectImage by lazy { findViewById<Button>(R.id.btnSelectImage) }
    private val btnAddProduct by lazy { findViewById<Button>(R.id.btnAddProduct) }
    private val etProductName by lazy { findViewById<EditText>(R.id.etProductName) }
    private val etProductPrice by lazy { findViewById<EditText>(R.id.etProductPrice) }
    private lateinit var rvProducts: RecyclerView

    private lateinit var ivProductImage: ImageView

    private val productList = mutableListOf<Product>()
    private lateinit var productAdapter: ProductAdapter

    private val GALLERY_REQUEST_CODE = 1001
    private var selectedImageUri: Uri? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        productList.addAll(loadProducts())

        rvProducts = findViewById(R.id.rvProducts)
        rvProducts.layoutManager = LinearLayoutManager(this)
        productAdapter = ProductAdapter(this, productList)
        rvProducts.adapter = productAdapter


        ivProductImage = findViewById(R.id.imageViewStore)
        btnSelectImage.setOnClickListener {
            pickFromGallery()
        }

        btnAddProduct.setOnClickListener {
            val name = etProductName.text.toString()
            val price = etProductPrice.text.toString().toDouble()

            if (selectedImageUri != null) {
                productList.add(Product(name, price.toString(), selectedImageUri!!))
                productList.add(Product(name, price.toString(),  Uri.parse(R.drawable.ic_launcher_foreground.toString())))
            } else {
                Toast.makeText(this, "Пожалуйста, выберите изображение.", Toast.LENGTH_SHORT).show()
            }
            productAdapter.notifyDataSetChanged()
            saveProducts()
            etProductName.text.clear()
            etProductPrice.text.clear()
            ivProductImage.setImageResource(R.drawable.ic_launcher_background)
        }

    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data
            selectedImageUri?.let {
                ivProductImage.setImageURI(selectedImageUri)
            }
        }


    }

    private fun clearFields() {
        etProductName.text.clear()
        etProductPrice.text.clear()
        selectedImageUri = null
    }


    private fun saveProducts() {
        val sharedPreferences = getSharedPreferences("products", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("size", productList.size)
        for (i in 0 until productList.size) {
            editor.putString("name_$i", productList[i].name)
            editor.putLong("price_$i", productList[i].price.toLong())
            editor.putString("image_$i", productList[i].imageUri.toString())
        }
        editor.apply()
    }

    private fun loadProducts(): List<Product> {
        val sharedPreferences = getSharedPreferences("products", MODE_PRIVATE)
        val size = sharedPreferences.getInt("size", 0)
        val loadedProducts = mutableListOf<Product>()
        for (i in 0 until size) {
            val name = sharedPreferences.getString("name_$i", "") ?: ""
            val price = sharedPreferences.getFloat("price_$i", 0f).toDouble() // Изменено на getFloat
            val image = Uri.parse(sharedPreferences.getString("image_$i", ""))
            loadedProducts.add(Product(name, price.toString(), image))
        }
        return loadedProducts
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