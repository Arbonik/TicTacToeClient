package com.ctrlya.tictactoe.ui.rate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ctrlya.tictactoe.databinding.FragmentRatingBinding
import com.ctrlya.tictactoe.ui.Resource
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class RatingFragment : Fragment()
{
   private lateinit var binding: FragmentRatingBinding
   private val viewModel: RatingViewModel by viewModel()
    private var adapter = RatingRecyclerAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRatingBinding.inflate(inflater)

        binding.ratingList.adapter = adapter
        binding.ratingList.layoutManager = LinearLayoutManager(context)

        viewModel.getRating()
        ratingCollect()

        return binding.root
    }



    private fun ratingCollect() {
        lifecycleScope.launchWhenStarted {
            viewModel.rating.collectLatest {
                when (it) {
                    is Resource.Error -> {
                        binding.progress.visibility = View.GONE
                    }
                    is Resource.Loading -> {
                        binding.progress.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        it.data?.let { rating ->
                            adapter.dataSet = rating
                            adapter.notifyDataSetChanged()
                        }
                        binding.progress.visibility = View.GONE
                    }
                }
            }
        }
    }
}