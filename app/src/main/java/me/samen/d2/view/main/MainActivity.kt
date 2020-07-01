package me.samen.d2.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import me.samen.d2.R
import me.samen.d2.data.AppDB
import me.samen.d2.data.daos.ThingDao
import me.samen.d2.data.entities.Thing
import me.samen.d2.databinding.ActivityMainBinding
/*
BACKUP commands
 adb shell dumpsys backup | grep "me.samen.d2"
 adb shell dumpsys backup | grep "me.samen.d2" -A 4 | grep Current
 adb shell bmgr restore 3b9f82a881618b53 me.samen.d2

 */

/*
FOR THE APP

        // TODO(satosh.dhanyamraju): dagger
// TODO(satosh.dhanyamraju): version control

 */
class MainActivity : AppCompatActivity() {
    private lateinit var thingDao: ThingDao
    private lateinit var vm : MainVM
    private lateinit var binding : ActivityMainBinding
    private lateinit var mAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO(satosh.dhanyamraju): move to fully data binding.
        thingDao = AppDB.instance(this).thingDao()
        vm = ViewModelProviders.of(this, MainVM.Factory(application, thingDao)).get(MainVM::class.java)
        mAdapter = MainAdapter(vm, this)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        with(binding.rv) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mAdapter
        }
        vm.fetch().observe(this, Observer {
            mAdapter.submitList(it)
        })
        //vm.ins(Thing(1, "evt", "hello world", "#building_app", "", "@me"))
    }
}