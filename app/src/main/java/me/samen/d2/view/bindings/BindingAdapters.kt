package me.samen.d2.view.bindings

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.chip.Chip
import me.samen.d2.data.entities.Bullet
import me.samen.d2.data.entities.ThingWithBullets
import me.samen.d2.view.bullet.BulletVM
import me.samen.d2.view.main.ThoughtsVM

@BindingAdapter("bind:tparent", "bind:vm", requireAll = true)
fun bindTime(textView: View, thing: ThingWithBullets, thoughtsViewModel: ThoughtsVM) {
    textView.setOnClickListener { thoughtsViewModel.click(textView, thing) }
    textView.setOnLongClickListener { thoughtsViewModel.onLongPress(textView, thing) }
}

@BindingAdapter("bind:ttype", "bind:vm", requireAll = true)
fun bindType(textView: TextView, thing: ThingWithBullets, thoughtsViewModel: ThoughtsVM) {
    textView.setText(thing.thing?.type)
}

@BindingAdapter("bind:ttime", "bind:vm", requireAll = true)
fun bindTime(textView: TextView, thing: ThingWithBullets, thoughtsViewModel: ThoughtsVM) {
    textView.setText(thing.thing?.ts)
}

@BindingAdapter("bind:tdesc", "bind:vm", requireAll = true)
fun bindDesc(textView: TextView, thing: ThingWithBullets, thoughtsViewModel: ThoughtsVM) {
    textView.setText(thing.thing?.desc)
}

@BindingAdapter("bind:ttags", "bind:vm", requireAll = true)
fun bindTags(textView: TextView, thing: ThingWithBullets, thoughtsViewModel: ThoughtsVM) {
    textView.setText(thing.thing?.tags)
}

@BindingAdapter("bind:tpeople", "bind:vm", requireAll = true)
fun bindPeople(textView: TextView, thing: ThingWithBullets, thoughtsViewModel: ThoughtsVM) {
    textView.setText(thing.thing?.people)
}

@BindingAdapter("bind:tbullets", "bind:vm", requireAll = true)
fun bindBullets(textView: TextView, thing: ThingWithBullets, thoughtsViewModel: ThoughtsVM) {
    textView.setText(thing.bullets.joinToString(", "))
}

@BindingAdapter("bind:burowdone", "bind:vm", requireAll = true)
fun bindburowDone(checkBox: Chip, b: Bullet, bulletVM: BulletVM) {
    if (!b.isTodoType) {
        checkBox.visibility = View.GONE
        return
    }
    checkBox.visibility = View.VISIBLE
    checkBox.isChecked = b.isDone
    checkBox.setOnClickListener {
        bulletVM.toggleDone(b)
    }
}