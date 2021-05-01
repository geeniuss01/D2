package me.samen.d2.view.edit

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import me.samen.d2.R
import me.samen.d2.data.AppDB
import me.samen.d2.data.BUNDLE_THING_ID
import me.samen.d2.data.daos.ThingDao
import me.samen.d2.data.entities.Thing
import me.samen.d2.databinding.ActivityEditBinding
import me.samen.d2.view.main.ThoughtsVM

class EditActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var thingDao: ThingDao
    private lateinit var vm: ThoughtsVM
    private lateinit var binding: ActivityEditBinding
    private var theThing: Thing? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val thingId = intent.getLongExtra(BUNDLE_THING_ID, -1L)
        if (thingId == -1L) {
            finish()
        }
        thingDao = AppDB.instance(this).thingDao()
        vm = ViewModelProviders.of(
            this,
            ThoughtsVM.Factory(application, thingDao, AppDB.instance(this).bulletDao())
        )
            .get(ThoughtsVM::class.java)
        binding =
            DataBindingUtil.setContentView<ActivityEditBinding>(this, R.layout.activity_edit)
        binding.listener = this
        lookup(thingId)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);

    }

    fun lookup(thingId: Long) {
        lifecycleScope.launch {
            theThing = vm.lookup(thingId)
            if (theThing != null) {
                binding.th = theThing
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            saveAndFinish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(p0: View?) {
        if (p0?.id == R.id.edit_ok && theThing != null) {
            if (saveAndFinish()) return
        }
        if (p0?.id == R.id.edit_delete && theThing != null) {
            val th = theThing ?: return
            lifecycleScope.launch {
                vm.delete(th)
                finish()
            }
        }
    }

    private fun saveAndFinish(): Boolean {
        val udpatedTh = with(binding) {
            theThing?.copy(
                theThing?.id ?: 0,
                editType.text.toString(), editDesc.text.toString(), editTags.text.toString(),
                "", editPeople.text.toString(), editDate.text.toString()
            )

        } ?: return true
        lifecycleScope.launch {
            val pageNum = binding.insPgNum.text.toString()
            vm.update(udpatedTh, pageNum)
            finish()
        }
        return false
    }
}