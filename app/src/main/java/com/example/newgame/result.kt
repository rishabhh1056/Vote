package com.example.newgame

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.newgame.databinding.ActivityResultBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class result : AppCompatActivity() {

    private val binding:ActivityResultBinding by lazy {
    ActivityResultBinding.inflate(layoutInflater)
    }
    private lateinit var db: FirebaseFirestore

    var BjpVoteCount : String?= null
    fun getBjpVotCount(vote: Any)
    {
        BjpVoteCount = vote.toString()
    }
    fun setBjpVoteCount() :Int{
        return BjpVoteCount?.toInt() ?: 0
    }

    var CongressVoteCount : String?= null
    fun getCongVotCount(vote: Any)
    {
        CongressVoteCount = vote.toString()
    }
    fun setCongVoteCount() :Int{
        return CongressVoteCount?.toInt() ?: 0
    }

    var AapVoteCount : String?= null
    fun getAapVotCount(vote: Any)
    {
        AapVoteCount = vote.toString()
    }
    fun setAapVoteCount() :Int{
        return AapVoteCount?.toInt() ?: 0
    }

    var OtherVoteCount : String?= null
    fun getOtherVotCount(vote: Any)
    {
        OtherVoteCount = vote.toString()
    }
    fun setOtherVoteCount() :Int{
        return OtherVoteCount?.toInt() ?: 0
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        db = Firebase.firestore






        binding.kingbtn.setOnClickListener {


        val winner = findGreatest(setBjpVoteCount(),setCongVoteCount(),setAapVoteCount(),setOtherVoteCount())

            Log.e("TAG", "onCreate: winner $winner", )

            val bjpTotalVote = setBjpVoteCount()
            val congTotalVote = setCongVoteCount()
            val aapTotalVote = setAapVoteCount()
            val otherTotalVote = setOtherVoteCount()

//            binding.first.setImageResource(R.drawable.congress)
//            binding.firstVoteCount.text = winner.toString()

        if (setBjpVoteCount() == winner)
        {
            binding.first.setImageResource(R.drawable.bjp)
            binding.firstVoteCount.text = winner.toString()

            binding.second.setImageResource(R.drawable.congress)
            binding.secVoteCount.text = congTotalVote.toString()

            binding.third.setImageResource(R.drawable.aap)
            binding.thirdVoteCount.text = aapTotalVote.toString()


            binding.fourth.setImageResource(R.drawable.other)
            binding.fourthVoteCount.text = otherTotalVote.toString()



        }
        else if (setCongVoteCount() == winner)
        {
            binding.first.setImageResource(R.drawable.congress)
            binding.firstVoteCount.text = congTotalVote.toString()

            binding.second.setImageResource(R.drawable.bjp)
            binding.secVoteCount.text = bjpTotalVote.toString()

            binding.third.setImageResource(R.drawable.aap)
            binding.thirdVoteCount.text = aapTotalVote.toString()


            binding.fourth.setImageResource(R.drawable.other)
            binding.fourthVoteCount.text = otherTotalVote.toString()
        }
        else if (setAapVoteCount() == winner)
        {
            binding.first.setImageResource(R.drawable.aap)
            binding.firstVoteCount.text = aapTotalVote.toString()

            binding.second.setImageResource(R.drawable.bjp)
            binding.secVoteCount.text = bjpTotalVote.toString()

            binding.third.setImageResource(R.drawable.congress)
            binding.thirdVoteCount.text = congTotalVote.toString()


            binding.fourth.setImageResource(R.drawable.other)
            binding.fourthVoteCount.text = otherTotalVote.toString()
        }
        else if (setOtherVoteCount() == winner)
        {
            binding.first.setImageResource(R.drawable.other)
            binding.firstVoteCount.text = otherTotalVote.toString()

            binding.second.setImageResource(R.drawable.bjp)
            binding.secVoteCount.text = bjpTotalVote.toString()

            binding.third.setImageResource(R.drawable.congress)
            binding.thirdVoteCount.text = congTotalVote.toString()


            binding.fourth.setImageResource(R.drawable.aap)
            binding.fourthVoteCount.text = aapTotalVote.toString()
        }
        else{
            Toast.makeText(this, "NO one is winner", Toast.LENGTH_SHORT).show()
        }









            binding.quection.visibility= View.GONE
           binding.first.visibility= View.VISIBLE
           binding.second.visibility= View.VISIBLE
           binding.third.visibility= View.VISIBLE
           binding.fourth.visibility= View.VISIBLE
           binding.firstVoteCount.visibility= View.VISIBLE
           binding.secVoteCount.visibility= View.VISIBLE
           binding.thirdVoteCount.visibility= View.VISIBLE
           binding.fourthVoteCount.visibility= View.VISIBLE
           binding.textView.visibility= View.VISIBLE
           binding.textView3.visibility= View.VISIBLE
           binding.textView4.visibility= View.VISIBLE
           binding.totalvote.visibility= View.VISIBLE
           binding.crown.visibility= View.VISIBLE
           binding.kingbtn.visibility= View.GONE
           binding.heading.visibility= View.GONE



        }









    }

    private fun readBjpVote() {
        db.collection("Vote").document("bjp").get()
            .addOnSuccessListener {

                if (it != null)
                {

                    it["vote"]?.let { it1 -> getBjpVotCount(it1) }

                }
            }
    }

    private fun readCongressVote() {
        db.collection("Vote").document("congress").get()
            .addOnSuccessListener {

                if (it != null)
                {
                    val  data = it["vote"]
                    Log.e("TAG", "readCongressVote: form result $data", )
                    it["vote"]?.let { it1 -> getCongVotCount(it1) }


                }
            }
    }

    private fun readAapVote() {
        db.collection("Vote").document("aap").get()
            .addOnSuccessListener {

                if (it != null)
                {

                    it["vote"]?.let { it1 -> getAapVotCount(it1) }

                }
            }
    }

    private fun readOtherVote() {
        db.collection("Vote").document("others").get()
            .addOnSuccessListener {

                if (it != null)
                {

                    it["vote"]?.let { it1 -> getOtherVotCount(it1) }

                }
            }
    }

    override fun onStart() {
        super.onStart()

        readBjpVote()
        readAapVote()
        readCongressVote()
        readOtherVote()
    }






    fun findGreatest(num1: Int, num2: Int, num3: Int, num4: Int): Int {
        val greatest1 = if (num1 > num2) num1 else num2
        val greatest2 = if (num3 > num4) num3 else num4

        return if (greatest1 > greatest2) greatest1 else greatest2
    }
}