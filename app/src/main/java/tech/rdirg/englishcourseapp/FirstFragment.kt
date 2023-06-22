package tech.rdirg.englishcourseapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import tech.rdirg.englishcourseapp.application.CourseApp
import tech.rdirg.englishcourseapp.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

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

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CourseListAdapter{ course ->
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(course)
            findNavController().navigate(action)
        }
        binding.dataRecyclerView.adapter = adapter
        binding.dataRecyclerView.layoutManager = LinearLayoutManager(context)
        courseViewModel.allCourses.observe(viewLifecycleOwner) { courses ->
            courses.let {
                if (courses.isEmpty()){
                    binding.notFoundImageView.visibility = View.VISIBLE
                    binding.notFoundTextView.visibility = View.VISIBLE
                }else{
                    binding.notFoundImageView.visibility = View.GONE
                    binding.notFoundTextView.visibility = View.GONE
                }
                adapter.submitList(courses)
            }
        }

        binding.addFAB.setOnClickListener {
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(null)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}