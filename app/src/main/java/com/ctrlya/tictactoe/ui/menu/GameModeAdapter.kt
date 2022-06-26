package com.ctrlya.tictactoe.ui.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ctrlya.tictactoe.R
import com.ctrlya.tictactoe.databinding.GameModeItemBinding
import com.ctrlya.tictactoe.ui.game.GameMode
import com.ctrlya.tictactoe.ui.game.GameModeItem


class GameModeAdapter(
//    var click: () -> Unit,
    var dataSet: Array<GameModeItem> = arrayOf(),
    var isNetwork: Boolean
)
    : RecyclerView.Adapter<GameModeAdapter.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val binding = GameModeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.onBind(position)
    }

    override fun getItemCount(): Int = dataSet.size

    inner class ViewHolder(var binding: GameModeItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        var context = binding.root.context

        fun onBind(position: Int)
        {
            if (isNetwork)
                binding.icon.setBackgroundResource(R.drawable.pvp)
            else
                binding.icon.setBackgroundResource(R.drawable.elephant)


            binding.title.text = dataSet[position].title
            binding.subtitle.text = dataSet[position].subtitle

            binding.root.setOnClickListener {
                dataSet[position].gameMode()
            }
        }
    }
}