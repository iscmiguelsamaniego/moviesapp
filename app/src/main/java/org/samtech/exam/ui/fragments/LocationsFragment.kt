package org.samtech.exam.ui.fragments

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.pm.PackageManager
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
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
import org.samtech.exam.firebase.models.FSLocation
import org.samtech.exam.ui.viewmodels.LocationsViewModel
import org.samtech.exam.utils.Constants.CHANNEL_ID
import org.samtech.exam.utils.Constants.INTERVAL_TIME_FOR_TAP
import org.samtech.exam.utils.Utils.customToast
import org.samtech.exam.utils.Utils.getSpannedText
import org.samtech.exam.utils.Utils.mayTapButton
import java.util.Date
import java.util.UUID


class LocationsFragment : Fragment(), View.OnClickListener {

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
        val viewLocationsBtn: Button = root.findViewById(R.id.fr_location_view_btn)
        val fabLocation = root.findViewById<FloatingActionButton>(R.id.fr_location_fab)

        viewLocationsBtn.setOnClickListener(this)
        fabLocation.setOnClickListener(this)
        setColorMap(map)

        val ctx = inflater.context
        getRoomLocations(ctx)
        return root
    }

    private fun notifyLocation(ctx: Context, paramTitle: String, paramBody: String) {

        var notificationManager: NotificationManager? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            notificationManager =
                ctx.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }


        val notificationBuilder = NotificationCompat.Builder(ctx, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_notification)
            .setContentTitle(paramTitle)
            .setContentText(paramBody)
            /*.setStyle( //TODO THIS IS A PLUS FEATURE
                NotificationCompat.BigPictureStyle()
                    .bigPicture(imageBitmap)
                    .setBigContentTitle(messageTitle)
            )*/
            .setAutoCancel(true)
        //.setContentIntent(pendingIntent)//TODO THIS IS A PLUS FEATURE

        if (notificationManager != null) {
            notificationManager.notify(0, notificationBuilder.build())
        }

    }

    private fun getFSLocationsOnMap(ctx: Context) {
        opLocationsViewModel.stopLocationUpdates()

        opLocationsViewModel.getLocationsFSValues().observe(viewLifecycleOwner) { location ->
            location.let { locations ->

                for (location in locations) {
                    val startPoint = GeoPoint(
                        location.latitude.toDouble(),
                        location.longitude.toDouble()
                    )

                    val distance = FloatArray(1)

                    if (locations.getOrNull(-1) != null) {
                        Location.distanceBetween(
                            locations.get(-1).latitude.toDouble(),
                            locations.get(-1).longitude.toDouble(),
                            location.latitude.toDouble(),
                            location.longitude.toDouble(),
                            distance
                        )
                    }

                    distance[0] = distance[0] / 100
                    if (distance[0] <= 1.0) { //If the current distance is minor or equal to early location stored then show the markers (prevents the markers be close to each other) TODO Improve this
                        setMap(
                            map,
                            false,
                            true,
                            false,
                            16.5,
                            startPoint,
                            ContextCompat.getDrawable(ctx, R.drawable.ic_marker_location),
                            location.date
                        )
                    }else{
                        customToast(ctx, getString(R.string.no_values))
                    }
                }
            }
        }
    }

    private fun getRoomLocations(ctx: Context) {
        opLocationsViewModel.locationListLiveData.observe(viewLifecycleOwner) { locations ->
            locations?.let {
                if (locations.isNotEmpty()) {
                    for (location in locations) {

                        val latitudeStr = location.latitude.toString()
                        val longitudeStr = location.longitude.toString()
                        val dateStr = Date().toString()
//TODO PLUS FEATURE ADD VALIDATION NO REGISTER SAME PLACE IN RANGE ABOUT 8 METERS
                        opLocationsViewModel.storeLocations(
                            FSLocation(
                                UUID.randomUUID().toString(),
                                latitudeStr,
                                longitudeStr,
                                dateStr
                            )
                        )
                        val profileUser = getSpannedText(
                            getString(
                                R.string.location_values,
                                latitudeStr,
                                longitudeStr
                            )
                        )

                        notifyLocation(
                            ctx, getString(R.string.register_location),
                            profileUser.toString()
                        )

                        val startPoint = GeoPoint(location.latitude, location.longitude)
                        setMap(
                            map,
                            false,
                            true,
                            true,
                            16.5,
                            startPoint,
                            ContextCompat.getDrawable(ctx, R.drawable.ic_marker_location),
                            dateStr
                        )
                    }
                }
            }
        }
    }

    private fun setColorMap(paramMap: MapView) {
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

    private fun setupPolicyOsmMap(ctx: Context) {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val preferences = androidx.preference.PreferenceManager
            .getDefaultSharedPreferences(ctx)
        Configuration.getInstance().load(ctx, preferences)
    }

    private fun setMap(
        paramMap: MapView, hasZoom: Boolean, hasMultiTouch: Boolean, paramClearOverLay: Boolean,
        paramZoom: Double, paramLocation: GeoPoint,
        paramIcon: Drawable?, paramMTitle: String?
    ) {
        paramMap.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        paramMap.zoomController.setVisibility(if (hasZoom) CustomZoomButtonsController.Visibility.ALWAYS else CustomZoomButtonsController.Visibility.NEVER)
        paramMap.setMultiTouchControls(hasMultiTouch)
        if (paramClearOverLay) {
            paramMap.overlays.clear()
        }
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

    private fun isGPSActive(): Boolean {
        val mLocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun hasPermissions(context: Context?, vararg permissions: String?): Boolean {
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
            val permissionAll = 1
            ActivityCompat.requestPermissions(
                requireActivity(),
                PERMISSIONS,
                permissionAll
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

    override fun onClick(v: View?) {
        if (v?.id == R.id.fr_location_view_btn) {
            getFSLocationsOnMap(v.context)
        }

        if (v?.id == R.id.fr_location_fab) {
            if (mayTapButton(INTERVAL_TIME_FOR_TAP)) {
                if (isGPSActive()) {
                    opLocationsViewModel.stopLocationUpdates()
                    opLocationsViewModel.startLocationUpdates()
                } else {
                    customToast(v.context, getString(R.string.enable_gps))
                }
            } else {
                customToast(v.context, getString(R.string.wait_n_seconds))
            }
        }
    }

}