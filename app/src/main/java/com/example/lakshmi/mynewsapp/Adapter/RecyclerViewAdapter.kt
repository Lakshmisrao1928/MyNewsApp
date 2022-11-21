package com.example.lakshmi.mynewsapp.Adapter


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.lakshmi.mynewsapp.NewsData.Article
import com.example.lakshmi.mynewsapp.R
import java.text.SimpleDateFormat
import java.util.*

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}


class RecyclerViewAdapter(
    private var articles: List<Article>?,
    private val context: Context? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class NewsItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val newsImage: ImageView = itemView.findViewById(R.id.newsImage)
        val newsTitle: TextView = itemView.findViewById(R.id.newsTitle)
        val newsDescription: TextView = itemView.findViewById(R.id.newsDescription)
        val newsDate: TextView = itemView.findViewById(R.id.newsDate)
    }

    //Refresh list items
    internal fun refreshNewsItems(articles: List<Article>?) {
        val initialSize = this.articles?.size ?: 0
        this.articles = articles
        if (articles != null) {
            notifyItemRangeChanged(1, articles?.size ?: 0)
            if (articles.size < initialSize) {
                val sizeDifference = initialSize - articles.size
                notifyItemRangeRemoved(articles?.size, sizeDifference)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) return 0
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = parent.inflate(R.layout.news_item)
        //  NewsItemViewHolder(view)
        return NewsItemViewHolder(view);
    }

    override fun getItemCount(): Int {
        return ((articles?.size ?: 0))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (position >= 0) {
            if (articles != null) {
                val idx = position
                if (articles?.indices?.contains(idx) == true) {
                    val a = articles!![idx]
                    val newsItemViewHolder = (holder as NewsItemViewHolder)
                    newsItemViewHolder.newsTitle.text = a.title
                    newsItemViewHolder.newsDescription.text = a.description
                    val date = dateFormat(a.publishedAt)
                    newsItemViewHolder.newsDate.text = date

                    //Click listener for item click
                    holder.itemView.setOnClickListener() {
                        val uri = Uri.parse(a.url)
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        context?.startActivity(intent)
                    }
                    //loading image using glide
                    if (context != null) {
                        Glide.with(context).load(a.urlToImage)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .placeholder(R.drawable.image)
                            .error(R.drawable.image)
                            .fallback(R.drawable.image)
                            .into(newsItemViewHolder.newsImage)
                    }
                }
            }
        }

    }

    fun dateFormat(publishedAt: String?): String {
        var date = publishedAt
        var spf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val newDate: Date = spf.parse(date)
        spf = SimpleDateFormat("MMM dd, yyyy hh:mm:ss aaa")
        date = spf.format(newDate)
        return date
    }

}