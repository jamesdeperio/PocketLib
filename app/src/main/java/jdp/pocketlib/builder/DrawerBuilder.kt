/*
 * jamesdeperio/PocketLib is licensed under the
 *
 * Apache License 2.0
 * A permissive license whose main conditions require preservation of copyright and license notices. Contributors provide an express grant of patent rights. Licensed works, modifications, and larger works may be distributed under different terms and without source code.
 */

package jdp.pocketlib.builder

import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout

/**
 * Created by Jamesdeperio on 28/02/2018.
 */

class DrawerBuilder {
    class Builder {
        var drawerLayout: DrawerLayout? = null
        var navigationView: NavigationView? = null
        fun build() {
        }

    }

    companion object {
        inline fun build(properties: Builder.() -> Unit) = Builder().apply(properties).build()
    }

}
