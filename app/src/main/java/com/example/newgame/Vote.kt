package com.example.newgame

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.newgame.databinding.ActivityVoteBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Calendar

class Vote : AppCompatActivity() {

    private val bindindg: ActivityVoteBinding by lazy {
        ActivityVoteBinding.inflate(layoutInflater)
    }

    lateinit var sharedPreferences: SharedPreferences
    var BjpVoteCount: String? = null
    fun getBjpVotCount(vote: Any) {
        BjpVoteCount = vote.toString()
    }

    fun setBjpVoteCount(): Int {
        return BjpVoteCount?.toInt() ?: 0
    }

    var CongressVoteCount: String? = null
    fun getCongVotCount(vote: Any) {
        CongressVoteCount = vote.toString()
    }

    fun setCongVoteCount(): Int {
        return CongressVoteCount?.toInt() ?: 0
    }

    var AapVoteCount: String? = null
    fun getAapVotCount(vote: Any) {
        AapVoteCount = vote.toString()
    }

    fun setAapVoteCount(): Int {
        return AapVoteCount?.toInt() ?: 0
    }

    var OtherVoteCount: String? = null
    fun getOtherVotCount(vote: Any) {
        OtherVoteCount = vote.toString()
    }

    fun setOtherVoteCount(): Int {
        return OtherVoteCount?.toInt() ?: 0
    }


