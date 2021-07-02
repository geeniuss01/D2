package me.samen.d2.view.obsnote

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.samen.d2.R
import me.samen.d2.data.entities.ObsNote
import me.samen.d2.databinding.MainObsnRowBinding

class ONoteMainAdapter(
    private val vm: ONoteVM,
    private val lifecycleOwner: LifecycleOwner
) : PagedListAdapter<ObsNote, MainVH>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainVH {
        val binding = DataBindingUtil.inflate<MainObsnRowBinding>(
            LayoutInflater.from(parent.context),
            R.layout.main_obsn_row,
            parent, false
        )
        return MainVH(binding)
    }

    override fun onBindViewHolder(holder: MainVH, position: Int) {
        holder.bind(getItem(position), vm)
    }

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<ObsNote>() {
            override fun areItemsTheSame(
                oldItem: ObsNote,
                newItem: ObsNote
            ): Boolean {
                return oldItem?.fileName == newItem?.fileName
            }

            override fun areContentsTheSame(
                oldItem: ObsNote,
                newItem: ObsNote
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}


class MainVH(
    private val mainRowBinding: MainObsnRowBinding
) : RecyclerView.ViewHolder(mainRowBinding.root) {
    fun bind(thing: ObsNote?, vm: ONoteVM) {
        thing?.run {
            mainRowBinding.obsnote = thing
            mainRowBinding.onvm = vm
            mainRowBinding.executePendingBindings()
        }
    }
}