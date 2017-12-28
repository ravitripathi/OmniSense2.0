package xyz.ravitripathi.omnisense

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    private var mAuth: FirebaseAuth? = null
    internal var RC_SIGN_IN = 9001
    var mGoogleApiClient: GoogleApiClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        initGoogleSignIn()
    }

    private fun initGoogleSignIn() {
        //Set up the button and listener
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.clientID))
                .requestEmail()
                .requestProfile()
                .build()

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        google.setOnClickListener {
            login_progress.visibility = View.VISIBLE
            signIn()
        }

    }

    fun signIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onStop() {
        super.onStop()
        mGoogleApiClient?.stopAutoManage(this)
        mGoogleApiClient?.disconnect()

        login_progress.visibility = View.GONE
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        Toast.makeText(this, requestCode.toString(), Toast.LENGTH_LONG).show()
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                val account = result.signInAccount
                firebaseAuthWithGoogle(account)
            } else {
                login_progress.visibility = View.GONE
                val alertDialog = AlertDialog.Builder(this).create()
                alertDialog.setTitle("Connection Issues")
                alertDialog.setMessage("Please check your internet connection and retry.")
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()
            }
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Toast.makeText(this, "Could not connect", Toast.LENGTH_SHORT).show()
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
        FirebaseSignIn(credential)
    }

    private fun FirebaseSignIn(credential: AuthCredential) {
        mAuth?.signInWithCredential(credential)?.addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
            if (!task.isSuccessful) {
                Toast.makeText(this@LoginActivity, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
            } else {
                val i = Intent(this@LoginActivity, MainActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(i)
            }
        })
    }

}
