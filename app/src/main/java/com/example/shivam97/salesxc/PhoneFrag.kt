package com.example.shivam97.salesxc


import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.support.constraint.Constraints.TAG
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.f_phone.*
import kotlinx.android.synthetic.main.f_phone.view.*
import java.util.concurrent.TimeUnit
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.example.shivam97.salesxc.SalesXC.mAuth

class PhoneFrag : Fragment() {
    lateinit var v: View
    private lateinit var mCallbacks :PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var mVerificationId: String
    lateinit var btn: Button

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

//                Toast.makeText(context,"number verified",Toast.LENGTH_LONG).show()
//                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

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
                v.top_view.text="Enter OTP for verification"
                v.country_code.visibility= View.INVISIBLE
                v.warning_view.text="Enter six digit verification code"
                btn.isEnabled=true
                btn.setBackgroundColor(0)
                editNumber.text=null
                editNumber.isEnabled=true
                btn.text="Submit"

            }


        }
        return view
    }

    fun goNext(btn:Button) {


        var phoneNumber: String = editNumber.text.toString()
        v.progress_circular.visibility = View.VISIBLE
        editNumber.isEnabled = false
        this.btn=btn

        if (!TextUtils.isEmpty(phoneNumber)) {
            phoneNumber = "+91$phoneNumber"

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    context as Activity,               // Activity (for callback binding)
                    mCallbacks)     // OnVerificationStateChangedCallbacks
        }

        btn.setOnClickListener {
            val code = editNumber.text.toString()
            if (!TextUtils.isEmpty(code)) {
                val credential = PhoneAuthProvider.getCredential(mVerificationId, code)
                signInWithPhoneAuthCredential(credential)

            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        mAuth.signInWithCredential(credential).addOnCompleteListener {task ->
            if (task.isSuccessful){
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithCredential:success")
                val user = task.result?.user
                Toast.makeText(activity,"SignIn Successful",Toast.LENGTH_LONG).show()
                v.progress_circular.visibility=View.INVISIBLE

            }
            else{
                // Sign in failed, display a message and update the UI
                v.progress_circular.visibility=View.INVISIBLE
                Log.w(TAG, "signInWithCredential:failure", task.exception)
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    // The verification code entered was invalid
                    Toast.makeText(activity,"Code doesn't match",Toast.LENGTH_LONG).show()
                }
            }

        }
    }


}
