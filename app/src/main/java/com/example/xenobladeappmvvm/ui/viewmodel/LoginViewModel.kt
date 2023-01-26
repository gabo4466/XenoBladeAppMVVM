package com.example.xenobladeappmvvm.ui.viewmodel

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.xenobladeappmvvm.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


class LoginViewModel() : ViewModel() {
    private val isLoading = MutableLiveData(false)
    private val logged = MutableLiveData(false)
    private val hasGoogleError = MutableLiveData(false)
    private val googleError = MutableLiveData("")
    private val email = MutableLiveData("")
    private val loggedUser = MutableLiveData(FirebaseAuth.getInstance().currentUser)
    fun loggedUser(): MutableLiveData<FirebaseUser?> = loggedUser
    fun isLoading(): LiveData<Boolean> = isLoading
    fun logged(): LiveData<Boolean> = logged
    fun googleError(): LiveData<String> = googleError
    fun hasGoogleError(): LiveData<Boolean> = hasGoogleError

    fun logIn() {
        logged.postValue(true)
    }

    fun logInWithGoogle(activity: Activity) {
        logged.postValue(false)
        loggedUser.postValue(null)
        isLoading.postValue(true)
        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        // Build a GoogleSignInClient with the options specified by gso.
        val client = GoogleSignIn.getClient(activity, gso)
        val signInIntent: Intent = client.signInIntent
        activity.startActivityForResult(signInIntent, 1)
    }

    fun finishLogIn(task: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)

            account?.idToken?.let { token ->
                val auth = FirebaseAuth.getInstance()
                val credential = GoogleAuthProvider.getCredential(token, null)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener{
                        if (it.isSuccessful) {
                            val user = auth.currentUser
                            email.postValue(user?.email)
                            loggedUser.postValue(auth.currentUser)
                        }else {
                            hasGoogleError.postValue(true)
                            googleError.postValue("Ha ocurrido un error al iniciar sesión")
                        }
                        isLoading.postValue(false)
                    }
            }
        } catch (e: ApiException) {
            hasGoogleError.postValue(true)
            googleError.postValue("Ha ocurrido algún error")
            isLoading.postValue(false)
            e.message?.let { Log.d("Login", "error: "+it) }

        }
    }

}