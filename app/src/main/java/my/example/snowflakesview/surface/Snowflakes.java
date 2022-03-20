package my.example.snowflakesview.surface;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


public class Snowflakes extends SurfaceView implements SurfaceHolder.Callback {

    /***
     * Снежинка
     */
    private static class Flake {
        private int x, y;
        private int size = 1;
        private int speed = 1;
        private int alfa = 0;
        private int alfaSpeed = 0;
        private int rotate = 0;
        private int rotateSpeed = 0;
        private int color;
        private ColorFilter colorFilter;
        private Bitmap img;
    }

    /***
     * Подготовленные изображения снежинок
     */
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();

    private ArrayList<Flake> flakes = new ArrayList<>();

    private MainThread thread;

    private Paint paint = new Paint();

    private Random rand = new Random();
    private int color = Color.WHITE;
    private int count = 50;
    private int maxSize = 50;
    private int vWidth = 1, vHeight = 1;


    public Snowflakes(Context context) {
        super(context);
        // Добавляем этот класс, как содержащий функцию обратного вызова для взаимодействия с событиями
        getHolder().addCallback(this);
        // создаем поток для игрового цикла
        thread = new MainThread(getHolder(), this);
        // делаем Snowflakes focusable, чтобы она могла обрабатывать сообщения
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        vHeight = height;
        vWidth = width;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //посылаем потоку команду на закрытие и дожидаемся, пока поток не будет закрыт.
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                // пытаемся снова остановить поток thread
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawFlakes(canvas);
    }

    /***
     * Добавить картинку снежинки
     * @param res номер картинки в ресурсах
     */
    public void addPicture(int res) {
        bitmaps.add(BitmapFactory.decodeResource(getResources(), res));
        Log.e("addPicture", "" + bitmaps.size());
    }

    /***
     * Задать максимальное количество снежинок
     * @param c количество
     * @return
     */
    public Snowflakes setCount(int c) {
        count = c;
        return this;
    }

    /***
     * Задать цвет снежинок
     * @param c цвет
     * @return
     */
    public Snowflakes setColor(int c) {
        color = c;
        return this;
    }

    /***
     * Задать максимальный размер снежинок
     * @param s размер
     * @return
     */
    public Snowflakes setMaxSize(int s) {
        s -= 2;
        if (s < 1) s = 1;
        maxSize = s;
        return this;
    }

    /***
     * Создать снежинку заданного цвета
     * @return всегда новая
     */
    private Flake genFlake() {
        Flake f = new Flake();
        f.size = rand.nextInt(maxSize) + 2;
        f.speed = rand.nextInt(f.size / 3 + 1) + 2;
        f.alfaSpeed = rand.nextInt(7) + 1;
        f.rotateSpeed = rand.nextInt(4) - 2;
        f.x = rand.nextInt(getWidth());
        f.y = rand.nextInt(vHeight / 2);
        f.color = color;
        f.colorFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);
        if (bitmaps.size() > 0) {
            f.img = bitmaps.get(rand.nextInt(bitmaps.size()));
        }
        return f;
    }

    /***
     * Рисуем все снежинки
     * @param canvas холст
     */
    private void drawFlakes(Canvas canvas) {
        // Заливаем canvas прозрачным цветом
        canvas.drawColor(Color.BLUE);

        // заполнить список снежинок по заданному количеству
        while (flakes.size() < count) {
            flakes.add(genFlake());
        }

        // используем итератор, чтобы легко избавляться от лишних снежинок во время цикла
        Iterator<Flake> it = flakes.iterator();
        while (it.hasNext()) {
            Flake f = it.next();

            // рисовать снежинку
            drawFlake(f, canvas);

            // изменение состояний движения и таяния
            f.y += f.speed;
            f.alfa += f.alfaSpeed;
            f.alfa = (f.alfa <= 255) ? f.alfa : 255 - f.alfa % 255;
            f.rotate = (f.rotate + f.rotateSpeed) % 360;

            if ((f.y > getHeight() + f.size) || f.alfa >= 510) {
                // удалить снежинку при исчезновении или уходе за границу виджета
                it.remove();
            }
        }
    }

    /***
     * Нарисовать снежинку картинкой или кружком
     * @param f снежинка
     * @param canvas холст
     */
    private void drawFlake(Flake f, Canvas canvas) {
        paint.setColor(f.color);
        paint.setAlpha(f.alfa);

        if (f.img == null) {
            paint.setColorFilter(null);
            canvas.drawCircle(f.x, f.y, f.size, paint);
        } else {
            paint.setColorFilter(f.colorFilter);

            float scale = f.size*2 / (float) Math.max(f.img.getWidth(), f.img.getHeight());

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            matrix.postRotate(f.rotate);
            Bitmap bm = Bitmap.createBitmap(f.img, 0 , 0, f.img.getWidth(), f.img.getHeight(), matrix, false);
            canvas.drawBitmap(bm, f.x - (bm.getWidth()/2f), f.y - (bm.getHeight()/2f), paint);
        }
    }

}
