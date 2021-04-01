package com.sva.weather.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sva.weather.others.GpsBroadcastListener

class GpsStatusReceiver(val listener: GpsBroadcastListener): BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        listener.updateGpsStatus()
    }
}