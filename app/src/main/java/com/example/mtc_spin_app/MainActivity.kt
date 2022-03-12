package com.example.mtc_spin_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import java.lang.Exception

class MainActivity() : AppCompatActivity() {

    private var isSpinning: Boolean = false
    private var numOfSpins: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    //----   INIT   ----
    private fun init() {
        /*
            set initial values for views and
            set button listeners

             params:    none
             -------

             returns:   void
             --------
         */

        findViewById<Button>(R.id.btn_spin).setOnClickListener {
            spinWheel()
        }

        findViewById<Button>(R.id.btn_add_credit).setOnClickListener {
            convertCreditToSpins()
        }
    }

    //----   CONVERT CREDIT TO SPINS   ----
    private fun convertCreditToSpins() {
        /*
            takes the credit amount the user entered,
            checks for errors, and then based on the amount,
            converts it into spins

             params:    none
             -------

             returns:   void
             --------
         */

        //--  get value from edit text --
        val creditAmountText: String =
            (findViewById<EditText>(R.id.edt_credit_amount).text).toString()

        //--  check if value is valid integer  --
        var creditAmount = 0
        try {
            creditAmount = Integer.parseInt(creditAmountText)
        } catch (ex: Exception) {
            this.showToast("Invalid number entered", Toast.LENGTH_SHORT)
            return
        }

        //--  convert to spins  --
        when (creditAmount) {
            in 5..9 -> this.setNumOfSpins(1)
            in 10..19 -> this.setNumOfSpins(2)
            in 20..49 -> this.setNumOfSpins(3)
            in 50..Int.MAX_VALUE -> this.setNumOfSpins(5)
            else -> {
                this.showToast("Not enough credit for a spin!", Toast.LENGTH_SHORT)
                return   // return but keep edit field as is
            }
        }

        //--  clear edit field if successfully added spins  --
        findViewById<EditText>(R.id.edt_credit_amount).setText("")
    }

    //----   SPIN WHEEL   ----
    private fun spinWheel() {
        /*
         checks if user has spins available then
         generates rand num between 1 and 5, this will represent
         the outcome for the spin
         A handler will be created that will scroll through the spin
         images for 3 complete iterations
         A 4th iteration will be performed until the wheel stops on
         the image that corresponds to the random number generated

         params:    none
         -------

         returns:   void
         --------
        */

        /*
            check if wheel is spinning
            if yes, display a toast and returns.
            else, set isSpinning to true and continue
         */
        if (this.isSpinning) {
            this.showToast("Wheel is already spinning!", Toast.LENGTH_SHORT)
            return
        } else
            this.setIsSpinning(true)

        /*
            check if user has any spins available
            if not, displays toast and returns
            else, decrease number of spins and continue
         */
        if (this.numOfSpins < 1) {
            this.showToast("You have no spins left!", Toast.LENGTH_SHORT)
            this.setIsSpinning(false)
            return
        }
        this.setNumOfSpins(this.numOfSpins - 1)   // num of spins decreased by 1

        //--  image resource ids to loop through  --
        val imageIDs = intArrayOf(
            R.drawable.spin_mtc_1,
            R.drawable.spin_mtc_2,
            R.drawable.spin_mtc_3,
            R.drawable.spin_mtc_4,
            R.drawable.spin_mtc_5
        )
        //--  prizes to show  --
        val prizes = arrayOf(
            "a New Toyota Prius",
            "a Brand new S21 Ultra",
            "Twenty Thousand Dollars",
            "an M1 Macbook Pro",
            "One Hundred Dollars"
        )

        //--  vars for loop control  --
        var index = 0
        var iter = 0
        val NUM_OF_ITER = 3

        //--  img view to change  --
        val img = findViewById<ImageView>(R.id.img_spin_id)

        //--  gen rand num between 0 and 4 (represents img indices) --
        val genNum = (0..4).random()

        //--  handler def  --
        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {

                //--  increase iter and reset index after each full iter  --
                if (index >= 5) {
                    iter += 1
                    index = 0
                }

                //--  set img  --
                img.setImageResource(imageIDs[index])

                //--  inc index  --
                index += 1

                /*
                    For the final iteration where the generated number is used,
                    the images should change slower.
                    The final iteration will only continue until the generated
                    number is reached.

                    For regular iterations, every 2 full iterations will take
                    1 second. The final iteration has a max time of 1 second.

                    Once iterations are complete, set spinning to false and return
                 */
                if (iter == NUM_OF_ITER && index < genNum) {
                    handler.postDelayed(this, 200)
                } else if (iter < NUM_OF_ITER) {
                    handler.postDelayed(this, 100)
                } else {
                    index -= 1  // decrease index to show the actual prize won. offsets the index increase done above

                    showToast("Congratulations!!! You won ${prizes[index]}", Toast.LENGTH_LONG)
                    setIsSpinning(false)
                }
            }
        })
    }

    private fun showToast(text: String, duration: Int) {
        /*
            displays a popup message to the user

             params:
             -------

                text: String
                    the text to display to the user
                duration: Int
                    how long the message should be displayed

             returns:   void
             --------
         */

        Toast.makeText(this, text, duration).show()
    }

    private fun setNumOfSpins(spins: Int) {
        /*
            set numOfSpins
            change text field that shows num of spins
            to new value

             params:
             -------

                spins: Int
                    the new number of spins the user has

             returns:   void
             --------
         */

        this.numOfSpins = spins
        findViewById<TextView>(R.id.txt_spins_avail).text = "Spins Available: ${spins}"
    }

    private fun setIsSpinning(isSpinning: Boolean) {
        this.isSpinning = isSpinning
    }
}