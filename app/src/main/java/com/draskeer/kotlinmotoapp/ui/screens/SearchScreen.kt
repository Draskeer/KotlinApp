package com.draskeer.kotlinmotoapp.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.draskeer.kotlinmotoapp.R
import com.draskeer.kotlinmotoapp.model.MotoBeansItem
import com.draskeer.kotlinmotoapp.ui.MyError
import com.draskeer.kotlinmotoapp.ui.theme.motoAppTheme
import com.draskeer.kotlinmotoapp.viewmodel.MainViewModel

@Composable
fun SearchScreen(modifier: Modifier = Modifier, mainViewModel: MainViewModel = viewModel()) {
    var searchText by remember { mutableStateOf("") }

    val list by mainViewModel.dataList.collectAsStateWithLifecycle()
    val runInProgress by mainViewModel.runInProgress.collectAsStateWithLifecycle()
    val errorMessage by mainViewModel.errorMessage.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBar(searchText = searchText, onValueChange = { searchText = it })

        if (errorMessage.isNotEmpty()) {
            MyError(errorMessage = errorMessage)
        }

        AnimatedVisibility(visible = runInProgress) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }

        LazyColumn(
            modifier = Modifier.weight(1f).padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(list.size) {
                MotoDetailsItem(moto = list[it])
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { searchText = "" },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Icon(Icons.Filled.Clear, contentDescription = "Clear")
                Spacer(Modifier.size(8.dp))
                Text(stringResource(R.string.bt_clear))
            }

            Button(
                onClick = { mainViewModel.loadMoto(searchText) },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
                Spacer(Modifier.size(8.dp))
                Text(stringResource(R.string.bt_load))
            }
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier, searchText: String, onValueChange: (String) -> Unit) {
    TextField(
        value = searchText,
        onValueChange = onValueChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null
            )
        },
        singleLine = true,
        label = { Text("Rechercher...") },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .shadow(6.dp, RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp)
    )
}

@Composable
fun MotoDetailsItem(modifier: Modifier = Modifier, moto: MotoBeansItem) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(12.dp))
            .clickable { expanded = !expanded }
            .padding(16.dp)
            .animateContentSize()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            moto.icon?.let { iconUrl ->
                Image(
                    painter = rememberAsyncImagePainter(iconUrl),
                    contentDescription = "Moto Icon",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
            Column {
                Text(
                    text = "${moto.make} ${moto.model} (${moto.year})",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = moto.type ?: "Unknown Type",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
            }
        }
        if (expanded) {
            Spacer(modifier = Modifier.height(8.dp))
            Column {
                DetailRow("Engine", moto.engine)
                DetailRow("Power", moto.power)
                DetailRow("Torque", moto.torque)
                DetailRow("Top Speed", moto.top_speed?.toString())
                DetailRow("Weight", moto.total_weight)
                DetailRow("Fuel System", moto.fuel_system)
                DetailRow("Emission", moto.emission)
                DetailRow("Clutch", moto.clutch)
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String?) {
    if (!value.isNullOrEmpty()) {
        Row(modifier = Modifier.padding(vertical = 2.dp)) {
            Text(
                text = "$label:",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES, locale = "fr")
@Composable
fun SearchScreenPreview() {
    motoAppTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            var viewModel = MainViewModel()
            // viewModel.loadFakeData(runInProgress = true, errorMessage = "une erreur")
            SearchScreen(modifier = Modifier.padding(innerPadding), mainViewModel = viewModel)
        }
    }
}