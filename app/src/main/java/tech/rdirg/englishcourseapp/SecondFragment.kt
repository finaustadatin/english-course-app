package tech.rdirg.englishcourseapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import tech.rdirg.englishcourseapp.application.CourseApp
import tech.rdirg.englishcourseapp.databinding.FragmentSecondBinding
import tech.rdirg.englishcourseapp.model.Course

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener{

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var applicationContext: Context
    private val courseViewModel: CourseViewModel by viewModels {
        CourseViewModelFactory((applicationContext as CourseApp).repository)
    }

    private val args: SecondFragmentArgs? by navArgs()
    private var course: Course? = null
    private lateinit var mMap: GoogleMap
    private var currentLatLng: LatLng? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val cameraRequestCode = 2

    override fun onAttach(context: Context) {
        super.onAttach(context)
        applicationContext = context.applicationContext
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        course = args?.course
        if (course != null){
            binding.deleteButton.visibility = View.VISIBLE
            binding.saveButton.text = "Update"
            binding.nameEditText.setText(course?.title)
            binding.categoryEditText.setText(course?.category)
            binding.contentTextMultiLine.setText(course?.content)
            val label = "Edit Course"
            navController.currentDestination?.label = label
        }else{
            val label = "Add Course"
            navController.currentDestination?.label = label
        }

        // binding google map
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        checkPermission()

        val title = binding.nameEditText.text
        val category = binding.categoryEditText.text
        val content = binding.contentTextMultiLine.text
        binding.saveButton.setOnClickListener {
            if(title.isEmpty() || category.isEmpty() || content.isEmpty()){
                if (title.isEmpty()){
                    Toast.makeText(context, "Title is required", Toast.LENGTH_SHORT).show()
                }
                if (category.isEmpty()){
                    Toast.makeText(context, "Category is required", Toast.LENGTH_SHORT).show()
                }
                if (content.isEmpty()){
                    Toast.makeText(context, "Content is required", Toast.LENGTH_SHORT).show()
                }
            }else{
                if (course == null){
                    val course = Course(title = title.toString(), category = category.toString(), content = content.toString(), longitude = currentLatLng?.longitude!!, latitude = currentLatLng?.latitude!!)
                    courseViewModel.insert(course)
                }else{
                    val course = Course(id = course?.id!!, title = title.toString(), category = category.toString(), content = content.toString(), longitude = currentLatLng?.longitude!!, latitude = currentLatLng?.latitude!!)
                    courseViewModel.update(course)
                }
                findNavController().popBackStack()
            }
        }

        binding.deleteButton.setOnClickListener {
            courseViewModel.delete(course!!)
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // implement drag marker

        val uiSettings = mMap.uiSettings
        uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerDragListener(this)
    }

    override fun onMarkerDrag(p0: Marker) {
    }

    override fun onMarkerDragEnd(marker: Marker) {
        val newPosition = marker.position
        currentLatLng = LatLng(newPosition.latitude, newPosition.longitude)
        Toast.makeText(context, currentLatLng.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onMarkerDragStart(p0: Marker) {
    }

    private fun checkPermission(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        if (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            getCurrentLocation()
        }else {
            Toast.makeText(applicationContext, "Access Location denied!", Toast.LENGTH_LONG).show()
        }
    }

    private fun getCurrentLocation(){
        // check permission
        if(ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null){
                var latLng = LatLng(location.latitude, location.longitude)
                currentLatLng = latLng
                var title = "Marker"

                if(course != null){
                    latLng = LatLng(course?.latitude!!, course?.longitude!!)
                    currentLatLng = latLng
                    title = course?.title.toString()
                }

                val markerOptions = MarkerOptions()
                    .position(latLng)
                    .title(title)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_course_map_32))
                mMap.addMarker(markerOptions)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
            }
        }
    }

//    private fun checkCameraPermission() {
//        if(ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            activity?.let { ActivityCompat.requestPermissions(it, arrayOf(android.Manifest.permission.CAMERA), cameraRequestCode) }
//        }else{
//            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            startActivityForResult(cameraIntent, cameraRequestCode)
//        }
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if ((requestCode == cameraRequestCode) && (resultCode == Activity.RESULT_OK)){
//            val imageBitmap = data?.extras?.get("data") as Bitmap
//            binding.photoImageView.setImageBitmap(imageBitmap)
//        }
//    }
}