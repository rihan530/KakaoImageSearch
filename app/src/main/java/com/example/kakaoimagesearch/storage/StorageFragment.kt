package com.example.kakaoimagesearch.storage

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.kakaoimagesearch.MainActivity
import com.example.kakaoimagesearch.SearchViewModel
import com.example.kakaoimagesearch.databinding.FragmentStorageBinding
import com.example.kakaoimagesearch.model.SearchModel


/**
 * A fragment representing a list of Items.
 */
class StorageFragment : Fragment() {

    private var columnCount = 2

    private var binding: FragmentStorageBinding? = null

    private val sharedViewModel: SearchViewModel by activityViewModels()

    private lateinit var adapter: StorageRVAdapter

    private lateinit var mContext: Context

    private var likedItem: List<SearchModel> = listOf()

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val transitionInflater = TransitionInflater.from(requireContext())
//            .inflateTransition(android.R.transition.move)
//        sharedElementEnterTransition = transitionInflater
//        sharedElementReturnTransition = transitionInflater
//        arguments?.let {
////            receivedItem = it.getParcelable(IntentKeys.EXTRA_DOCUMENT)
////            Log.d(TAG, "receivedItem: $receivedItem")
//        }
//    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val mainActivity = activity as MainActivity
        likedItem = mainActivity.likedItems

        Log.d("StorageFragment", "likedItems size = ${likedItem.size}")

        adapter = StorageRVAdapter(mContext).apply {
            imageList = likedItem.toMutableList()
        }


        binding = FragmentStorageBinding.inflate(inflater, container, false).apply {
            rvStorageList.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            rvStorageList.adapter = adapter
        }

        return binding?.root
    }

//    private fun loadPref() {
//        val sharedPreferences = this.requireActivity().getSharedPreferences(KEY_ADD, Context.MODE_PRIVATE)
//        val gson = GsonBuilder().create()
//        val groupListType: Type = object : TypeToken<ArrayList<SearchModel?>?>() {}.type
//        if (sharedPreferences.contains(KEY_ADD)) {
//            val tempArray : ArrayList<SearchModel> = arrayListOf()
//            val favItem = sharedPreferences.getString(KEY_ADD, "")
//            val items = Gson().fromJson(
//                favItem,
//                tempArray::class.java
//            )
//
//            Log.d(ContentValues.TAG, "loadPref: $favItem")
//            Log.d(ContentValues.TAG, "items: $items")
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

//    companion object {
//        fun newInstance(data: SearchModel) =
//            StorageFragment().apply {
//                arguments = Bundle().apply {
////                    putParcelable(IntentKeys.EXTRA_DOCUMENT, data)
//                }
//                Log.d(TAG, "newInstance: $arguments")
//            }
//    }

//    private fun deleteItemPref(document: SearchModel) {
//        val sharedPreferences = this.requireActivity().getSharedPreferences(KEY_ADD, Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        val gson = GsonBuilder().create()
//        val tempArray : ArrayList<SearchModel> = arrayListOf()
//        val groupListType: Type = object : TypeToken<ArrayList<SearchModel?>?>() {}.type // 이 부분이 중요하다. Json을 객체로 만들 때 타입을 추론하는 역할을 한다.
//        val data = SearchModel(document.datetime, document.sitename, document.image)
//        val prev =sharedPreferences.getString(KEY_ADD,"none")
//
//        if(prev!="none"){ //데이터가 비어있지 않다면?
//            if(prev!="[]" || prev!="")
//            tempArray.remove(data)
//            val strList = gson.toJson(tempArray,groupListType)
//            editor.putString(KEY_ADD,strList)
//        }else{
//            tempArray.remove(data)
//            val strList = gson.toJson(tempArray,groupListType)
//            editor.putString(KEY_ADD,strList)
//        }
//        Log.d(ContentValues.TAG, "addItemPref: $KEY_DELETE")
//        Log.d(ContentValues.TAG, "addItemPref: $document")
//        editor.apply()
//    }
}