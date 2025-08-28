@file:Suppress("DEPRECATION")

package com.example.whatsappclone.adapter

import android.content.Context
import android.webkit.WebSettings
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.whatsappclone.adapter.ViewPagerAdapter.Companion.TAB_TITLES
import android.webkit.WebSettings.TextSize as TextSize1

class ViewPagerAdapter(
    private val context : Context,
    fm : FragmentManager?,
    private val list: ArrayList<Fragment>
) : FragmentPagerAdapter(fm!!) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getPageTitle(position: Int): CharSequence {
        return TAB_TITLES[position]
    }

    companion object{
        val TAB_TITLES = arrayOf("Chats","Status","Notes")
    }
}