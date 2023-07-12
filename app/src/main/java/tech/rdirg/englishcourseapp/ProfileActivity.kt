package tech.rdirg.englishcourseapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import tech.rdirg.englishcourseapp.databinding.ActivityCameraBinding
import tech.rdirg.englishcourseapp.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private var _binding: ActivityProfileBinding? = null
    private lateinit var applicationContext: Context
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = "Profile"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        applicationContext = getApplicationContext()
    }
}