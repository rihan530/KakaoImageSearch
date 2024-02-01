package com.example.kakaoimagesearch

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kakaoimagesearch.data.Document
import com.example.kakaoimagesearch.databinding.SearchItemBinding

interface OnItemClickListener {
    fun onItemClicked(position: Int, imageView: ImageView, drawable: Drawable)
}

interface DocumentItemClick {
    fun onClick(view: View?, data: Document)
}

class MyViewHolder(val binding: SearchItemBinding) :
    RecyclerView.ViewHolder(binding.root)

class SearchRVAdapter(
    private val context: Context, private val searchList: MutableList<Document>, myInterface: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var myClickInterface = myInterface
    private var imageList = mutableListOf<Document>()
    var itemClick: DocumentItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)).apply {
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        val items = searchList[position]
        Glide.with(context)
            .load(items.thumbnailUrl)
            .load(items.imageUrl)
            .dontTransform()
            .into(binding.ivItem)
        binding.ivItem.transitionName = items.imageUrl

        binding.ivItem.setOnClickListener {
            val drawable = binding.ivItem.drawable
            val imageView = binding.ivItem
            myClickInterface.onItemClicked(position, imageView, drawable!!)
            itemClick?.onClick(it, items)
        }
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

}