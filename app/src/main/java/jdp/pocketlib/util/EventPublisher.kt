/**
 * @author github.com/jamesdeperio
 * @version codepocket template builder v1.0
 */
package jdp.pocketlib.util

import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.*

@Suppress("UNCHECKED_CAST", "NON_EXHAUSTIVE_WHEN")
class EventPublisher(private val bus: Bus) {
    private var publisher: MutableMap<String,Any> = HashMap()
    private var disposable: MutableMap<String,Disposable> = HashMap()

    fun sendEvent(channel:String,event: Any): Unit? {
        if (publisher[channel]==null) throw RuntimeException("CHANNEL IS NOT REGISTERED! Please Subscribe.")
        return when (bus) {
            Bus.PublishSubject -> (publisher[channel]!! as PublishSubject<Any>).onNext(event)
            Bus.BehaviorSubject -> (publisher[channel]!! as BehaviorSubject<Any>).onNext(event)
            Bus.AsyncSubject -> (publisher[channel]!! as AsyncSubject<Any>).onNext(event)
            Bus.ReplaySubject -> (publisher[channel]!! as ReplaySubject<Any>).onNext(event)
            Bus.UnicastSubject -> (publisher[channel]!! as UnicastSubject<Any>).onNext(event)
        }
    }

    fun unSubscribeReceiver(channel:String) {
        if (disposable[channel]==null) throw RuntimeException("CHANNEL IS NOT REGISTERED! Please Subscribe.")
        disposable[channel]!!.dispose()
    }

    fun subscribeReceiver(channel:String,eventReceiver: Consumer<in Any> ) {
        when (bus) {
            Bus.PublishSubject -> {
                publisher[channel]  = PublishSubject.create<Any>()
                disposable[channel]= (publisher[channel]!! as PublishSubject<Any>).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(eventReceiver)
            }
            Bus.BehaviorSubject -> {
                publisher[channel]  = BehaviorSubject.create<Any>()
                disposable[channel]=(publisher[channel]!! as BehaviorSubject<Any>).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(eventReceiver)
            }
            Bus.AsyncSubject -> {
                publisher[channel]  = AsyncSubject.create<Any>()
                disposable[channel]= (publisher[channel]!! as AsyncSubject<Any>).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(eventReceiver)
            }
            Bus.ReplaySubject -> {
                publisher[channel]  = ReplaySubject.create<Any>()
                disposable[channel]= (publisher[channel]!! as ReplaySubject<Any>).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(eventReceiver)
            }
            Bus.UnicastSubject -> {
                publisher[channel]  = UnicastSubject.create<Any>()
                disposable[channel]= (publisher[channel]!! as UnicastSubject<Any>).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(eventReceiver)
            }
        }
    }

    fun subscribeReceiver(channel: String, eventReceiver: (event:Any) -> Unit){
        when (bus) {
            Bus.PublishSubject -> {
                publisher[channel]  = PublishSubject.create<Any>()
                (publisher[channel]!! as PublishSubject<Any>).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe{ eventReceiver(it) }
            }
            Bus.BehaviorSubject -> {
                publisher[channel]  = BehaviorSubject.create<Any>()
                (publisher[channel]!! as BehaviorSubject<Any>).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe{ eventReceiver(it) }
            }
            Bus.AsyncSubject -> {
                publisher[channel]  = AsyncSubject.create<Any>()
                (publisher[channel]!! as AsyncSubject<Any>).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe{ eventReceiver(it) }
            }
            Bus.ReplaySubject -> {
                publisher[channel]  = ReplaySubject.create<Any>()
                (publisher[channel]!! as ReplaySubject<Any>).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe{ eventReceiver(it) }
            }
            Bus.UnicastSubject -> {
                publisher[channel]  = UnicastSubject.create<Any>()
                (publisher[channel]!! as UnicastSubject<Any>).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe{ eventReceiver(it) }
            }
        }
    }

}

