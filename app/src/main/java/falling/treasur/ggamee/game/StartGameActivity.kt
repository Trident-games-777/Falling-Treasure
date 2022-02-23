package falling.treasur.ggamee.game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import falling.treasur.ggamee.R

class StartGameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_game)

        findViewById<Button>(R.id.btStartGame).setOnClickListener {
            startActivity(Intent(this@StartGameActivity, GameActivity::class.java))
        }
    }
}