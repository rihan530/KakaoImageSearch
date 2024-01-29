package com.example.kakaoimagesearch

import android.Manifest
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
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

    private lateinit var searchListFragment: SearchListFragment

    val adapter = ViewPagerAdapter(this)

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_MEDIA_IMAGES
            ), 0)
        }

        window.apply {
            // 상태바의 아이콘과 배경색 변경
            statusBarColor = Color.WHITE
            WindowInsetsControllerCompat(this, this.decorView).isAppearanceLightStatusBars = true
        }

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

//    override fun update(position: Int) {
//        adapter.updateLike(position)
//    }

//    override fun onDataReceived(data: Contact.Person) {
//        supportFragmentManager.commit {
//            detailFragment = ContactDetailFragment.newInstance(data)
//            replace(R.id.frame_layout_main, detailFragment)
//            setReorderingAllowed(true)
//            addToBackStack("")
//        }
//    }
}