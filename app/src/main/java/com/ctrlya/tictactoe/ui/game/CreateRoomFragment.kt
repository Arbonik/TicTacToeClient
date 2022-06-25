package com.ctrlya.tictactoe.ui.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ctrlya.tictactoe.core.domain.BattlefieldSettings
import com.ctrlya.tictactoe.databinding.FragmentCreateRoomBinding
import com.google.android.material.snackbar.Snackbar

class CreateRoomFragment : Fragment() {
   lateinit var binding: FragmentCreateRoomBinding
   private val viewModel: CreateRoomViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = FragmentCreateRoomBinding.inflate(inflater)


        return binding.root
    }


    private fun createRoom()
    {
        var gameSetting = BattlefieldSettings(3,3,3,false)
        viewModel.createRoom(gameSetting) {
            if (it != null && it.roomId.isNotEmpty())
            {

            } else
                Snackbar.make(binding.root, "Ошибка в создании комнаты", Snackbar.LENGTH_LONG).show()

        }

    }


}