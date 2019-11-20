package com.liang.live.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.liang.live.changeFromOverall
import com.liang.live.observerFromOverall
import com.liang.live.postOverall

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        observerFromOverall<String> {
            Log.e("MainActivity", "String...$it")
        }

        observerFromOverall<Int> {
            Log.e("MainActivity", "Int...$it")
        }

        observerFromOverall<AppCompatActivity>("MainActivity") {
            Log.e("MainActivity", "AppCompatActivity...${it.localClassName}")
        }

        Observer<AppCompatActivity> {
            Log.e("MainActivity", "Observer...AppCompatActivity...${it.localClassName}")
        }.changeFromOverall("MainActivity", this)

        observerFromOverall<String>(priority = 1) {
            Log.e("MainActivity", "priority...String...$it")
        }
    }

    fun test(view: View) {
        "金蛋的测试".postOverall()

        val testInt = 10086
        testInt.postOverall(delay = 2000)

        postOverall(tag = "MainActivity", delay = 5000)
    }


}
