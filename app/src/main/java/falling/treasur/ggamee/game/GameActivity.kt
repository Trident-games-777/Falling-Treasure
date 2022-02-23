package falling.treasur.ggamee.game

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import falling.treasur.ggamee.R
import falling.treasur.ggamee.databinding.ActivityGameBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private var points = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listOf(
            binding.imageView1,
            binding.imageView2,
            binding.imageView3,
            binding.imageView4,
            binding.imageView5,
        ).forEach { imageView -> imageView.setOnClickListener { view -> onClick(view as ImageView) } }

        lifecycleScope.launch {
            repeat(20) {
                delay(800)
                shuffleItems()
            }
        }
    }

    private fun shuffleItems() {
        val listImages = listOf(
            R.drawable.e1,
            R.drawable.e2,
            R.drawable.e3,
            R.drawable.e4,
            R.drawable.e5,
        ).shuffled()
        binding.imageView1.setImageResource(listImages[0])
        binding.imageView1.tag = listImages[0]
        binding.imageView1.isClickable = true
        binding.imageView2.setImageResource(listImages[1])
        binding.imageView2.tag = listImages[1]
        binding.imageView2.isClickable = true
        binding.imageView3.setImageResource(listImages[2])
        binding.imageView3.tag = listImages[2]
        binding.imageView3.isClickable = true
        binding.imageView4.setImageResource(listImages[3])
        binding.imageView4.tag = listImages[3]
        binding.imageView4.isClickable = true
        binding.imageView5.setImageResource(listImages[4])
        binding.imageView5.tag = listImages[4]
        binding.imageView5.isClickable = true
    }

    private fun onClick(view: ImageView) {
        if (view.tag == R.drawable.e2) {
            view.isClickable = false
            points++
            binding.textViewTotal.text = "${points}/20"
        }
    }
}