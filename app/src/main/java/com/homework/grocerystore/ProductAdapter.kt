package com.homework.grocerystore


import android.widget.BaseAdapter
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class ProductAdapter(context: Context, private val productList: List<Product>) :
    ArrayAdapter<Product>(context, 0, productList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)

        val product = productList[position]

        val ivProductImage: ImageView = view.findViewById(R.id.ivProductImage)
        val tvProductName: TextView = view.findViewById(R.id.tvProductName)
        val tvProductPrice: TextView = view.findViewById(R.id.tvProductPrice)

        // Загрузка изображения с помощью Glide
        Glide.with(context).load(product.imageUri).into(ivProductImage)
        tvProductName.text = product.name
        tvProductPrice.text = product.price

        return view
    }
}