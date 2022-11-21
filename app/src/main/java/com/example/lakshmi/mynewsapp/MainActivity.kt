package com.example.lakshmi.mynewsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lakshmi.mynewsapp.Adapter.RecyclerViewAdapter
import com.example.lakshmi.mynewsapp.NewsData.Article
import com.example.lakshmi.mynewsapp.NewsData.NewsFetchedListener
import com.example.lakshmi.mynewsapp.NewsData.NewsFetchingAsyncTask
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), NewsFetchedListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var newsList: ArrayList<Article>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        newsList = ArrayList()
        //initialising Recycler view
        initializeRecyclerView()
        //Update data in Recycler view
        fetchNewsItems()
    }


    private fun initializeRecyclerView() {

        recyclerView = findViewById(R.id.recycler_view)
        val recyclerViewAdapter = RecyclerViewAdapter(null, this)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(
                context,
                RecyclerView.VERTICAL, false
            )
            adapter = recyclerViewAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // this adds items to the action bar.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val adapter = recyclerView.adapter as RecyclerViewAdapter
        return when (item.itemId) {
            R.id.action_new_first -> {
                newsList.sortByDescending { it.publishedAt }
                adapter.refreshNewsItems(newsList)
                true
            }
            R.id.action_old_first -> {
                newsList.sortBy { it.publishedAt }
                adapter.refreshNewsItems(newsList)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun fetchNewsItems() {

        val n = NewsFetchingAsyncTask(this)
        n.execute()
    }

    override fun whenNewsFetchedSuccessfully(articles: List<Article>?) {

        newsList.addAll(articles!!)
        Log.d("list", "newsList" + newsList)
        val adapter = recyclerView.adapter as RecyclerViewAdapter
        adapter.refreshNewsItems(articles)

    }

    override fun whenNewsFetchedOnError(error: String?) {
        val t = Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT)
        t.setGravity(Gravity.TOP, 0, 500)
        t.show()
    }

}