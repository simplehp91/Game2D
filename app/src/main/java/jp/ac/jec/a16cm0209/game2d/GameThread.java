package jp.ac.jec.a16cm0209.game2d;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by nguyenhiep on 2/7/2017 AD.
 */

public class GameThread extends Thread {

    private boolean running;
    private GameSurface gameSurface;
    private SurfaceHolder surfaceHolder;

    public GameThread(GameSurface gameSurface, SurfaceHolder surfaceHolder)  {
        this.gameSurface= gameSurface;
        this.surfaceHolder= surfaceHolder;
    }

    @Override
    public void run()  {
        long startTime = System.nanoTime();

        while(running)  {
            Canvas canvas= null;
            try {
                //Lay ra doi tuong Canvas va khoa no lai.
                canvas = this.surfaceHolder.lockCanvas();


                //Dong bo hoa.
                synchronized (canvas)  {
                    this.gameSurface.update();
                    this.gameSurface.draw(canvas);
                }
            }catch(Exception e)  {
                //No action
            } finally {
                if(canvas!= null)  {
                    //Mo khoa cho Canvas.
                    this.surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
            long now = System.nanoTime() ;
            // Thoi gian cap nhap lai Game
            // (Doi tu Nanosecond ra milisecond).
            long waitTime = (now - startTime)/1000000;
            if(waitTime < 10)  {
                waitTime = 10; // Millisecond.
            }
            System.out.print(" Wait Time="+ waitTime);

            try {
                //Stop game.
                this.sleep(waitTime);
            } catch(InterruptedException e)  {

            }
            startTime = System.nanoTime();
            System.out.print(".");
        }
    }

    public void setRunning(boolean running)  {
        this.running= running;
    }
}
