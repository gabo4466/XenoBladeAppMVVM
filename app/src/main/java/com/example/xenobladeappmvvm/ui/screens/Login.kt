package com.example.xenobladeappmvvm.ui.screens

import android.annotation.SuppressLint
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*

import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.xenobladeappmvvm.MainActivity
import com.example.xenobladeappmvvm.R
import com.example.xenobladeappmvvm.ui.TopBar
import com.example.xenobladeappmvvm.ui.viewmodel.LoginViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Login(navController: NavController) {
    navController.enableOnBackPressed(false)
    val activity = LocalContext.current as MainActivity
    val viewModel: LoginViewModel by activity.viewModels()
    val loggedUser by viewModel.loggedUser().observeAsState(null)
    val logged by viewModel.logged().observeAsState(false)
    if (loggedUser != null && !logged) {
        loggedUser!!.displayName?.let {
            PopUpLogin(it) {
                viewModel.logIn()
                navController.navigate("main_menu")

            }
        }
    }
    Scaffold(
        topBar = { TopBar(pageName = stringResource(id = R.string.login), navController = navController, backbutton = false) },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Image(modifier = Modifier.fillMaxWidth(), contentScale = ContentScale.FillWidth,painter = painterResource(id = R.drawable.xenoblade_banner_large), contentDescription = "Banner")
            LoginForm(navController, viewModel, activity)
        }
    }
}


@Composable
fun LoginForm(navController: NavController, viewModel: LoginViewModel, activity: MainActivity) {
    val isLoading by viewModel.isLoading().observeAsState(false)
    val googleError by viewModel.googleError().observeAsState("")
    val hasGoogleError by viewModel.hasGoogleError().observeAsState(false)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp)
    ) {

        ButtonAccces(text = "Acceder con Google", icon = R.drawable.google_icon) {
            viewModel.logInWithGoogle(activity)
        }

        // ERROR TEXT
        if (hasGoogleError){
            Spacer(modifier = Modifier.size(20.dp))
            Text(text = googleError, color = Color.Red)
        }

        if (isLoading){
            CircularProgressIndicator()
        }
    }
}


@Composable
fun ButtonAccces(text: String, icon: Int?, onClick: () -> Unit) {

    Button(
        onClick = onClick, modifier = Modifier.width(350.dp)
    ) {
        if (icon != null){
            Icon(painter =  painterResource(id = icon), contentDescription = "")
            Spacer(modifier = Modifier.size(10.dp))
        }

        Text(
            text = text,
            style = MaterialTheme.typography.button
        )
    }
}

@Composable
fun PopUpLogin(name: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text= {
            Text(
                text = name,
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