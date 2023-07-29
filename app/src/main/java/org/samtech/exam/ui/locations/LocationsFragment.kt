package org.samtech.exam.ui.locations

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.samtech.exam.R
import org.samtech.exam.Singleton
import org.samtech.exam.utils.Constants.INTERVAL_TIME_FOR_TAP
import org.samtech.exam.utils.Utils.customToast
import org.samtech.exam.utils.Utils.mayTapButton

class LocationsFragment : Fragment() {

    private val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val opLocationsViewModel: LocationsViewModel by activityViewModels {
        LocationsViewModel.LocationsViewModelFactory(
            requireActivity().application,
            Singleton.instance!!.fireStoreLocationsRepository
        )
    }

    private lateinit var map: MapView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_locations, container, false)
        setupPolicyOsmMap(inflater.context)
        map = root.findViewById(R.id.fr_location_map)
        val fabLocation = root.findViewById<FloatingActionButton>(R.id.fr_location_fab)

        fabLocation.setOnClickListener {
            if (mayTapButton(INTERVAL_TIME_FOR_TAP)) {
                if (isGPSActive()) {
                    opLocationsViewModel.stopLocationUpdates()
                    opLocationsViewModel.startLocationUpdates()
                } else {
                    customToast(inflater.context, getString(R.string.enable_gps))
                }
            } else {
                customToast(inflater.context, getString(R.string.wait_n_seconds))
            }
        }

        val locations = getLocations()

        if (!locations.equals(getString(R.string.no_provider))) {
            Log.d("***", ""+locations)
            val startPoint = GeoPoint(locations.latitude, locations.longitude)
            setColor(map)
            centerMap(inflater.context, startPoint, getString(R.string.my_location))
        } else {
            Log.d("***", ""+locations)
            customToast(inflater.context, getString(R.string.no_locations))
        }

        return root
    }

    fun getLocations(): Location {
        val auxLocation = Location(getString(R.string.no_provider))
        opLocationsViewModel.locationListLiveData.observe(viewLifecycleOwner) { locations ->
            locations?.let {
                if (!locations.isEmpty()) {
                    //opLocationsViewModel.stopLocationUpdates()
                    for (location in locations) {
                        auxLocation.latitude = location.latitude
                        auxLocation.longitude = location.longitude
                    }
                }
            }
        }
        return auxLocation
    }

    fun setColor(paramMap: MapView) {
        paramMap.apply {
            val matrixA = ColorMatrix()
            matrixA.setSaturation(0.3f)
            val matrixB = ColorMatrix()
            matrixB.setScale(1.12f, 1.13f, 1.13f, 1.0f)
            matrixA.setConcat(matrixB, matrixA)
            val filter = ColorMatrixColorFilter(matrixA)
            overlayManager.tilesOverlay.setColorFilter(filter)
        }
    }

    fun setupPolicyOsmMap(ctx: Context) {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val preferences = androidx.preference.PreferenceManager
            .getDefaultSharedPreferences(ctx)
        Configuration.getInstance().load(ctx, preferences)
    }

    fun centerMap(ctx: Context, paramLocation: GeoPoint, paramTitle: String) {
        setMap(
            map,
            false,
            true,
            16.5,
            paramLocation,
            ContextCompat.getDrawable(ctx, R.drawable.ic_marker_location),
            paramTitle
        )
    }

    fun setMap(
        paramMap: MapView, hasZoom: Boolean, hasMultiTouch: Boolean,
        paramZoom: Double, paramLocation: GeoPoint,
        paramIcon: Drawable?, paramMTitle: String?
    ) {
        paramMap.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        paramMap.zoomController.setVisibility(if (hasZoom) CustomZoomButtonsController.Visibility.ALWAYS else CustomZoomButtonsController.Visibility.NEVER)
        paramMap.setMultiTouchControls(hasMultiTouch)
        paramMap.overlays.clear()
        val mapController = paramMap.controller
        mapController.setZoom(paramZoom)
        mapController.setCenter(paramLocation)
        val startMarker = Marker(paramMap)
        startMarker.title = paramMTitle
        startMarker.position = paramLocation
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        startMarker.setOnMarkerClickListener { marker, mapView ->
            marker.showInfoWindow()
            mapView.controller.animateTo(marker.position)
            true
        }
        paramMap.overlays.add(startMarker)
        startMarker.icon = paramIcon
    }

    fun isGPSActive(): Boolean {
        val mLocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun hasPermissions(context: Context?, vararg permissions: String?): Boolean {
        if (context != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission!!
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        map.onResume()

        if (!hasPermissions(context, *PERMISSIONS)) {
            val PERMISSION_ALL = 1
            ActivityCompat.requestPermissions(
                requireActivity(),
                PERMISSIONS,
                PERMISSION_ALL
            )
        }
    }

    override fun onPause() {
        super.onPause()
        map.onPause()

        if ((opLocationsViewModel.receivingLocationUpdates.value == true) &&
            (!hasPermissions(context, *PERMISSIONS))
        ) {
            opLocationsViewModel.stopLocationUpdates()
        }
    }

    override fun onDetach() {
        super.onDetach()
        map.onDetach()
    }

}