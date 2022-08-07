package com.ctrlya.tictactoe.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.ctrlya.tictactoe.R
import com.ctrlya.tictactoe.UserPreference
import com.ctrlya.tictactoe.databinding.FragmentAcquaintedYouBinding

class AcquaintedYouFragment : Fragment()
{
    private lateinit var binding: FragmentAcquaintedYouBinding
    val preference by lazy { UserPreference(requireContext()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = FragmentAcquaintedYouBinding.inflate(inflater)

        binding.continueButton.setOnClickListener {
            val name = binding.userName.text.toString()

            preference.authUserOnDevice(name)

            findNavController().navigate(
                R.id.action_acquaintedYouFragment_to_acquaintedXObotFragment,
                bundleOf("username" to name)
            )

        }

        binding.skip.setOnClickListener {
            preference.authUserOnDevice("")
            findNavController().navigateUp()
        }
        return binding.root
    }

}