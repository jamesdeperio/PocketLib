/**
 * @author github.com/jamesdeperio
 * @version codepocket template builder v1.0
 */
package jdp.pocketlib.manager

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.*

class EventBusManager(private val bus: Bus) {
    private var publisher: MutableMap<String,Any> = HashMap()
    private var disposable: MutableMap<String,Disposable> = HashMap()
    private var eventReceiverF: MutableMap<String,MutableMap<Int,(event:Any) -> Unit>> = HashMap()


    fun getActiveChannel(): List<String> = disposable.filter { !it.value.isDisposed }.map { it.key }
    fun getActiveChannelSize(): Int = disposable.size
    fun getActiveReceiverID(channel: String): List<Int> {
        if (eventReceiverF[channel]==null) throw RuntimeException("CHANNEL IS NOT YET REGISTERED")
        return eventReceiverF[channel]!!.map { it.key }
    }
    fun getActiveReceiverSize(channel: String): Int {
        if (eventReceiverF[channel]==null) throw RuntimeException("CHANNEL IS NOT YET REGISTERED")
        return eventReceiverF[channel]!!.size
    }

    @Suppress("UNCHECKED_CAST")
    fun sendEvent(channel:String, event: Any) {
        if (publisher[channel]==null) return
        return when (bus) {
            Bus.PublishSubject -> (publisher[channel]!! as PublishSubject<Any>).onNext(event)
            Bus.BehaviorSubject -> (publisher[channel]!! as BehaviorSubject<Any>).onNext(event)
            Bus.AsyncSubject -> (publisher[channel]!! as AsyncSubject<Any>).onNext(event)
            Bus.ReplaySubject -> (publisher[channel]!! as ReplaySubject<Any>).onNext(event)
            Bus.UnicastSubject -> (publisher[channel]!! as UnicastSubject<Any>).onNext(event)
        }
    }

    fun unSubscribeReceiver(channel:String,receiverID:Int) {
        if (eventReceiverF[channel]==null) throw RuntimeException("RECEIVER IS NOT YET SUBSCRIBE TO ANY CHANNEL")
        eventReceiverF[channel]!!.remove(receiverID)
    }

    fun disposeChannel(channel:String) {
        if (publisher[channel]==null) throw RuntimeException("CHANNEL IS NOT YET REGISTERED")
        publisher.remove(channel)
        disposable[channel]!!.dispose()
        disposable.remove(channel)
        eventReceiverF[channel]!!.clear()
        eventReceiverF.remove(channel)
    }

    fun disposeAllChannel() {
        publisher.clear()
        disposable.forEach { it.value.dispose() }
        disposable.clear()
        eventReceiverF.clear()
    }
    @Suppress("UNCHECKED_CAST")
    fun subscribeReceiver(channel: String, eventReceiver: (event:Any) -> Unit): Int {
        if (eventReceiverF[channel]==null) eventReceiverF[channel] = HashMap()
        val eventReceiverID = eventReceiverF[channel]!!.size +1
        eventReceiverF[channel]!![eventReceiverID] = eventReceiver

        if (!publisher.containsKey(channel)) when (bus) {
            Bus.PublishSubject -> {
                publisher[channel]  = PublishSubject.create<Any>()
                disposable[channel]= (publisher[channel]!! as PublishSubject<Any>).subscribeOn(AndroidSchedulers.mainThread()).observeOn(Schedulers.newThread()).subscribe{
                    eventReceiverF[channel]!!.forEach {receiver-> receiver.value(it) }
                }
            }
            Bus.BehaviorSubject -> {
                publisher[channel]  = BehaviorSubject.create<Any>()
                disposable[channel]=(publisher[channel]!! as BehaviorSubject<Any>).subscribeOn(AndroidSchedulers.mainThread()).observeOn(Schedulers.newThread()).subscribe{
                    eventReceiverF[channel]!!.forEach {receiver-> receiver.value(it) }
                }
            }
            Bus.AsyncSubject -> {
                publisher[channel]  = AsyncSubject.create<Any>()
                disposable[channel]= (publisher[channel]!! as AsyncSubject<Any>).subscribeOn(AndroidSchedulers.mainThread()).observeOn(Schedulers.newThread()).subscribe{
                    eventReceiverF[channel]!!.forEach {receiver-> receiver.value(it) }
                }
            }
            Bus.ReplaySubject -> {
                publisher[channel]  = ReplaySubject.create<Any>()
                disposable[channel]= (publisher[channel]!! as ReplaySubject<Any>).subscribeOn(AndroidSchedulers.mainThread()).observeOn(Schedulers.newThread()).subscribe{
                    eventReceiverF[channel]!!.forEach {receiver-> receiver.value(it) }
                }
            }
            Bus.UnicastSubject -> {
                publisher[channel]  = UnicastSubject.create<Any>()
                disposable[channel]= (publisher[channel]!! as UnicastSubject<Any>).subscribeOn(AndroidSchedulers.mainThread()).observeOn(Schedulers.newThread()).subscribe{
                    eventReceiverF[channel]!!.forEach {receiver-> receiver.value(it) }
                }
            }
        }
        return eventReceiverID
    }
}
