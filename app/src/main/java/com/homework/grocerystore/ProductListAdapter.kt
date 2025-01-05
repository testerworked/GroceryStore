package com.homework.grocerystore

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class ProductListAdapter(private val context: Context, private var productList: MutableList<Product>) : BaseAdapter() {

    override fun getCount(): Int = productList.size

    override fun getItem(position: Int): Any = productList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_product, parent, false)

        val productName: TextView = view.findViewById(R.id.tvProductName)
        val productPrice: TextView = view.findViewById(R.id.tvProductPrice)
        val productImage: ImageView = view.findViewById(R.id.ivProductImage)

        val product = productList[position]

        productName.text = product.name
        productPrice.text = "${product.price} ₽"
        productImage.setImageURI(Uri.parse(product.imageUri))

        return view
    }

    fun addProduct(product: Product) {
        productList.add(product)
        notifyDataSetChanged()
    }
}