package com.liang.live.core

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class LiveSubscriber<T>(tag: String, priority: Int = 5) : Subscriber<T>(tag, priority),
    LifecycleObserver {

    private var version = lastVersion
    private var mLifecycle: Lifecycle? = null

    @Volatile
    private var mData: T? = null

    override fun bindLifecycle(lifecycle: Lifecycle) {
        mLifecycle = lifecycle.apply {
            addObserver(this@LiveSubscriber)
        }
    }

    override fun unBindLifecycle(unit: () -> Unit) {
        mLifecycle?.removeObserver(this)
        unit.invoke()
    }

    override fun postValue(value: T) {
        mData = value
        version++
        onChange()
    }

    override fun getVersion(): Int {
        return lastVersion
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        enable = true
        onChange()
    }

    private fun onChange() {
        if (enable && version > lastVersion) {
            observer?.onChanged(mData)
            lastVersion = version
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        enable = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        unBindLifecycle {
            LiveBus.dispatchers[tag]?.let {
                (it as LiveDispatcher<T>).removeSubscriber(this)
            }
        }
    }

}