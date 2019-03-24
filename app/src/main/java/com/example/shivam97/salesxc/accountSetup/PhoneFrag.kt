package com.example.shivam97.salesxc.accountSetup


import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.shivam97.salesxc.R
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.f_phone.*
import kotlinx.android.synthetic.main.f_phone.view.*
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.example.shivam97.salesxc.SalesXC.mAuth
import com.example.shivam97.salesxc.SalesXC.mUser
import com.google.firebase.auth.AuthCredential
import java.util.concurrent.TimeUnit

class PhoneFrag : Fragment() {
    private lateinit var v: View
    lateinit var phoneNumber:String
    private lateinit var mCallbacks :PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var mVerificationId: String
    private val TAG="MyPhoneAuth"

    private var d:Boolean=false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.f_phone, container, false)
        v=view

        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:$credential")
                v.progress_circular.visibility = View.INVISIBLE

//                Toast.makeText(context,"number verified",Toast.LENGTH_LONG).show()
//                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)
                Log.i(TAG,e.message)
                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Log.i(TAG,"invalid request")
                    e.printStackTrace()
                    // ...
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Log.i(TAG,"for project exceeded")
                    e.printStackTrace()
                    // ...
                }
                v.progress_circular.visibility = View.INVISIBLE

                // Show a message and update the UI
                // ...
            }

            @SuppressLint("SetTextI18n")
            override fun onCodeSent(verificationId: String?,
                                    token: PhoneAuthProvider.ForceResendingToken?) {

             /*    The SMS verification code has been sent to the provided phone number, we
                 now need to ask the user to enter the code and then construct a credential
                by combining the code with a verification ID.

                 val credential = PhoneAuthProvider.getCredential(verificationId, token.toString())
                signInWithPhoneAuthCredential(credential)*/

                mVerificationId=verificationId!!
                // Save verification ID and resending token so we can use them later
                otp_layout.visibility=View.VISIBLE
                phone_layout.visibility=View.GONE
            }


        }
        return view
    }

     fun goNext() {

        d=true
        phoneNumber = editNumber.text.toString()
        v.progress_circular.visibility = View.VISIBLE

        if (!TextUtils.isEmpty(phoneNumber)) {
            phoneNumber = "+91$phoneNumber"
            val act=activity as SetupPagersAct
            act.pager.currentItem+=1
            act.pager.setCurrentItem(act.pager.currentItem,true)
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    context as Activity,               // Activity (for callback binding)
                    mCallbacks)     // OnVerificationStateChangedCallbacks
        }

    }

    fun getCheck():Boolean{
        return d
    }
    fun signInWithPhone(){
        val code = edit_otp.text.toString()
        if (!TextUtils.isEmpty(code)) {
            v.progress_circular.visibility = View.VISIBLE

            val credential = PhoneAuthProvider.getCredential(mVerificationId, code)
            signInWithPhoneAuthCredential(credential)

        }
    }

    private fun signInWithPhoneAuthCredential(credential: AuthCredential) {

        mAuth.signInWithCredential(credential).addOnCompleteListener {task ->
            v.progress_circular.visibility = View.INVISIBLE

            if (task.isSuccessful){
                val act=activity as SetupPagersAct
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithCredential:success")
                val user = task.result?.user
                mUser=user
                Toast.makeText(act,"Number Verified",Toast.LENGTH_LONG).show()
                act.credential=credential
                act.pager.currentItem+=1
                act.pager.setCurrentItem(act.pager.currentItem,true)
            }

            else{
                // Sign in failed, display a message and update the UI
                Log.w(TAG, "signInWithCredential:failure", task.exception)
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    // The verification code entered was invalid
                    Toast.makeText(activity,"Code doesn't match",Toast.LENGTH_LONG).show()
                }
            }

        }
    }


}
