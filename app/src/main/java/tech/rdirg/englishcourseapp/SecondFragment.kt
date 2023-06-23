package tech.rdirg.englishcourseapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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

    private val args: SecondFragmentArgs? by navArgs()
    private var course: Course? = null

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
                    val course = Course(title = title.toString(), category = category.toString(), content = content.toString())
                    courseViewModel.insert(course)
                }else{
                    val course = Course(id = course?.id!!, title = title.toString(), category = category.toString(), content = content.toString())
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
}