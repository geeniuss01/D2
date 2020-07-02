package me.samen.d2.view.bullet

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import androidx.paging.PagedList
import androidx.paging.toLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.samen.d2.data.daos.BulletDao
import me.samen.d2.data.daos.ThingDao
import me.samen.d2.data.entities.Bullet


class BulletVM(
    context: Application,
    private val bulletDao: BulletDao,
    private val thoughtDao: ThingDao
) : AndroidViewModel(context) {
    // view will set hem
    val thought = MutableLiveData<Long>()
    val storedThought = Transformations.switchMap(thought) {
        thoughtDao.lookupLive(it)
    }
    val query = MutableLiveData<String>()
    val type = MutableLiveData<String>()
    val status = MutableLiveData<String>()
    private val mediator = MediatorLiveData<Bullet>()


    init {
        mediator.addSource(thought) { sendMediatorEvent() }
        mediator.addSource(query) { sendMediatorEvent() }
        mediator.addSource(type) { sendMediatorEvent() }
        mediator.addSource(status) { sendMediatorEvent() }
    }


    fun bullets(): LiveData<PagedList<Bullet>> {
        return Transformations.switchMap(mediator) {
            bulletDao.all(it.thought_id, it.desc, it.type, it.status)
                .toLiveData(pageSize = 50)
        }
    }

    fun ins() {
        val th = thought.value ?: return
        val q = query.value ?: return
        val ty = type.value ?: return
        val st = status.value ?: return
        val bullet = Bullet(th, ty, q, st)
        viewModelScope.launch {
            bulletDao.ins(bullet)
        }
    }

    fun delete(view: View, bullet: Bullet) {
        viewModelScope.launch { bulletDao.del(bullet) }
    }

    private fun sendMediatorEvent() {
        fun String.like() = "%$this%"
        val th = thought.value ?: return
        val q = query.value?.like() ?: return
        val ty = type.value?.like() ?: return
        val st = status.value?.like() ?: return
        mediator.value = Bullet(th, ty, q, st)
    }

    fun toggleDone(b: Bullet) {
        viewModelScope.launch(Dispatchers.IO) {
            bulletDao.doneToggle(b.id)
        }
    }

    fun hpnd(hap: String?) {
        hap ?: return
        val t = thought?.value ?: return
        viewModelScope.launch {
            bulletDao.insHpnd(t, hap)
        }
    }

    class Factory(
        private val app: Application,
        private val dao: BulletDao,
        private val thoughtDao: ThingDao
    ) : ViewModelProvider
    .AndroidViewModelFactory(app) {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return BulletVM(app, dao, thoughtDao) as T
        }
    }

}