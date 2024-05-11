package org.d3if3120.assesment2mobpro1.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.d3if3120.assesment2mobpro1.model.Pesanan

@Dao
interface PesananDao {
    @Insert
    suspend fun insert(pesanan: Pesanan)

    @Update
    suspend fun update(pesanan: Pesanan)

    @Query("SELECT * FROM pesanan_3 ORDER BY pedas ASC ")
    fun getPesanan(): Flow<List<Pesanan>>

    @Query("SELECT * FROM pesanan_3 WHERE id = :id")
    suspend fun getPesananById(id: Long):Pesanan?

    @Query("DELETE FROM pesanan_3 WHERE id = :id")
    suspend fun deleteById(id:Long)
}