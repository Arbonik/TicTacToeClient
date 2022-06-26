package com.ctrlya.tictactoe.ui.menu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ctrlya.tictactoe.R
import com.ctrlya.tictactoe.UserPreference
import com.ctrlya.tictactoe.databinding.FragmentMainBinding


class MainFragment : Fragment()
{
    lateinit var binding: FragmentMainBinding
    val preference by lazy { UserPreference(requireContext()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = FragmentMainBinding.inflate(inflater)

        if (!preference.checkAuth())
            findNavController().navigate(
                R.id.action_mainFragment_to_acquaintedYouFragment
            )

        preference.name?.let { name ->
            Log.d("preference1", name)
            if (name.isBlank())
                binding.message.text = "Чекаво, \nсыграем? ;)"
            else
                binding.message.text = "С возвращением,\n$name!"
        }

        binding.startGame.setOnClickListener {
            findNavController().navigate(
                R.id.action_mainFragment_to_chooseLocalGameFragment
            )
        }
        binding.networkButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_mainFragment_to_chooseNetworkGameFragment
            )
        }

//        binding.settingButton.setOnClickListener {  }
        return binding.root
    }


}