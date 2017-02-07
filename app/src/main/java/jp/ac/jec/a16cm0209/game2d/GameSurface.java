package jp.ac.jec.a16cm0209.game2d;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by nguyenhiep on 2/7/2017 AD.
 */

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {
    private GameThread gameThread;

    private final List<ChibiCharacter> chibiList = new ArrayList<ChibiCharacter>();
    private final List<Explosion> explosionList = new ArrayList<Explosion>();

    private static final int MAX_STREAMS=100;
    private int soundIdExplosion;
    private int soundIdBackground;

    private boolean soundPoolLoaded;
    private SoundPool soundPool;



    public GameSurface(Context context)  {
        super(context);

        //Dam bao Game Surface co the focus de dieu khien cac su kien.
        this.setFocusable(true);

        //Set cac su kien lien quan toi Game.
        this.getHolder().addCallback(this);

        this.initSoundPool();
    }

    private void initSoundPool()  {
        //Voi phien ban Android API >= 21
        if (Build.VERSION.SDK_INT >= 21 ) {

            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            SoundPool.Builder builder= new SoundPool.Builder();
            builder.setAudioAttributes(audioAttrib).setMaxStreams(MAX_STREAMS);

            this.soundPool = builder.build();
        }
        //Voi phien ban Android API < 21
        else {
            // SoundPool(int maxStreams, int streamType, int srcQuality)
            this.soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }


        //Su kien SoundPool da tai len bo nho thanh cong.
        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPoolLoaded = true;

                //Phat nhac nen
                playSoundBackground();
            }
        });

        //Tai file am thanh tieng no (background.mp3) vao SoundPool.
        this.soundIdBackground= this.soundPool.load(this.getContext(), R.raw.background,1);

        //Tai file am thanh tieng no (explosion.wav) vao SoundPool.
        this.soundIdExplosion = this.soundPool.load(this.getContext(), R.raw.explosion,1);


    }

    public void playSoundExplosion()  {
        if(this.soundPoolLoaded) {
            float leftVolumn = 0.8f;
            float rightVolumn =  0.8f;

            //Phat am thanh explosion.wav
            int streamId = this.soundPool.play(this.soundIdExplosion,leftVolumn, rightVolumn, 1, 0, 1f);
        }
    }

    public void playSoundBackground()  {
        if(this.soundPoolLoaded) {
            float leftVolumn = 0.8f;
            float rightVolumn =  0.8f;

            //Phat am thanh background.mp3
            int streamId = this.soundPool.play(this.soundIdBackground,leftVolumn, rightVolumn, 1, -1, 1f);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            int x=  (int)event.getX();
            int y = (int)event.getY();

            Iterator<ChibiCharacter> iterator= this.chibiList.iterator();

            //Kiem tra xem co click vao nhan vat nao khong.
            while(iterator.hasNext()) {
                ChibiCharacter chibi = iterator.next();
                if( chibi.getX() < x && x < chibi.getX() + chibi.getWidth()
                        && chibi.getY() < y && y < chibi.getY()+ chibi.getHeight())  {

                    //Loai bo nhan vat Game hien tai ra khoi iterator va list.
                    iterator.remove();

                    //Tao moi 1 doi tuong Explosion.
                    Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.explosion);
                    Explosion explosion = new Explosion(this, bitmap,chibi.getX(),chibi.getY());

                    this.explosionList.add(explosion);
                }
            }


            for(ChibiCharacter chibi: chibiList) {
                int movingVectorX = x-  chibi.getX() ;
                int movingVectorY = y-  chibi.getY() ;
                chibi.setMovingVector(movingVectorX, movingVectorY);
            }
            return true;
        }
        return false;
    }

    public void update()  {
        for(ChibiCharacter chibi: chibiList) {
            chibi.update();
        }
        for(Explosion explosion: this.explosionList)  {
            explosion.update();
        }

        Iterator<Explosion> iterator= this.explosionList.iterator();
        while(iterator.hasNext())  {
            Explosion explosion = iterator.next();

            if(explosion.isFinish()) {
                //Neu explosion da hoan thanh, loai no ra khoi iterator & list(Loai bo phan tu hien thoi gia khoi vong lap).
                iterator.remove();
                continue;
            }
        }
    }

    @Override
    public void draw(Canvas canvas)  {
        super.draw(canvas);

        for(ChibiCharacter chibi: chibiList)  {
            chibi.draw(canvas);
        }

        for(Explosion explosion: this.explosionList)  {
            explosion.draw(canvas);
        }

    }


    //Thi hanh phuong thuc cua interface SurfaceHolder.Callback
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Bitmap chibiBitmap1 = BitmapFactory.decodeResource(this.getResources(),R.drawable.chibi1);
        ChibiCharacter chibi1 = new ChibiCharacter(this,chibiBitmap1,100,50);

        Bitmap chibiBitmap2 = BitmapFactory.decodeResource(this.getResources(),R.drawable.chibi2);
        ChibiCharacter chibi2 = new ChibiCharacter(this,chibiBitmap2,300,150);

        this.chibiList.add(chibi1);
        this.chibiList.add(chibi2);

        this.gameThread = new GameThread(this,holder);
        this.gameThread.setRunning(true);
        this.gameThread.start();
    }

    //Thi hanh phuong thuc cua interface SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }


    //Thi hanh phuong thuc cua interface SurfaceHolder.Callback
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry= true;
        while(retry) {
            try {
                this.gameThread.setRunning(false);

                // Luong cha, can phai tam dung GameThread ket thuc.
                this.gameThread.join();
            }catch(InterruptedException e)  {
                e.printStackTrace();
            }
            retry= true;
        }
    }
}