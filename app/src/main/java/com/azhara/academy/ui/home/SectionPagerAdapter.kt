package com.azhara.academy.ui.home

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.azhara.academy.R
import com.azhara.academy.ui.academy.AcademyFragment
import com.azhara.academy.ui.bookmark.BookmarkFragment

class SectionPagerAdapter(private val context: Context, fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT ) {

    companion object{
        private val TABS_TITLE = intArrayOf(R.string.home, R.string.bookmark)
    }

    override fun getItem(position: Int): Fragment =
        when(position){
            0 -> AcademyFragment()
            1 -> BookmarkFragment()
            else -> Fragment()
        }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TABS_TITLE[position])
    }

    override fun getCount(): Int  = 2

}