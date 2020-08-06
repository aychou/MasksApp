package com.example.masks;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.masks.GameView.screenRatioX;
import static com.example.masks.GameView.screenRatioY;

public class Germ {
    public int speed = 100;
    public boolean wasShot = true;
    int x, y, width, height, germCounter = 1;
    Bitmap germ1, germ2, germ3, germ4;

    Germ (Resources res) {
        germ1 = BitmapFactory.decodeResource(res, R.drawable.germ1);
        germ2 = BitmapFactory.decodeResource(res, R.drawable.germ2);
        germ3 = BitmapFactory.decodeResource(res, R.drawable.germ4);
        germ4 = BitmapFactory.decodeResource(res, R.drawable.germ4);

        width = germ1.getWidth();
        height = germ1.getHeight();

        width /= 6;
        height /= 6;

        width = (int) (screenRatioX * width);
        height = (int) (screenRatioY * height);

        germ1 = Bitmap.createScaledBitmap(germ1, width, height, false);
        germ2 = Bitmap.createScaledBitmap(germ2, width, height, false);
        germ3 = Bitmap.createScaledBitmap(germ3, width, height, false);
        germ4 = Bitmap.createScaledBitmap(germ4, width, height, false);

        y = -height;
    }
    Bitmap getGerm() {
        if (germCounter == 1) {
            germCounter++;
            return germ1;
        }
        if (germCounter == 1) {
            germCounter++;
            return germ2;
        }
        if (germCounter == 1) {
            germCounter++;
            return germ3;
        }

        germCounter = 1;
        return germ4;
    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }
}
