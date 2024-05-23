package com.example.udppc_parcial2.view

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.udppc_parcial2.ui.theme.MainColor
import com.example.udppc_parcial2.repository.PetSaveRepository
import com.example.udppc_parcial2.viewModel.ScreenAddPetViewModel
import com.example.udppc_parcial2.viewModel.appNavegation.appScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun screenAddPet(navController: NavController, viewModel: ScreenAddPetViewModel) {


    var context = LocalContext.current
    val scope = rememberCoroutineScope()

    val type:String by viewModel.type.observeAsState(initial = "")
    val name:String by viewModel.name.observeAsState(initial = "")
    val age:Int by viewModel.age.observeAsState(initial = 0)
    val breed:String by viewModel.breed.observeAsState(initial = "")
    val image: Uri? by viewModel.image.observeAsState()


    val nametype = arrayOf("Dog","Cat")
    val selectPet = remember { mutableStateOf(nametype[0]) }
    val expanded = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ){
        Row() {
            TopAppBar(
                title = { Text(text=" Storage the pet") },
                colors = TopAppBarDefaults.topAppBarColors(
                    MainColor,
                    titleContentColor = Color.White
                )
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MainColor)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = name,
                onValueChange = {
                    viewModel.setName(it)
                },
                label = {
                    Text(text = "Name Pet", color = MainColor)
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.background

                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Description Pet", color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))
            ////////////
            ExposedDropdownMenuBox(
                expanded = expanded.value,
                onExpandedChange = { expanded.value= !expanded.value}
            ) {
                TextField(
                    value = selectPet.value,
                    onValueChange = {
                        viewModel.setType(it)
                    },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
                    },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = {expanded.value= false}
                ) {
                    nametype.forEach {
                        DropdownMenuItem(
                            text = { Text(text = it)},
                            onClick = {
                                selectPet.value= it
                                viewModel.setType(it)
                                expanded.value= false
                            }
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(16.dp))
            var ageString by remember {
                mutableStateOf(age.toString())
            }
            TextField(
                value = ageString,
                onValueChange = {
                    newAge ->
                                ageString = newAge
                                newAge.toIntOrNull()?.let { viewModel.setAge(it) }
                },label = {
                    Text(text = "Age (In months)", color = MainColor)
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.background


                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = breed,
                onValueChange = {
                    viewModel.setBreed(it)
                },
                placeholder = {
                    Text(text = "Breed of pet",color = MainColor)
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.background

                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            ImageInput(uriState = image, onUriChange = {viewModel.setImage(it)})







            Row(
                modifier = Modifier.padding(16.dp)
            ){
                Button(onClick = {
                    viewModel.save()
                    viewModel.imprimir()
                    viewModel.setName("")
                    viewModel.setType("")
                    viewModel.setAge(0)
                    viewModel.setBreed("")
                    Toast.makeText(
                        context, "Saved ✅", Toast.LENGTH_SHORT
                    ).show()

                },colors = ButtonDefaults.buttonColors(Color.White)
                ) {
                    Text(text = " Save ", color = MainColor)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(onClick = {
                    navController.navigate(route = appScreens.screenMain.router)
                },colors = ButtonDefaults.buttonColors(Color.White)
                ) {
                    Text(text = "Cancel", color = MainColor)
                }

            }

        }
    }

}

@Composable
fun ImageInput(uriState: Uri?, onUriChange: (Uri) -> Unit, labelId: String = "ImageUri") {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    val laucher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            selectedImageUri = uri
            uri?.let { onUriChange(it) }
        }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        selectedImageUri?.let {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                bitmap.value = ImageDecoder.decodeBitmap(source)

            }
            bitmap.value?.let { btm ->
                Image(
                    bitmap = btm.asImageBitmap(), contentDescription = null, modifier = Modifier
                        .size(200.dp)
                        .padding(20.dp)
                )
            }
        }
        Button(onClick = {
            laucher.launch("image/*")
            println(selectedImageUri)
        },colors = ButtonDefaults.buttonColors(Color.White)) {
            Text(text = "Photo", color = MainColor)
        }

    }
}

@Preview(showBackground = true)
@Composable
fun screenAddPet() {
    val context = LocalContext.current
    val service = PetSaveRepository()
    val viewModel = ScreenAddPetViewModel(context,service)
    screenAddPet(NavController(LocalContext.current),viewModel)
}

