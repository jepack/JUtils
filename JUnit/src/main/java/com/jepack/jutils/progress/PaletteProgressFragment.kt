package com.jepack.jutils.progress

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.jepack.jutils.R
import com.jepack.jutils.base.BaseBindingFragment
import com.jepack.jutils.databinding.PaletteBinding
import kotlinx.android.synthetic.main.fg_palette.*

class PaletteProgressFragment : BaseBindingFragment<PaletteBinding>() {

    internal var progress = 0
    internal var handler: Handler? = null
    internal var runnable: ProgressRunnable? = null
    internal var direction = 1
    internal var step = 5
    internal var interval = step / 100L * 10000L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflateContentLayout(R.layout.fg_palette, inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progress_ctr.max = palette_progress.getMax()
        progress_ctr.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                palette_progress.setProgress(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                handler?.removeCallbacks(runnable)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                handler?.postDelayed(runnable, interval)
            }
        })
        handler = Handler()
        runnable = ProgressRunnable()
        handler?.postDelayed(runnable, interval )

        palette_direction_group.setOnCheckedChangeListener{ radio, index->
            palette_progress.setDirection(index - 1)
        }
        palette_type.setOnCheckedChangeListener{ radio, index->
            palette_progress.setType(index - 1)
        }
    }

    internal inner class ProgressRunnable : Runnable {
        override fun run() {
            progress += step * direction
            if (progress > palette_progress.getMax()) {
                direction = -1
                progress = palette_progress.getMax() - step
            } else if (progress < 0) {
                direction = 1
                progress = step
            }
            palette_progress.setProgress(progress)
            handler?.postDelayed(runnable, interval)
        }
    }
}
