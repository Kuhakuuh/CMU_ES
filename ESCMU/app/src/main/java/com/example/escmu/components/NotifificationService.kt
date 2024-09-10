package com.example.escmu.components

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import android.os.Build
import android.util.Log
import com.example.escmu.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore

class MyService : Service() {

    private val CHANNEL_ID = "MyNotificationChannel"

    override fun onCreate() {
        super.onCreate()
        // Create the notification channel when the service is created
        createNotificationChannel()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Send a notification
        var email = FirebaseAuth.getInstance().currentUser?.email
        sendNotification("Group Observation", "Service is Up.User:$email")
        observeGroups()

        return START_STICKY // Service will restart if terminated
    }

    private fun observeGroups() {

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser!=null){
            Log.d("Service","User:$currentUser")

            val firestore = FirebaseFirestore.getInstance()
            // Pega o grupo do currentUser
            firestore.collection("Users")
                .document(currentUser?.email ?: "")
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val currentGroupId = document.getString("group")
                        firestore.collection("Groups")
                            .addSnapshotListener { snapshots, e ->
                                if (e != null || currentGroupId == null) {
                                    return@addSnapshotListener
                                }

                                for (dc in snapshots!!.documentChanges) {
                                    if (dc.type == DocumentChange.Type.REMOVED) {
                                        val removedGroupId = dc.document.getString("name")
                                        val total = dc.document.getDouble("totalValue")

                                        // Verifique se o grupo do usuário removido é o mesmo do currentUser
                                        if (currentGroupId == removedGroupId) {
                                            Log.d("Notification", "Notification send!")
                                            // Enviar notificação
                                            sendNotification("ESCMU", "O seu grupo fui eliminado\n Total:$total")
                                        }
                                    }
                                }
                            }
                    }
                }


        }else{
            Log.d("Service","User:$currentUser")
        }
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    // Method to send the notification
    private fun sendNotification(title: String, message: String) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Build the notification
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_notifications_24)  // Your notification icon
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)  // Set priority
            .setAutoCancel(true)  // Auto-cancel after clicking
            .build()

        // Send the notification (ID can be any unique number)
        notificationManager.notify(1, notification)
    }

    // Create a notification channel for Android 8.0 and above
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My Channel"
            val descriptionText = "This is a description for the notification channel."
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
