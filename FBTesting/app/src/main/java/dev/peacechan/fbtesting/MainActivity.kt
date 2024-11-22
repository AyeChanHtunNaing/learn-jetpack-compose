package com.example.firebaserealtimedbapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firebaserealtimedbapp.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var firebaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userList = ArrayList<UserModel>()
        userList.add(UserModel("1","John","john@gmail.com"))
        userList.add(UserModel("2","David","david@gmail.com"))
        userList.add(UserModel("3","Mary","mary@gmail.com"))
        userList.add(UserModel("4","Steve","steve@gmail.com"))
        userList.add(UserModel("5","Eric","eric@gmail.com"))
        userList.add(UserModel("6","William","william@gmail.com"))

        binding.btnSave.setOnClickListener{
            saveUser(userList)
        }

        binding.btnShow.setOnClickListener{
            showAllUsers()
        }

    }//onCreate

    private fun showAllUsers() {
        showToast("show all users")

        val userList = ArrayList<UserModel>()

        firebaseRef=FirebaseDatabase
            //.getInstance()
            .getInstance("https://xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.firebasedatabase.app/")
            .getReference("users")

        firebaseRef.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                showToast("on Data Change")
                val strBuilder = StringBuilder()
                for(userSnapshot in snapshot.children){

                    val user = userSnapshot.getValue(UserModel::class.java)

                    if(user!=null){
                        userList.add(user)
                        strBuilder.append(user.name+" >> "+user.email+"\n\n")
                    }
                }
                showAlertDialog("All Users", strBuilder.toString())
            }
            override fun onCancelled(error: DatabaseError) {
                showToast(error.message)
            }
        })

    }//showAllUsers

    private fun saveUser(userList: ArrayList<UserModel>) {

        showToast("Saved User")

        firebaseRef=FirebaseDatabase
            .getInstance("https://xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.firebasedatabase.app/")
            .getReference("users")

        for(user in userList){
            //user.key = firebaseRef.push().key!!

            firebaseRef.child(user.key).setValue(user)
                .addOnCompleteListener{
                    showAlertDialog("Success","Saved user!")
                    showToast("success")
                }
                .addOnFailureListener{ error ->

                    showAlertDialog("Failure","Fail to save")
                    Log.e("Firebase DB App","${error.message}")
                }

        }//for loop

    }//saveUser

    private fun showAlertDialog(title:String, message:String){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("OK"){
                dialog, _ -> dialog.dismiss()
            }
            .show()
    }
    private fun showToast(text:String){
        Toast.makeText(this,text,Toast.LENGTH_LONG).show()
    }
}