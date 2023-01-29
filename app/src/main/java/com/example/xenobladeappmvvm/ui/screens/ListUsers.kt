package com.example.xenobladeappmvvm.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.xenobladeappmvvm.R
import com.example.xenobladeappmvvm.model.Blade
import com.example.xenobladeappmvvm.ui.TopBar
import com.example.xenobladeappmvvm.ui.viewmodel.ListUsersViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ListUsers(navController: NavController, viewModel: ListUsersViewModel) {

    val message by viewModel.message.observeAsState("")
    val isLoading by viewModel.isLoading.observeAsState(true)
    val users by viewModel.users.observeAsState(mutableListOf())
    val user by viewModel.user.observeAsState("")
    viewModel.loadUsers()

    Scaffold(topBar = {
        TopBar(
            navController = navController,
            pageName = stringResource(id = R.string.main_menu_opt2)
        )
    }) {


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
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
                Spacer(modifier = Modifier.size(30.dp))
                Button(onClick = { 
                    viewModel.navigateOwnBlades()
                },
                modifier = Modifier.fillMaxWidth()) {
                    Text(text = stringResource(id = R.string.list_blades_own))
                }
                for (email in users) {
                    if (email != user){
                        UserItem(email = email) { viewModel.navigateToBlades(email) }
                    }
                }
            }
        }


    }
}

@Composable
fun UserItem(email: String, navigate: ()->Unit) {

    Spacer(modifier = Modifier.size(30.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = email, textAlign = TextAlign.Left)
        Button(onClick = { navigate() }, modifier = Modifier.width(130.dp)) {
            Text(text = stringResource(id = R.string.list_users_action))
        }
    }

    Divider(color = MaterialTheme.colors.secondary, thickness = 1.dp)


}