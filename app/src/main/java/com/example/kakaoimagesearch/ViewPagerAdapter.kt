package com.example.kakaoimagesearch

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    private val searchFragment = SearchFragment()
    private val storageFragment = StorageFragment()

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> searchFragment
            else -> storageFragment
        }
    }

//    fun getInfo(): List<String> {
//        return storageFragment.giveData()
//    }

    //ContactListFragment의 isLiked 값 상태를 바꾸는 함수
//    fun updateLike(position: Int) {
//        searchFragment.updateLike(position)
//    }
}