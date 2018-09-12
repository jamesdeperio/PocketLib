package jdp.pocketlib.util

import io.reactivex.Observable
import io.reactivex.annotations.SchedulerSupport
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.internal.functions.Functions

@SchedulerSupport(SchedulerSupport.NONE)
fun <T> Observable<T>.subscribeWithServerErrorHandler(doOnError: (error: Throwable) -> Unit = { it.printStackTrace() }): Disposable =
        subscribe(Functions.emptyConsumer(), Consumer { doOnError(it) }, Functions.EMPTY_ACTION, Functions.emptyConsumer())
