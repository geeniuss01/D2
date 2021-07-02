package me.samen.d2.view.obsnote

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
import me.samen.d2.data.BUNDLE_THING_CONTENT
import me.samen.d2.data.BUNDLE_THING_ID
import me.samen.d2.data.daos.ObsNoteDao
import me.samen.d2.data.entities.ObsNote
import me.samen.d2.databinding.ActivityThoughtsBinding
import me.samen.d2.view.main.HPLinearLayoutManager
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

class ONoteActivity : AppCompatActivity(), View.OnClickListener, SearchView.OnQueryTextListener {
    private val TAG: String = "ObsNoteActivity"
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var obsNoteDao: ObsNoteDao
    private lateinit var vm: ONoteVM
    private lateinit var mAdapter: ONoteMainAdapter
    private lateinit var binding: ActivityThoughtsBinding
    private lateinit var executor: Executor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO(satosh.dhanyamraju): move to fully data binding.
        obsNoteDao = AppDB.instance(this).obsNoteDao()
        vm = ViewModelProviders.of(
            this,
            ONoteVM.Factory(application, obsNoteDao)
        ).get(ONoteVM::class.java)

        mAdapter = ONoteMainAdapter(vm, this)
        binding = DataBindingUtil.setContentView<ActivityThoughtsBinding>(
            this,
            R.layout.activity_thoughts
        )
        showBiometric()
        binding.setListener(this)
        with(binding.rv) {
            layoutManager = HPLinearLayoutManager(this@ONoteActivity)
            adapter = mAdapter
            addItemDecoration(
                DividerItemDecoration(this@ONoteActivity, DividerItemDecoration.VERTICAL)
            )

        }
        vm.fetch().observe(this, Observer {
            mAdapter.submitList(it)
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
        menuInflater.inflate(R.menu.onote_options, menu)
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
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun open(thing: ObsNote) {
        // TODO(satosh.dhanyamraju): new activity
        val intent = Intent(this@ONoteActivity, ONoteDetailActivity::class.java)
        intent.putExtra(BUNDLE_THING_ID, thing.fileName)
        intent.putExtra(BUNDLE_THING_CONTENT, thing.cont)
        startActivity(intent)
    }

}