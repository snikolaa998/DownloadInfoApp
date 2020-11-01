package com.example.top10downloader

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL
import kotlin.properties.Delegates

class FeedEnrty {
    var name :String = ""
    var artist :String = ""
    var releaseDate :String = ""
    var imageURL :String = ""
    var summary :String = ""
}

private val TAG = "MainActivity"
private val STATE_URL = "feedUrl"
private val STATE_LIMIT = "feedLimit"

class MainActivity : AppCompatActivity() {
    private var feedUrl: String = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml" //Ovaj %d zamenjuje feedLimit tj. bilo koji broj koji upisemo preko feedUrl.format
    private var feedLimit = 10
    private val feedViewModel: FeedViewModel by lazy { ViewModelProviders.of(this).get(FeedViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val feedAdapter = FeedAdapter(this, R.layout.list_record, EMPTY_FEED_LIST)
        xmlListView.adapter = feedAdapter
        if (savedInstanceState != null) {
            feedUrl = savedInstanceState.getString(STATE_URL)!!
            feedLimit = savedInstanceState.getInt(STATE_LIMIT)
        }

        feedViewModel.feedEntries.observe(this, Observer<List<FeedEnrty>>{
                feedEntires -> feedAdapter.setFeedList(feedEntires)
        })
       feedViewModel.downloadUrl(feedUrl.format(feedLimit))
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
       menuInflater.inflate(R.menu.feed_menu, menu)
        if (feedLimit == 10) {
            menu?.findItem(R.id.menu10)?.isChecked = true
        } else
            menu?.findItem(R.id.menu25)?.isChecked = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menuFree -> feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml"
            R.id.menuPaid -> feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml"
            R.id.menuSongs -> feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml"
            R.id.menu10, R.id.menu25 -> {
                if (!item.isChecked) {
                    item.isChecked = true
                    feedLimit = 35 - feedLimit
                }
            }
            R.id.menuRefresh -> feedViewModel.invalidate()
            else -> return super.onOptionsItemSelected(item)
        }
        feedViewModel.downloadUrl(feedUrl.format(feedLimit))
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_URL, feedUrl)
        outState.putInt(STATE_LIMIT, feedLimit)
     }
}
/*
val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val inputBuffer = CharArray(500)
                    var charsRead = 0
                    while (charsRead >= 0) {
                        charsRead = reader.read(inputBuffer)
                        if (charsRead > 0) {
                            xmlResult.append(String(inputBuffer, 0, charsRead))
                        }
                    }
                    reader.close()
 */









