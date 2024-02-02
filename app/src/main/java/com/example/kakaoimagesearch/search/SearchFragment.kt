package com.example.kakaoimagesearch.search

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.kakaoimagesearch.SearchViewModel
import com.example.kakaoimagesearch.data.ImageModel
import com.example.kakaoimagesearch.databinding.FragmentSearchBinding
import com.example.kakaoimagesearch.model.SearchModel
import com.example.kakaoimagesearch.retrofit.NetWorkClient.apiService
import com.example.kakaoimagesearch.util.Constants
import com.example.kakaoimagesearch.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//interface FragmentDataListener {
//    fun onDataReceived(data: SearchModel)
//}

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
//    private val binding get() = _binding!!

//    private val sharedViewModel: SearchViewModel by activityViewModels()

//    private lateinit var document: ArrayList<SearchModel>

    private var searchItems: ArrayList<SearchModel> = ArrayList()

    private lateinit var context: Context

    private lateinit var adapter: SearchRVAdapter

    private lateinit var gridmanager: StaggeredGridLayoutManager

//    private var listener: FragmentDataListener? = null

    // OnBackPressedCallback(뒤로가기 기능) 객체 선언
//    private lateinit var callback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
//        val bundle = Bundle() // 번들을 통해 값 전달
//        postponeEnterTransition()
//        callback = object : OnBackPressedCallback(true) {
//            // 뒤로가기 했을 때 실행되는 기능
//            override fun handleOnBackPressed() {
//                savePref()
//                activity?.finish()
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        setupViews()
        setupListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        loadPref()

        // 검색창 엔터키 입력시
