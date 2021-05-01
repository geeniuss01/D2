package me.samen.d2.view.main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import me.samen.d2.R
import me.samen.d2.data.AppDB
import me.samen.d2.data.BUNDLE_THING_ID
import me.samen.d2.data.daos.ThingDao
import me.samen.d2.data.entities.Thing
import me.samen.d2.databinding.ActivityThoughtsBinding
import me.samen.d2.view.bullet.BulletsActivity
import me.samen.d2.view.edit.EditActivity


/*
BACKUP commands
 adb shell bmgr backupnow me.samen.d2
 adb shell dumpsys backup | grep "me.samen.d2" -A 4 | grep Current
 adb shell bmgr restore 3b9f82a881618b53 me.samen.d2

 */

class ThoughtsActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG: String = "MainActivity"
    private lateinit var thingDao: ThingDao
    private lateinit var vm: ThoughtsVM
    private lateinit var binding: ActivityThoughtsBinding
    private lateinit var mAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO(satosh.dhanyamraju): move to fully data binding.
        thingDao = AppDB.instance(this).thingDao()
        vm = ViewModelProviders.of(
            this,
            ThoughtsVM.Factory(application, thingDao, AppDB.instance(this).bulletDao())
        )
            .get(ThoughtsVM::class.java)
        mAdapter = MainAdapter(vm, this)
        binding = DataBindingUtil.setContentView<ActivityThoughtsBinding>(
            this,
            R.layout.activity_thoughts
        )
        binding.setListener(this)
        with(binding.rv) {
            layoutManager = HPLinearLayoutManager(this@ThoughtsActivity)
            adapter = mAdapter
            addItemDecoration(
                DividerItemDecoration(this@ThoughtsActivity, DividerItemDecoration.VERTICAL)
            )

        }
        vm.fetch().observe(this, Observer {
            mAdapter.submitList(it)
        })
        vm.edits.observe(this, Observer { evt ->
            evt.getContentIfNotHandled()?.let {
                edit(it)
            }
        })
        vm.opens.observe(this, Observer { evt ->
            evt.getContentIfNotHandled()?.let {
                open(it)
            }
        })
        binding.mainSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                vm.searchQuery.value = p0?.trim()?.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        vm.searchQuery.value = null
    }


    override fun onClick(p0: View?) {
        if (p0?.id == R.id.main_new) {
            vm.insNewDefault(binding.mainSearch.text?.toString() ?: "")
        }
    }

    override fun onBackPressed() {
        if (binding.mainSearch.text.toString().isNotEmpty()) {
            binding.mainSearch.setText("")
        } else
            super.onBackPressed()
    }

    private fun edit(thing: Thing) {
        val intent = Intent(this@ThoughtsActivity, EditActivity::class.java)
        intent.putExtra(BUNDLE_THING_ID, thing.id)
        startActivity(intent)
    }

    private fun open(thing: Thing) {
        val intent = Intent(this@ThoughtsActivity, BulletsActivity::class.java)
        intent.putExtra(BUNDLE_THING_ID, thing.id)
        startActivity(intent)
    }

}