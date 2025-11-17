package com.example.a23012021015_mad_practical_7.db

import android.util.Log
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.ProtocolException
import java.net.URL

class HttpRequest {

    private val TAG = "HttpRequest"

    fun makeServiceCall(url: String?, token: String? = null): String? {
        var response: String? = null

        try {
            val urlObj = URL(url)
            val connection = urlObj.openConnection() as HttpURLConnection

            if (token != null) {
                connection.setRequestProperty("Authorization", "Bearer $token")
                connection.setRequestProperty("content-type", "application/json")
            }

            connection.requestMethod = "GET"

            response = convertStreamToString(
                BufferedInputStream(connection.inputStream)
            )

        } catch (e: ProtocolException) {
            Log.e(TAG, "ProtocolException: ${e.message}")
        } catch (e: IOException) {
            Log.e(TAG, "IOException: ${e.message}")
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}")
        }

        return response
    }

    private fun convertStreamToString(inputStream: InputStream): String {
        val reader = BufferedReader(InputStreamReader(inputStream))
        val sb = StringBuilder()
        var line: String?

        try {
            while (reader.readLine().also { line = it } != null) {
                sb.append(line).append('\n')
            }
        } catch (e: IOException) {
            Log.e(TAG, "convertStreamToString IOException: ${e.message}")
        } finally {
            try {
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return sb.toString()
    }
}
