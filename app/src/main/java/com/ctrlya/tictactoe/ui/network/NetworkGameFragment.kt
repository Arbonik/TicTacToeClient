package com.ctrlya.tictactoe.ui.network

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.ctrlya.tictactoe.core.data.Mark
import com.ctrlya.tictactoe.core.player.RealPlayer
import com.ctrlya.tictactoe.databinding.NetworkGameFragmentBinding
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class NetworkGameFragment : Fragment() {

    private val viewModel: ConnectGameViewModel by viewModel<ConnectGameViewModel>()
    private lateinit var binding: NetworkGameFragmentBinding
    private val player = RealPlayer(Mark.X, lifecycleScope)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NetworkGameFragmentBinding.inflate(inflater, container, false)

        binding.tictacktoeview.setField(
            viewModel.field
        )

        lifecycleScope.launchWhenCreated {
            player.connectToTouchListener(binding.tictacktoeview)
        }

        lifecycleScope.launchWhenCreated {
            viewModel.info.collectLatest {
                binding.infoTextView.text = it
            }
        }

        val id = arguments?.getString("id")
        if (id != null)
        {

            lifecycleScope.launchWhenCreated {
                viewModel.ws(id, player.turn(), callback = {
                    binding.tictacktoeview.setField(viewModel.field)
                })
            }
        } else {
            Toast.makeText(requireContext(), "ID не передан", Toast.LENGTH_LONG).show()
        }

        return binding.root
    }
}

