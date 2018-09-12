package jdp.pocketlib.util

import io.reactivex.functions.Consumer
import io.reactivex.subjects.*

typealias EventReceiver = Consumer<in Any>
typealias RxPublishSubject = PublishSubject<Any>
typealias RxSingleSubject = SingleSubject<Any>
typealias RxAsyncSubject = AsyncSubject<Any>
typealias RxBehaviorSubject = BehaviorSubject<Any>
typealias RxReplaySubject = ReplaySubject<Any>
typealias RxUnicastSubject = UnicastSubject<Any>
typealias RxSubject = Subject<Any>
typealias Event = Any
typealias Channel = String
