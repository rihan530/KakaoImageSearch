package com.example.kakaoimagesearch

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kakaoimagesearch.data.Document

class SearchViewModel : ViewModel() {

    private var _searchTerm = ""
    var searchTerm: String = _searchTerm

    private var _imgUrl = MutableLiveData<String>("")
    val imgUrl: MutableLiveData<String> get() = _imgUrl

    var document = Document()

    private var _page = MutableLiveData<Int>(0)
    val page: MutableLiveData<Int> get() = _page

    var drawable: Drawable? = null
}