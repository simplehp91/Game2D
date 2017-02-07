package jp.ac.jec.a16cm0209.game2d;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set Fullscren
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Loai bo tieu de
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Set giao dien cua Activity.
        this.setContentView(new GameSurface(this));
    }
}
