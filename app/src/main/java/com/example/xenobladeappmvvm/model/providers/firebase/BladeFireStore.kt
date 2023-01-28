package com.example.xenobladeappmvvm.model.providers.firebase

import androidx.compose.ui.res.stringResource
import com.example.xenobladeappmvvm.R
import com.example.xenobladeappmvvm.model.Blade
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class BladeFireStore {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    // TODO: SHOULD LOAD FROM ANOTHER FILE
    private val usersCollectionName = "users"
    private val bladesCollectionName = "blades"

    private fun convertToData(blade: Blade): HashMap<String, String?> {
        return hashMapOf(
                "name" to blade.name.trim(),
                "description" to blade.description?.trim(),
                "element" to blade.element
        )
    }

    fun createBlade(blade: Blade, onSuccessBehavior: () -> Unit, onFailureBehavior: () -> Unit, onCompleteBehavior: () -> Unit){
        val user = auth.currentUser?.email
        val userData = hashMapOf(
            "email" to user
        )
        val data = convertToData(blade)
        if (user != null) {

            db.collection(usersCollectionName)
                .document(user)
                .set(userData, SetOptions.merge())
                .addOnSuccessListener {
                    db.collection(usersCollectionName)
                        .document(user)
                        .collection(bladesCollectionName)
                        .document(blade.name.toString().trim().lowercase())
                        .set(data, SetOptions.merge())
                        .addOnSuccessListener {
                            onSuccessBehavior()
                        }
                        .addOnFailureListener {
                            onFailureBehavior()
                        }
                        .addOnCompleteListener {
                            onCompleteBehavior()
                        }
                }
                .addOnFailureListener {
                    onFailureBehavior()
                }

        }

    }


}