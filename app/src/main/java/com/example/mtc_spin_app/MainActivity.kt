package com.example.mtc_spin_app

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

class MainActivity() : AppCompatActivity() {

    private var isSpinning: Boolean = false

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
    }

    //----   SPIN WHEEL   ----
    private fun spinWheel() {
        /*

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
            if yes, display a toast and return.
            else, set isSpinning to true and continue
         */
        if (this.isSpinning) {
            Toast.makeText(this, "Wheel is already spinning", Toast.LENGTH_SHORT)
            return
        } else
            this.isSpinning = true


        //--  image resource ids to loop through  --
        val imageIDs = intArrayOf(
            R.drawable.spin_mtc_1,
            R.drawable.spin_mtc_2,
            R.drawable.spin_mtc_3,
            R.drawable.spin_mtc_4,
            R.drawable.spin_mtc_5
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
                    isSpinning = false
                }
            }
        })
    }

}