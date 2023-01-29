package com.example.xenobladeappmvvm.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.xenobladeappmvvm.model.providers.firebase.UserFireStore
import com.google.firebase.auth.FirebaseAuth

class ListUsersViewModel(private val navController: NavController) {

    private val userFireStore = UserFireStore()
    private val auth = FirebaseAuth.getInstance()
    private val listBladesRoute = "list_blades/"

    private val _user = MutableLiveData<String>()
    val user : LiveData<String> = _user

    private val _users = MutableLiveData<List<String>>()
    val users : LiveData<List<String>> = _users

    private val _errorMessage = "Ha ocurrido un error"

    private val _message = MutableLiveData<String>()
    val message : LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    fun loadUsers() {
        _user.value = auth.currentUser?.email
        userFireStore.getUsers(
            onSuccessBehaviour = {
                _users.value = it
            },
            onFailureBehavior = {
                _message.value = _errorMessage
            },
            onCompleteBehavior = {
                _isLoading.value = false
            }
        )
    }

    fun navigateToBlades(user:String){
        navController.navigate(listBladesRoute+user)
    }

    fun navigateOwnBlades() {
        Log.d("NAVIGATING", _user.value.toString())
        navController.navigate(listBladesRoute+_user.value)
    }


}