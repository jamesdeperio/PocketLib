/*
 * jamesdeperio/PocketLib is licensed under the
 *
 * Apache License 2.0
 * A permissive license whose main conditions require preservation of copyright and license notices. Contributors provide an express grant of patent rights. Licensed works, modifications, and larger works may be distributed under different terms and without source code.
 */

package jdp.pocketlib.builder

import com.google.android.material.tabs.TabLayout
import jdp.pocketlib.base.ViewPagerAdapter

/**
 * Created by James de perio on 28/02/2018.
 */

class PageBuilder {
    lateinit var viewPager: androidx.viewpager.widget.ViewPager
    private val titleList: MutableList<String> = ArrayList()
    private val fragmentList: MutableList<androidx.fragment.app.Fragment> = ArrayList()
    var tabLayout: TabLayout? = null
    var pageTransformer: androidx.viewpager.widget.ViewPager.PageTransformer? = null

    fun create(fragmentManager: androidx.fragment.app.FragmentManager) {
        viewPager.adapter = ViewPagerAdapter(fragmentManager, fragmentList, titleList)
        tabLayout?.setupWithViewPager(viewPager)
        viewPager.setPageTransformer(true, pageTransformer)
    }

    fun addPage(title: String="", fragment: androidx.fragment.app.Fragment) {
        titleList.add(title)
        fragmentList.add(fragment)
    }
    companion object Builder {
        inline fun build(fragmentManager: androidx.fragment.app.FragmentManager, properties: PageBuilder.() -> Unit) = PageBuilder().apply(properties).create(fragmentManager)
    }
}
