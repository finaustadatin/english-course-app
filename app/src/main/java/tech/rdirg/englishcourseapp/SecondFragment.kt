package tech.rdirg.englishcourseapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import tech.rdirg.englishcourseapp.application.CourseApp
import tech.rdirg.englishcourseapp.databinding.FragmentSecondBinding
import tech.rdirg.englishcourseapp.model.Course

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var applicationContext: Context
    private val courseViewModel: CourseViewModel by viewModels {
        CourseViewModelFactory((applicationContext as CourseApp).repository)
    }

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

        val title = binding.nameEditText.text
        val category = binding.categoryEditText.text
        val content = binding.contentTextMultiLine.text
        binding.saveButton.setOnClickListener {
            val course = Course(title = title.toString(), category = category.toString(), content = content.toString())
            courseViewModel.insert(course)
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}