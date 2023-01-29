package com.example.xenobladeappmvvm.ui.screens

import android.annotation.SuppressLint
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.xenobladeappmvvm.R
import com.example.xenobladeappmvvm.model.Blade
import com.example.xenobladeappmvvm.ui.TopBar
import com.example.xenobladeappmvvm.ui.theme.Gray200
import com.example.xenobladeappmvvm.ui.theme.Gray500
import com.example.xenobladeappmvvm.ui.viewmodel.ListBladesViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ListBlades(navController: NavController, viewModel: ListBladesViewModel) {

    val userLogged by viewModel.user.observeAsState("")
    val user = viewModel.email
    val blades by viewModel.blades.observeAsState(mutableListOf())

    val message by viewModel.message.observeAsState("")
    val messageDelete by viewModel.messageDelete.observeAsState("")
    val isResponse by viewModel.isResponse.observeAsState(false)
    val isLoading by viewModel.isLoading.observeAsState(true)
    viewModel.loadBlades()

    if (isResponse){
        PopUpConfirmation(text = messageDelete) {
            viewModel.responseHandled()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBar(navController = navController, pageName = stringResource(id = R.string.blade_list)
        ) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = message, color = MaterialTheme.colors.primary)
            if (isLoading) {
                Spacer(modifier = Modifier.size(30.dp))
                CircularProgressIndicator()
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp),
            ) {
                blades.forEachIndexed { index, blade ->
                    val colorCell: Color = if (index % 2 == 0) {
                        Gray200
                    }else {
                        Gray500
                    }
                    var delete = false
                    if (userLogged == user){
                        delete = true
                    }
                    BladeItem(blade = blade, color = colorCell, delete = delete) {
                        viewModel.deleteBlade(blade.id)
                    }
                }

            }
        }
    }
    
}

@Composable
fun BladeItem(blade: Blade, color: Color, delete: Boolean, deleteButtonAction: () -> Unit) {
    Spacer(modifier = Modifier.size(15.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = color, shape = RoundedCornerShape(5.dp))
            .padding(10.dp)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = blade.name, color = Color.Black, style = MaterialTheme.typography.h5)
            blade.getColorElement()?.let {
                blade.element?.let { Text(text = it, color = Color.Black, modifier = Modifier
                    .background(color = blade.getColorElement()!!, shape = RoundedCornerShape(5.dp))
                    .padding(10.dp)) }
            }
        }
        blade.description?.let {
            Row() {
                Text(text = blade.description!!, color = Color.Black)
            }
        }
        if (delete){
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(onClick = deleteButtonAction, modifier = Modifier.width(100.dp)) {
                    Text(text = "Borrar")
                }

            }
        }

    }
}


@Composable
fun PopUpConfirmation(text:String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text= {
            Text(
                text = text,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Continuar")
            }
        },
        backgroundColor = MaterialTheme.colors.surface
    )
}
