package me.samen.d2.view.bullet

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import me.samen.d2.R
import me.samen.d2.data.entities.Bullet
import me.samen.d2.databinding.ActivityEditBulletBinding

class EditBulletActivity : AppCompatActivity(), Observer<Bullet?> {
    lateinit var contentView: ActivityEditBulletBinding
    lateinit var bulletVM: BulletVM
    lateinit var curBullet: Bullet
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bulletId = intent.getLongExtra("bullet_id", -1L)
        if (bulletId == -1L) finish()

        bulletVM =
            ViewModelProviders.of(this, BulletVM.Factory(application)).get(BulletVM::class.java)

        bulletVM.lookup(bulletId).observe(this, this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bullet_options, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ok -> {
                saveAndFinish()
            }
            R.id.delitem -> {
                deleteAndFinish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAndFinish() {
        bulletVM.delete(curBullet)
        finish()
    }

    private fun saveAndFinish() {
        bulletVM.update(
            curBullet.copy(
                desc = contentView.editDesc.text.toString(),
                type = contentView.editType.text.toString()
            )
        )
        finish()
    }

    override fun onChanged(t: Bullet?) {
        curBullet = t ?: return
        if (!::contentView.isInitialized) {
            contentView = DataBindingUtil.setContentView<ActivityEditBulletBinding>(
                this,
                R.layout.activity_edit_bullet
            )
        }
        contentView.bullet = t
        contentView.executePendingBindings()
    }


}