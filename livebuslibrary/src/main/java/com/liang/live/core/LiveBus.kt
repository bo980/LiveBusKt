package com.liang.live.core

import androidx.collection.ArrayMap
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

object LiveBus {

    val dispatchers by lazy {
        ArrayMap<String, LiveDispatcher<*>>()
    }

    inline fun <reified T> post(
        tag: String = T::class.java.simpleName,
        value: T,
        delay: Long = 0L
    ): LiveDispatcher<T> {
        synchronized(this) {
            return (dispatchers.getOrPut(
                tag,
                { LiveDispatcher<T>(tag) }) as LiveDispatcher<T>).apply {
                post(value, delay)
            }
        }
    }

    inline fun <reified T> observer(
        tag: String = T::class.java.simpleName,
        lifecycleOwner: LifecycleOwner,
        priority: Int = 5,
        observer: Observer<T>
    ): Subscriber<T> {
        synchronized(this) {
            return (dispatchers.getOrPut(
                tag,
                { LiveDispatcher<T>(tag) }) as LiveDispatcher<T>).with(lifecycleOwner, priority)
                .apply {
                    this.observer = observer
                }
        }
    }
}