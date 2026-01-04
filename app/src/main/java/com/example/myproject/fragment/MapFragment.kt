package com.example.myproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myproject.R
import com.example.myproject.utilities.Score
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment() {

    private lateinit var googleMap: GoogleMap
    private val allScores = ArrayList<Score>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment?
        mapFragment?.getMapAsync { map ->
            googleMap = map
            showAllMarkers()
            // Center map on Tel Aviv by default
            val defaultLoc = LatLng(32.0853, 34.7818)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLoc, 10f))
        }
    }

    // Refresh map markers when data changes
    fun updateScores(scores: ArrayList<Score>) {
        allScores.clear()
        allScores.addAll(scores)
        if (::googleMap.isInitialized) {
            showAllMarkers()
        }
    }

    private fun showAllMarkers() {
        for (score in allScores) {
            val location = LatLng(score.lat, score.lon)
            googleMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .title(score.name)
                    .snippet("Score: ${score.score}")
            )
        }
    }

    // Zoom into a specific player's location (triggered from List)
    fun zoom(lat: Double, lon: Double) {
        if (::googleMap.isInitialized) {
            val location = LatLng(lat, lon)
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(location).title("Selected Player"))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        }
    }
}