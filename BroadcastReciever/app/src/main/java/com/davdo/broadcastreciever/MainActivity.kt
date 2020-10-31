package com.davdo.broadcastreciever

import android.content.BroadcastReceiver
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {


    private lateinit var reciever : BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureReceiver()
    }

    private fun configureReceiver() {

        val filter = IntentFilter()

        filter.addAction("com.davdo.SendBroadcast")
        reciever = MyReciever()
        registerReceiver(reciever, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(reciever)
    }
}
