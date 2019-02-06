@file:Suppress("LiftReturnOrAssignment")

package jdp.pocketlib.util

import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import jdp.pocketlib.R

class FragmentTransaction(
        private val fragmentManager:FragmentManager,
        private val allowStateLoss: Boolean,
        private val isAnimationEnabled: Boolean) {
    @field:[LayoutRes] var layoutID:Int=0
    var fromFragment:Fragment? = null
    lateinit var toFragment:Fragment

    var toAnimEnter: Int = R.anim.h_fragment_pop_enter
    var toAnimExit: Int = R.anim.h_fragment_pop_exit
    var fromAnimEnter: Int = R.anim.h_fragment_enter
    var fromAnimExit: Int = R.anim.h_fragment_exit


    fun add() {
        val transaction: FragmentTransaction
        when {
            isAnimationEnabled  ->  transaction= fragmentManager.beginTransaction()
                    .setCustomAnimations(fromAnimEnter, fromAnimExit, toAnimEnter, toAnimExit)
                    .add(layoutID, toFragment, toFragment.javaClass.simpleName)
                    .hide(fromFragment!!)
                    .addToBackStack(toFragment.javaClass.simpleName)
            else  ->  transaction= fragmentManager.beginTransaction()
                    .add(layoutID, toFragment, toFragment.javaClass.simpleName)
                    .hide(fromFragment!!)
                    .addToBackStack(toFragment.javaClass.simpleName)
        }
        commit(transaction)
    }

    fun replace() {
        val transaction: FragmentTransaction
        when {
            isAnimationEnabled  && fromFragment==null ->  transaction= fragmentManager.beginTransaction()
                    .setCustomAnimations(fromAnimEnter, fromAnimExit, toAnimEnter, toAnimExit)
                    .replace(layoutID, toFragment, toFragment.javaClass.simpleName)
                    .disallowAddToBackStack()
            !isAnimationEnabled  && fromFragment==null ->  transaction= fragmentManager.beginTransaction()
                    .replace(layoutID, toFragment, toFragment.javaClass.simpleName)
                    .disallowAddToBackStack()
            isAnimationEnabled  ->  transaction= fragmentManager.beginTransaction()
                    .setCustomAnimations(fromAnimEnter, fromAnimExit, toAnimEnter, toAnimExit)
                    .replace(layoutID, toFragment, toFragment.javaClass.simpleName)
                    .hide(fromFragment!!)
                    .disallowAddToBackStack()
            else -> transaction= fragmentManager.beginTransaction()
                    .replace(layoutID, toFragment, toFragment.javaClass.simpleName)
                    .hide(fromFragment!!)
                    .disallowAddToBackStack()
        }
        commit(transaction)
    }

    private fun commit(transaction: FragmentTransaction) {
        if (allowStateLoss) transaction.commitAllowingStateLoss()
        else transaction.commit()
    }


    companion object Builder {
        inline fun add(fragmentManager:FragmentManager,allowStateLoss:Boolean=false,isAnimationEnabled: Boolean = true,properties: jdp.pocketlib.util.FragmentTransaction.() -> Unit)
                = FragmentTransaction(fragmentManager,allowStateLoss,isAnimationEnabled).apply(properties).add()
        inline fun replace(fragmentManager:FragmentManager,allowStateLoss:Boolean=false ,isAnimationEnabled: Boolean = true,properties: jdp.pocketlib.util.FragmentTransaction.() -> Unit)
                = FragmentTransaction(fragmentManager,allowStateLoss,isAnimationEnabled).apply(properties).replace()
    }

}
