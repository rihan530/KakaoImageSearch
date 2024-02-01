package com.example.kakaoimagesearch

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.commit
import com.example.kakaoimagesearch.data.Document
import com.example.kakaoimagesearch.data.SearchModel
import com.example.kakaoimagesearch.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity(), FragmentDataListener {

    // 하단의 뒤로가기 버튼을 눌렀을 때 종료 확인 다이얼로그가 뜨는 콜백 함수
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {

        }
    }

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var storageFragment: StorageFragment

    val adapter = ViewPagerAdapter(this)

    val favItems: ArrayList<SearchModel> = arrayListOf()

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(
                Manifest.permission.INTERNET
            ), 0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        this.onBackPressedDispatcher.addCallback(this, callback)

        with(binding) {
            viewPagerMain.adapter = adapter

            TabLayoutMediator(tabLayoutMainBottom, viewPagerMain) { tab, position ->
                when (position) {
                    0 -> tab.text = "이미지 검색"/*getString(R.string.contact_list_tab)*/
                    1 -> tab.text = "내 보관함"/*getString(R.string.my_page_tab)*/
                }
            }.attach()
        }
    }

    companion object {
        const val KEY_ADD = "add_item"
        const val KEY_DELETE = "delete_item"
    }

    override fun onDataReceived(data: Document) {
        supportFragmentManager.commit {
            storageFragment = StorageFragment.newInstance(data)
            replace(R.id.list, storageFragment)
            setReorderingAllowed(true)
            addToBackStack("")
        }
    }
}