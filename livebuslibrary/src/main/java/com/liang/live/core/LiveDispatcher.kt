package com.liang.live.core


import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.liang.live.utils.BACKGROUND
import com.liang.live.utils.put


class LiveDispatcher<T>(tag: String) : Dispatcher<T>(tag) {

    private var version = -1

    private val subscribers by lazy {
        ArrayList<Subscriber<T>>()
    }

    private val mLiveData by lazy {
        MutableLiveData<T>()
    }

    override fun post(value: T, delay: Long) {
        BACKGROUND.submit {
            Thread.sleep(delay)
            mLiveData.postValue(value)
        }
    }

    init {
        mLiveData.observeForever {
            version++
            subscribers.forEach { s ->
                if (version > s.getVersion()) {
                    s.postValue(it)
                }
            }
        }
    }

    fun with(lifecycleOwner: LifecycleOwner, priority: Int): Subscriber<T> {
        return subscribers.put(LiveSubscriber<T>(tag, priority).apply {
            bindLifecycle(lifecycleOwner.lifecycle)
            mLiveData.value?.let {
                if (version > getVersion()) {
                    postValue(it)
                }
            }
        })
    }

    fun removeSubscriber(subscriber: Subscriber<T>) {
        subscribers.remove(subscriber)
    }
}
