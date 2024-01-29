package com.example.kakaoimagesearch

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.kakaoimagesearch.placeholder.PlaceholderContent.PlaceholderItem
import com.example.kakaoimagesearch.databinding.FragmentSearchListBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class SearchRVAdapter(
    private val values: List<PlaceholderItem>,
) : RecyclerView.Adapter<SearchRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentSearchListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentSearchListBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

}