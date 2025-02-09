package com.example.photogalleryapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PhotoAdapter
    private var photos: List<Photo> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2) // Set GridLayout with 2 columns
        recyclerView.adapter = PhotoAdapter(photos)

        // Fetch photos from the network
        fetchPhotos()

        // Add OnScrollListener to implement the parallax effect
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Loop through all visible items and apply the parallax effect
                val firstVisiblePosition = (recyclerView.layoutManager as GridLayoutManager)
                    .findFirstVisibleItemPosition()

                val lastVisiblePosition = (recyclerView.layoutManager as GridLayoutManager)
                    .findLastVisibleItemPosition()

                for (position in firstVisiblePosition..lastVisiblePosition) {
                    val view = recyclerView.findViewHolderForAdapterPosition(position)?.itemView
                    view?.let {
                        val offset = (dy * 0.5f).toInt() // The amount of parallax effect (adjustable)
                        it.translationY = offset.toFloat() // Apply the parallax effect by shifting the Y-axis
                    }
                }
            }
        })
    }

    private fun fetchPhotos() {
        NetworkManager.api.getPhotos().enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                if (response.isSuccessful) {
                    photos = response.body() ?: emptyList()
                    adapter = PhotoAdapter(photos)
                    recyclerView.adapter = adapter
                }
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                Log.e("MainActivity", "Error fetching photos", t)
            }
        })
    }
}
