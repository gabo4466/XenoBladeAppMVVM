package com.example.xenobladeappmvvm.ui.viewmodel

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.xenobladeappmvvm.model.Blade
import com.example.xenobladeappmvvm.model.providers.firebase.BladeFireStore
import com.google.firebase.auth.FirebaseAuth

class ListBladesViewModel(private val navController: NavController, val email: String?) {

    private val bladeFireStore = BladeFireStore()
    private val auth = FirebaseAuth.getInstance()

    private val _blades = MutableLiveData<List<Blade>>()
    val blades: LiveData<List<Blade>> = _blades

    private val _user = MutableLiveData<String>()
    val user: LiveData<String> = _user

    private val _errorMessage = "Ha ocurrido un error"

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _messageDelete = MutableLiveData<String>()
    val messageDelete: LiveData<String> = _messageDelete

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isResponse = MutableLiveData<Boolean>()
    val isResponse: LiveData<Boolean> = _isResponse

    fun loadBlades() {
        _isLoading.value = true
        _user.value = auth.currentUser?.email
        bladeFireStore.getBladesById(email = email.toString(),
            onSuccessBehaviour = {
                _blades.value = it
            },
            onFailureBehavior = {
                _message.value = _errorMessage
            },
            onCompleteBehavior = {
                _isLoading.value = false
            }
        )
    }

    fun deleteBlade(id: String) {
        _isLoading.value = true
        bladeFireStore.deleteBlade(email.toString(), id,
            onSuccessBehaviour = {
                _messageDelete.value = "Se ha eliminado con exito"
            },
            onFailureBehavior = {
                _messageDelete.value = _errorMessage
            },
            onCompleteBehavior = {
                _isLoading.value = false
                _isResponse.value = true
            }
        )
    }

    fun responseHandled(){
        _isResponse.value = false
        _messageDelete.value = ""
    }


}