    val currentDateTime: Calendar = Calendar.getInstance()
    val currentDate: String = "${currentDateTime.get(Calendar.DAY_OF_MONTH)}-${currentDateTime.get(
        Calendar.MONTH) + 1}-${currentDateTime.get(Calendar.YEAR)}"


    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(bindindg.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sharedPreferences = getSharedPreferences("Votebtn", Context.MODE_PRIVATE)

        val BjpVoteCount = sharedPreferences.getBoolean("votebtn", true)

        if (BjpVoteCount == false) {
            bindindg.bjpbtn.isEnabled = false
            bindindg.congressbtn.isEnabled = false
            bindindg.aapbtn.isEnabled = false
            bindindg.otherbtn.isEnabled = false

            bindindg.bjpbtn.setBackgroundColor(getColor(R.color.gray))
            bindindg.congressbtn.setBackgroundColor(getColor(R.color.gray))
            bindindg.aapbtn.setBackgroundColor(getColor(R.color.gray))
            bindindg.otherbtn.setBackgroundColor(getColor(R.color.gray))
        }

        db = Firebase.firestore
        readBjpVote()
        readCongressVote()
        readAapVote()
        readOtherVote()

        Log.e("TAG", "current date.. $currentDate ", )

        bindindg.result.setOnClickListener {

            if (currentDate == "26-4-2024"){
                startActivity(Intent(this, result::class.java))
            }
            else{
                showConfirmDialogForResult()
            }


        }

        bindindg.bjpbtn.setOnClickListener {

            showConfirmDialogForBJP()


        }

        bindindg.congressbtn.setOnClickListener {
            showConfirmDialogForCong()

        }

        bindindg.aapbtn.setOnClickListener {
            showConfirmDialogForAAp()

        }

        bindindg.otherbtn.setOnClickListener {

             showConfirmDialogForOther()
        }


    }

    private fun readBjpVote() {
        db.collection("Vote").document("bjp").get()
            .addOnSuccessListener {

                if (it != null) {

                    it["vote"]?.let { it1 -> getBjpVotCount(it1) }


                }
            }
    }

    private fun readCongressVote() {
        db.collection("Vote").document("congress").get()
            .addOnSuccessListener {

                if (it != null) {
                    val data = it["vote"]
//                    Log.e("TAG", "readCongressVote: form vote $data", )
                    it["vote"]?.let { it1 -> getCongVotCount(it1) }


                }
            }
    }

    private fun readAapVote() {
        db.collection("Vote").document("aap").get()
            .addOnSuccessListener {

                if (it != null) {

                    it["vote"]?.let { it1 -> getAapVotCount(it1) }


                }
            }
    }

    private fun readOtherVote() {
        db.collection("Vote").document("others").get()
            .addOnSuccessListener {

                if (it != null) {

                    it["vote"]?.let { it1 -> getOtherVotCount(it1) }

                }
            }
    }

    private fun bjpBtn() {
        val value = setBjpVoteCount()
        Log.e("TAG", "onCreate: congcount from  vote $value",)
        val voteForBJP = hashMapOf("vote" to value + 1)
        db.collection("Vote").document("bjp").set(voteForBJP).addOnFailureListener {
            Toast.makeText(this, "failed ${it.message.toString()}", Toast.LENGTH_SHORT).show()
        }
        bindindg.bjpbtn.isEnabled = false
        bindindg.congressbtn.isEnabled = false
        bindindg.aapbtn.isEnabled = false
        bindindg.otherbtn.isEnabled = false

        bindindg.bjpbtn.setBackgroundColor(getColor(R.color.green))
        bindindg.congressbtn.setBackgroundColor(getColor(R.color.gray))
        bindindg.aapbtn.setBackgroundColor(getColor(R.color.gray))
        bindindg.otherbtn.setBackgroundColor(getColor(R.color.gray))

        sharedPreferences.edit().putBoolean("votebtn", false).apply()
    }


    private fun showConfirmDialogForBJP() {
        // Build the alert dialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("BJP")
        builder.setMessage("Please Confirm Your Vote")
        builder.setIcon(R.drawable.confirm)

        // Set a button for the user to dismiss the dialog
        builder.setPositiveButton("Yes") { dialog, which ->
            // You can perform any action here when the user clicks OK
            // For example, you can close the activity or dismiss the dialog
            bjpBtn()
        }

        builder.setNegativeButton("No") { dialog, which ->
            // You can perform any action here when the user clicks OK
            // For example, you can close the activity or dismiss the dialog
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun showConfirmDialogForCong() {
        // Build the alert dialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("CONGRESS")
        builder.setMessage("Please Confirm Your Vote")
        builder.setIcon(R.drawable.confirm)

        // Set a button for the user to dismiss the dialog
        builder.setPositiveButton("Yes") { dialog, which ->
            // You can perform any action here when the user clicks OK
            // For example, you can close the activity or dismiss the dialog
            congBtn()
        }

        builder.setNegativeButton("No") { dialog, which ->
            // You can perform any action here when the user clicks OK
            // For example, you can close the activity or dismiss the dialog
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun showConfirmDialogForAAp() {
        // Build the alert dialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("AAP")
        builder.setMessage("Please Confirm Your Vote")
        builder.setIcon(R.drawable.confirm)

        // Set a button for the user to dismiss the dialog
        builder.setPositiveButton("Yes") { dialog, which ->
            // You can perform any action here when the user clicks OK
            // For example, you can close the activity or dismiss the dialog
            aapBtn()
        }

        builder.setNegativeButton("No") { dialog, which ->
            // You can perform any action here when the user clicks OK
            // For example, you can close the activity or dismiss the dialog
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }


    private fun showConfirmDialogForOther() {
        // Build the alert dialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("OTHERS")
        builder.setMessage("Please Confirm Your Vote")
        builder.setIcon(R.drawable.confirm)

        // Set a button for the user to dismiss the dialog
        builder.setPositiveButton("Yes") { dialog, which ->
            // You can perform any action here when the user clicks OK
            // For example, you can close the activity or dismiss the dialog
            otherBtn()
        }

        builder.setNegativeButton("No") { dialog, which ->
            // You can perform any action here when the user clicks OK
            // For example, you can close the activity or dismiss the dialog
            dialog.dismiss()
        }

        // Create and show the dialog
        val dialog = builder.create()
        dialog.show()
    }

    private fun showConfirmDialogForResult() {
        // Build the alert dialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Result")
        builder.setMessage("The results will be displayed on 26 April, 2024!")
        builder.setIcon(R.drawable.locked)

        // Set a button for the user to dismiss the dialog
        builder.setPositiveButton("Ok") { dialog, which ->
            // You can perform any action here when the user clicks OK
            // For example, you can close the activity or dismiss the dialog
            dialog.dismiss()
        }

        // Create and show the dialog
        val dialog = builder.create()
        dialog.show()
    }

    private fun congBtn() {
        val value = setCongVoteCount()
        val voteForCogress = hashMapOf("vote" to value + 1)
        db.collection("Vote").document("congress").set(voteForCogress)
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    "failed ${it.message.toString()}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        bindindg.bjpbtn.isEnabled = false
        bindindg.congressbtn.isEnabled = false
        bindindg.aapbtn.isEnabled = false
        bindindg.otherbtn.isEnabled = false

        bindindg.bjpbtn.setBackgroundColor(getColor(R.color.gray))
        bindindg.congressbtn.setBackgroundColor(getColor(R.color.green))
        bindindg.aapbtn.setBackgroundColor(getColor(R.color.gray))
        bindindg.otherbtn.setBackgroundColor(getColor(R.color.gray))
        sharedPreferences.edit().putBoolean("votebtn", false).apply()
    }

    private fun aapBtn()
    {
        val value = setAapVoteCount()
        val voteForAAP = hashMapOf("vote" to value + 1)
        db.collection("Vote").document("aap").set(voteForAAP).addOnFailureListener {
            Toast.makeText(this, "failed ${it.message.toString()}", Toast.LENGTH_SHORT).show()
        }
        bindindg.bjpbtn.isEnabled = false
        bindindg.congressbtn.isEnabled = false
        bindindg.aapbtn.isEnabled = false
        bindindg.otherbtn.isEnabled = false

        bindindg.bjpbtn.setBackgroundColor(getColor(R.color.gray))
        bindindg.congressbtn.setBackgroundColor(getColor(R.color.gray))
        bindindg.aapbtn.setBackgroundColor(getColor(R.color.green))
        bindindg.otherbtn.setBackgroundColor(getColor(R.color.gray))

        sharedPreferences.edit().putBoolean("votebtn", false).apply()
    }

   private fun otherBtn()
    {
        val value = setOtherVoteCount()
        val voteForOthers = hashMapOf("vote" to value + 1)
        db.collection("Vote").document("others").set(voteForOthers).addOnFailureListener {
            Toast.makeText(this, "failed ${it.message.toString()}", Toast.LENGTH_SHORT).show()
        }

        bindindg.bjpbtn.isEnabled = false
        bindindg.congressbtn.isEnabled = false
        bindindg.aapbtn.isEnabled = false
        bindindg.otherbtn.isEnabled = false

        bindindg.bjpbtn.setBackgroundColor(getColor(R.color.gray))
        bindindg.congressbtn.setBackgroundColor(getColor(R.color.gray))
        bindindg.aapbtn.setBackgroundColor(getColor(R.color.gray))
        bindindg.otherbtn.setBackgroundColor(getColor(R.color.green))

        sharedPreferences.edit().putBoolean("votebtn", false).apply()
    }
}
