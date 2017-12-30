package xyz.ravitripathi.omnisense

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var fireAuth:FirebaseAuth

    //Not using List<E>. The reason being in Kotlin, List is immutable (cant be changed)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fireAuth = FirebaseAuth.getInstance()


        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        val a = activeUSB("Hello", "WoRLD", "Test")
        val usbList: ArrayList<activeUSB> = ArrayList()
        for (i in 1..10)
            usbList.add(a)
        recyclerView.adapter = recycleAdapter(usbList)
    }
}
