package com.example.xenobladeappmvvm.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.xenobladeappmvvm.R
import com.example.xenobladeappmvvm.ui.TopBar
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize
import com.example.xenobladeappmvvm.ui.viewmodel.AddBladeViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddBlade(navController: NavController, viewModel: AddBladeViewModel) {
    Scaffold(topBar = {
        TopBar(
            navController = navController,
            pageName = stringResource(id = R.string.main_menu_opt1)
        )
    }) {

        // STATES
        val name: String by viewModel.name.observeAsState("")
        val description: String by viewModel.description.observeAsState("")


        val isLoading by viewModel.isLoading.observeAsState(false)
        val isResponse by viewModel.isResponse.observeAsState(false)
        val responseMessage by viewModel.responseMessage.observeAsState("")

        val focusManager = LocalFocusManager.current


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { viewModel.onChangeName(it) },
                label = { Text(text = stringResource(id = R.string.add_blade_name)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.size(20.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { viewModel.onChangeDescription(it) },
                label = { Text(text = stringResource(id = R.string.add_blade_description)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.size(20.dp))


            var expanded by remember { mutableStateOf(false) }
            val items: List<String> = viewModel.items

            val element: String by viewModel.element.observeAsState("")

            var textFieldSizeDropDownMenu by remember { mutableStateOf(Size.Zero) }

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
                colors = TextFieldDefaults.textFieldColors(
                    disabledLabelColor = MaterialTheme.colors.secondary,
                    disabledIndicatorColor = MaterialTheme.colors.secondary
                ),
                value = element,
                onValueChange = { },
                readOnly = true,
                label = { Text("Elemento", Modifier.clickable { expanded = !expanded }) },
                trailingIcon = {
                    Icon(icon, "contentDescription",
                        Modifier.clickable { expanded = !expanded })
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { textFieldSizeDropDownMenu.width.toDp() })
            ) {
                items.forEach { label ->
                    DropdownMenuItem(onClick = {
                        viewModel.onChangeElement(label)
                        expanded = false
                    }) {
                        Text(text = label)
                    }
                }
            }
            Spacer(modifier = Modifier.size(20.dp))

//
            Button(onClick = {
                viewModel.createBlade()
            }) {
                Text(text = stringResource(id = R.string.add_blade_submit))
            }

            if (isResponse) {
                PopUpResponse(response = responseMessage) {
                    viewModel.responseHandled()
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
