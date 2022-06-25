package com.ctrlya.tictactoe.ui.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctrlya.tictactoe.R
import com.ctrlya.tictactoe.databinding.FragmentMainBinding


class MainFragment : Fragment()
{
    lateinit var binding: FragmentMainBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = FragmentMainBinding.inflate(inflater)



        return binding.root
    }


}