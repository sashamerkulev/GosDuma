package ru.merkulyevsasha.gosduma.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage?) {
        message?.let { remoteMessage ->
//            val data = remoteMessage.data
//            if (data.containsKey("commentId")) {
//                val userName = data["userName"] ?: ""
//                val dataCommentId = data["commentId"] ?: ""
//                val dataArticleId = data["articleId"] ?: ""
//                val dataLikeOrDislike = data["likeOrDislike"] ?: ""
//                if (userName.isEmpty() || dataCommentId.isEmpty() || dataLikeOrDislike.isEmpty()) {
//                    return
//                }
//                val commentId = try {
//                    dataCommentId.toInt()
//                } catch (e: NumberFormatException) {
//                    -1
//                }
//                val articleId = try {
//                    dataArticleId.toInt()
//                } catch (e: NumberFormatException) {
//                    -1
//                }
//                if (articleId < 0 || commentId < 0) {
//                    return
//                }
//                val likeOrDislike = dataLikeOrDislike.toBoolean()
//                val likeOrDislikePart = if (likeOrDislike) " не" else " "
//                val notificationMessage = "Пользователю $userName$likeOrDislikePart понравился ваш комментарий"
//            }
        }
    }

    override fun onNewToken(token: String?) {
//        val serviceLocator = ServiceLocatorImpl.getInstance(applicationContext)
//        val setupInteractor = serviceLocator.get(SetupInteractor::class.java)
//        token?.let {
//            setupInteractor.updateFirebaseToken(token)
//                .subscribe({}, { Timber.e(it) })
//        }
    }
}