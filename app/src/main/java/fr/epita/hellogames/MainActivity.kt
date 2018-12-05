package fr.epita.hellogames

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var gameList = arrayListOf<Game>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firstGame.setOnClickListener(this@MainActivity)
        secondGame.setOnClickListener(this@MainActivity)
        thirdGame.setOnClickListener(this@MainActivity)
        fourthGame.setOnClickListener(this@MainActivity)

        val baseURL = "https://androidlessonsapi.herokuapp.com/api/"
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofit = Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(jsonConverter)
                .build()
        val service: WebServiceInterface = retrofit.create(WebServiceInterface::class.java)

        val callback = object : Callback<List<Game>> {
            override fun onResponse(call: Call<List<Game>>?,
                                    response: Response<List<Game>>?) {
                if (response != null) {
                    if (response.code() == 200) {
                        val responseData = response.body()
                        if (responseData != null) {
                            gameList.add(responseData[0])
                            gameList.add(responseData[1])
                            gameList.add(responseData[2])
                            gameList.add(responseData[3])
                            findViewById<ImageView>(R.id.firstGame).setImageResource(R.drawable.slidingpuzzle)
                            findViewById<ImageView>(R.id.secondGame).setImageResource(R.drawable.sudoku)
                            findViewById<ImageView>(R.id.thirdGame).setImageResource(R.drawable.tictactoe)
                            findViewById<ImageView>(R.id.fourthGame).setImageResource(R.drawable.gameoflife)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<List<Game>>?, t: Throwable?) {
                Log.d("TAG", "WebService call failed")
            }
        }
        service.listofGame().enqueue(callback)
    }

    override fun onClick(clickedView: View?) {

        if (clickedView != null) {
            when (clickedView.id) {
                R.id.firstGame -> {
                    sendIntent(gameList.get(0).id!!, R.drawable.slidingpuzzle)
                }
                R.id.secondGame -> {
                    sendIntent(gameList.get(1).id!!, R.drawable.sudoku)
                }
                R.id.thirdGame -> {
                    sendIntent(gameList.get(2).id!!, R.drawable.tictactoe)
                }
                R.id.fourthGame -> {
                    sendIntent(gameList.get(3).id!!, R.drawable.gameoflife)
                }
            }
        }
    }

    fun sendIntent(gameId: Int, imageId: Int)
    {
        val CODE_GAMEID = "CODE_ID"
        val CODE_IMAGEID = "CODE_IMAGEID"
        val explicitIntent = Intent(this, SecondActivity::class.java)
        explicitIntent.putExtra(CODE_GAMEID, gameId)
        explicitIntent.putExtra(CODE_IMAGEID, imageId)
        startActivity(explicitIntent)
    }
}




