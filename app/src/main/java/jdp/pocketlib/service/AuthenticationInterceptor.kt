/*
 * jamesdeperio/PocketLib is licensed under the
 *
 * Apache License 2.0
 * A permissive license whose main conditions require preservation of copyright and license notices. Contributors provide an express grant of patent rights. Licensed works, modifications, and larger works may be distributed under different terms and without source code.
 */

package jdp.pocketlib.service

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by jamesdeperio on 12/29/2017.
 **/

class AuthenticationInterceptor(private val authToken: String) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val builder = original.newBuilder()
                .header("Authorization", authToken)
        val request = builder.build()
        return chain.proceed(request)
    }

}
