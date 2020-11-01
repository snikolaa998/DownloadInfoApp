package com.example.top10downloader

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

private const val TAG = "FeedViewModel"
val EMPTY_FEED_LIST: List<FeedEnrty> = Collections.emptyList()
class FeedViewModel : ViewModel(), DownloadData.DownloaderCallBack {
    private var downloadData: DownloadData? = null
    private var feedCachedUrl = "INVALIDATED"

    private val feed = MutableLiveData<List<FeedEnrty>>()
    val feedEntries: LiveData<List<FeedEnrty>>
        get() = feed
    init {
        feed.postValue(EMPTY_FEED_LIST)
    }
    fun downloadUrl(feedUrl: String) {
        Log.d(TAG, "downloadURL: called with url $feedUrl")
        if (feedUrl != feedCachedUrl) {
            downloadData = DownloadData(this)
            downloadData?.execute(feedUrl)
            feedCachedUrl = feedUrl
        } else {
            Log.d(TAG, "downloadUrl - URL not changed")
        }
    }
    fun invalidate() {
        feedCachedUrl = "INVALIDATED"
    }

    override fun onDataAvailable(data: List<FeedEnrty>) {
        Log.d(TAG, "onDataAvailable called")
        feed.value = data
        Log.d(TAG, "onDataAvailable ends")
    }

    override fun onCleared() {
        Log.d(TAG, "onCleared: cancelling pending downloads")
        downloadData?.cancel(true)
    }
}