package jp.ac.jec.a16cm0209.game2d;

import android.graphics.Bitmap;

/**
 * Created by nguyenhiep on 2/7/2017 AD.
 */

public abstract class GameObject {
    protected Bitmap image;

    protected final int rowCount;
    protected final int colCount;

    protected final int WIDTH;
    protected final int HEIGHT;

    protected final int width;
    protected final int height;

    protected int x;
    protected int y;

    public GameObject(Bitmap image, int rowCount, int colCount, int x, int y) {
        this.rowCount = rowCount;
        this.colCount = colCount;
        this.image = image;
        this.x = x;
        this.y = y;

        this.WIDTH = image.getWidth();
        this.HEIGHT = image.getHeight();

        this.width = this.WIDTH/ colCount;
        this.height= this.HEIGHT/ rowCount;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    protected Bitmap createSubImageAt(int row, int col){
        //create Bitmap(bitmap, x, y, width, height).
        Bitmap subImage = Bitmap.createBitmap(image, col*width, row*height, width, height);
        return subImage;
    }
}
