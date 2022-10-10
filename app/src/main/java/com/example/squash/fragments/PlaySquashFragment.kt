package com.example.squash.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.squash.R
import com.example.squash.databinding.FragmentPlaySquashBinding


class PlaySquashFragment : Fragment() {

    private var _binding : FragmentPlaySquashBinding? = null
    private  val binding get() = _binding!!

    override fun onResume() {
        super.onResume()

        val category = resources.getStringArray(R.array.category)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down, category)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)


        val timer = resources.getStringArray(R.array.timer)
        val timerAdapter = ArrayAdapter(requireContext(), R.layout.drop_down, timer)
        binding.autoCompleteTextView2.setAdapter(timerAdapter)

        val languages = resources.getStringArray(R.array.languages)
        val languageAdapter = ArrayAdapter(requireContext(),R.layout.drop_down, languages)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentPlaySquashBinding.inflate(inflater, container, false)



        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}