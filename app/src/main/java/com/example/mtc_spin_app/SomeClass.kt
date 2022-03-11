package com.example.mtc_spin_app

import android.os.Handler

class SomeClass {
    private fun runTimer() {
        val handler = Handler()
        handler.post(object : Runnable {
            override fun run() {
                handler.postDelayed(this, 1000)
            }
        })
    }
}