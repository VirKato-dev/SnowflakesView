package my.example.snowflakesview.view;

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
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


public class SnowflakesView extends View {

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
        private Bitmap img;
    }


    private Random random = new Random();
    private Paint paint = new Paint();

    /***
     * Размер виджета
     */
    private int vWidth = 0, vHeight = 0;

    /***
     * Цвет всех снежинок
     */
    private int color = Color.MAGENTA;

    /***
     * Количество снежинок
     */
    private int count = 20;

    /***
     * Максимальный размер снежинки
     */
    private int maxSize = 19;

    /***
     * Радиус касания
     */
    private int touchR = 200;

    /***
     * Список снежинок
     */
    private ArrayList<Flake> flakes = new ArrayList<>();

    /***
     * Картинки снежинок
     */
    private ArrayList<Bitmap> images = new ArrayList<>();


    public SnowflakesView(Context context) {
        super(context);
    }

    public SnowflakesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            for (int i = 0; i < attrs.getAttributeCount(); i++) {
                switch (attrs.getAttributeName(i)) {
                    case "colorFlakes":
                        color = attrs.getAttributeIntValue(i, Color.MAGENTA);
                        break;
                    case "countFlakes":
                        count = attrs.getAttributeIntValue(i, 20);
                        break;
                    case "sizeFlakes":
                        maxSize = attrs.getAttributeIntValue(i, 20);
                }
            }
        }
        init();
    }

    public SnowflakesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SnowflakesView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawFlakes(canvas);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        vWidth = MeasureSpec.getSize(widthMeasureSpec);
        vHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(vWidth, vHeight);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
//        if (changed) {
        vWidth = right - left;
        vHeight = bottom - top;
        setMeasuredDimension(vWidth, vHeight);
//        }
    }

    /***
     * Настроить карандаш
     */
    private void init() {
//        paint.setShadowLayer(5, 0, 0, Color.BLACK);
        paint.setColor(color);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.FILL);
    }

    /***
     * Задать максимальное количество снежинок
     * @param c количество
     * @return
     */
    public SnowflakesView setCount(int c) {
        count = c;
        return this;
    }

    /***
     * Задать цвет снежинок
     * @param c цвет
     * @return
     */
    public SnowflakesView setColor(int c) {
        color = c;
        return this;
    }

    /***
     * Задать максимальный размер снежинок
     * @param s размер
     * @return
     */
    public SnowflakesView setMaxSize(int s) {
        s -= 2;
        if (s < 1) s = 1;
        maxSize = s;
        return this;
    }

    /***
     * Добавить картинку снежинки
     * @return
     */
    public SnowflakesView addPicture(int img) {
        // Храним готовый Bitmap, для ускорения отрисовки
        images.add(BitmapFactory.decodeResource(getResources(), img));
        return this;
    }

    /***
     * Рисуем все снежинки
     * @param canvas холст
     */
    private void drawFlakes(Canvas canvas) {
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

            // раздвигаем снежинки
            if (touchX >= 0) {
                int kx = (int) (f.x - touchX);
                int ky = (int) (f.y - touchY);
                if (touchR * touchR > kx * kx + ky * ky) {
                    if (kx >= 0) f.x++;
                    else f.x--;
//                    if (ky >= 0) f.y++; else f.y--;
                }
            }

            if ((f.y > getHeight() + f.size) || f.alfa >= 510) {
                // удалить снежинку при исчезновении или уходе за границу виджета
                it.remove();
            }
        }

        // отрисовка холста на виджете
        invalidate();
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
            ColorFilter filter = new PorterDuffColorFilter(f.color, PorterDuff.Mode.SRC_IN);
            paint.setColorFilter(filter);

            float scale = f.size*2 / (float) Math.max(f.img.getWidth(), f.img.getHeight());

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            matrix.postRotate(f.rotate);

            Bitmap bm = Bitmap.createBitmap(f.img, 0 , 0, f.img.getWidth(), f.img.getHeight(), matrix, false);

            canvas.drawBitmap(bm, f.x - (bm.getWidth()/2f), f.y - (bm.getHeight()/2f), paint);
        }
    }

    /***
     * Создать снежинку заданного цвета
     * @return всегда новая
     */
    @NonNull
    private Flake genFlake() {
        Flake f = new Flake();
        f.size = random.nextInt(maxSize) + 2;
        f.speed = random.nextInt(f.size / 3 + 1) + 2;
        f.alfaSpeed = random.nextInt(7) + 1;
        f.rotateSpeed = random.nextInt(4) - 2;
        f.x = random.nextInt(getWidth());
        f.y = random.nextInt(vHeight / 2);
        f.color = color;
        if (images.size() > 0) {
            f.img = images.get(random.nextInt(images.size()));
        }
        return f;
    }

    /***
     * Место касания
     */
    private float touchX = -1, touchY = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                touchX = -1;
                touchY = -1;
                result = true;
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_DOWN:
                touchX = event.getX();
                touchY = event.getY();
                result = true;
                break;
        }
        return result;
    }
}
