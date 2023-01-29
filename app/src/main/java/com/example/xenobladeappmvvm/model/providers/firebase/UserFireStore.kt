package com.example.xenobladeappmvvm.model.providers.firebase

import com.google.api.FieldBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserFireStore {

    private val db = FirebaseFirestore.getInstance()

    // TODO: SHOULD LOAD FROM ANOTHER FILE
    private val usersCollectionName = "users"

    fun getUsers(onSuccessBehaviour: (users:MutableList<String>)->Unit,onFailureBehavior: () -> Unit, onCompleteBehavior: ()->Unit){
        val users: MutableList<String> = mutableListOf<String>()
        db.collection(usersCollectionName)
            .get()
            .addOnSuccessListener {
                for (user in it) {
//                    users.add(user.id)
                    users += user.id
                }
                onSuccessBehaviour(users)
            }
            .addOnFailureListener {
                onFailureBehavior()
            }
            .addOnCompleteListener {
                onCompleteBehavior()
            }


    }

}