package com.example.lakshmi.mynewsapp.NewsData


import android.os.AsyncTask
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


interface NewsFetchedListener {

    fun whenNewsFetchedSuccessfully(articles: List<Article>?)
    fun whenNewsFetchedOnError(error: String?)

}

class NewsFetchingAsyncTask(private val newsFetchedListener: NewsFetchedListener? = null) :
    AsyncTask<String, String, String>() {

    @Throws(IOException::class)
    private fun sendGet(url: String): String {
        val urlObj = URL(url)
        val connectionObj = urlObj.openConnection() as HttpURLConnection
        connectionObj.requestMethod = "GET"
        val responseCode = connectionObj.responseCode

        if (responseCode == HttpURLConnection.HTTP_OK) { // connection ok
            val bufferReader = BufferedReader(InputStreamReader(connectionObj.inputStream))
            val response = StringBuffer()
            var line: String?
            do {
                line = bufferReader.readLine()
                if (line == null)
                    break
                response.append(line)
            } while (true)
            bufferReader.close()
            return response.toString()
        } else {
            return "Error!"
        }
    }

    override fun doInBackground(vararg p0: String?): String {
        val myUrl =
            "https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json"
        val result = this.sendGet(myUrl)
        return result
    }

    override fun onPostExecute(result: String?) {

        if (result != null) {
            parseReturnedJsonData(result)
        }
    }

    //Parsing json data
    private fun parseReturnedJsonData(s: String) {
        val parser = Gson()
        val result = parser.fromJson(s, NewsResult::class.java)
        if (result.status == "ok") {
            newsFetchedListener?.whenNewsFetchedSuccessfully(result.articles)
        } else {
            newsFetchedListener?.whenNewsFetchedOnError("Error")
        }
    }

}