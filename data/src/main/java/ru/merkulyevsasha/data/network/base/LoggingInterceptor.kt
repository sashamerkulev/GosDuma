package ru.merkulyevsasha.data.network.base

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        try {
            val response = chain.proceed(request)
            if (!response.isSuccessful) {
                logRequest(request)
            }
            return response
        } catch (e: IOException) {
            logRequest(request)
            throw e
        }
    }

    private fun logRequest(request: Request) {
        Log.d(TAG, request.toString())
        request.body()?.let {
            Log.d(TAG, it.toString())
        }
    }

    companion object {
        private val TAG = LoggingInterceptor::class.java.simpleName
    }
}
