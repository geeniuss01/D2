package me.samen.d2.view.bindings

import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.chip.Chip
import me.samen.d2.R
import me.samen.d2.data.entities.Bullet
import me.samen.d2.data.entities.Thing
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

@BindingAdapter("bind:bullet_type", requireAll = true)
fun bindBulletType(atv: AutoCompleteTextView, bullet: Bullet?) {
    // TODO(satosh.dhanyamraju): read from DB
    val items = (setOf("note", "todo", "evt") + (bullet?.type ?: "evt")).toList()
    val adapter = ArrayAdapter(atv.context, R.layout.list_item, items)
    atv.setAdapter(adapter)
    bullet?.type?.let { atv.setText(it, false) }
}

@BindingAdapter("bind:thought_type", requireAll = true)
fun bindBulletType(atv: AutoCompleteTextView, thought: Thing?) {
    // TODO(satosh.dhanyamraju): read from DB

    val items = (setOf(
        "evt", "event", "app", "feeling", "tracker", "list", "phone", "sw", "company",
        "person", "Book", "notes", "task", "recipe", "programming language", "note", "concept",
        "comparison", "lists", "notes", "feelings", "experience", "design", "hap", "thought",
        "wisdom", "dream", "misc"
    ) + (thought?.type ?: "misc")).toList()
    val adapter = ArrayAdapter(atv.context, R.layout.list_item, items)
    atv.setAdapter(adapter)
    thought?.type?.let { atv.setText(it, false) }
}

@BindingAdapter("bind:tdesc", "bind:clk", requireAll = true)
fun bindAbulletDesc(textView: TextView, bullet: Bullet, clk: View.OnClickListener) {
    textView.text = bullet.desc
    textView.tag = bullet.id
    textView.setOnClickListener(clk)
}