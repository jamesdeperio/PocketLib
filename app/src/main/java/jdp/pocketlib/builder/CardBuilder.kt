/*
 * jamesdeperio/PocketLib is licensed under the
 *
 * Apache License 2.0
 * A permissive license whose main conditions require preservation of copyright and license notices. Contributors provide an express grant of patent rights. Licensed works, modifications, and larger works may be distributed under different terms and without source code.
 */

package jdp.pocketlib.builder

import android.support.v7.widget.RecyclerView
import android.view.animation.OvershootInterpolator
import jp.wasabeef.recyclerview.adapters.*

/**
 * Created by Jamesdeperio on 28/02/2018.
 */

class CardBuilder {
    class Builder {
        private lateinit var recyclerView: RecyclerView
        private lateinit var layoutManager: RecyclerView.LayoutManager
        private lateinit var pocketAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
        private lateinit var anim: CardAnim
        private var duration = 1000
        private var isFirstLoadAnimationOnly = false
        fun setFirstLoadAnimationOnly(value: Boolean) {
            isFirstLoadAnimationOnly = value
        }

        fun setDuration(duration: Int) {
            this.duration = duration
        }

        fun setAnim(anim: CardAnim) {
            this.anim = anim
        }

        fun setRecyclerView(recyclerView: RecyclerView) {
            this.recyclerView = recyclerView
        }

        fun setAdapter(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
            this.pocketAdapter = adapter
        }

        fun setLayoutManager(layoutManager: RecyclerView.LayoutManager) {
            this.layoutManager = layoutManager
        }

        fun build() {
            if (anim != CardAnim.NONE) {
                recyclerView.adapter = pocketAdapter
                recyclerView.layoutManager = layoutManager

            } else {
                val adapter: AnimationAdapter = when (anim) {
                    CardAnim.ALPHA_IN -> AlphaInAnimationAdapter(pocketAdapter)
                    CardAnim.SCALE_IN -> ScaleInAnimationAdapter(pocketAdapter)
                    CardAnim.SLIDE_BOTTOM -> SlideInBottomAnimationAdapter(pocketAdapter)
                    CardAnim.SLIDE_LEFT -> SlideInLeftAnimationAdapter(pocketAdapter)
                    CardAnim.SLIDE_RIGHT -> SlideInRightAnimationAdapter(pocketAdapter)
                    else -> {
                        AlphaInAnimationAdapter(pocketAdapter)
                    }
                }
                adapter.setDuration(duration)
                adapter.setFirstOnly(isFirstLoadAnimationOnly)
                adapter.setInterpolator(OvershootInterpolator())
                recyclerView.adapter = adapter
                recyclerView.layoutManager = layoutManager
            }
        }

    }

    companion object {
        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

}
