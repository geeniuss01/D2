package me.samen.d2.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.samen.d2.R
import me.samen.d2.data.entities.ThingWithBullets
import me.samen.d2.databinding.MainRowBinding

class MainAdapter(
    private val vm: ThoughtsVM,
    private val lifecycleOwner: LifecycleOwner
) : PagedListAdapter<ThingWithBullets, MainVH>(DIFF_CALLBACK) {
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
            DiffUtil.ItemCallback<ThingWithBullets>() {
            override fun areItemsTheSame(
                oldItem: ThingWithBullets,
                newItem: ThingWithBullets
            ): Boolean {
                return oldItem.thing?.id == newItem.thing?.id
            }

            override fun areContentsTheSame(
                oldItem: ThingWithBullets,
                newItem: ThingWithBullets
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}


// TODO(satosh.dhanyamraju): make it generic
class MainVH(
    private val mainRowBinding: MainRowBinding
) : RecyclerView.ViewHolder(mainRowBinding.root) {
    fun bind(thing: ThingWithBullets?, vm: ThoughtsVM) {
        thing?.run {
            mainRowBinding.d = thing
            mainRowBinding.vm = vm
            mainRowBinding.executePendingBindings()
        }
    }
}