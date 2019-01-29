/*
 * jamesdeperio/PocketLib is licensed under the
 *
 * Apache License 2.0
 * A permissive license whose main conditions require preservation of copyright and license notices. Contributors provide an express grant of patent rights. Licensed works, modifications, and larger works may be distributed under different terms and without source code.
 */

package jdp.pocketlib.builder

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import jdp.pocketlib.base.PocketViewPagerAdapter

/**
 * Created by Jamesdeperio on 28/02/2018.
 */

class PageBuilder {
    lateinit var viewPager: ViewPager
    lateinit var fragmentManager: FragmentManager
    private val titleList: MutableList<String> = ArrayList()
    private val fragmentList: MutableList<Fragment> = ArrayList()
    var tabLayout: TabLayout? = null
    var pageTransformer: ViewPager.PageTransformer? = null

    fun create() {
        viewPager.adapter = PocketViewPagerAdapter(fragmentManager, fragmentList, titleList)
        tabLayout?.setupWithViewPager(viewPager)
        viewPager.setPageTransformer(true, pageTransformer)
    }

    fun addPage(title: String="", fragment: Fragment) {
        titleList.add(title)
        fragmentList.add(fragment)
    }
    companion object Builder {
        inline fun build(properties: PageBuilder.() -> Unit) = PageBuilder().apply(properties).create()
    }
}
