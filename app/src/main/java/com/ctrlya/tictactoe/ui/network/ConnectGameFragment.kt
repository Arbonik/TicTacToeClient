package com.ctrlya.tictactoe.ui.network

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ctrlya.tictactoe.R
import com.ctrlya.tictactoe.databinding.ConnectGameFragmentBinding
import com.ctrlya.tictactoe.ui.Resource
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConnectGameFragment : Fragment(R.layout.connect_game_fragment) {

    private val viewModel: ConnectGameViewModel by viewModel()
    private lateinit var binding: ConnectGameFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadRooms()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding = ConnectGameFragmentBinding.bind(view)
        roomsCollect()
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

            findNavController().navigate(
                R.id.action_connectGameFragment_to_createRoomFragment
            )
//            viewModel.createRoom {
//                viewModel.loadRooms()
//                findNavController().navigate(
//                    GameRoomsFragmentDirections.actionGameRoomsFragmentToNetworkBattleFragment(it.roomId!!)
//                )
//            }
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