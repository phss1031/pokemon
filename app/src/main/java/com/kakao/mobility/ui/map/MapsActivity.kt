package com.kakao.mobility.ui.map

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.kakao.mobility.R
import com.kakao.mobility.databinding.ActivityMapsBinding
import com.kakao.mobility.model.dto.PokemonLocation
import com.kakao.mobility.ui.ViewStatus
import com.kakao.mobility.utils.px
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


fun Context.startMapActivity(id: Long = 0L) = id.takeIf { it >= 0 }?.let {
    startActivity(Intent(this, MapsActivity::class.java).apply {
        putExtra(EXTRA_ID, id)
    })
}

const val EXTRA_ID = "EXTRA_ID"

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val viewModel: MapViewModel by viewModel {
        parametersOf(intent?.extras?.get(EXTRA_ID) ?: 0L)
    }

    private var _binding: ActivityMapsBinding? = null
    private val binding get() = _binding!!

    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.backButton.setOnClickListener { finish() }

        viewModel.viewStatus.observe(this, ::onViewStatusChanged)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(_googleMap: GoogleMap) {
        googleMap = _googleMap

        _googleMap.uiSettings.isZoomControlsEnabled = true
        viewModel.pokemonLocations.observe(this, ::onLocationChanged)
    }

    private fun onViewStatusChanged(viewStatus: ViewStatus) = when (viewStatus) {
        is ViewStatus.ErrorFinish -> {
            Toast.makeText(this, viewStatus.resourceId, Toast.LENGTH_LONG).show()
            finish()
        }
        else -> {
        }//DoNothing
    }

    private fun onLocationChanged(pokemonLocation: List<PokemonLocation>) {
        val builder = LatLngBounds.Builder()

        pokemonLocation.forEach {
            val position = LatLng(it.lat, it.lng)
            val marker = MarkerOptions().position(position).title(viewModel.getName())
            googleMap.addMarker(marker)
            builder.include(marker.position)
        }

        val bounds = builder.build()
        binding.backButton.post {
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, 64.px)
            googleMap.moveCamera(cu)
        }
    }
}
