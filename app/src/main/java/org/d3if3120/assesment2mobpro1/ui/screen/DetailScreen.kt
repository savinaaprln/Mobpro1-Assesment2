package org.d3if3120.assesment2mobpro1.ui.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3120.assesment2mobpro1.R
import org.d3if3120.assesment2mobpro1.database.PesananDb

import org.d3if3120.assesment2mobpro1.util.ViewModelFactory
import org.d3if3120.assesment2mobpro1.ui.theme.Assesment2Mobpro1Theme

const val KEY_ID_PESANAN ="idPesanan"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id:Long? = null) {
    val context = LocalContext.current
    val db = PesananDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var nama by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }

    // daftar pilihan pedas
    val sizeOption = listOf(
        stringResource(id = R.string.tidak_pedas),
        stringResource(id = R.string.sedang),
        stringResource(id = R.string.pedas)
    )

    // daftar pilihan toping
    val toppingOption = listOf(
        stringResource(id = R.string.kerupuk),
        stringResource(id = R.string.mie),
        stringResource(id = R.string.Batagor),
        stringResource(id = R.string.sayuran),
        stringResource(id = R.string.seafood)
    )



    // var simpan data
    var pilihanSize by rememberSaveable { mutableStateOf(sizeOption[0]) }
    var pilihanToping by rememberSaveable { mutableStateOf(toppingOption[0]) }


    LaunchedEffect(true) {
        if (id == null) return@LaunchedEffect
        val data = viewModel.getPesanan(id) ?: return@LaunchedEffect
        nama = data.nama
        pilihanSize = data.pedas
        pilihanToping = data.topping
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    if (id == null)
                        Text(text = stringResource(id = R.string.tambah_pesanan))
                    else
                        Text(text = stringResource(id = R.string.ubah_pesanan))

                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                        if (nama == ""  ){
                            Toast.makeText(context,R.string.invalid, Toast.LENGTH_LONG).show()
                            return@IconButton
                        }
                        if (id == null){
                            viewModel.insert(nama,pilihanSize,pilihanToping)
                        } else {
                            viewModel.update(id,nama,pilihanSize,pilihanToping)
                        }
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(id = R.string.simpan),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (id != null){
                        DeleteAction {showDialog = true}
                        DisplayAlertDialog(
                            openDialog = showDialog,
                            onDismissRequest = {showDialog = false}) {
                            showDialog = false
                            viewModel.delete(id)
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    ) { padding ->
        FormPesanan(
            title = nama,
            onTitleChange = {nama = it},
            pilihanSize = pilihanSize,
            pilihanSizeChange = { pilihanSize = it },
            pilihanToping = pilihanToping,
            pilihanTopingChange = { pilihanToping = it },
            radioOptionSize = sizeOption,
            radioOptionToping = toppingOption,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun DeleteAction(delete:()->Unit ){
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = {expanded = true}) {
        Icon(
            imageVector = Icons.Filled.MoreVert ,
            contentDescription = stringResource(R.string.opsi_lainnya),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded ,
            onDismissRequest = { expanded = false}
        ) {
            DropdownMenuItem(text = { Text(text = stringResource(R.string.hapus))},
                onClick = {
                    expanded = false
                    delete()
                }
            )
        }
    }
}


@Composable
fun FormPesanan(
    title:String,onTitleChange:(String)-> Unit,
    pilihanSize: String, pilihanSizeChange: (String) -> Unit,
    pilihanToping: String, pilihanTopingChange: (String) -> Unit,
    radioOptionSize: List<String>,
    radioOptionToping: List<String>,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        //nama Pelanggan
        OutlinedTextField(
            value = title,
            onValueChange = {onTitleChange(it)},
            singleLine = true,
            label = { Text(text = stringResource(id = R.string.nama))},
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Done
            ),
            modifier=Modifier.fillMaxWidth()
        )
        Column {


            Text(text = stringResource(id = R.string.pedas_judul))
            // row Size
            Row(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
            ) {

                radioOptionSize.forEach { option ->
                    RedioOption(
                        label = option,
                        isSelected = pilihanSize == option,
                        modifier = Modifier
                            .selectable(
                                selected = pilihanSize == option,
                                onClick = { pilihanSizeChange(option) },
                                role = Role.RadioButton
                            )
                            .padding(8.dp)
                    )
                }
            }
        }
        // kolom Toping
        Column {
            Text(text = stringResource(id = R.string.topping))
            Column (
                modifier = Modifier
                    .padding(top = 6.dp)
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
            ){
                radioOptionToping.forEach { option ->
                    RedioOption(
                        label = option,
                        isSelected = pilihanToping == option,
                        modifier = Modifier
                            .selectable(
                                selected = pilihanToping == option,
                                onClick = { pilihanTopingChange(option) },
                                role = Role.RadioButton
                            )
                            .padding(8.dp)
                    )
                }
            }
        }

    }
}

@Composable
fun RedioOption(label: String, isSelected: Boolean, modifier: Modifier) {
    Row(
        modifier = modifier,

        ) {
        RadioButton(selected = isSelected, onClick = null)
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DetailScreenPreview() {
    Assesment2Mobpro1Theme {
        DetailScreen(rememberNavController())
    }
}