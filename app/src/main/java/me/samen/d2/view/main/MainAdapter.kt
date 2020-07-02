package me.samen.d2.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.samen.d2.R
import me.samen.d2.data.entities.Thing
import me.samen.d2.databinding.MainRowBinding

// TODO(satosh.dhanyamraju): use pagination; diffutils callbacks

class MainAdapter(
    private val vm: ThoughtsVM,
    private val lifecycleOwner: LifecycleOwner
) : PagedListAdapter<Thing, MainVH>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainVH {
        val binding = DataBindingUtil.inflate<MainRowBinding>(
            LayoutInflater.from(parent.context),
            R.layout.main_row,
            parent, false
        )
        return MainVH(binding)
    }

    override fun onBindViewHolder(holder: MainVH, position: Int) {
        holder.bind(getItem(position), vm)
    }

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<Thing>() {
            override fun areItemsTheSame(oldItem: Thing, newItem: Thing): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Thing, newItem: Thing): Boolean {
                return oldItem == newItem
            }

        }
    }
}


// TODO(satosh.dhanyamraju): make it generic
class MainVH(
    private val mainRowBinding: MainRowBinding
) : RecyclerView.ViewHolder(mainRowBinding.root) {
    fun bind(thing: Thing?, vm: ThoughtsVM) {
        thing?.run {
            mainRowBinding.d = thing
            mainRowBinding.vm = vm
            mainRowBinding.executePendingBindings()
        }
    }
}