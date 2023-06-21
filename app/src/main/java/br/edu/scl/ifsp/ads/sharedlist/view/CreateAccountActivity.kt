package br.edu.scl.ifsp.ads.sharedlist.view

import android.os.Bundle
import android.widget.Toast
import br.edu.scl.ifsp.ads.sharedlist.databinding.ActivityCreateAccountBinding
import com.google.firebase.auth.FirebaseAuth

class CreateAccountActivity : BaseActivity() {
    private val acab: ActivityCreateAccountBinding by lazy {
        ActivityCreateAccountBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(acab.root)

        acab.createAccountBt.setOnClickListener {
            val email = acab.emailEt.text.toString()
            val password = acab.passwordEt.text.toString()
            val password2 = acab.repeatPasswordEt.text.toString()

            if (password.equals(password2)) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        Toast.makeText(
                            this@CreateAccountActivity,
                            "User $email registered!",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(
                            this@CreateAccountActivity,
                            "Error during the user creation!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            } else {
                Toast.makeText(
                    this@CreateAccountActivity,
                    "Passwords don't match!",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    }
}