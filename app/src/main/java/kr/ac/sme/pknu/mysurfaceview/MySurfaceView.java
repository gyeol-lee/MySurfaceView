package kr.ac.sme.pknu.mysurfaceview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import java.util.Random;


public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    public Ball basket[] = new Ball[10];
    private MyThread thread;

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true); // 스레드를 시작한다.
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Ball.WIDTH = width;
        // Ball.HEIGHT = height;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        // 스레드를 중지시킨다.
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join(); // 메인 스레드와 합친다.
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }


    public MySurfaceView(Context context) { // 생성자
        super(context);
        SurfaceHolder holder = getHolder(); // 서피스 뷰의 홀더를 얻는다.
        holder.addCallback(this); // 콜백 메소드를 처리한다.
        thread = new MyThread(holder); // 스레드를 생성한다.


        //display 정보를 받아와 Ball 의 public 변수인 WIDTH와 HEIGHT의 값에 저장한다.
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int actionBarHeight = getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material);
        Point size = new Point();
        display.getSize(size);
        Ball.WIDTH = size.x;
        Ball.HEIGHT = size.y - actionBarHeight;

        // Ball 객체의 색 변경을위한 랜덤 숫자 생성
        Random random = new Random();
        // Ball 객체를 생성하여서 배열에 넣는다.
        for (int i = 0; i < 10; i++)
            basket[i] = new Ball(100, random.nextInt(0x00FFFFFF) + 0xFF000000);
    }


    public MyThread getThread() {
        return thread;
    }


    public class MyThread extends Thread {
        private boolean mRun = false;
        private SurfaceHolder mSurfaceHolder;
        public MyThread(SurfaceHolder surfaceHolder) {
            mSurfaceHolder = surfaceHolder;
        }
        public void run() {
            while (mRun) {
                Canvas c = null;
                try {
                    c = mSurfaceHolder.lockCanvas(null); // 캔버스를 얻는다.
                    c.drawColor(Color.BLACK); // 캔버스의 배경을 지운다.
                    synchronized (mSurfaceHolder) {
                        for (Ball b : basket) { // basket의 모든 원소를 그린다.
                            b.draw(c);
                        }
                    }
                } finally {
                    if (c != null) {
                        mSurfaceHolder.unlockCanvasAndPost(c); // 캔버스의 로킹을 푼다.
                    }
                }
            }
        }
        public void setRunning(boolean b) {
            mRun = b;
        }
    }
}
