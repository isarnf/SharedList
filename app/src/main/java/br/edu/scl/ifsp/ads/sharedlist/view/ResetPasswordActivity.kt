package br.edu.scl.ifsp.ads.sharedlist.view

import android.os.Bundle
import android.widget.Toast
import br.edu.scl.ifsp.ads.sharedlist.databinding.ActivityResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : BaseActivity() {
    private val arpb: ActivityResetPasswordBinding by lazy{
        ActivityResetPasswordBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(arpb.root)

        arpb.sendEmailBt.setOnClickListener {
            val email = arpb.recoveryPasswordEmailEt.text.toString()
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener { resultado ->
                if(resultado.isSuccessful){
                    Toast.makeText(this, "Recovering e-mail sent to $email", Toast.LENGTH_LONG).show()
                    finish()
                }else{
                    Toast.makeText(this, "Recovering e-mail not sent!", Toast.LENGTH_LONG).show()
                }
            }
        }

    }
}