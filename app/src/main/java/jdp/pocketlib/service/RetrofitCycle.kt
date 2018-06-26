/*
 * jamesdeperio/PocketLib is licensed under the
 *
 * Apache License 2.0
 * A permissive license whose main conditions require preservation of copyright and license notices. Contributors provide an express grant of patent rights. Licensed works, modifications, and larger works may be distributed under different terms and without source code.
 */

package jdp.pocketlib.service

import okhttp3.Cache
import retrofit2.CallAdapter
import retrofit2.Converter

/**
 * Created by jamesdeperio on 7/5/2017
 *  jamesdeperio.github.com.codepocket.service
 */

internal interface RetrofitCycle {
    fun initCacheSize(): Int
    fun initBaseURL(): String
    fun initWriteTimeOut(): Long = 10
    fun initConnectTimeOut(): Long = 10
    fun initReadTimeOut(): Long = 30
    fun create(service: Class<*>, username: String, password: String): Any
    fun create(service: Class<*>): Any
    fun initConverterFactory(): Converter.Factory
    fun initRxAdapterFactory(): CallAdapter.Factory
    fun isDebugMode(): Boolean
    fun debugMode(cache: Cache?)
    fun releaseMode(cache: Cache?)
}