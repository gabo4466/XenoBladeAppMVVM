package com.example.xenobladeappmvvm.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.xenobladeappmvvm.MainActivity
import com.example.xenobladeappmvvm.ui.TopBar
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.example.xenobladeappmvvm.R

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainMenu(navController: NavController) {
    navController.enableOnBackPressed(true)
    val auth = FirebaseAuth.getInstance()
    val activity = LocalContext.current as MainActivity

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBar(navController = navController, backbutton = false, pageName = stringResource(
            id = R.string.main_menu_home
        )) }
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Image(modifier = Modifier.fillMaxWidth(), contentScale = ContentScale.FillWidth,painter = painterResource(id = R.drawable.xenoblade_banner_large), contentDescription = "Banner")
            Column (
                modifier = Modifier.padding(20.dp)
                    ) {
                Button(onClick = { navController.navigate("add_blade") }, modifier = Modifier.width(250.dp)) {
                    Text(text = stringResource(id = R.string.main_menu_opt1))
                }
                Spacer(modifier = Modifier.size(20.dp))
                Button(onClick = { navController.navigate("list_users") }, modifier = Modifier.width(250.dp)) {
                    Text(text = stringResource(id = R.string.list_users_action))
                }
                Spacer(modifier = Modifier.size(20.dp))
                Button(
                    onClick = {
                        val gso: GoogleSignInOptions =
                            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(activity.getString(R.string.default_web_client_id))
                                .requestEmail()
                                .build()
                        val client = GoogleSignIn.getClient(activity, gso)
                        Auth.GoogleSignInApi.signOut(client.asGoogleApiClient())
                        navController.navigate("login_screen")
                    },
                    modifier = Modifier.width(250.dp)) {
                    Text(text = stringResource(id = R.string.logout))
                }
            }
        }

    }
}


@Preview
@Composable
fun Testing() {
    var navController = rememberNavController()
    MainMenu(navController = navController)


}



