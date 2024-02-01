package com.example.kakaoimagesearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kakaoimagesearch.data.Document
import com.example.kakaoimagesearch.data.SearchModel
import com.example.kakaoimagesearch.databinding.StorageItemBinding

class StorageRVAdapter(private val searchList: MutableList<Document>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var imageList = mutableListOf<Document>()
    var itemClick: DocumentItemClick? = null

    class ViewHolder (val binding : StorageItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(document: Document) {
            Glide.with(binding.root.context)
                .load(document.imageUrl)
                .placeholder(binding.root.resources.getIdentifier("symbol_questionmark", "drawable", binding.root.context.packageName))
                .into(binding.ivItem)
            binding.tvFrom.text = document.sitename
            binding.tvWriteTime.text = document.datetime
        }
    }

    fun setList(imageList : MutableList<Document>) {
        this.imageList = imageList
        notifyItemRangeChanged(0, this.imageList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(StorageItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)).apply {

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        val items = searchList[position]

        binding.ivItem.setOnClickListener {
            val drawable = binding.ivItem.drawable
            val imageView = binding.ivItem
            itemClick?.onClick(it, items)
        }
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

}