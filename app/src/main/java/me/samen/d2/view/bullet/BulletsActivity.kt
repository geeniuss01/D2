package me.samen.d2.view.bullet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
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

class BulletsActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var thingDao: ThingDao
    private lateinit var bulletsDao: BulletDao
    private lateinit var vm: BulletVM
    private lateinit var binding: ActivityBulletsBinding
    private lateinit var mAdapter: BulletAdapter
    private val tw = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            sendLiveData()
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

    }

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
        binding.buSearch.addTextChangedListener(tw)
        binding.buHpnd.setOnEditorActionListener { textView, i, keyEvent ->
            vm.hpnd(textView?.text?.toString())
            textView.setText("")
            true
        }
        mAdapter = BulletAdapter(vm, this)
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
        sendLiveData()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.bu_add -> {
                sendLiveData()
                vm.ins()
                binding.buSearch.selectAll()
            }
            R.id.bu_hpnd -> {
            }
            else -> {
                sendLiveData()
            }
        }
    }

    fun sendLiveData() {
        vm.query.value = binding.buSearch.text.toString()
        val type = when (binding.radioGroup.checkedRadioButtonId) {
            R.id.radioButton -> BULLET_TYPE_TODO
            R.id.radioButton2 -> BULLET_TYPE_EVT
            R.id.radioButton3 -> BULLET_TYPE_NOTE
            else -> ""
        }
        vm.type.value = type
        vm.status.value = if (type == BULLET_TYPE_TODO && binding.buStatus.isChecked) "todo" else ""
    }
}