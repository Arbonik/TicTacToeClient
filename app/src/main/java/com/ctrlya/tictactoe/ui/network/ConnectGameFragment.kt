package com.ctrlya.tictactoe.ui.network

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ctrlya.tictactoe.R
import com.ctrlya.tictactoe.core.domain.BattlefieldSettings
import com.ctrlya.tictactoe.databinding.ConnectGameFragmentBinding
import com.ctrlya.tictactoe.ui.Resource
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConnectGameFragment : Fragment(R.layout.connect_game_fragment) {

    private val viewModel: ConnectGameViewModel by viewModel()
    private lateinit var binding: ConnectGameFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.matchMaking()
//        viewModel.loadRooms()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ConnectGameFragmentBinding.bind(view)
//        roomsCollect()
        matchMakingCollect()
        newRoomCreateListener()
        refreshLayoutListener()
    }

    private fun refreshLayoutListener() {
        binding.swipeRefrest.setOnRefreshListener {
            viewModel.loadRooms()
        }
    }

    private fun newRoomCreateListener() {
        binding.createRoomsFab.setOnClickListener {
            viewModel.createRoom(BattlefieldSettings(3, 3,3,false)) {
                viewModel.loadRooms()
            }
        }
    }

    private fun matchMakingCollect() {
        lifecycleScope.launchWhenCreated {
            viewModel.roomId.collectLatest {
                when (it) {
                    is Resource.Error -> {
                        binding.progress.visibility = View.GONE
                    }
                    is Resource.Loading -> {
                        binding.progress.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progress.visibility = View.GONE
                        val bundle = bundleOf("id" to it.data)
                        findNavController().navigate(
                            R.id.action_connectGameFragment_to_networkGameFragment,
                            bundle
                        )
                    }
                }
            }
        }
    }

    private fun roomsCollect() {
        lifecycleScope.launchWhenStarted {
            viewModel.rooms.collectLatest {
                when (it) {
                    is Resource.Error -> {
                        binding.swipeRefrest.isRefreshing = false
                    }
                    is Resource.Loading -> {
                        binding.swipeRefrest.isRefreshing = true
                    }
                    is Resource.Success -> {
                        binding.swipeRefrest.isRefreshing = false
                        //TODO NO ERROR VALIDATION ADD PLEASE
                        if (it.data?.items?.isNotEmpty() == true) {
                            binding.noFreeRoomsHit.visibility = View.GONE
                            binding.roomsListView.adapter =
                                ArrayAdapter(
                                    requireContext(),
                                    android.R.layout.simple_list_item_1,
                                    it.data.items
                                )
                            binding.roomsListView.setOnItemClickListener { parent, view, position, id ->
                                findNavController().navigate(
                                    R.id.action_connectGameFragment_to_networkGameFragment,
                                    bundleOf("id" to it.data.items[position].id)
                                )
                            }
                        } else {
                            binding.noFreeRoomsHit.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

}