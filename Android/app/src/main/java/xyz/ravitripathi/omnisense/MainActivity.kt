package xyz.ravitripathi.omnisense

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item.*

class MainActivity : AppCompatActivity() {

    lateinit var fireAuth:FirebaseAuth
    lateinit var fireDB: FirebaseDatabase
    lateinit var fireRef: DatabaseReference
    lateinit var urlListener : ValueEventListener

    //Not using List<E>. The reason being in Kotlin, List is immutable (cant be changed)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fireAuth = FirebaseAuth.getInstance()
        fireDB = FirebaseDatabase.getInstance()
        fireRef = fireDB.getReference("activeUSB")
        val usbList: ArrayList<activeUSB> = ArrayList()
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        recyclerView.adapter = recycleAdapter(usbList)

        urlListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                usbList.clear()
                for (deviceSnapshot in dataSnapshot.children) {
                    val usb = deviceSnapshot.getValue(activeUSB::class.java)!!
                    usbList.add(usb)
                    recyclerView.adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }

        fireRef.addValueEventListener(urlListener)


    }
}
