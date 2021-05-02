package me.samen.d2.view.bullet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.samen.d2.R
import me.samen.d2.data.entities.Bullet
import me.samen.d2.databinding.BulletRowBinding

class BulletAdapter(
    private val vm: BulletVM,
    private val lifecycleOwner: LifecycleOwner,
    private val clickListener: View.OnClickListener
) : PagedListAdapter<Bullet, BulletVH>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BulletVH {
        val binding = DataBindingUtil.inflate<BulletRowBinding>(
            LayoutInflater.from(parent.context),
            R.layout.bullet_row,
            parent, false
        )
        return BulletVH(binding, clickListener)
    }

    override fun onBindViewHolder(holder: BulletVH, position: Int) {
        holder.bind(getItem(position), vm)
    }

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<Bullet>() {
            override fun areItemsTheSame(oldItem: Bullet, newItem: Bullet): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Bullet, newItem: Bullet): Boolean {
                return oldItem == newItem
            }

        }
    }
}


// TODO(satosh.dhanyamraju): make it generic
class BulletVH(
    private val bulletRowBinding: BulletRowBinding,
    private val onClickListener: View.OnClickListener
) : RecyclerView.ViewHolder(bulletRowBinding.root) {
    fun bind(Bullet: Bullet?, vm: BulletVM) {
        Bullet?.run {
            bulletRowBinding.d = Bullet
            bulletRowBinding.vm = vm
            bulletRowBinding.click = onClickListener
            bulletRowBinding.executePendingBindings()
        }
    }
}