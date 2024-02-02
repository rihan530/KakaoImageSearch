package com.example.kakaoimagesearch.storage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kakaoimagesearch.databinding.SearchItemBinding
import com.example.kakaoimagesearch.model.SearchModel
import com.example.kakaoimagesearch.util.Utils.getDateFromTimestampWithFormat

class StorageRVAdapter(var mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var imageList = mutableListOf<SearchModel>()
//    var itemClick: DocumentItemClick? = null

//    class ViewHolder (val binding : StorageItemBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(document: Document) {
//            Glide.with(binding.root.context)
//                .load(document.imageUrl)
//                .placeholder(binding.root.resources.getIdentifier("symbol_questionmark", "drawable", binding.root.context.packageName))
//                .into(binding.ivItem)
//            binding.tvFrom.text = document.sitename
//            binding.tvWriteTime.text = document.datetime
//        }
//    }

    fun setList(imageList : MutableList<SearchModel>) {
        this.imageList = imageList
        notifyItemRangeChanged(0, this.imageList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        val binding = (holder as ItemViewHolder).binding
        val items = imageList[position]

//        binding.ivItem.setOnClickListener {
//            val drawable = binding.ivItem.drawable
//            val imageView = binding.ivItem
//            itemClick?.onClick(it, items)
//        }

        Glide.with(mContext)
            .load(items.image)
            .into((holder as ItemViewHolder).iv_thum_image)

        holder.tv_title.text = items.sitename
        holder.iv_like.visibility = View.GONE
        holder.tv_datetime.text =
            getDateFromTimestampWithFormat(
                items.datetime,
                "yyyy-MM-dd'T'HH:mm:ss.SSS+09:00",
                "yyyy-MM-dd HH:mm:ss"
            )
    }

    inner class ItemViewHolder(binding: SearchItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var iv_thum_image: ImageView = binding.ivItem
        var iv_like: ImageView = binding.icFavorite
        var tv_title: TextView = binding.tvFrom
        var tv_datetime: TextView = binding.tvWriteTime
        var cl_item: ConstraintLayout = binding.thumbItem

        init {
            iv_like.visibility = View.GONE

            cl_item.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    imageList.removeAt(position)
                    notifyItemRemoved(position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

}