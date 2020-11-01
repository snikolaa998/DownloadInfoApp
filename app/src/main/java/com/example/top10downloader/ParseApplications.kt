package com.example.top10downloader


import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ParseApplications {
    private val TAG = "ParseApplications"
    val applications = ArrayList<FeedEnrty>()

    fun parse(xmlData :String) : Boolean {
        //Log.d(TAG, "parse started with $xmlData")
        var status = true
        var isEntry = false
        var textValue = ""
        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()
            xpp.setInput(xmlData.reader())
            var eventType = xpp.eventType
            var currentRecord = FeedEnrty()
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = xpp.name?.toLowerCase(Locale.ROOT)
                when(eventType) {
                    XmlPullParser.START_TAG -> {
                        //Log.d(TAG, "Parse startin for $tagName")
                        if (tagName == "entry") {
                            isEntry = true
                        }
                    }
                    XmlPullParser.TEXT -> textValue = xpp.text

                    XmlPullParser.END_TAG -> {
                        //Log.d(TAG, "Parse ending for $tagName")
                        if (isEntry) {
                            when(tagName) {
                                "entry" -> {
                                    applications.add(currentRecord)
                                    isEntry = false
                                    currentRecord = FeedEnrty()
                                }
                                "name" -> currentRecord.name = textValue
                                "artist" -> currentRecord.artist = textValue
                                "releasedate" -> currentRecord.releaseDate = textValue
                                "summary" -> currentRecord.summary = textValue
                                "image" -> currentRecord.imageURL = textValue
                            }
                        }
                    }
                }
                eventType = xpp.next()
            }
//            for (app in applications) {
//                Log.d(TAG, "*************")
//                Log.d(TAG, app.toString())
//            }
        } catch (e :Exception) {
            e.printStackTrace()
            status = false
        }
        return status
    }
}