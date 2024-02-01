package com.example.kakaoimagesearch

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kakaoimagesearch.MainActivity.Companion.KEY_ADD
import com.example.kakaoimagesearch.data.Document
import com.example.kakaoimagesearch.data.ImageResponse
import com.example.kakaoimagesearch.data.SearchModel
import com.example.kakaoimagesearch.databinding.FragmentSearchListBinding
import com.example.kakaoimagesearch.retrofit.NetWorkInterface
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

interface FragmentDataListener {
    fun onDataReceived(data: Document)
}

class SearchFragment : Fragment(), OnItemClickListener {

    private var _binding: FragmentSearchListBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SearchViewModel by activityViewModels()

    private lateinit var document: MutableList<Document>

    private lateinit var searchRVAdapter: SearchRVAdapter

    private var listener: FragmentDataListener? = null

    // OnBackPressedCallback(뒤로가기 기능) 객체 선언
    private lateinit var callback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchListBinding.inflate(inflater, container, false)
        val bundle = Bundle() // 번들을 통해 값 전달
        postponeEnterTransition()
        callback = object : OnBackPressedCallback(true) {
            // 뒤로가기 했을 때 실행되는 기능
            override fun handleOnBackPressed() {
                savePref()
                activity?.finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadPref()

        // 검색창 엔터키 입력시
        binding.etSearch.setOnKeyListener { _, keyCode, _ ->
            when (keyCode) {
                KeyEvent.KEYCODE_ENTER -> {
                    sharedViewModel.searchTerm = binding.etSearch.text.toString()
                    sharedViewModel.page.value = 1
                    searchImage(sharedViewModel.searchTerm, sharedViewModel.page.value!!)
                    searchAction()
                    true
                }
                else -> false
            }
        }

        if (sharedViewModel.searchTerm.isNotEmpty()) {
            Log.d("로그", "sharedViewModel.searchTerm : ${sharedViewModel.searchTerm}")
            binding.etSearch.doOnPreDraw {
                binding.etSearch.setText(sharedViewModel.searchTerm)
            }
            searchImage(sharedViewModel.searchTerm, sharedViewModel.page.value!!)
        }

        binding.searchBtn.setOnClickListener {
            sharedViewModel.page.value = 1
            sharedViewModel.searchTerm = binding.etSearch.text.toString()
            searchImage(sharedViewModel.searchTerm, sharedViewModel.page.value!!)

            searchAction()
        }


        binding.searchBtn.setOnClickListener {
            val inputText = binding.etSearch.text.toString()
            sharedViewModel.page.value = sharedViewModel.page.value!! + 1

            if (inputText == sharedViewModel.searchTerm) {
                searchImage(inputText, sharedViewModel.page.value!!)
            } else {
                sharedViewModel.searchTerm = inputText
                searchImage(inputText)
            }
        }
    }

    // 키보드 내리고 EditText에서 포커스 클리어
    private fun searchAction() {
        val imm =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
        binding.etSearch.clearFocus()
    }

    private fun savePref() {
        val sharedPreferences = this.requireActivity().getSharedPreferences(SearchFragment.KEY_SEARCH, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString(SearchFragment.KEY_SEARCH, binding.etSearch.text.toString())
        Log.d(TAG, "savePref: ${SearchFragment.KEY_SEARCH}")
        editor.apply()
    }

    private fun loadPref() {
        val sharedPreferences = this.requireActivity().getSharedPreferences(SearchFragment.KEY_SEARCH, Context.MODE_PRIVATE)
        if (sharedPreferences.contains(SearchFragment.KEY_SEARCH)) {
            val searchText = sharedPreferences.getString(SearchFragment.KEY_SEARCH, "")
            with(binding) {
                etSearch.setText(searchText)
            }
            Log.d(TAG, "loadPref: $searchText")
        }
    }

    // 카카오 API 이미지 검색
    private fun searchImage(query: String, page: Int = 1) {
        val CLIENT_ID = "KakaoAK 72f1bc517f9c7728c12eadfefd955397"

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(NetWorkInterface::class.java)
        val callGetSearchImage = api.searchImage(CLIENT_ID, query, page)

        callGetSearchImage.enqueue(object : Callback<ImageResponse> {
            override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>) {
                if (response.isSuccessful) {
                    binding.list.layoutManager =
                        GridLayoutManager(context, 2)
                    searchRVAdapter = SearchRVAdapter(requireContext(), response.body()!!.documents, this@SearchFragment)
                    binding.list.adapter = searchRVAdapter

                    binding.list.doOnPreDraw {
                        startPostponedEnterTransition()
                    }

                    document = response.body()!!.documents
                }
            }

            override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
                Log.d("로그", "MainActivity - onFailure() 호출됨")
            }
        })
    }

    // 리사이클러뷰 아이템 클릭했을 때
    override fun onItemClicked(position: Int, imageView: ImageView, drawable: Drawable) {
        sharedViewModel.imgUrl.value = document[position].imageUrl
        sharedViewModel.document = document[position]
        sharedViewModel.drawable = drawable

        val extras = FragmentNavigatorExtras(
            imageView to document[position].imageUrl
        )
        addItemPref(document[position])
        var itemClick: DocumentItemClick? = null
        itemClick = object : DocumentItemClick {
            override fun onClick(view: View?, data: Document) {
                val clickedItem = data
                val bundle = Bundle()
                when (val item = data){
                    is Document ->  {
                        listener?.onDataReceived(item)
                        bundle.putParcelable(
                            IntentKeys.EXTRA_DOCUMENT,
                            clickedItem
                        )
                    }
                    else -> Unit
                }
                requireActivity().supportFragmentManager.beginTransaction().remove(SearchFragment()).commit()
            }
        }
    }

    private fun addItemPref(document: Document) {
        val sharedPreferences = this.requireActivity().getSharedPreferences(KEY_ADD, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = GsonBuilder().create()
        val tempArray : ArrayList<Document> = arrayListOf()
        val groupListType: Type = object : TypeToken<ArrayList<Document?>?>() {}.type // 이 부분이 중요하다. Json을 객체로 만들 때 타입을 추론하는 역할을 한다.
        val data = Document(document.datetime, document.sitename, document.docUrl, document.imageUrl, document.thumbnailUrl)
        val prev =sharedPreferences.getString(KEY_ADD,"none")
        if(prev!="none"){ //데이터가 비어있지 않다면?
            if(prev!="[]" || prev!="")tempArray.addAll(gson.fromJson(prev,groupListType))
            tempArray.add(data)
            val strList = gson.toJson(tempArray,groupListType)
            editor.putString(KEY_ADD,strList)
        }else{
            tempArray.add(data)
            val strList = gson.toJson(tempArray,groupListType)
            editor.putString(KEY_ADD,strList)
        }
        Log.d(TAG, "addItemPref: $KEY_ADD")
        Log.d(TAG, "addItemPref: $document")
        editor.apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        callback.remove()
    }

    companion object {
        private const val KEY_SEARCH = "search_text"
    }
}