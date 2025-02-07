package com.amonteiro.a2025_sdv_parisa.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amonteiro.a2025_sdv_parisa.R
import com.amonteiro.a2025_sdv_parisa.model.PictureBean
import com.amonteiro.a2025_sdv_parisa.ui.MyError
import com.amonteiro.a2025_sdv_parisa.ui.theme._2025_sdv_parisaTheme
import com.amonteiro.a2025_sdv_parisa.viewmodel.MainViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder

@Composable
fun SearchScreen(modifier: Modifier = Modifier, mainViewModel: MainViewModel = viewModel()) {

    println("SearchScreen")
    var searchText = remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        var list = mainViewModel.dataList.collectAsStateWithLifecycle().value
            //Si c'est un filtre de recherche
            //.filter { it.title.contains(searchText.value, true)  }
        var runInProgress = mainViewModel.runInProgress.collectAsStateWithLifecycle().value
        var errorMessage = mainViewModel.errorMessage.collectAsStateWithLifecycle().value

        SearchBar(searchText= searchText)



        MyError(errorMessage = errorMessage)

        AnimatedVisibility (runInProgress) {
            CircularProgressIndicator()
        }


        LazyColumn(modifier= Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(list.size) {
                PictureRowItem(data = list[it])
            }
        }

        Row {

            Button(
                onClick = { searchText.value = "" },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding
            ) {
                Icon(
                    Icons.Filled.Clear,
                    contentDescription = "Localized description",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(stringResource(R.string.bt_clear))
            }

            Button(
                onClick = { mainViewModel.loadWeathers(searchText.value) },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Localized description",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(stringResource(R.string.bt_load))
            }
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier, searchText: MutableState<String>) {

    TextField(
        value = searchText.value, //Valeur affichée
        onValueChange = {newValue:String -> searchText.value = newValue }, //Nouveau texte entrée
        leadingIcon = { //Image d'icone
            Icon(
                imageVector = Icons.Default.Search,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null
            )
        },
        singleLine = true,
        label = { //Texte d'aide qui se déplace
            Text("Enter text")
            //Pour aller le chercher dans string.xml
            //Text(stringResource(R.string.placeholder_search))
        },
        //placeholder = { //Texte d'aide qui disparait
        //Text("Recherche")
        //},

        //keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search), // Définir le bouton "Entrée" comme action de recherche
        //keyboardActions = KeyboardActions(onSearch = {onSearchAction()}), // Déclenche l'action définie
        //Comment le composant doit se placer
        modifier = modifier
            .fillMaxWidth() // Prend toute la largeur
            .heightIn(min = 56.dp) //Hauteur minimum
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable //Composable affichant 1 PictureBean
fun PictureRowItem(modifier: Modifier = Modifier, data: PictureBean) {

    //variable d'état
    var longText = remember { mutableStateOf(false) }

    Row(modifier = modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.tertiaryContainer)) {
        //Permission Internet nécessaire
        GlideImage(
            model = data.url,
            //Pour aller le chercher dans string.xml
            //contentDescription = getString(R.string.picture_of_cat),
            //En dur
            contentDescription = "une photo de chat",
            // Image d'attente. Permet également de voir l'emplacement de l'image dans la Preview
            loading = placeholder(R.mipmap.ic_launcher_round),
            // Image d'échec de chargement
            failure = placeholder(R.mipmap.ic_launcher),
            contentScale = ContentScale.Fit,
            //même autres champs qu'une Image classique
            modifier = Modifier
                .heightIn(max = 100.dp) //Sans hauteur il prendra tous l'écran
                .widthIn(max = 100.dp)
        )

        Column(modifier = Modifier.padding(10.dp).clickable {
            longText.value = !longText.value
        }) {
            Text(
                text = data.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text =  if(longText.value) data.longText else ( data.longText.take(20) + "..."),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier.animateContentSize()
            )
        }
    }


}


@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES, locale = "fr")
@Composable
fun SearchScreenPreview() {
    //Il faut remplacer NomVotreAppliTheme par le thème de votre application
    //Utilisé par exemple dans MainActivity.kt sous setContent {...}
    _2025_sdv_parisaTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            var viewModel = MainViewModel()
            viewModel.loadFakeData(runInProgress = true, errorMessage = "une erreur")
            SearchScreen(modifier = Modifier.padding(innerPadding), mainViewModel = viewModel)
        }
    }
}