package me.samen.d2.view.main

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import androidx.paging.PagedList
import androidx.paging.toLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.samen.d2.R
import me.samen.d2.data.daos.ThingDao
import me.samen.d2.data.entities.Thing
import me.samen.d2.util.LiveEvent

class ThoughtsVM(
    context: Application,
    private val thingDao: ThingDao
) : AndroidViewModel(context) {
    val edits = MutableLiveData<LiveEvent<Thing>>()
    val opens = MutableLiveData<LiveEvent<Thing>>()
    val searchQuery = MutableLiveData<String>()

    fun fetch(): LiveData<PagedList<Thing>> {
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

    fun click(view: View, thing: Thing) {
        if (view.id == R.id.tdesc) {
            viewModelScope.launch {
                thingDao.upd(thing.copy(lastOpened = System.currentTimeMillis()))
                opens.value = LiveEvent(thing)
            }
        } else {
            edits.value = LiveEvent(thing)
        }
    }

    suspend fun lookup(thingId: Long): Thing? {
        return withContext(Dispatchers.IO) {
            thingDao.lookup(thingId)
        }
    }

    suspend fun update(updatedTh: Thing) {
        return withContext(Dispatchers.IO) {
            thingDao.upd(updatedTh)
        }
    }

    suspend fun delete(theThing: Thing) {
        return withContext(Dispatchers.IO) {
            thingDao.del(theThing)
        }
    }

    class Factory(private val app: Application, private val dao: ThingDao) : ViewModelProvider
    .AndroidViewModelFactory(app) {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ThoughtsVM(app, dao) as T
        }
    }
}