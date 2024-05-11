package org.d3if3120.assesment2mobpro1.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pesanan_3")
data class Pesanan(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nama: String,
    val pedas: String,
    val topping: String,

    )
