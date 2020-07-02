package me.samen.d2.view.bindings

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.databinding.BindingAdapter
import me.samen.d2.data.entities.Bullet
import me.samen.d2.data.entities.Thing
import me.samen.d2.view.bullet.BulletVM
import me.samen.d2.view.main.ThoughtsVM

@BindingAdapter("bind:ttype", "bind:vm", requireAll = true)
fun bindType(textView: TextView, thing: Thing, thoughtsViewModel: ThoughtsVM) {
    textView.setText(thing.type)
}

@BindingAdapter("bind:ttime", "bind:vm", requireAll = true)
fun bindTime(textView: TextView, thing: Thing, thoughtsViewModel: ThoughtsVM) {
    textView.setText(thing.ts)
    textView.setOnClickListener { thoughtsViewModel.click(textView, thing) }
}

@BindingAdapter("bind:tdesc", "bind:vm", requireAll = true)
fun bindDesc(textView: TextView, thing: Thing, thoughtsViewModel: ThoughtsVM) {
    textView.setText(thing.desc)
    textView.setOnClickListener { thoughtsViewModel.click(textView, thing) }
}

@BindingAdapter("bind:ttags", "bind:vm", requireAll = true)
fun bindTags(textView: TextView, thing: Thing, thoughtsViewModel: ThoughtsVM) {
    textView.setText(thing.tags)
    textView.setOnClickListener { thoughtsViewModel.click(textView, thing) }
}

@BindingAdapter("bind:tpeople", "bind:vm", requireAll = true)
fun bindPeople(textView: TextView, thing: Thing, thoughtsViewModel: ThoughtsVM) {
    textView.setText(thing.people)
    textView.setOnClickListener { thoughtsViewModel.click(textView, thing) }
}

@BindingAdapter("bind:burowdone", "bind:vm", requireAll = true)
fun bindburowDone(checkBox: CheckBox, b: Bullet, bulletVM: BulletVM) {
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