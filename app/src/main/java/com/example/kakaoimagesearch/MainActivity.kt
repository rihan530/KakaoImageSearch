package com.example.kakaoimagesearch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.kakaoimagesearch.storage.StorageFragment
import com.example.kakaoimagesearch.databinding.ActivityMainBinding
import com.example.kakaoimagesearch.model.SearchModel
import com.example.kakaoimagesearch.search.SearchFragment


class MainActivity : AppCompatActivity() {

    // 하단의 뒤로가기 버튼을 눌렀을 때 종료 확인 다이얼로그가 뜨는 콜백 함수
//    private val callback = object : OnBackPressedCallback(true) {
//        override fun handleOnBackPressed() {
//            finish()
//        }
//    }

    private lateinit var binding: ActivityMainBinding

    val likedItems: ArrayList<SearchModel> = ArrayList()

//    override fun onStart() {
//        super.onStart()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            requestPermissions(arrayOf(
//                Manifest.permission.INTERNET
//            ), 0)
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        this.onBackPressedDispatcher.addCallback(this, callback)

        binding.run {
            btnSearchFragment.setOnClickListener{
                setFragment(SearchFragment())
            }
            btnStorageFragment.setOnClickListener {
                setFragment(StorageFragment())
            }
        }
        setFragment(SearchFragment())
    }

    private fun setFragment(frag : Fragment) {
        supportFragmentManager.commit {
            replace(R.id.frameLayout, frag)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

    fun addLikedItem(item: SearchModel) {
        if(!likedItems.contains(item)) {
            likedItems.add(item)
        }
    }

    fun removeLikedItem(item: SearchModel) {
        likedItems.remove(item)
    }

//    override fun onDataReceived(data: SearchModel) {
//        supportFragmentManager.commit {
//            storageFragment = StorageFragment.newInstance(data)
//            replace(R.id.rvStorageList, storageFragment)
//            setReorderingAllowed(true)
//            addToBackStack("")
//        }
//    }
}