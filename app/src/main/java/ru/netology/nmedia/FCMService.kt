package ru.netology.nmedia

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.viewModels
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import kotlin.random.Random

class FCMService : FirebaseMessagingService() {

    private val action = "action"
    private val content = "content"
    private val gson = Gson()
    private val CHANNEL_ID = "CHANNEL_NMEDIA"

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channelName = getString(R.string.channel_name)
            val channelDescription = getString(R.string.channel_description)
            val channelImportance = NotificationManager.IMPORTANCE_DEFAULT

            val notificationChannel = NotificationChannel(CHANNEL_ID, channelName, channelImportance).apply {

                description = channelDescription

            }

            val channelManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            channelManager.createNotificationChannel(notificationChannel)

        }

    }


    override fun onMessageReceived(message: RemoteMessage) {
        message.data[action]?.let {

            when (getAction(it)) {

                Action.LIKE -> if (message.data[content] != null) handleLike(message.data[content])
                Action.NEWPOST -> if (message.data[content] != null) handleNewPost(message.data[content])
                else -> println("Action not found")
            }


        }
    }

    override fun onNewToken(token: String) {
        //super.onNewToken(token)
        println(Gson().toJson(token))
    }


    private fun handleLike(content: String?) {

        val like = getLikeOnContent(content)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_favorite_24_red)
            .setContentTitle(getString(R.string.notification_liked, like.userName, like.postAuthor))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from( this)
            .notify(Random.nextInt(100_000), notification)

    }

    private fun handleNewPost(content: String?) {

        val notificationNewPost = gson.fromJson(content, NotificationNewPost::class.java)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_avatar_empty_48dp)
            .setContentTitle(getString(R.string.notification_new_post, notificationNewPost.author))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentText(notificationNewPost.content)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(notificationNewPost.content))
            .build()

        NotificationManagerCompat.from( this)
            .notify(Random.nextInt(100_000), notification)


    }

    private fun getAction (actionOnString : String) = when (actionOnString.lowercase()) {

        "like" -> Action.LIKE
        "newpost" -> Action.NEWPOST
        else -> Action.OTHER

    }

    private fun getLikeOnContent (content: String?) : Like{

        return if (content == null) {

            Like(0L, "", 0L, "")

        }  else {

            gson.fromJson(content, Like::class.java)

        }


    }




}

enum class Action {

    LIKE,
    NEWPOST,
    OTHER

}

data class Like (

    val userId : Long,
    val userName : String,
    val postId : Long,
    val postAuthor : String

)

data class NotificationNewPost (

    val id : Long,
    val content : String,
    val author: String

)