package me.samen.d2.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
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
import java.util.concurrent.Executor


/*
BACKUP commands
 adb shell bmgr backupnow me.samen.d2
 adb shell dumpsys backup | grep "me.samen.d2" -A 4 | grep Current
 adb shell bmgr restore 3b9f82a881618b53 me.samen.d2

todos
1. title per screen (not d2 everywhere)
2. appname and icon
3. deletion need confirmation


 */

class ThoughtsActivity : AppCompatActivity(), View.OnClickListener, SearchView.OnQueryTextListener {
    private val TAG: String = "MainActivity"
    private lateinit var thingDao: ThingDao
    private lateinit var vm: ThoughtsVM
    private lateinit var binding: ActivityThoughtsBinding
    private lateinit var mAdapter: MainAdapter

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

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
        showBiometric()
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
        vm.searchQuery.value = null
    }

    private fun showBiometric() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        applicationContext,
                        "Who are you?", Toast.LENGTH_SHORT
                    )
                        .show()
                    finish()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
//                    Toast.makeText(applicationContext,
//                        "Authentication succeeded!", Toast.LENGTH_SHORT)
//                        .show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
//                    Toast.makeText(applicationContext, "No match",
//                        Toast.LENGTH_SHORT)
//                        .show()
                    finish()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Need Biometric login")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()
        biometricPrompt.authenticate(promptInfo)
        // TODO(satosh.dhanyamraju): auth every 5m onResume
    }


    override fun onClick(p0: View?) {
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.thought_options, menu)
        val searchView =
            menu?.getItem(0)?.actionView as? SearchView // TODO: 02/05/21 change to find
        searchView?.setOnQueryTextListener(this)
        return true
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        vm.searchQuery.value = p0?.trim()?.toString()
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        vm.searchQuery.value = p0?.trim()?.toString()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.newitem -> {
                vm.insNewDefault(vm.searchQuery.value ?: "")
            }
        }
        return super.onOptionsItemSelected(item)
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