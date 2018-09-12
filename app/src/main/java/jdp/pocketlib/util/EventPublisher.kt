/**
 * @author github.com/jamesdeperio
 * @version codepocket template builder v1.0
 */
package jdp.pocketlib.util

import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class EventPublisher<T : RxSubject>(private val bus: T) {
    private lateinit var publisher: MutableMap<String,T>

    fun sendEvent(channel:Channel,event: Event): Unit? {
        if (publisher[channel]==null) throw RuntimeException("CHANNEL IS NOT REGISTERED! Please Subscribe.")
        return publisher[channel]!!.onNext(event)
    }

    fun unSubscribeReceiver(channel:Channel) {
        if (publisher[channel]==null) throw RuntimeException("CHANNEL IS NOT REGISTERED! Please Subscribe.")
        (publisher[channel] as Disposable).dispose()
    }

    fun subscribeReceiver(channel:Channel,eventReceiver: EventReceiver ) {
        publisher[channel]  = bus
        publisher[channel]!!.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(eventReceiver)
    }

    fun subscribeReceiver(channel: String, eventReceiver: (event:Event) -> Unit) {
        publisher[channel]  = bus
        publisher[channel]!!.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe { eventReceiver(it) }
    }
}

