package com.example.xenobladeappmvvm.model.providers.firebase

import androidx.compose.ui.res.stringResource
import com.example.xenobladeappmvvm.R
import com.example.xenobladeappmvvm.model.Blade
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class BladeFireStore {


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

    fun createBlade(
        blade: Blade,
        user: String,
        onSuccessBehavior: () -> Unit,
        onFailureBehavior: () -> Unit,
        onCompleteBehavior: () -> Unit
    ) {
        val userData = hashMapOf(
            "email" to user
        )
        val data = convertToData(blade)


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

    fun getBladesById(
        email: String,
        onSuccessBehaviour: (blades: MutableList<Blade>) -> Unit,
        onFailureBehavior: () -> Unit,
        onCompleteBehavior: () -> Unit
    ) {
        val blades: MutableList<Blade> = mutableListOf<Blade>()
        db.collection(usersCollectionName)
            .document(email)
            .collection(bladesCollectionName)
            .get()
            .addOnSuccessListener {
                blades.clear()
                for (blade in it) {
                    val auxBlade = Blade(
                        blade.id,
                        blade.get("name") as String,
                        blade.get("description") as String?,
                        blade.get("element") as String?
                    )
                    blades.add(auxBlade)
                }
                onSuccessBehaviour(blades)
            }
            .addOnFailureListener {
                onFailureBehavior()
            }
            .addOnCompleteListener {
                onCompleteBehavior()
            }
    }

    fun deleteBlade(
        email: String, id: String,
        onSuccessBehaviour: () -> Unit,
        onFailureBehavior: () -> Unit,
        onCompleteBehavior: () -> Unit
    ) {
        db.collection(usersCollectionName)
            .document(email)
            .collection(bladesCollectionName)
            .document(id)
            .delete()
            .addOnSuccessListener {
                onSuccessBehaviour()
            }
            .addOnFailureListener {
                onFailureBehavior()
            }
            .addOnCompleteListener {
                onCompleteBehavior()
            }
    }


}