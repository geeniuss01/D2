package me.samen.d2.view.bullet

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import me.samen.d2.R
import me.samen.d2.data.AppDB
import me.samen.d2.data.BUNDLE_THING_ID
import me.samen.d2.data.daos.BulletDao
import me.samen.d2.data.daos.ThingDao
import me.samen.d2.data.entities.BULLET_TYPE_EVT
import me.samen.d2.data.entities.BULLET_TYPE_NOTE
import me.samen.d2.data.entities.BULLET_TYPE_TODO
import me.samen.d2.databinding.ActivityBulletsBinding

class BulletsActivity : AppCompatActivity(), View.OnClickListener, SearchView.OnQueryTextListener {
    private lateinit var thingDao: ThingDao
    private lateinit var bulletsDao: BulletDao
    private lateinit var vm: BulletVM
    private lateinit var binding: ActivityBulletsBinding
    private lateinit var mAdapter: BulletAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val thingId = intent.getLongExtra(BUNDLE_THING_ID, -1L)
        if (thingId == -1L) {
            finish()
        }
        binding =
            DataBindingUtil.setContentView<ActivityBulletsBinding>(this, R.layout.activity_bullets)
        thingDao = AppDB.instance(this).thingDao()
        bulletsDao = AppDB.instance(this).bulletDao()
        vm = ViewModelProviders.of(this, BulletVM.Factory(application, bulletsDao, thingDao))
            .get(BulletVM::class.java)
        binding.l = this
        mAdapter = BulletAdapter(vm, this, this)
        with(binding.buRv) {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@BulletsActivity)
            adapter = mAdapter
        }
        vm.thought.value = thingId
        vm.bullets().observe(this, Observer {
            mAdapter.submitList(it)
        })
        vm.storedThought.observe(this, Observer {
            binding.buThInfo.text = it?.desc ?: ""
        })
        runOnUiThread(Runnable {
            query = ""
            sendLiveData()
        })
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.bu_row_desc, R.id.bu_row_ts -> {
                val bulletId = p0?.tag as? Long
                startActivity(Intent(this, EditBulletActivity::class.java).apply {
                    putExtra("bullet_id", bulletId)
                })
            }

        }
        sendLiveData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.thought_options, menu)
        val searchView =
            menu?.getItem(0)?.actionView as? SearchView // TODO: 02/05/21 change to find
        searchView?.setOnQueryTextListener(this)
        return true
    }

    private var query: String? = ""

    override fun onQueryTextSubmit(p0: String?): Boolean {
        query = p0?.trim()?.toString()
        sendLiveData()
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        query = p0?.trim()?.toString()
        sendLiveData()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.newitem -> {
                sendLiveData()
                vm.ins()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun sendLiveData() {
        vm.query.value = query
        val type = when {
            binding.buChipAll.isChecked -> ""
            binding.buChipTodo.isChecked -> BULLET_TYPE_TODO
            binding.buChipEvt.isChecked -> BULLET_TYPE_EVT
            binding.buChipNote.isChecked -> BULLET_TYPE_NOTE
            else -> ""
        }
        vm.type.value = type
        vm.status.value = ""
    }
}