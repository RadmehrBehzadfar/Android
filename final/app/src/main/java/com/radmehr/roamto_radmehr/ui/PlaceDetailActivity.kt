package com.radmehr.roamto_radmehr.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.radmehr.roamto_radmehr.R
import com.radmehr.roamto_radmehr.data.Place
import com.radmehr.roamto_radmehr.databinding.ActivityPlaceDetailBinding

class PlaceDetailActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityPlaceDetailBinding
    private lateinit var place: Place

    companion object {
        private const val EXTRA = "extra_place"
        fun start(ctx: Context, p: Place) {
            ctx.startActivity(Intent(ctx, PlaceDetailActivity::class.java).apply {
                putExtra(EXTRA, p)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        place = intent.getParcelableExtra(EXTRA)
            ?: run {
                Toast.makeText(this,"No data",Toast.LENGTH_SHORT).show()
                finish(); return
            }

        binding.tvTitle.text       = place.title
        binding.tvDescription.text = place.description
        binding.tvDate.text        = place.date
        binding.tvAddress.text     = place.address

        (supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment)
            .getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        val pos = LatLng(place.latitude, place.longitude)
        map.addMarker(MarkerOptions().position(pos).title(place.title))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 12f))
        map.setOnInfoWindowClickListener {
            Toast.makeText(this, it.title, Toast.LENGTH_SHORT).show()
        }
    }
}