package my.example.snowflakesview.surface;


import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class MainThread extends Thread {

    /***
     * флаг, указывающий на то, что игра запущена.
     */
    private boolean running;

    private SurfaceHolder surfaceHolder;
    private Snowflakes snowflakes;

    private static final String TAG = MainThread.class.getSimpleName();


    public MainThread(SurfaceHolder holder, Snowflakes owner) {
        super();
        surfaceHolder = holder;
        snowflakes = owner;
    }


    public void setRunning(boolean r) {
        running = r;
    }

    @Override
    public void run() {
        Canvas canvas;
        Log.e(TAG, "Starting game loop");
        while (running) {
            canvas = null;
            // пытаемся заблокировать canvas
            // для изменение картинки на поверхности
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    // здесь будет обновляться состояние игры
                    // и формироваться кадр для вывода на экран
                    snowflakes.onDraw(canvas);//Вызываем метод для рисования
                }
            } finally {
                // в случае ошибки, плоскость не перешла в
                //требуемое состояние
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

}
