package com.liang.live.core

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer

abstract class Subscriber<T>(val tag: String,val priority: Int) {

    protected var lastVersion = -1

    protected var enable = false

    var observer: Observer<T>? = null

    abstract fun bindLifecycle(lifecycle: Lifecycle)

    abstract fun unBindLifecycle(unit: () -> Unit)

    abstract fun postValue(value: T)

    abstract fun getVersion(): Int
}