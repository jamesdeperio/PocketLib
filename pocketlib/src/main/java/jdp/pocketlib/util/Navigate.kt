/*
 * jamesdeperio/PocketLib is licensed under the
 *
 * Apache License 2.0
 * A permissive license whose main conditions require preservation of copyright and license notices. Contributors provide an express grant of patent rights. Licensed works, modifications, and larger works may be distributed under different terms and without source code.
 */

@file:Suppress("DEPRECATION")

package jdp.pocketlib.util

import android.annotation.SuppressLint
import android.view.View
import jdp.pocketlib.R
import java.lang.ref.WeakReference

/**
 * Created by jamesdeperio on 7/15/2017
 *  jamesdeperio.github.com.codepocket.util
 */
@Deprecated("Use FragmentManager.add {}  or FragmentManager.replace {} ")
object Navigate {
    private var fromFragment: WeakReference<androidx.fragment.app.Fragment>? = null
    private var removeFragment: WeakReference<androidx.fragment.app.Fragment>? = null
    private var fragmentManager: WeakReference<androidx.fragment.app.FragmentTransaction>? = null
    private var toFragment: WeakReference<androidx.fragment.app.Fragment>? = null
    private var toAnimEnter: Int = R.anim.h_fragment_pop_enter
    private var toAnimExit: Int = R.anim.h_fragment_pop_exit
    private var fromAnimEnter: Int = R.anim.h_fragment_enter
    private var fromAnimExit: Int = R.anim.h_fragment_exit

    private var isBackstackEnabled: Boolean = false
    private var isAnimationEnabled: Boolean = false
    private var layoutID: Int = 0

    private var WRONG_SEQUENCE = "WRONG SEQUENCE FRAGMENT!!"
    @SuppressLint("CommitTransaction")
    fun using(fragmentManager: androidx.fragment.app.FragmentManager): Navigate {
        Navigate.fragmentManager=WeakReference(fragmentManager.beginTransaction())
        return this
    }

    fun change(layoutID: Int): Navigate {
        Navigate.layoutID = layoutID
        return this
    }

    fun withBackStack(isBackstackEnabled: Boolean): Navigate {
        Navigate.isBackstackEnabled = isBackstackEnabled
        isAnimationEnabled = true
        return this
    }

    fun from(currentFragment: androidx.fragment.app.Fragment): Navigate {
        fromFragment = WeakReference(currentFragment)
        isAnimationEnabled = false
        return this
    }

    fun to(fragmentToChange: androidx.fragment.app.Fragment): Navigate {
        toFragment = WeakReference(fragmentToChange)
        isAnimationEnabled = false
        return this
    }

    fun from(currentFragment: androidx.fragment.app.Fragment, fromAnimEnter: Int, fromAnimExit: Int): Navigate {
        fromFragment = WeakReference(currentFragment)
        Navigate.fromAnimEnter = fromAnimEnter
        Navigate.fromAnimExit = fromAnimExit
        isAnimationEnabled = true
        return this
    }

    fun to(fragmentToChange: androidx.fragment.app.Fragment, toAnimEnter: Int, toAnimExit: Int): Navigate {
        toFragment = WeakReference(fragmentToChange)
        Navigate.toAnimEnter = toAnimEnter
        Navigate.toAnimExit = toAnimExit
        isAnimationEnabled = true
        return this
    }
    fun remove(fragmentToRemove: androidx.fragment.app.Fragment): Navigate {
        this.removeFragment= WeakReference(fragmentToRemove)
        return this
    }
    fun commit(): Navigate {
        if (toFragment != null) changeFragment(fragmentManager!!.get()!!)
        else throw IllegalStateException("WRONG SEQUENCE COMMIT!!")
        unRegister()
        return this
    }
    fun commitAllowingStateLoss(): Navigate {
        when {
            toFragment != null -> changeFragmentWithStateLoss(fragmentManager!!.get()!!)
            removeFragment!=null -> removeFragmentFromView(fragmentManager!!.get()!!)
            else -> throw IllegalStateException("WRONG SEQUENCE COMMIT!!")
        }
        unRegister()
        return this
    }

    fun addSharedElement(view:View,name:String):Navigate {
        fragmentManager!!.get()!!.addSharedElement(view,name)
        return this
    }
    private fun removeFragmentFromView(fragmentManager: androidx.fragment.app.FragmentTransaction) {
        fragmentManager.remove(removeFragment!!.get()!!)
                .commitAllowingStateLoss()
    }

