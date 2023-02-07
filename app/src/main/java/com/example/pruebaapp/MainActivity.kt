package com.example.pruebaapp

import android.Manifest
import android.app.usage.UsageStatsManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.widget.TextView

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager;
import android.icu.util.Calendar
import android.provider.Settings;

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Permisos
        val packageManager = packageManager
        val packageNameThis = "com.example.pruebaapp"
        val packageName = "com.google.android.youtube"

        val permissionGranted =
            packageManager.checkPermission(Manifest.permission.PACKAGE_USAGE_STATS, packageNameThis) == PackageManager.PERMISSION_GRANTED

        if (!permissionGranted) {
            println("Estado del permiso: $permissionGranted ")
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }

            val totalTime = getTotalTimeYesterdayInMinutes(packageName)
            val textView = findViewById<TextView>(R.id.TextBox1)

            if (totalTime == -1L) {
                textView.text = "Application not found"
                println("Application not found")
            } else {
                textView.text = "Tiempo de uso de Youtube ayer: \n $totalTime minutos"
                println("Total time in foreground: $totalTime milliseconds")
            }

        println("Ya me sali alv")





    }

    fun getTotalTimeInForeground(packageName: String): Long {

        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val calendar = Calendar.getInstance()
        val endTime = calendar.timeInMillis
        calendar.add(Calendar.YEAR, -1)
        val startTime = calendar.timeInMillis
        val usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime)
        for (usageStats in usageStatsList) {
            if (usageStats.packageName == packageName) {
                return usageStats.totalTimeInForeground
            }
        }
        return -1
    }

    fun getTotalTimeYesterdayInMinutes(packageName: String): Long {
        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        val endTime = calendar.timeInMillis
        calendar.add(Calendar.DATE, -1)
        val startTime = calendar.timeInMillis
        val usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime)
        for (usageStats in usageStatsList) {
            if (usageStats.packageName == packageName) {
                return usageStats.totalTimeInForeground / 1000 / 60
            }
        }
        return -1
    }


}