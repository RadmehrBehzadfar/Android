package com.radmehr.roamto_radmehr.ui

import android.app.DatePickerDialog
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.radmehr.roamto_radmehr.R
import com.radmehr.roamto_radmehr.data.AppDatabase
import com.radmehr.roamto_radmehr.data.Place
import com.radmehr.roamto_radmehr.databinding.ActivityMainBinding
import com.radmehr.roamto_radmehr.ui.adapter.PlaceAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val dao by lazy { AppDatabase.getInstance(this).placeDao() }

    private var editingPlace: Place? = null
    private val dateFormat = SimpleDateFormat("yyyy‑MM‑dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // DatePicker on date field
        binding.etDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, y, m, d ->
                    cal.set(y, m, d)
                    binding.etDate.setText(dateFormat.format(cal.time))
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        val adapter = PlaceAdapter(
            onEdit = { place ->
                // prefill form
                editingPlace = place
                binding.etTitle.setText(place.title)
                binding.etDescription.setText(place.description)
                binding.etDate.setText(place.date)
                binding.etLocation.setText(place.address)
                binding.btnSavePlace.text = getString(R.string.update_place)
            },
            onDelete = { place ->
                lifecycleScope.launch(Dispatchers.IO) {
                    dao.delete(place)
                    runOnUiThread {
                        Toast.makeText(this@MainActivity,"Deleted",Toast.LENGTH_SHORT).show()
                    }
                }
            },
            onClick = { place ->
                PlaceDetailActivity.start(this, place)
            }
        )

        binding.rvPlaces.layoutManager = LinearLayoutManager(this)
        binding.rvPlaces.adapter       = adapter

        // observe list
        dao.getAllPlaces().observe(this) { list ->
            adapter.submitList(list)
        }

        // Save or update
        binding.btnSavePlace.setOnClickListener {
            val t = binding.etTitle.text.toString().trim()
            val d = binding.etDescription.text.toString().trim()
            val dt= binding.etDate.text.toString().trim()
            val a = binding.etLocation.text.toString().trim()
            if (t.isEmpty()||d.isEmpty()||dt.isEmpty()||a.isEmpty()) {
                Toast.makeText(this,"All fields required",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch(Dispatchers.IO) {
                // geocode
                val res = Geocoder(this@MainActivity, Locale.getDefault())
                    .getFromLocationName(a, 1)
                if (res.isNullOrEmpty()) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity,"Address not found",Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }
                val loc = res[0]

                val place = Place(
                    id          = editingPlace?.id ?: 0,
                    title       = t,
                    description = d,
                    date        = dt,
                    address     = a,
                    latitude    = loc.latitude,
                    longitude   = loc.longitude
                )

                if (editingPlace != null) {
                    dao.update(place)
                } else {
                    dao.insert(place)
                }

                runOnUiThread {
                    // clear form
                    binding.etTitle.text?.clear()
                    binding.etDescription.text?.clear()
                    binding.etDate.text?.clear()
                    binding.etLocation.text?.clear()
                    binding.btnSavePlace.text = getString(R.string.add_place)
                    editingPlace = null
                }
            }
        }
    }
}