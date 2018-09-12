/**
 * @author github.com/jamesdeperio
 * @version codepocket template builder v1.0
 */
package jdp.pocketlib.util

import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.Subject

class EventPublisher<T : Subject<Any>>(private val bus: T) {
    private var publisher: MutableMap<String,T> = HashMap()

    fun sendEvent(channel:String,event: Any): Unit? {
        if (publisher[channel]==null) throw RuntimeException("CHANNEL IS NOT REGISTERED! Please Subscribe.")
        return publisher[channel]!!.onNext(event)
    }

    fun unSubscribeReceiver(channel:String) {
        if (publisher[channel]==null) throw RuntimeException("CHANNEL IS NOT REGISTERED! Please Subscribe.")
        (publisher[channel] as Disposable).dispose()
    }

    fun subscribeReceiver(channel:String,eventReceiver: Consumer<in Any> ) {
        publisher[channel]  = bus
        publisher[channel]!!.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(eventReceiver)
    }

    fun subscribeReceiver(channel: String, eventReceiver: (event:Any) -> Unit) {
        publisher[channel]  = bus
        publisher[channel]!!.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe { eventReceiver(it) }
    }
}

