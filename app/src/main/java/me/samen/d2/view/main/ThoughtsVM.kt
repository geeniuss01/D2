package me.samen.d2.view.main

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import androidx.paging.PagedList
import androidx.paging.toLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.samen.d2.data.daos.BulletDao
import me.samen.d2.data.daos.ThingDao
import me.samen.d2.data.entities.Bullet
import me.samen.d2.data.entities.Thing
import me.samen.d2.data.entities.ThingWithBullets
import me.samen.d2.util.LiveEvent

class ThoughtsVM(
    context: Application,
    private val thingDao: ThingDao,
    private val bulletDao: BulletDao
) : AndroidViewModel(context) {
    val edits = MutableLiveData<LiveEvent<Thing>>()
    val opens = MutableLiveData<LiveEvent<Thing>>()
    val searchQuery = MutableLiveData<String>()

    fun fetch(): LiveData<PagedList<ThingWithBullets>> {
        return Transformations.switchMap(searchQuery) {
            if (it.isNullOrBlank()) {
                thingDao.all().toLiveData(pageSize = 50)
            } else {
                thingDao.search("%$it%").toLiveData(pageSize = 50)
            }
        }
    }

    fun ins(thing: Thing) {
        viewModelScope.launch(Dispatchers.IO) {
            thingDao.ins(thing)
        }
    }

    fun insNewDefault(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val t = Thing(0, "", text, "", "", "")
            val th = kotlin.runCatching {
                val newid = thingDao.ins1(t)
                thingDao.lookup(newid)
            }.getOrNull()
            if (th != null) {
                edits.postValue(LiveEvent(th))
            }
        }
    }

    fun click(view: View, thing: ThingWithBullets) {
        viewModelScope.launch {
            val t = thing.thing ?: return@launch
            thingDao.upd(t.copy(lastOpened = System.currentTimeMillis()))
            opens.value = LiveEvent(t)
        }
    }

    fun onLongPress(view: View, thing: ThingWithBullets): Boolean {
        thing.thing?.let { t ->
            edits.value = LiveEvent(t)
        }
        return true
    }

    suspend fun lookup(thingId: Long): Thing? {
        return withContext(Dispatchers.IO) {
            thingDao.lookup(thingId)
        }
    }

    suspend fun update(updatedTh: Thing, pageNum: String?) {
        return withContext(Dispatchers.IO) {
            if (pageNum?.isNotEmpty() == true) {
                bulletDao.ins(Bullet(updatedTh.id, "note", "page $pageNum", ""))
            }
            thingDao.upd(updatedTh.copy(lastOpened = System.currentTimeMillis()))
        }
    }

    suspend fun delete(theThing: Thing) {
        return withContext(Dispatchers.IO) {
            thingDao.del(theThing)
        }
    }

    class Factory(
        private val app: Application,
        private val dao: ThingDao,
        val bulletDao: BulletDao
    ) : ViewModelProvider
    .AndroidViewModelFactory(app) {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ThoughtsVM(app, dao, bulletDao) as T
        }
    }
}