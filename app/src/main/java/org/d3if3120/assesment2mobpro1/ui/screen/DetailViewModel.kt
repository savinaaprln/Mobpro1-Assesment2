package org.d3if3120.assesment2mobpro1.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3120.assesment2mobpro1.database.PesananDao
import org.d3if3120.assesment2mobpro1.model.Pesanan

class DetailViewModel(private val dao: PesananDao) : ViewModel() {

    fun insert(nama: String, pedas: String, topping: String) {
        val pesanan = Pesanan(
            nama = nama,
            pedas = pedas,
            topping = topping,

        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(pesanan)
        }
    }

    suspend fun getPesanan(id: Long): Pesanan? {
        return dao.getPesananById(id)
    }

    fun update(id: Long, nama: String, size: String, topping: String) {
        val pesanan = Pesanan(
            id, nama, size, topping
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(pesanan)
        }

    }

    fun delete(id: Long){
        viewModelScope.launch(Dispatchers.IO){
            dao.deleteById(id)
        }
    }

}