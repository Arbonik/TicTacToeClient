package com.ctrlya.tictactoe.ui.network

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.ctrlya.tictactoe.R
import com.ctrlya.tictactoe.databinding.ConnectGameFragmentBinding
import com.ctrlya.tictactoe.ui.Resource
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConnectGameFragment : Fragment(R.layout.connect_game_fragment) {

    private val viewModel: ConnectGameViewModel by viewModel()
    private lateinit var bindind: ConnectGameFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadRooms()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindind = ConnectGameFragmentBinding.bind(view)
        roomsCollect()
        newRoomCreateListener()
        refreshLayoutListener()
    }

    private fun refreshLayoutListener() {
        bindind.swipeRefrest.setOnRefreshListener {
            viewModel.loadRooms()
        }
    }

    private fun newRoomCreateListener() {
        bindind.createRoomsFab.setOnClickListener {
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
                        bindind.swipeRefrest.isRefreshing = false
                    }
                    is Resource.Loading -> {
                        bindind.swipeRefrest.isRefreshing = true
                    }
                    is Resource.Success -> {
                        bindind.swipeRefrest.isRefreshing = false
                        //TODO NO ERROR VALIDATION ADD PLEASE
                        if (it.data?.items?.isNotEmpty() == true) {
                            bindind.noFreeRoomsHit.visibility = View.GONE
                            bindind.roomsListView.adapter =
                                ArrayAdapter(
                                    requireContext(),
                                    android.R.layout.simple_list_item_1,
                                    it.data.items
                                )
                            bindind.roomsListView.setOnItemClickListener { parent, view, position, id ->
//                                findNavController().navigate(
//                                    GameRoomsFragmentDirections.actionGameRoomsFragmentToNetworkBattleFragment(
//                                        it.data.items[position].id
//                                    )
//                                )
                            }
                        } else {
                            bindind.noFreeRoomsHit.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

}