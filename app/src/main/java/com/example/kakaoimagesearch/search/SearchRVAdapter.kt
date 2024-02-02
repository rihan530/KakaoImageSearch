package com.example.kakaoimagesearch.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kakaoimagesearch.MainActivity
import com.example.kakaoimagesearch.databinding.SearchItemBinding
import com.example.kakaoimagesearch.model.SearchModel
import com.example.kakaoimagesearch.util.Utils.getDateFromTimestampWithFormat
import java.util.Stack




//interface OnItemClickListener {
//    fun onItemClicked(position: Int, imageView: ImageView, drawable: Drawable)
//}

//interface DocumentItemClick {
//    fun onClick(view: View?, data: SearchModel)
//}

//class MyViewHolder(val binding: SearchItemBinding) :
//    RecyclerView.ViewHolder(binding.root)

class SearchRVAdapter(private val mContext: Context) : RecyclerView.Adapter<SearchRVAdapter.ItemViewHolder>() {

    var imageList = ArrayList<SearchModel>()
//    var itemClick: DocumentItemClick? = null

    fun clearItem() {
        imageList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val items = imageList[position]
        Glide.with(mContext)
            .load(items.image)
            .into(holder.iv_thum_image)

        holder.iv_like.visibility = if (items.isLike) {
            View.VISIBLE
        } else {
            View.GONE
        }
        holder.tv_title.text = items.sitename
        holder.tv_datetime.text = getDateFromTimestampWithFormat(
            items.datetime,
            "yyyy-MM-dd'T'HH:mm:ss.SSS+09:00",
            "yyyy-MM-dd HH:mm:ss"
        )
//        binding.ivItem.setOnClickListener {
//            val drawable = binding.ivItem.drawable
//            val imageView = binding.ivItem
//            myClickInterface.onItemClicked(position, imageView, drawable!!)
//            itemClick?.onClick(it, items)
//
//        }
    }

    override fun getItemCount() = imageList.size

    inner class ItemViewHolder(binding: SearchItemBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        var iv_thum_image: ImageView = binding.ivItem
        var iv_like: ImageView = binding.icFavorite
        var tv_title: TextView = binding.tvFrom
        var tv_datetime: TextView = binding.tvWriteTime
        var cl_thumb_item: ConstraintLayout = binding.thumbItem

        init {
            iv_like.visibility = View.GONE
            iv_thum_image.setOnClickListener(this)
            cl_thumb_item.setOnClickListener(this)
        }

        /**
         * 각 항목 클릭 시 발생하는 이벤트를 처리하는 메서드입니다.
         */
        override fun onClick(view: View) {
            val position = adapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return
            val item = imageList[position]

            item.isLike = !item.isLike

            if (item.isLike) {
                (mContext as MainActivity).addLikedItem(item)
            } else {
                (mContext as MainActivity).removeLikedItem(item)
            }
            notifyItemChanged(position)
        }
    }
}