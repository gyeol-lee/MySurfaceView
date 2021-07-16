package kr.ac.sme.pknu.mysurfaceview;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Ball {
    // 한 번에 움직이는 거리
    int x, y, xInc = 1, yInc = 1;
    int diameter;
    static int WIDTH , HEIGHT ; //전체 공간
    int color;



    public Ball(int d, int c) { // Ball 객체의 입력변수를 늘려 색을 정하게한다.
        this.diameter = d;
        this.color = c;
        // 볼의 위치를 랜덤하게 설정
        x = (int) (Math.random() * (WIDTH - d) + 3);
        y = (int) (Math.random() * (HEIGHT - d) + 3);
        // 한번에 움직이는 거리도 랜덤하게 설정
        xInc = (int) (Math.random() * 30 + 1);
        yInc = (int) (Math.random() * 30 + 1);
    }

    // 여기서 공을 그린다.
    public void draw(Canvas g) {
        Paint paint = new Paint();
        // 벽이 부딪치면 반사하게 한다.
        if (x < diameter || x > (WIDTH - diameter))
            xInc = -xInc;
        if (y < diameter || y > (HEIGHT - diameter))
            yInc = -yInc;
        // 볼의 좌표를 갱신한다.
        x += xInc;
        y += yInc;
        // 볼을 화면에 그린다.
        paint.setColor(color);
        g.drawCircle(x, y, diameter, paint);
    }
}
