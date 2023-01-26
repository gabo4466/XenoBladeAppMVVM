package com.example.xenobladeappmvvm.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.xenobladeappmvvm.R

@Composable
fun TopBar(backbutton: Boolean = true, navController: NavController, pageName: String = "") {
    TopAppBar (
        backgroundColor = MaterialTheme.colors.primary
    ) {
        var title = stringResource(id = R.string.app_name)
        if (pageName != ""){
            title += " - $pageName"
        }
        if (backbutton) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Arrow Back",
                modifier = Modifier.clickable { navController.popBackStack() })
            Spacer(modifier = Modifier.width(8.dp))
        }

        Text(
            text = title,
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(10.dp)
        )

    }
}