package com.ctrlya.tictactoe.ui.rate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ctrlya.tictactoe.databinding.RatingItemBinding

class RatingRecyclerAdapter(var dataSet: List<RatingResponse> = listOf()) : RecyclerView.Adapter<RatingRecyclerAdapter.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val binding = RatingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.onBind(position)
    }

    override fun getItemCount(): Int = dataSet.size

    inner class ViewHolder(var binding: RatingItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        var context = binding.root.context

        fun onBind(position: Int)
        {
            binding.rating.text = dataSet[position].pts.toString()
                //allraiting
        }
    }
}