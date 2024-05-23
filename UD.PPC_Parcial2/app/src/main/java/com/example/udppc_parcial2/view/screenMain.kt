package com.example.udppc_parcial2.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.udppc_parcial2.R
import com.example.udppc_parcial2.viewModel.PetDTO
import com.example.udppc_parcial2.ui.theme.MainColor
import com.example.udppc_parcial2.viewModel.appNavegation.appScreens
import com.example.udppc_parcial2.viewModel.ScreenMainViewModel



@SuppressLint("ComposableNaming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun screenMain(navController: NavController, petViewModel : ScreenMainViewModel = viewModel()) {

    var pets by remember { mutableStateOf<List<PetDTO>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("age", "breed", "type", "name")
    var selectedOrderBy by remember { mutableStateOf(items.first()) }

    val context = LocalContext.current
    val repository = "https://github.com/Chocolatamargo2607/UD.PPC_Parcial2.git"
    val repositoryintent = remember { Intent(Intent.ACTION_VIEW, Uri.parse(repository)) }
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    val onSearch: (String) -> Unit = { searchQuery ->
        petViewModel.searchPets(
            name = searchQuery,
            onResult = { searchedPets ->
                pets = searchedPets
                errorMessage = null
            },
            onError = { error ->
                errorMessage = error.localizedMessage
            }
        )
        active = false
    }

    val fetchPets = { sortBy: String ->
        petViewModel.fetchPets(sortBy, onResult = {
            pets = it
            errorMessage = null
        }, onError = { error ->
            errorMessage = error.localizedMessage
        })
    }

    LaunchedEffect(selectedOrderBy) {
        fetchPets(selectedOrderBy)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        TopAppBar(
            title = { Text(text=" My Little Pet ღ", onTextLayout = { }) },
            colors = TopAppBarDefaults.topAppBarColors(
                MainColor,
                titleContentColor = Color.White
            ),
            navigationIcon = { IconButton(onClick ={ context.startActivity(repositoryintent)}) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null,
                    tint = Color.White
                )}
            }
        )
        Image(
            painter = painterResource(id = R.drawable.logo_pet),
            contentDescription = "Logo"
        )

        Row {
            Button(onClick = { navController.navigate(route = appScreens.screenAddPet.router)},
                colors = ButtonDefaults.buttonColors(MainColor)) {
                Text(text = "Add New Pet")
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Box(
            modifier = Modifier
                .padding(16.dp)
                .clickable(onClick = { expanded = true })
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(text = "Order List By: $selectedOrderBy")
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(IntrinsicSize.Min)
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            selectedOrderBy = item
                            expanded = false
                            fetchPets(selectedOrderBy)
                        }
                    )
                }

            }
        }

        SearchBar(
            query = query,
            onQueryChange = { newQuery -> query = newQuery },
            onSearch = { onSearch(query) },
            active = active,
            onActiveChange = { newActive -> active = newActive },
            placeholder = {
                Box { Text(text = "Search") }
            },
            trailingIcon = {
                IconButton(
                    onClick = { onSearch(query) },
                    enabled = query.isNotEmpty()
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                }
            }
        ){
        }
        errorMessage?.let {
            Text(text = "Error: $it", color = Color.Red)
        }
        PetList(pets = pets)
    }
}

@Composable
fun PetList(pets: List<PetDTO>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(pets) { pet ->
            PetCard(pet = pet)
        }
    }
}

@Composable
fun PetItem(pet: PetDTO) {
    Column {
        Text(text = "Name: ${pet.name}")
        Text(text = "${pet.type}")
    }
}

@Composable
fun PetCard(pet: PetDTO) {
    val context = LocalContext.current
    var open_Dialog = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(17.dp)
            .clickable {
                open_Dialog.value = true
            }
        ,elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {


        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color.Black
            )
            PetItem(pet = pet)
        }

        if (open_Dialog.value) {
            AlertDialog(
                onDismissRequest = { open_Dialog.value = false },
                title = null,
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "${pet.name}",
                            style = TextStyle(
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Image(
                            contentDescription = "",
                            modifier = Modifier
                                .padding(30.dp)
                                .height(300.dp),
                            painter = rememberImagePainter(data = pet.image)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Age:  ${pet.age}")
                        Text(text = "Type Pet:  ${pet.type}")
                        Text(text = "Breed:  ${pet.breed}")
                    }
                },
                confirmButton = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(onClick = { open_Dialog.value = false }) {
                            Text(text = "Close")
                        }
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun screenMainpreview() {
    screenMain(NavController(LocalContext.current))
}
