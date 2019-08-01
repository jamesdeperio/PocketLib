/*
 * jamesdeperio/PocketLib is licensed under the
 *
 * Apache License 2.0
 * A permissive license whose main conditions require preservation of copyright and license notices. Contributors provide an express grant of patent rights. Licensed works, modifications, and larger works may be distributed under different terms and without source code.
 */

package jdp.pocketlib.base

import android.os.Bundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

/**
 * Created by Jamesdeperio on 04/03/2018.
 **/
interface BaseContract {
    interface Common:CoroutineScope {
        fun onInitialization(savedInstanceState: Bundle?): Job
        fun onViewDidLoad(savedInstanceState: Bundle?): Job

    }

}