package me.samen.d2.view.obsnote

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import androidx.paging.PagedList
import androidx.paging.toLiveData
import kotlinx.coroutines.launch
import me.samen.d2.data.daos.ObsNoteDao
import me.samen.d2.data.entities.ObsNote
import me.samen.d2.util.LiveEvent

class ONoteVM(
    context: Application,
    private val obsNoteDao: ObsNoteDao
) : AndroidViewModel(context) {
    val opens = MutableLiveData<LiveEvent<ObsNote>>()
    val searchQuery = MutableLiveData<String>()

    fun fetch(): LiveData<PagedList<ObsNote>> {
        return Transformations.switchMap(searchQuery) {
            if (it.isNullOrBlank()) {
                obsNoteDao.all().toLiveData(pageSize = 50)
            } else {
                obsNoteDao.search("%$it%").toLiveData(pageSize = 50)
            }
        }
    }

    fun click(view: View, thing: ObsNote?) {
        viewModelScope.launch {
            val t = thing ?: return@launch
            opens.value = LiveEvent(t)
        }
    }

    class Factory(
        private val app: Application,
        val obsNoteDao: ObsNoteDao
    ) : ViewModelProvider
    .AndroidViewModelFactory(app) {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ONoteVM(app, obsNoteDao) as T
        }
    }
}