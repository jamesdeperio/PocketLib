@file:Suppress("NOTHING_TO_INLINE")

package jdp.pocketlib.ext

import android.support.v4.app.FragmentManager
import jdp.pocketlib.builder.PageBuilder
import jdp.pocketlib.util.FragmentTransaction

inline fun FragmentManager.add(allowStateLoss:Boolean=false,isAnimationEnabled: Boolean = true,properties: FragmentTransaction.() -> Unit) {
    FragmentTransaction.add(this, allowStateLoss,isAnimationEnabled,properties)
}

inline fun FragmentManager.replace(allowStateLoss:Boolean=false,isAnimationEnabled: Boolean = true,properties: FragmentTransaction.() -> Unit) {
    FragmentTransaction.replace(this, allowStateLoss,isAnimationEnabled,properties)
}

inline fun FragmentManager.createViewPager(properties: PageBuilder.() -> Unit) {
    PageBuilder.build(this,properties)
}

