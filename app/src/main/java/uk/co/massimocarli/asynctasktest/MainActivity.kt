package uk.co.massimocarli.asynctasktest

import android.graphics.drawable.ClipDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

  companion object {
    const val TAG_LOG = "AsyncTaskTest"
  }

  private var mCurrentAsyncTask: CounterAsyncTask? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  fun buttonPressed(pressedButton: View) {
    when (pressedButton.getId()) {
      R.id.start_button -> {
        if (mCurrentAsyncTask == null) {
          mCurrentAsyncTask = CounterAsyncTask().apply {
            execute(1000)
          }
        }
      }
      R.id.stop_button -> {
        if (mCurrentAsyncTask != null) {
          mCurrentAsyncTask?.cancel(true)
          mCurrentAsyncTask = null
        }
      }
    }
  }

  private fun updateDirectly(progress: Int) {
    progress_view.background = (progress_view.background as ClipDrawable)
      .apply {
        level = progress
        progress_view.background = this
      }
  }

  inner class CounterAsyncTask : AsyncTask<Int, Int, Long>() {

    private var mSum: Long = 0

    override fun onPreExecute() {
      super.onPreExecute()
      asToast("TASK STARTED!!!")
      progress_view.setVisibility(View.VISIBLE);
    }

    override fun onPostExecute(aLong: Long?) {
      super.onPostExecute(aLong)
      progress_view.setVisibility(View.GONE)
      asToast("TASK COMPLETED!!! Sum: $mSum")
    }

    override fun onCancelled(result: Long?) {
      super.onCancelled(result)
      asToast("TASK CANCELLED!!! Sum: $mSum")
    }

    override fun onProgressUpdate(vararg values: Int?) {
      super.onProgressUpdate(*values)
      updateDirectly(values[0] ?: 0)
    }

    override fun doInBackground(vararg numberToCount: Int?): Long {
      val maxNumber = numberToCount[0] ?: 0
      val step = 10000 / maxNumber
      mSum = 0
      var progressValue = 0
      for (counter in 0 until maxNumber) {
        if (isCancelled) {
          break
        }
        Thread.sleep(5)
        if (isCancelled) {
          break
        }
        mSum += counter.toLong()
        progressValue += step
        Log.d(TAG_LOG, "Sum: $mSum")
        if (isCancelled) {
          break
        }
        publishProgress(progressValue)
      }
      return mSum
    }
  }
}
