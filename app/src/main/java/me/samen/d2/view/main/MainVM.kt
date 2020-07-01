package me.samen.d2.view.main

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import androidx.paging.PagedList
import androidx.paging.toLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.samen.d2.data.daos.ThingDao
import me.samen.d2.data.entities.Thing

class MainVM(
    context: Application,
    private val thingDao: ThingDao
) : AndroidViewModel(context) {

    fun fetch(): LiveData<PagedList<Thing>> {
        val all = thingDao.all()
        return all.toLiveData(pageSize = 50)
    }

    fun ins(thing: Thing) {
        viewModelScope.launch(Dispatchers.IO) {
            thingDao.ins(thing)
        }
    }

    fun click(view: View, thing: Thing) {

    }

    class Factory(private val app: Application, private val dao: ThingDao) : ViewModelProvider
    .AndroidViewModelFactory(app) {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainVM(app, dao) as T
        }
    }
}