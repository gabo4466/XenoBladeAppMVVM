package com.example.xenobladeappmvvm.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.xenobladeappmvvm.R
import com.example.xenobladeappmvvm.ui.TopBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddBlade(navController: NavController) {
    Scaffold(topBar = {
        TopBar(
            navController = navController,
            pageName = stringResource(id = R.string.main_menu_opt1)
        )
    }) {

        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val usersCollectionName = stringResource(id = R.string.collection_users)
        val bladesCollectionName = stringResource(id = R.string.collection_blades)

        // STATES
        var name by rememberSaveable { mutableStateOf("") }
        var description by rememberSaveable { mutableStateOf("") }
        var isResponse by rememberSaveable { mutableStateOf(false) }
        var responseMessage by rememberSaveable { mutableStateOf("") }
        var isLoading by rememberSaveable { mutableStateOf(false) }

        val successMessage = stringResource(id = R.string.add_blade_success_res)
        val errorMessage = stringResource(id = R.string.add_blade_error_res)
        val focusManager = LocalFocusManager.current


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(text = stringResource(id = R.string.add_blade_name)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.size(20.dp))
            OutlinedTextField(value = description,
                onValueChange = { description = it },
                label = { Text(text = stringResource(id = R.string.add_blade_description)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.size(20.dp))


            var expanded by remember { mutableStateOf(false) }
            val items = listOf(
                "Luz",
                "Oscuridad",
                "Fuego",
                "Agua",
                "Electricidad",
                "Tierra",
                "Viento",
                "Hielo"
            )


            var element by rememberSaveable { mutableStateOf("") }

            var textFieldSizeDropDownMenu by remember { mutableStateOf(Size.Zero)}

            val icon = if (expanded)
                Icons.Filled.KeyboardArrowUp
            else
                Icons.Filled.KeyboardArrowDown


            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        textFieldSizeDropDownMenu = coordinates.size.toSize()
                    },
                colors = TextFieldDefaults.textFieldColors(disabledLabelColor = MaterialTheme.colors.secondary, disabledIndicatorColor = MaterialTheme.colors.secondary),
                value = element,
                onValueChange = {  },
                readOnly = true,
                label = {Text("Elemento", Modifier.clickable { expanded = !expanded })},
                trailingIcon = {
                    Icon(icon,"contentDescription",
                        Modifier.clickable { expanded = !expanded })
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current){textFieldSizeDropDownMenu.width.toDp()})
            ) {
                items.forEach { label ->
                    DropdownMenuItem(onClick = {
                        element = label
                        expanded = false
                    }) {
                        Text(text = label)
                    }
                }
            }
            Spacer(modifier = Modifier.size(20.dp))

            val data = hashMapOf(
                "name" to name.toString().trim(),
                "description" to description.toString().trim(),
                "element" to element.toString()
            )

            val user = auth.currentUser?.email

            val userData = hashMapOf(
                "email" to user
            )

            Button(onClick = {
                isLoading = true
                if (user != null) {

                    db.collection(usersCollectionName)
                        .document(user)
                        .set(userData, SetOptions.merge())
                        .addOnSuccessListener {
                            db.collection(usersCollectionName)
                                .document(user)
                                .collection(bladesCollectionName)
                                .document(name.toString().trim().lowercase())
                                .set(data, SetOptions.merge())
                                .addOnSuccessListener {
                                    responseMessage = successMessage
                                }
                                .addOnFailureListener {
                                    responseMessage = errorMessage
                                }
                                .addOnCompleteListener {
                                    isLoading = false
                                    isResponse = true
                                }
                        }
                        .addOnFailureListener {
                            responseMessage = errorMessage
                        }

                }
            }) {
                Text(text = stringResource(id = R.string.add_blade_submit))
            }

            if (isResponse) {
                PopUpResponse(response = responseMessage) {
                    isResponse = false
                    name = ""
                    description = ""
                    element = ""
                    focusManager.clearFocus()
                }
            }

            if (isLoading) {
                Spacer(modifier = Modifier.size(50.dp))
                CircularProgressIndicator()
            }


        }
    }
}

@Composable
fun PopUpResponse(response: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(
                text = response,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.confirmation_button))
            }
        },
        backgroundColor = MaterialTheme.colors.surface
    )
}
