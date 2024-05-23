package com.example.udppc_parcial2.viewModel

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File
import java.io.FileOutputStream
import androidx.lifecycle.viewModelScope
import com.example.udppc_parcial2.repository.PetSaveRepository
import kotlinx.coroutines.launch

class ScreenAddPetViewModel (private val context: Context,private val service: PetSaveRepository):ViewModel(){



    private val _type = MutableLiveData<String>()
    val type: LiveData<String> get() = _type

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    private val _age = MutableLiveData<Int>()
    val age: LiveData<Int> get() = _age

    private val _breed = MutableLiveData<String>()
    val breed: LiveData<String> get() = _breed

    private val _image = MutableLiveData<Uri?>()
    val image: LiveData<Uri?> get() = _image

    fun setType(type: String) {
        _type.value = type
    }
    fun setName(name: String) {
        _name.value = name
    }
    fun setAge(age: Int) {
        _age.value = age
    }
    fun setBreed(breed: String) {
        _breed.value = breed
    }
    fun setImage(image: Uri?) {
        _image.value = image
    }

    fun imprimir(){
        println(_type.value)
        println(_name.value)
        println(_age.value)
        println(_breed.value)
        println(_image.value)
    }

    fun getFile(uri: Uri): File{
        val contentResolver: ContentResolver = context.contentResolver
        val tempFile = File.createTempFile("temp_image", ".jpg", context.cacheDir)
        val inputStream = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(tempFile)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        return tempFile
    }

    fun save(){
        viewModelScope.launch{
            val name=_name.value?:""
            val type=_type.value?:""
            val age=_age.value?:0
            val breed=_breed.value?:""
            val image=_image.value
            if (image!=null){
                val auxfile = getFile(image)
                val pet = Pet(type,name,age,breed,Uri.fromFile(auxfile))
                service.save(pet)
            }else{
                println("No selecciono imagen")
            }
        }
    }

}