//        binding.etSearch.setOnKeyListener { _, keyCode, _ ->
//            when (keyCode) {
//                KeyEvent.KEYCODE_ENTER -> {
//                    sharedViewModel.searchTerm = binding.etSearch.text.toString()
//                    sharedViewModel.page.value = 1
//                    searchImage(sharedViewModel.searchTerm)
//                    searchAction()
//                    true
//                }
//                else -> false
//            }
//        }
//
//        if (sharedViewModel.searchTerm.isNotEmpty()) {
//            Log.d("로그", "sharedViewModel.searchTerm : ${sharedViewModel.searchTerm}")
//            binding.etSearch.doOnPreDraw {
//                binding.etSearch.setText(sharedViewModel.searchTerm)
//            }
//            searchImage(sharedViewModel.searchTerm)
//        }
//
//        binding.searchBtn.setOnClickListener {
//            sharedViewModel.page.value = 1
//            sharedViewModel.searchTerm = binding.etSearch.text.toString()
//            searchImage(sharedViewModel.searchTerm)
//
//            searchAction()
//        }
//
//
//        binding.searchBtn.setOnClickListener {
//            val inputText = binding.etSearch.text.toString()
//            if (inputText.isNotEmpty()) {
//                Utils.savePref(requireContext(), inputText)
//                searchRVAdapter.clearItem()
//                searchImage(inputText)
//            } else {
//                Toast.makeText(context, "검색어를 입력해 주세요.", Toast.LENGTH_SHORT).show()
//            }
//        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this@SearchFragment.context = context
    }

    private fun setupViews() {
        // RecyclerView 설정
        gridmanager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvSearchList.layoutManager = gridmanager

        adapter = SearchRVAdapter(context)
        binding.rvSearchList.adapter = adapter
        binding.rvSearchList.itemAnimator = null

        // 최근 검색어를 가져와 EditText에 설정
        val lastSearch = Utils.loadPref(requireContext())
        binding.etSearch.setText(lastSearch)
    }

    /**
     * 화면에 있는 UI 요소들의 리스너 설정을 하는 메서드입니다.
     */
    private fun setupListeners() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.searchBtn.setOnClickListener {
            val query = binding.etSearch.text.toString()
            if (query.isNotEmpty()) {
                Utils.savePref(requireContext(), query)
                adapter.clearItem()
                searchImage(query)
            } else {
                Toast.makeText(context, "검색어를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            }

            // 키보드 숨기기
            imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
        }
    }

    // 키보드 내리고 EditText에서 포커스 클리어
    private fun searchAction() {
        val imm =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
        binding.etSearch.clearFocus()
    }

//    private fun savePref() {
//        val sharedPreferences = this.requireActivity().getSharedPreferences(KEY_SEARCH, Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//
//        editor.putString(KEY_SEARCH, binding.etSearch.text.toString())
//        Log.d(TAG, "savePref: $KEY_SEARCH")
//        editor.apply()
//    }
//
//    private fun loadPref() {
//        val sharedPreferences = this.requireActivity().getSharedPreferences(KEY_SEARCH, Context.MODE_PRIVATE)
//        if (sharedPreferences.contains(KEY_SEARCH)) {
//            val searchText = sharedPreferences.getString(KEY_SEARCH, "")
//            with(binding) {
//                etSearch.setText(searchText)
//            }
//            Log.d(TAG, "loadPref: $searchText")
//        }
//    }

    // 카카오 API 이미지 검색
    private fun searchImage(query: String) {
//        val retrofit = Retrofit.Builder()
//            .baseUrl(Constants.BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//        val api = retrofit.create(NetWorkInterface::class.java)
//        val callGetSearchImage = api.searchImage(Constants.AUTH_HEADER, query, 1)

        apiService.searchImage(Constants.AUTH_HEADER, query, 1,"recency",  80)
            ?.enqueue(object : Callback<ImageModel?> {
                override fun onResponse(call: Call<ImageModel?>, response: Response<ImageModel?>) {
                    response.body()?.metaData?.let { meta ->
                        if (meta.totalCount > 0) {
                            response.body()!!.documents.forEach { document ->
                                val title = document.sitename
                                val datetime = document.datetime
                                val url = document.thumbnailUrl
                                searchItems.add(SearchModel(title, datetime, url))
                            }
                        }
                    }
                    adapter.imageList = searchItems
                    adapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<ImageModel?>, t: Throwable) {
                    Log.e("#jblee", "onFailure: ${t.message}")
                }
            })

//        callGetSearchImage.enqueue(object : Callback<ImageResponse> {
//            override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>) {
//                if (response.isSuccessful) {
//                    binding.list.layoutManager =
//                        GridLayoutManager(context, 2)
//                    searchRVAdapter = SearchRVAdapter(requireContext(), response.body()!!.documents, this@SearchFragment)
//                    binding.list.adapter = searchRVAdapter
//
//                    binding.list.doOnPreDraw {
//                        startPostponedEnterTransition()
//                    }
//
//                    document = response.body()!!.documents
//                }
//            }
//
//            override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
//                Log.d("로그", "MainActivity - onFailure() 호출됨")
//            }
//        })
    }

    // 리사이클러뷰 아이템 클릭했을 때
//    override fun onItemClicked(position: Int, imageView: ImageView, drawable: Drawable) {
//        sharedViewModel.imgUrl.value = document[position].imageUrl
//        sharedViewModel.document = document[position]
//        sharedViewModel.drawable = drawable
//
//        val extras = FragmentNavigatorExtras(
//            imageView to document[position].imageUrl
//        )
//        addItemPref(document[position])
//        var itemClick: DocumentItemClick? = null
//        itemClick = object : DocumentItemClick {
//            override fun onClick(view: View?, data: Document) {
//                val clickedItem = data
//                val bundle = Bundle()
//                when (val item = data){
//                    is Document ->  {
//                        listener?.onDataReceived(item)
//                        bundle.putParcelable(
//                            IntentKeys.EXTRA_DOCUMENT,
//                            clickedItem
//                        )
//                    }
//                    else -> Unit
//                }
//                requireActivity().supportFragmentManager.beginTransaction().remove(SearchFragment()).commit()
//            }
//        }
//    }

//    private fun addItemPref(document: Document) {
//        val sharedPreferences = this.requireActivity().getSharedPreferences(KEY_ADD, Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        val gson = GsonBuilder().create()
//        val tempArray : ArrayList<Document> = arrayListOf()
//        val groupListType: Type = object : TypeToken<ArrayList<Document?>?>() {}.type // 이 부분이 중요하다. Json을 객체로 만들 때 타입을 추론하는 역할을 한다.
//        val data = Document(document.datetime, document.sitename, document.docUrl, document.imageUrl, document.thumbnailUrl)
//        val prev =sharedPreferences.getString(KEY_ADD,"none")
//        if(prev!="none"){ //데이터가 비어있지 않다면?
//            if(prev!="[]" || prev!="")tempArray.addAll(gson.fromJson(prev,groupListType))
//            tempArray.add(data)
//            val strList = gson.toJson(tempArray,groupListType)
//            editor.putString(KEY_ADD,strList)
//        }else{
//            tempArray.add(data)
//            val strList = gson.toJson(tempArray,groupListType)
//            editor.putString(KEY_ADD,strList)
//        }
//        Log.d(TAG, "addItemPref: $KEY_ADD")
//        Log.d(TAG, "addItemPref: $document")
//        editor.apply()
//    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//        callback.remove()
//    }

//    companion object {
//        private const val KEY_SEARCH = "search_text"
//    }
}