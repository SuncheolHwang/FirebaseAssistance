package com.example.android.firebaseassistance.cloudstorage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.firebaseassistance.R

class FileListAdapter(var items: ArrayList<UploadInfo>, var context: Context)
    : RecyclerView.Adapter<FileListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleView?.text = items[position].name
        holder.contentsView?.text = items[position].path
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var titleView: TextView? = null
        var contentsView: TextView? = null

        init {
            titleView = view.findViewById(R.id.memoTitle)
            contentsView = view.findViewById(R.id.memoContents)
        }

        override fun onClick(p0: View?) {
        }
    }
}