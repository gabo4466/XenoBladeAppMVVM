package com.example.xenobladeappmvvm.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.xenobladeappmvvm.model.Blade
import com.example.xenobladeappmvvm.model.providers.firebase.BladeFireStore
import com.google.firebase.auth.FirebaseAuth

class AddBladeViewModel: ViewModel() {

    private val bladeFireStore = BladeFireStore()
    private val auth = FirebaseAuth.getInstance()
    private val _blade = Blade("","","","")

    // TODO: SHOULD LOAD FROM DB
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

    // TODO: SHOULD LOAD FROM FILE
    private val _successMessage = "Se ha guardado con éxito"
    private val _errorMessage = "Ha ocurrido un error"

    private val _responseMessage = MutableLiveData<String>()
    val responseMessage : LiveData<String> = _responseMessage

    private val _name = MutableLiveData<String>()
    val name : LiveData<String> = _name

    private val _description = MutableLiveData<String>()
    val description : LiveData<String> = _description

    private val _element = MutableLiveData<String>()
    val element : LiveData<String> = _element

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _isResponse = MutableLiveData<Boolean>()
    val isResponse : LiveData<Boolean> = _isResponse

    fun onChangeName(newName:String){
        _name.value = newName
        checkFields()
    }

    fun onChangeDescription(newDescription:String){
        _description.value = newDescription
        checkFields()
    }

    fun onChangeElement(newElement:String){
        _element.value = newElement
        checkFields()
    }

    private fun checkFields(){
        _blade.name = _name.value.toString()
        _blade.description = _description.value.toString()
        _blade.element = _element.value.toString()

    }

    fun responseHandled(){
        _isResponse.value = false
    }

    fun createBlade(){
        val user = auth.currentUser?.email
        _isLoading.value = true

        if (user != null) {
            bladeFireStore.createBlade(blade = _blade, user = user,onCompleteBehavior = {
                _isLoading.value = false
                _isResponse.value = true
                _name.value = ""
                _description.value = ""
                _element.value = ""
            },
                onSuccessBehavior = {
                    _responseMessage.value = _successMessage
                },
                onFailureBehavior = {
                    _responseMessage.value = _errorMessage
                })
        }

    }

}