    private fun changeFragmentWithStateLoss(fragmentManager: androidx.fragment.app.FragmentTransaction) {
        if (fromFragment == null) when {
            isBackstackEnabled && isAnimationEnabled -> fragmentManager
                    .setCustomAnimations(fromAnimEnter, fromAnimExit, toAnimEnter, toAnimExit)
                    .add(layoutID, toFragment!!.get()!!, toFragment!!.javaClass.simpleName)
                    .addToBackStack(toFragment!!.javaClass.simpleName)
                    .commitAllowingStateLoss()
            isBackstackEnabled -> fragmentManager
                    .add(layoutID, toFragment!!.get()!!, toFragment!!.javaClass.simpleName)
                    .addToBackStack(toFragment!!.javaClass.simpleName)
                    .commitAllowingStateLoss()
            !isBackstackEnabled -> fragmentManager
                    .replace(layoutID, toFragment!!.get()!!, toFragment!!.javaClass.simpleName)
                    .disallowAddToBackStack()
                    .commitAllowingStateLoss()
            else -> throw IllegalStateException(WRONG_SEQUENCE)
        } else when {
            isBackstackEnabled && isAnimationEnabled -> fragmentManager
                    .setCustomAnimations(fromAnimEnter, fromAnimExit, toAnimEnter, toAnimExit)
                    .add(layoutID, toFragment!!.get()!!, toFragment!!.javaClass.simpleName)
                    .hide(fromFragment!!.get()!!)
                    .addToBackStack(toFragment!!.javaClass.simpleName)
                    .commit()
            isBackstackEnabled -> fragmentManager
                    .add(layoutID, toFragment!!.get()!!, toFragment!!.javaClass.simpleName)
                    .hide(fromFragment!!.get()!!)
                    .addToBackStack(toFragment!!.javaClass.simpleName)
                    .commit()
            !isBackstackEnabled -> fragmentManager
                    .replace(layoutID, toFragment!!.get()!!, toFragment!!.javaClass.simpleName)
                    .disallowAddToBackStack()
                    .commit()
            else -> throw IllegalStateException(WRONG_SEQUENCE)
        }
    }

    private fun changeFragment(fragmentManager: androidx.fragment.app.FragmentTransaction) {
        if (fromFragment == null) when {
            isBackstackEnabled && isAnimationEnabled -> fragmentManager
                    .setCustomAnimations(fromAnimEnter, fromAnimExit, toAnimEnter, toAnimExit)
                    .add(layoutID, toFragment!!.get()!!, toFragment!!.javaClass.simpleName)
                    .addToBackStack(toFragment!!.javaClass.simpleName)
                    .commit()
            isBackstackEnabled -> fragmentManager
                    .add(layoutID, toFragment!!.get()!!, toFragment!!.javaClass.simpleName)
                    .addToBackStack(toFragment!!.javaClass.simpleName)
                    .commit()
            !isBackstackEnabled -> fragmentManager
                    .replace(layoutID, toFragment!!.get()!!, toFragment!!.javaClass.simpleName)
                    .disallowAddToBackStack()
                    .commit()
            else -> throw IllegalStateException(WRONG_SEQUENCE)
        } else when {
            isBackstackEnabled && isAnimationEnabled -> fragmentManager
                    .setCustomAnimations(fromAnimEnter, fromAnimExit, toAnimEnter, toAnimExit)
                    .add(layoutID, toFragment!!.get()!!, toFragment!!.javaClass.simpleName)
                    .hide(fromFragment!!.get()!!)
                    .addToBackStack(toFragment!!.javaClass.simpleName)
                    .commit()
            isBackstackEnabled -> fragmentManager
                    .add(layoutID, toFragment!!.get()!!, toFragment!!.javaClass.simpleName)
                    .hide(fromFragment!!.get()!!)
                    .addToBackStack(toFragment!!.javaClass.simpleName)
                    .commit()
            !isBackstackEnabled -> fragmentManager
                    .replace(layoutID, toFragment!!.get()!!, toFragment!!.javaClass.simpleName)
                    .disallowAddToBackStack()
                    .commit()
            else -> throw IllegalStateException(WRONG_SEQUENCE)
        }
    }

    private fun unRegister() {
        fromFragment = null
        fragmentManager = null
        toFragment = null
        layoutID = 1
    }
}