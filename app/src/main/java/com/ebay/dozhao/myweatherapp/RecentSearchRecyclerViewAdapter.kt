package com.ebay.dozhao.myweatherapp

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class RecentSearchRecyclerViewAdapter(private val recentSearches: ArrayList<String>)
    : RecyclerView.Adapter<RecentSearchRecyclerViewAdapter.ViewHolder>(), View.OnClickListener{

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recent_search_item, parent, false)
        view.setOnClickListener(this)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = recentSearches.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val textView = holder.view.findViewById<TextView>(R.id.recent_search_item_text)
        textView.text = recentSearches[position]
    }

    override fun onClick(view: View?) {
        view?.let {
            val query = it.findViewById<TextView>(R.id.recent_search_item_text).text.toString()
            if (query.isNotEmpty()) {
                NavigationUtils.startSearchResultActivity(view.context, query)
            }
        }
    }
}