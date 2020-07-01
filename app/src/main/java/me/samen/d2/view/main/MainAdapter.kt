package me.samen.d2.view.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import me.samen.d2.R
import me.samen.d2.data.entities.Thing
import me.samen.d2.databinding.MainRowBinding

// TODO(satosh.dhanyamraju): use pagination; diffutils callbacks

class MainAdapter(private val vm : MainVM,
private val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<MainVH>() {
    private val list = arrayListOf<Thing>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainVH {
        val binding = DataBindingUtil.inflate<MainRowBinding>(LayoutInflater.from(parent.context),
            R.layout.main_row,
            parent, false)
        return MainVH(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MainVH, position: Int) {
        holder.bind(list[position], vm)
    }

    fun update(l: List<Thing>) {
        list.clear()
        list.addAll(l)
        notifyDataSetChanged()
    }
}


// TODO(satosh.dhanyamraju): make it generic
class MainVH(
    private val mainRowBinding: MainRowBinding
) : RecyclerView.ViewHolder(mainRowBinding.root) {
    fun bind(thing: Thing, vm: MainVM) {
        mainRowBinding.d = thing
        mainRowBinding.vm = vm
        mainRowBinding.executePendingBindings()
    }
}