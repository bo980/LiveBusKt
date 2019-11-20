package com.liang.live

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.liang.live.core.LiveBus
import com.liang.live.core.LiveDispatcher
import com.liang.live.core.Subscriber

inline fun <reified T> T.postOverall(
    tag: String = T::class.java.simpleName,
    delay: Long = 0L
): LiveDispatcher<T> {
    return LiveBus.post(tag, this, delay)
}

inline fun <reified T> Observer<T>.changeFromOverall(
    tag: String = T::class.java.simpleName, lifecycleOwner: LifecycleOwner, priority: Int = 5
): Subscriber<T> {
    return LiveBus.observer(tag, lifecycleOwner, priority, this)
}

inline fun <reified T> LifecycleOwner.observerFromOverall(
    tag: String = T::class.java.simpleName,
    priority: Int = 5,
    crossinline action: (value: T) -> Unit
): Subscriber<T> {
    return LiveBus.observer(tag, this, priority, Observer {
        action(it)
    })
}