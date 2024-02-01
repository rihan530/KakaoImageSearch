package com.example.kakaoimagesearch

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.kakaoimagesearch.MainActivity.Companion.KEY_ADD
import com.example.kakaoimagesearch.MainActivity.Companion.KEY_DELETE
import com.example.kakaoimagesearch.data.Document
import com.example.kakaoimagesearch.databinding.FragmentStorageListBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONException
import java.lang.reflect.Type


/**
 * A fragment representing a list of Items.
 */
class StorageFragment : Fragment(), OnItemClickListener {

    private var columnCount = 2

    private var _binding: FragmentStorageListBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SearchViewModel by activityViewModels()

    private lateinit var document: MutableList<Document>

    private var receivedItem: Document? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transitionInflater = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = transitionInflater
        sharedElementReturnTransition = transitionInflater
        arguments?.let {
            receivedItem = it.getParcelable(IntentKeys.EXTRA_DOCUMENT)
            Log.d(TAG, "receivedItem: $receivedItem")
        }
        val list: ArrayList<Document> = getStringArrayPref(requireContext(), KEY_ADD)
        if (list != null) {
            for (value in list) {
                Log.d(TAG, "Get json : $value")
            }
        }
    }

    private fun getStringArrayPref(context: Context, key: String): ArrayList<Document> {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val json = prefs.getString(key, null)
        val urls = ArrayList<Document>()
        if (json != null) {
            try {
                val a = JSONArray(json)
                for (i in 0 until a.length()) {
                    val url = Document()
                    urls.add(url)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return urls
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentStorageListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun loadPref() {
        val sharedPreferences = this.requireActivity().getSharedPreferences(KEY_ADD, Context.MODE_PRIVATE)
        val gson = GsonBuilder().create()
        val groupListType: Type = object : TypeToken<ArrayList<Document?>?>() {}.type
        if (sharedPreferences.contains(KEY_ADD)) {
            val tempArray : ArrayList<Document> = arrayListOf()
            val favItem = sharedPreferences.getString(KEY_ADD, "")
            val items = Gson().fromJson(
                favItem,
                tempArray::class.java
            )

            Log.d(ContentValues.TAG, "loadPref: $favItem")
            Log.d(ContentValues.TAG, "items: $items")
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loadPref()
    }

    override fun onResume() {
        super.onResume()
        loadPref()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(data: Document) =
            StorageFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(IntentKeys.EXTRA_DOCUMENT, data)
                }
                Log.d(TAG, "newInstance: $arguments")
            }
    }

    private fun deleteItemPref(document: Document) {
        val sharedPreferences = this.requireActivity().getSharedPreferences(KEY_ADD, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = GsonBuilder().create()
        val tempArray : ArrayList<Document> = arrayListOf()
        val groupListType: Type = object : TypeToken<ArrayList<Document?>?>() {}.type // 이 부분이 중요하다. Json을 객체로 만들 때 타입을 추론하는 역할을 한다.
        val data = Document(document.datetime, document.sitename, document.docUrl, document.imageUrl, document.thumbnailUrl)
        val prev =sharedPreferences.getString(KEY_ADD,"none")

        if(prev!="none"){ //데이터가 비어있지 않다면?
            if(prev!="[]" || prev!="")
            tempArray.remove(data)
            val strList = gson.toJson(tempArray,groupListType)
            editor.putString(KEY_ADD,strList)
        }else{
            tempArray.remove(data)
            val strList = gson.toJson(tempArray,groupListType)
            editor.putString(KEY_ADD,strList)
        }
        Log.d(ContentValues.TAG, "addItemPref: $KEY_DELETE")
        Log.d(ContentValues.TAG, "addItemPref: $document")
        editor.apply()
    }

    override fun onItemClicked(position: Int, imageView: ImageView, drawable: Drawable) {
        sharedViewModel.imgUrl.value = document[position].imageUrl
        sharedViewModel.document = document[position]
        sharedViewModel.drawable = drawable

        deleteItemPref(document[position])

    }
}