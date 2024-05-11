package org.d3if3120.assesment2mobpro1.navigation

import org.d3if3120.assesment2mobpro1.ui.screen.KEY_ID_PESANAN

sealed class Screen (val route: String) {
    data object Home: Screen("mainScreen")
    data object FormBaru: Screen ("detailScreen")
    data object FormUbah: Screen ("detailScreen/{$KEY_ID_PESANAN}"){
        fun withId(id:Long)="detailScreen/$id"
        }
}