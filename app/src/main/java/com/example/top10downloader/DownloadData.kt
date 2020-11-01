package com.example.top10downloader
import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

private const val TAG = "DownloadData"
class DownloadData(private val callBack: DownloaderCallBack) : AsyncTask<String, Void, String>() {

    interface DownloaderCallBack {
        fun onDataAvailable(data: List<FeedEnrty>)
    }

    override fun onPostExecute(result: String) {
        val parseApplication = ParseApplications()
        if (result.isNotEmpty()) {
            parseApplication.parse(result)
        }
        callBack.onDataAvailable(parseApplication.applications)
    }

    override fun doInBackground(vararg url: String): String {
        val rssFeed = downloadXML(url[0])
        if (rssFeed.isEmpty()) {
            Log.e(TAG, "doInBackground: error downloading")
        }
        return rssFeed
    }
    private fun downloadXML(urlPath :String) : String {
        try {
            return URL(urlPath).readText()
        } catch (e: MalformedURLException) {
            Log.d(TAG, "downloadXML: Invalid URL + ${e.message}")
        } catch (e: IOException) {
            Log.d(TAG, "downloadXML: IO Exception reading data + ${e.message}")
        } catch (e: SecurityException) {
            Log.d(TAG, "downloadXML: Security exception. Needs permission? + ${e.message}")
        }
        return "" //Vraca prazan string ukoliko ima neki Exception!
    }
}