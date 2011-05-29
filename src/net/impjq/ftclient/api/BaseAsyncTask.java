package net.impjq.ftclient.api;
import android.os.AsyncTask;

import java.util.LinkedList;
import java.util.Queue;

/**
 * The BaseAsyncTask,if you need to listener the task status,pass
 * {@link TaskListener} to the constructor. <br>
 * Example
 * 
 * <pre>
 * RegisterTask registerTask = new RegisterTask();
 * BaseAsyncTask baseAsyncTask = new BaseAsyncTask(new TaskListener() {
 * 
 *     &#064;Override
 *     public void onTaskStart() {
 *         // TODO Auto-generated method stub
 *         Log.i(TAG, &quot;onTaskStart&quot;);
 * 
 *     }
 * 
 *     &#064;Override
 *     public void onTaskProgressUpdate(Integer... values) {
 *         // TODO Auto-generated method stub
 *         Log.i(TAG, &quot;onTaskProgressUpdate,values=&quot; + values[0]);
 * 
 *     }
 * 
 *     &#064;Override
 *     public void OnTaskFinished(String result) {
 *         // TODO Auto-generated method stub
 *         Log.i(TAG, &quot;OnTaskFinished,result=&quot; + result);
 * 
 *     }
 * });
 * 
 * baseAsyncTask.execute(registerTask);
 * </pre>
 * 
 * @author pjq0274@arcsoft.com
 * @date 2011-3-22
 */

public class BaseAsyncTask extends AsyncTask<String, Integer, Object> {
    private TaskListener mListener;

    private BaseTask mBaseTask=null;

    Queue<BaseTask> mQueue = new LinkedList<BaseTask>();

    private int mAsyncTaskStatus;

    public static final int ASYNC_TASK_STATUS_ON_PREEXECUTE = 0;

    public static final int ASYNC_TASK_STATUS_DO_IN_BACKGROUND = 1;

    public static final int ASYNC_TASK_STATUS_ON_POSTEXECUTE = 2;

    /**
     * Add task,if it already exist,just skip it.
     * 
     * @param object
     */
    public void add(BaseTask object) {
        if (!mQueue.contains(object)) {
            mQueue.add(object);
        }
    }

    public void setListener(TaskListener listener) {
        mListener = listener;
    }

    public int getAsyncTaskSatus() {
        return mAsyncTaskStatus;
    }

    public BaseAsyncTask(TaskListener taskListener) {
        // TODO Auto-generated constructor stub
        mListener = taskListener;
    }

    public BaseAsyncTask(BaseTask baseTask, TaskListener taskListener) {
        // TODO Auto-generated constructor stub
        mListener = taskListener;
        mBaseTask = baseTask;
    }

    public Queue<BaseTask> getTaskQueue() {
        return mQueue;
    }

    @Override
    protected Object doInBackground(String... params) {
        mAsyncTaskStatus = ASYNC_TASK_STATUS_DO_IN_BACKGROUND;    

        if (null != mBaseTask) {
            mBaseTask.run();
        }

        return mBaseTask;
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        mAsyncTaskStatus = ASYNC_TASK_STATUS_ON_PREEXECUTE;
        if (null != mListener) {
            mListener.onTaskStart(mBaseTask);
        }

        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        // TODO Auto-generated method stub
        if (null != mListener) {
            mListener.onTaskProgressUpdate(mBaseTask, values);
        }

        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Object result) {
        // TODO Auto-generated method stub
        mAsyncTaskStatus = ASYNC_TASK_STATUS_ON_POSTEXECUTE;
        if (null != mListener) {
            mListener.OnTaskFinished(mBaseTask);
        }

        super.onPostExecute(result);
    }

    /**
     * The task listener.You can convert the Object to {@link BaseTask},and call
     * {@link BaseTask#getTaskId()} to get the task id,and use it to distinguish
     * the different task.
     * 
     * @author pjq0274
     */
    public interface TaskListener {
        /**
         * When the task start to run.
         * 
         * @param task
         */
        void onTaskStart(Object task);

        /**
         * When the task need update the progress.
         * 
         * @param task
         * @param values
         */
        void onTaskProgressUpdate(Object task, Integer... values);

        /**
         * When the task finished.
         * 
         * @param result
         */
        void OnTaskFinished(Object task);
    }
}