@file:Suppress("LiftReturnOrAssignment")

package jdp.pocketlib.util

import androidx.annotation.LayoutRes
import jdp.pocketlib.R

class FragmentTransaction(
    private val fragmentManager: androidx.fragment.app.FragmentManager,
    private val allowStateLoss: Boolean,
    private val isAnimationEnabled: Boolean) {
    @field:[LayoutRes] var layoutID:Int=0
    var fromFragment: androidx.fragment.app.Fragment? = null
    lateinit var toFragment: androidx.fragment.app.Fragment

    var toAnimEnter: Int = R.anim.h_fragment_pop_enter
    var toAnimExit: Int = R.anim.h_fragment_pop_exit
    var fromAnimEnter: Int = R.anim.h_fragment_enter
    var fromAnimExit: Int = R.anim.h_fragment_exit
    var tag :String? = null

    fun add() {
        val transaction: androidx.fragment.app.FragmentTransaction
        when {
            isAnimationEnabled && fromFragment!=null ->  transaction= fragmentManager.beginTransaction()
                .setCustomAnimations(fromAnimEnter, fromAnimExit, toAnimEnter, toAnimExit)
                .add(layoutID, toFragment, tag?:toFragment.javaClass.simpleName)
                .hide(fromFragment!!)
                .addToBackStack(tag?:toFragment.javaClass.simpleName)
            isAnimationEnabled && fromFragment==null ->  transaction= fragmentManager.beginTransaction()
                .setCustomAnimations(fromAnimEnter, fromAnimExit, toAnimEnter, toAnimExit)
                .add(layoutID, toFragment, tag?:toFragment.javaClass.simpleName)
                .addToBackStack(tag?:toFragment.javaClass.simpleName)
            !isAnimationEnabled && fromFragment==null ->  transaction= fragmentManager.beginTransaction()
                .setCustomAnimations(fromAnimEnter, fromAnimExit, toAnimEnter, toAnimExit)
                .add(layoutID, toFragment, tag?:toFragment.javaClass.simpleName)
                .addToBackStack(tag?:toFragment.javaClass.simpleName)
            else  ->  transaction= fragmentManager.beginTransaction()
                    .add(layoutID, toFragment, tag?:toFragment.javaClass.simpleName)
                    .hide(fromFragment!!)
                    .addToBackStack(tag?:toFragment.javaClass.simpleName)
        }
        commit(transaction)
    }

    fun replace() {
        val transaction: androidx.fragment.app.FragmentTransaction
        when {
            isAnimationEnabled  && fromFragment==null ->  transaction= fragmentManager.beginTransaction()
                    .setCustomAnimations(fromAnimEnter, fromAnimExit, toAnimEnter, toAnimExit)
                    .replace(layoutID, toFragment, tag?:toFragment.javaClass.simpleName)
                    .disallowAddToBackStack()
            !isAnimationEnabled  && fromFragment==null ->  transaction= fragmentManager.beginTransaction()
                    .replace(layoutID, toFragment, tag?:toFragment.javaClass.simpleName)
                    .disallowAddToBackStack()
            isAnimationEnabled  ->  transaction= fragmentManager.beginTransaction()
                    .setCustomAnimations(fromAnimEnter, fromAnimExit, toAnimEnter, toAnimExit)
                    .replace(layoutID, toFragment, tag?:toFragment.javaClass.simpleName)
                    .hide(fromFragment!!)
                    .disallowAddToBackStack()
            else -> transaction= fragmentManager.beginTransaction()
                    .replace(layoutID, toFragment,tag?: toFragment.javaClass.simpleName)
                    .hide(fromFragment!!)
                    .disallowAddToBackStack()
        }
        commit(transaction)
    }

    private fun commit(transaction: androidx.fragment.app.FragmentTransaction) {
        if (allowStateLoss) transaction.commitAllowingStateLoss()
        else transaction.commit()
    }


    companion object Builder {
        inline fun add(fragmentManager: androidx.fragment.app.FragmentManager, allowStateLoss:Boolean=false, isAnimationEnabled: Boolean = true, properties: jdp.pocketlib.util.FragmentTransaction.() -> Unit)
                = FragmentTransaction(fragmentManager,allowStateLoss,isAnimationEnabled).apply(properties).add()
        inline fun replace(fragmentManager: androidx.fragment.app.FragmentManager, allowStateLoss:Boolean=false, isAnimationEnabled: Boolean = true, properties: jdp.pocketlib.util.FragmentTransaction.() -> Unit)
                = FragmentTransaction(fragmentManager,allowStateLoss,isAnimationEnabled).apply(properties).replace()
    }

}
