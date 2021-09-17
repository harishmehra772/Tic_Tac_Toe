package com.example.tictactoegame


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.firebase.analytics.FirebaseAnalytics
import android.widget.Toast
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private var database= FirebaseDatabase.getInstance()
    private var myRef=database.reference

    var myEmail:String?=null

    private lateinit var analytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        analytics=Firebase.analytics
        var b: Bundle = intent.extras!!
        myEmail=b.getString("email")
        incomigCall()
    }

    var player1count=0
    var player2count=0

    fun buttonClick(view: View) {
        val buttonSelected: Button = view as Button
        var cellId=0
        when(buttonSelected.id)
        {
            R.id.button1 -> cellId=1
            R.id.button2 -> cellId=2
            R.id.button3 -> cellId=3
            R.id.button4 -> cellId=4
            R.id.button5 -> cellId=5
            R.id.button6 -> cellId=6
            R.id.button7 -> cellId=7
            R.id.button8 -> cellId=8
            R.id.button9 -> cellId=9

        }

        myRef.child("PlayerOnline").child(sessionId!!).child(cellId.toString()).setValue(myEmail)

    }

    var activePlayer=1
    var player1= ArrayList<Int>()
    var player2= ArrayList<Int>()

    fun playGame(cellId:Int,buttonSelected:Button)
    {
        if(activePlayer==1)
        {
            buttonSelected.text = "X"
            buttonSelected.setBackgroundResource(R.color.color1)
            player1.add(cellId)
            activePlayer=2

        }
        else
        {
            buttonSelected.text = "O"
            buttonSelected.setBackgroundResource(R.color.color2)
            player2.add(cellId)
            activePlayer=1
        }

        buttonSelected.isEnabled=false
        checkWinner()
    }

    fun checkWinner()
    {
        var winner=-1
        //     row1
        if(player1.contains(1)&&player1.contains(2)&&player1.contains(3))
            winner=1
       //row2
        if(player1.contains(4)&&player1.contains(5)&&player1.contains(6))
            winner=1
        //row3
        if(player1.contains(7)&&player1.contains(8)&&player1.contains(9))
            winner=1

        //col1
        if(player1.contains(1)&&player1.contains(4)&&player1.contains(7))
            winner=1
        //col2
        if(player1.contains(2)&&player1.contains(5)&&player1.contains(8))
            winner=1
        //col3
        if(player1.contains(3)&&player1.contains(6)&&player1.contains(9))
            winner=1

        //diag1
        if(player1.contains(1)&&player1.contains(5)&&player1.contains(9))
            winner=1
        //diag2
        if(player1.contains(3)&&player1.contains(5)&&player1.contains(7))
            winner=1

//        player2 row1
        if(player2.contains(1)&&player2.contains(2)&&player2.contains(3))
            winner=2
        //row2
        if(player2.contains(4)&&player2.contains(5)&&player2.contains(6))
            winner=2
        //row3
        if(player2.contains(7)&&player2.contains(8)&&player2.contains(9))
            winner=2

        //col1
        if(player2.contains(1)&&player2.contains(4)&&player2.contains(7))
            winner=2
        //col2
        if(player2.contains(2)&&player2.contains(5)&&player2.contains(8))
            winner=2
        //col3
        if(player2.contains(3)&&player2.contains(6)&&player2.contains(9))
            winner=2

        //diag1
        if(player2.contains(1)&&player2.contains(5)&&player2.contains(9))
            winner=2
        //diag2
        if(player2.contains(3)&&player2.contains(5)&&player2.contains(7))
            winner=2

        if(winner==1)
        {

           Toast.makeText(this,"Player 1 wins !",Toast.LENGTH_LONG).show()
            player1count++

            restartGame()
        }
        else if(winner==2)
            {
                
                Toast.makeText(this,"Player 2 wins !",Toast.LENGTH_LONG).show()
                player2count++

                restartGame()
            }
    }

    fun restartGame()
    {
        activePlayer=1
        player1.clear()
        player2.clear()
        var cellId=0
        for(cellId in 1..9)
        {
            var buttonSelected:Button?
            buttonSelected = when(cellId){
                1-> findViewById(R.id.button1)
                2-> findViewById(R.id.button2)
                3-> findViewById(R.id.button3)
                4-> findViewById(R.id.button4)
                5-> findViewById(R.id.button5)
                6-> findViewById(R.id.button6)
                7-> findViewById(R.id.button7)
                8-> findViewById(R.id.button8)
                9-> findViewById(R.id.button9)
                else
                ->{findViewById(R.id.button1)}
            }
            buttonSelected!!.text=""
            buttonSelected!!.isEnabled=true
            buttonSelected!!.setBackgroundResource(R.color.buttonColor)
        }
        Toast.makeText(this,"Player 1:${player1count} Player 2:${player2count}",Toast.LENGTH_SHORT).show()
    }

    fun AutoPlay(cellId:Int){



        var buttonSelected:Button?
        buttonSelected = when(cellId){
            1-> findViewById(R.id.button1)
            2-> findViewById(R.id.button2)
            3-> findViewById(R.id.button3)
            4-> findViewById(R.id.button4)
            5-> findViewById(R.id.button5)
            6-> findViewById(R.id.button6)
            7-> findViewById(R.id.button7)
            8-> findViewById(R.id.button8)
            9-> findViewById(R.id.button9)
            else
            ->{findViewById(R.id.button1)}
        }

        playGame(cellId,buttonSelected)

    }


    fun buReqEvent(view: View) {
        val editText:EditText=findViewById(R.id.etEmail)
        var userEmail=editText.text.toString()
        myRef.child("Users").child(splitString(userEmail)).child("Request").push().setValue(myEmail)
        playerOnline(splitString(myEmail!!)+splitString(userEmail))
        playerSymbol="X"
    }

    fun buAcceptEvent(view: View){
        val editText:EditText=findViewById(R.id.etEmail)
        var userEmail=editText.text.toString()
        myRef.child("Users").child(splitString(userEmail)).child("Request").push().setValue(myEmail)

        playerOnline(splitString(userEmail)+splitString( myEmail!!))
        playerSymbol="O"
    }

    var sessionId:String?=null
    var playerSymbol:String?=null
    fun playerOnline(sessionId:String){
        this.sessionId=sessionId

        myRef.removeValue()

        myRef.child("PlayerOnline").child(sessionId!!).
        addValueEventListener(object :ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    player1.clear()
                    player2.clear()
                    val td=snapshot!!.value as HashMap<String,Any>
                    if(td!=null)
                    {
                        var value:String
                        for(key in td.keys)
                        {
                            value=td[key] as String

                            if(value!=myEmail)
                            {
                                activePlayer=if(playerSymbol=="X") 1 else 2
                            }
                            else
                            {
                                activePlayer=if(playerSymbol=="X") 2 else 1
                            }
                            AutoPlay(key.toInt())
                        }
                    }
                }catch (ex:Exception){}
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }

    var number=0

    fun incomigCall(){
        myRef.child("Users").child(splitString(myEmail!!)).child("Request").
        addValueEventListener(object :ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
             try {
                 val td=snapshot!!.value as HashMap<String,Any>
                 if(td!=null)
                 {
                     var value:String
                     for(key in td.keys)
                     {
                         value=td[key] as String
                         val editText:EditText=findViewById(R.id.etEmail)
                         var userEmail=editText.text.toString()
                         editText.setText(value)

                         val notifyMe=Notifications()
                         notifyMe.notify(applicationContext,value+" wants to play tic tac toe ",number)
                         number++
                         myRef.child("Users").child(splitString(myEmail!!)).child("Request").setValue(true)

                         break
                     }
                 }
             }catch (ex:Exception){}
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun splitString(str:String):String{
        var split=str.split("@")
        return split[0]
    }
}
