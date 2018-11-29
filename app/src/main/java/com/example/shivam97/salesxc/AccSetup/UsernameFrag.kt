package com.example.shivam97.salesxc.AccSetup
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.shivam97.salesxc.R
import com.example.shivam97.salesxc.SalesXC.mAuth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.f_username.view.*


class UsernameFrag : Fragment() {

    lateinit var username:String;lateinit var pass:String;lateinit var confirm:String
    internal lateinit var view: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.f_username, container, false)
        mAuth= FirebaseAuth.getInstance()

        return view
    }

    fun createAcc(){
        Toast.makeText(activity,"creating account",Toast.LENGTH_LONG).show()
        username=view.username.text.toString()
        pass=view.password.text.toString()
        confirm=view.confirm.text.toString()
        if( ! TextUtils.isEmpty(username) && !TextUtils.isEmpty(pass) && pass==confirm){
            if(pass.length>5){
                username += "@salesxc.com"
                mAuth.createUserWithEmailAndPassword(username,pass).addOnCompleteListener {
                    result->
                    if(result.isSuccessful){
                        (activity as SetupPagersAct).accSetup()
//                        mAuth.currentUser?.linkWithCredential((activity as SetupPagersAct).credential)
                    }
                    else{

                        if(result.exception is FirebaseAuthUserCollisionException)
                        Toasty.error(activity!!,"username already exists ",Toast.LENGTH_LONG,true).show()

                        else
                            Toasty.error(activity!!,"Error in creating Account "+
                                    result.exception?.message,Toast.LENGTH_LONG,true).show()
                    }
                }
            }
            else
                Toasty.warning(context!!,"key must be minimum 6 character long"
                        ,Toast.LENGTH_LONG,true).show()
        }
    }

}
