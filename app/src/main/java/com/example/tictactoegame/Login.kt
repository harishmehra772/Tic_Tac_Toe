package com.example.tictactoegame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.*
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {

    private  var mAuth:FirebaseAuth?=null

    private var database= FirebaseDatabase.getInstance()
    private var myRef=database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth= FirebaseAuth.getInstance()
    }

    fun buttonLogin(view: View) {
   val email:EditText=findViewById(R.id.userName)
        val password:EditText=findViewById(R.id.Password)
        LoginToFirebase(email.text.toString(),password.text.toString())

    }

    fun LoginToFirebase(email:String,password:String)
    {
       mAuth!!.createUserWithEmailAndPassword(email, password)
           .addOnCompleteListener(this){task->
               if(task.isSuccessful)
               {
                   Toast.makeText(applicationContext,"Successful Login",Toast.LENGTH_LONG)
                       .show()
                   var currentUser=mAuth!!.currentUser
                 if(currentUser!=null)
                   myRef.child("Users").child(splitString(currentUser.email.toString())).child("Request").setValue(currentUser.uid)

                   loadMain()
               }
               else{
                   Toast.makeText(applicationContext,"Unsuccessful Login",Toast.LENGTH_LONG)
                       .show()
               }

           }
    }

    override fun onStart() {
        super.onStart()
        loadMain()
    }

    fun loadMain()
    {

        var currentUser=mAuth!!.currentUser
        if(currentUser!=null) {
            //save database
           // myRef.child("Users").child(splitString(currentUser.email.toString())).setValue(currentUser.uid)


            var intent = Intent(this, MainActivity::class.java)
            intent.putExtra("email", currentUser!!.email)
            intent.putExtra("uid", currentUser!!.uid)
            startActivity(intent)
        }
    }

    fun splitString(str:String):String{
        var split=str.split("@")
        return split[0]
    }
}











