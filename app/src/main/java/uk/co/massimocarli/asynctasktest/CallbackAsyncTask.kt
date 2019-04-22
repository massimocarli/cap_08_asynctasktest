package uk.co.massimocarli.asynctasktest

import android.os.AsyncTask
import java.util.function.Consumer
import java.util.function.Function

typealias Task<Param, Result> = (Param) -> Result

class CallbackAsyncTask<Param, Progress, Result>(
  val callableInBackground: Function<Array<out Param>, Result>,
  val preExecuteRunnable: Runnable? = null,
  val progressUpdateRunnable: Consumer<Array<out Progress?>>? = null,
  val postExecuteRunnable: Runnable? = null,
  val onCancelledRunnable: Runnable? = null
) : AsyncTask<Param, Progress, Result>() {

  override fun onPreExecute() {
    super.onPreExecute()
    preExecuteRunnable?.run()
  }

  override fun onPostExecute(result: Result) {
    super.onPostExecute(result)
    postExecuteRunnable?.run()
  }

  override fun onProgressUpdate(vararg values: Progress) {
    super.onProgressUpdate(*values)
    progressUpdateRunnable?.accept(values)
  }

  override fun onCancelled() {
    super.onCancelled()
    onCancelledRunnable?.run()
  }

  override fun doInBackground(vararg params: Param): Result =
    callableInBackground.apply(params)

}