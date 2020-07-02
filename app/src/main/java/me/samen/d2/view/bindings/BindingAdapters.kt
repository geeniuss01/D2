package me.samen.d2.view.bindings

import android.widget.TextView
import androidx.databinding.BindingAdapter
import me.samen.d2.data.entities.Thing
import me.samen.d2.view.main.ThoughtsVM

@BindingAdapter("bind:ttype", "bind:vm", requireAll = true)
fun bindType(textView: TextView, thing: Thing, thoughtsViewModel: ThoughtsVM) {
    textView.setText(thing.type)
}

@BindingAdapter("bind:ttime", "bind:vm", requireAll = true)
fun bindTime(textView: TextView, thing: Thing, thoughtsViewModel: ThoughtsVM) {
    textView.setText(thing.ts)
}

@BindingAdapter("bind:tdesc", "bind:vm", requireAll = true)
fun bindDesc(textView: TextView, thing: Thing, thoughtsViewModel: ThoughtsVM) {
    textView.setText(thing.desc)
    textView.setOnClickListener { thoughtsViewModel.click(textView, thing) }
}

@BindingAdapter("bind:ttags", "bind:vm", requireAll = true)
fun bindTags(textView: TextView, thing: Thing, thoughtsViewModel: ThoughtsVM) {
    textView.setText(thing.tags)
}

@BindingAdapter("bind:tpeople", "bind:vm", requireAll = true)
fun bindPeople(textView: TextView, thing: Thing, thoughtsViewModel: ThoughtsVM) {
    textView.setText(thing.people)
}