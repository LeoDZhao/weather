package com.ebay.dozhao.myweatherapp

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class RecentSearchRecyclerViewAdapter
    : RecyclerView.Adapter<RecentSearchRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.recent_search_text)
        val textLayout: View = view.findViewById(R.id.recent_search_text_layout)
        val deleteIconLayout: View = view.findViewById(R.id.delete_icon_layout)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recent_search_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = RecentSearchRepository.recentSearches.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = RecentSearchRepository.recentSearches[position]

        holder.textLayout.setOnClickListener({
            val query = holder.textView.text.toString()
            if (query.isNotEmpty()) {
                NavigationUtils.startSearchResultActivity(it.context, query)
            }
        })

        holder.deleteIconLayout.setOnClickListener({
            RecentSearchRepository.deleteRecentSearch(RecentSearchRepository.recentSearches[position])
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, RecentSearchRepository.recentSearches.size)
        })
    }
}