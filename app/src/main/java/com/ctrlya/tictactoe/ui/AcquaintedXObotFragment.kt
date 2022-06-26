package com.ctrlya.tictactoe.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ctrlya.tictactoe.R
import com.ctrlya.tictactoe.databinding.FragmentAcquaintedXObotBinding


class AcquaintedXObotFragment : Fragment()
{
    private lateinit var binding: FragmentAcquaintedXObotBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = FragmentAcquaintedXObotBinding.inflate(inflater)

        binding.hello.text = "Привет, " + arguments?.getString("username")
        binding.startGame.setOnClickListener {
            findNavController().navigate(R.id.action_acquaintedXObotFragment_to_mainFragment)
        }
        return binding.root
    }


}