package com.example.top10downloader

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ViewHolder(v: View) {
    val tvName: TextView = v.findViewById(R.id.tvName)
    val tvArtist: TextView = v.findViewById(R.id.tvArtist)
    val tvSummary: TextView = v.findViewById(R.id.tvSummary)
}

class FeedAdapter(
    context: Context,
    private val resource: Int,
    private var applications: List<FeedEnrty>
) : ArrayAdapter<FeedEnrty>(context, resource) {

    private val inflater = LayoutInflater.from(context)

    fun setFeedList(feedList: List<FeedEnrty>) {
        this.applications = feedList
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return applications.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder
        if (convertView == null) {
            view = inflater.inflate(resource, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        //val view = inflater.inflate(resource, parent, false)
//        val tvName: TextView = view.findViewById(R.id.tvName)
//        val tvArtist: TextView = view.findViewById(R.id.tvArtist)
//        val tvSummary: TextView = view.findViewById(R.id.tvSummary)
        viewHolder.tvName.text = applications[position].name
        viewHolder.tvArtist.text = applications[position].artist
        viewHolder.tvSummary.text = applications[position].summary
        return view
    }
}