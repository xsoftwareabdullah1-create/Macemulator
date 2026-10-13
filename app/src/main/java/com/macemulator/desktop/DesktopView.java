package com.macemulator.desktop;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class DesktopView extends View {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF chromeButton = new RectF();
    private float downX;
    private float downY;

    public DesktopView(Context context) {
        super(context);
        setFocusable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float w = getWidth();
        float h = getHeight();

        paint.setShader(new LinearGradient(0, 0, w, h,
                new int[]{0xFF101D3D, 0xFF5D4B91, 0xFFEC8C83},
                null, Shader.TileMode.CLAMP));
        canvas.drawRect(0, 0, w, h, paint);
        paint.setShader(null);

        // Subtle glowing desktop orbs.
        paint.setColor(0x2278D8FF);
        canvas.drawCircle(w * 0.78f, h * 0.34f, Math.min(w, h) * 0.27f, paint);
        paint.setColor(0x22FFFFFF);
        canvas.drawCircle(w * 0.22f, h * 0.72f, Math.min(w, h) * 0.18f, paint);

        // Translucent menu bar.
        paint.setColor(0xB8202230);
        canvas.drawRoundRect(new RectF(12, 12, w - 12, 54), 18, 18, paint);
        paint.setColor(0xFFFFFFFF);
        paint.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        paint.setTextSize(15);
        canvas.drawText("●  Macemulator", 28, 38, paint);
        paint.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        paint.setTextSize(13);
        canvas.drawText("File    Edit    View    Window", 165, 38, paint);
        String time = "Desktop";
        float timeWidth = paint.measureText(time);
        canvas.drawText(time, w - timeWidth - 30, 38, paint);

        // Welcome card.
        RectF card = new RectF(w * 0.09f, h * 0.22f, w * 0.91f, h * 0.57f);
        paint.setColor(0xD91B2030);
        canvas.drawRoundRect(card, 26, 26, paint);
        paint.setColor(0xFF9CB9FF);
        paint.setTextSize(12);
        paint.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        canvas.drawText("YOUR ANDROID DESKTOP", card.left + 24, card.top + 34, paint);
        paint.setColor(0xFFFFFFFF);
        paint.setTextSize(Math.max(22, Math.min(32, w * 0.065f)));
        canvas.drawText("A desktop, reimagined.", card.left + 24, card.top + 78, paint);
        paint.setColor(0xFFD8DDF0);
        paint.setTextSize(14);
        canvas.drawText("Java-powered • Touch and mouse ready", card.left + 24, card.top + 108, paint);

        // Dock.
        float dockWidth = Math.min(w - 36, 330);
        RectF dock = new RectF((w - dockWidth) / 2, h - 82, (w + dockWidth) / 2, h - 20);
        paint.setColor(0xBCECEFFA);
        canvas.drawRoundRect(dock, 22, 22, paint);

        float iconY = dock.centerY();
        float startX = dock.left + 34;
        String[] icons = {"F", "C", "⚙", "＋"};
        int[] colors = {0xFF6EA9FF, 0xFF4E91F5, 0xFF7A8196, 0xFF7B69D8};
        for (int i = 0; i < icons.length; i++) {
            float x = startX + i * ((dockWidth - 68) / 3f);
            paint.setColor(colors[i]);
            canvas.drawRoundRect(new RectF(x - 17, iconY - 17, x + 17, iconY + 17), 10, 10, paint);
            paint.setColor(0xFFFFFFFF);
            paint.setTextSize(19);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            float tw = paint.measureText(icons[i]);
            canvas.drawText(icons[i], x - tw / 2, iconY + 7, paint);
        }
        chromeButton.set(startX + (dockWidth - 68) / 3f - 22, iconY - 24,
                startX + (dockWidth - 68) / 3f + 22, iconY + 24);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (chromeButton.contains(event.getX(), event.getY())) {
                launchChrome();
            }
            performClick();
            return true;
        }
        return true;
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    private void launchChrome() {
        try {
            Intent intent = getContext().getPackageManager()
                    .getLaunchIntentForPackage("com.android.chrome");
            if (intent == null) {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "Chrome is not available on this device", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(getContext(), "Could not open browser", Toast.LENGTH_SHORT).show();
        }
    }
}
