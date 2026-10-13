package com.macemulator.desktop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/** A small Java-only desktop shell. Browser content runs in Android WebView. */
public class DesktopView extends FrameLayout {
    private final int white = Color.WHITE;
    private final int ink = Color.rgb(26, 31, 48);
    private final FrameLayout windowsLayer;
    private int nextWindowOffset = 0;
    private TextView clockView;

    public DesktopView(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);

        BackgroundView background = new BackgroundView(context);
        addView(background, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        windowsLayer = new FrameLayout(context);
        addView(windowsLayer, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        buildMenuBar(context);
        buildDesktopShortcuts(context);
        buildDock(context);
        post(new Runnable() {
            @Override public void run() {
                if (clockView != null) {
                    clockView.setText(new SimpleDateFormat("EEE  h:mm a", Locale.getDefault()).format(new Date()));
                    postDelayed(this, 30000L);
                }
            }
        });
    }

    private void buildMenuBar(Context context) {
        LinearLayout bar = new LinearLayout(context);
        bar.setGravity(Gravity.CENTER_VERTICAL);
        bar.setPadding(dp(16), 0, dp(16), 0);
        bar.setBackground(roundDrawable(0xC9181D2B, 22));
        TextView brand = label("●  Macemulator", 14, white, true);
        bar.addView(brand, new LinearLayout.LayoutParams(0, -1, 1));
        TextView menus = label("File    Edit    View    Window", 12, 0xFFE4E8F3, false);
        bar.addView(menus);
        clockView = label("Desktop", 12, white, false);
        LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(-2, -1);
        timeParams.leftMargin = dp(14);
        bar.addView(clockView, timeParams);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, dp(44), Gravity.TOP);
        params.setMargins(dp(10), dp(10), dp(10), 0);
        addView(bar, params);
    }

    private void buildDesktopShortcuts(Context context) {
        LinearLayout shortcuts = new LinearLayout(context);
        shortcuts.setOrientation(LinearLayout.VERTICAL);
        shortcuts.setGravity(Gravity.CENTER_HORIZONTAL);
        shortcuts.setPadding(dp(8), dp(8), dp(8), dp(8));
        shortcuts.setBackground(roundDrawable(0x33151B2B, 16));
        TextView finder = label("📁\nFiles", 14, white, true);
        finder.setGravity(Gravity.CENTER);
        finder.setPadding(dp(8), dp(8), dp(8), dp(8));
        finder.setOnClickListener(v -> openFilesWindow());
        shortcuts.addView(finder);
        TextView browser = label("🌐\nBrowser", 14, white, true);
        browser.setGravity(Gravity.CENTER);
        browser.setPadding(dp(8), dp(8), dp(8), dp(8));
        browser.setOnClickListener(v -> openBrowserWindow());
        shortcuts.addView(browser);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(dp(92), -2, Gravity.TOP | Gravity.START);
        params.setMargins(dp(16), dp(76), 0, 0);
        addView(shortcuts, params);
    }

    private void buildDock(Context context) {
        LinearLayout dock = new LinearLayout(context);
        dock.setGravity(Gravity.CENTER);
        dock.setOrientation(LinearLayout.HORIZONTAL);
        dock.setPadding(dp(10), dp(8), dp(10), dp(8));
        dock.setBackground(roundDrawable(0xDDEBF0FA, 24));

        addDockItem(dock, "⌂", "Home", 0xFF5A82D8, this::showWelcome);
        addDockItem(dock, "📁", "Files", 0xFF4389D8, this::openFilesWindow);
        addDockItem(dock, "🌐", "Chrome", 0xFF3985E8, this::openBrowserWindow);
        addDockItem(dock, "⚙", "Settings", 0xFF7B70C8, this::openSettingsWindow);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-2, dp(66), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        params.setMargins(dp(10), 0, dp(10), dp(16));
        addView(dock, params);
    }

    private void addDockItem(LinearLayout dock, String icon, String title, int color, Runnable action) {
        LinearLayout item = new LinearLayout(getContext());
        item.setGravity(Gravity.CENTER);
        item.setOrientation(LinearLayout.VERTICAL);
        item.setPadding(dp(10), 0, dp(10), 0);
        TextView iconView = label(icon, 23, white, true);
        iconView.setGravity(Gravity.CENTER);
        iconView.setBackground(roundDrawable(color, 13));
        item.addView(iconView, new LinearLayout.LayoutParams(dp(40), dp(38)));
        TextView caption = label(title, 9, 0xFF202638, true);
        caption.setGravity(Gravity.CENTER);
        item.addView(caption, new LinearLayout.LayoutParams(-1, dp(16)));
        item.setOnClickListener(v -> action.run());
        dock.addView(item);
    }

    private void showWelcome() {
        Toast.makeText(getContext(), "Welcome to your desktop", Toast.LENGTH_SHORT).show();
    }

    private void openFilesWindow() {
        LinearLayout body = verticalPanel();
        TextView heading = label("Your places", 20, ink, true);
        body.addView(heading);
        body.addView(infoRow("🏠", "Home", "Local app space"));
        body.addView(infoRow("📄", "Documents", "Demo location"));
        body.addView(infoRow("⬇", "Downloads", "Demo location"));
        body.addView(infoRow("🖼", "Pictures", "Demo location"));
        TextView note = label("This is a UI preview. File access will be added with Android's Storage Access Framework.", 12, 0xFF677087, false);
        note.setPadding(0, dp(14), 0, 0);
        body.addView(note);
        createWindow("Finder", body, dp(300), dp(340));
    }

    private View infoRow(String icon, String title, String subtitle) {
        LinearLayout row = new LinearLayout(getContext());
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(dp(12), dp(10), dp(12), dp(10));
        row.setBackground(roundDrawable(0xFFF0F3FA, 14));
        TextView glyph = label(icon, 22, ink, true);
        row.addView(glyph, new LinearLayout.LayoutParams(dp(42), dp(42)));
        LinearLayout text = new LinearLayout(getContext());
        text.setOrientation(LinearLayout.VERTICAL);
        text.addView(label(title, 14, ink, true));
        text.addView(label(subtitle, 11, 0xFF6B7385, false));
        row.addView(text, new LinearLayout.LayoutParams(0, -2, 1));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        params.topMargin = dp(8);
        row.setLayoutParams(params);
        return row;
    }

    private void openSettingsWindow() {
        LinearLayout body = verticalPanel();
        body.addView(label("Desktop settings", 20, ink, true));
        body.addView(infoRow("🖱", "Pointer", "Use touch or a connected mouse"));
        body.addView(infoRow("🌙", "Appearance", "Mac-inspired dark glass"));
        body.addView(infoRow("🌐", "Browser", "In-app Android WebView"));
        body.addView(infoRow("⚙", "Native engine", NativeBridge.engineInfo()));
        body.addView(infoRow("C#", ".NET helper", "Separate module; not packaged in this APK"));
        createWindow("Settings", body, dp(310), dp(390));
    }

    private void openBrowserWindow() {
        LinearLayout body = new LinearLayout(getContext());
        body.setOrientation(LinearLayout.VERTICAL);
        body.setBackgroundColor(Color.WHITE);

        LinearLayout toolbar = new LinearLayout(getContext());
        toolbar.setGravity(Gravity.CENTER_VERTICAL);
        toolbar.setPadding(dp(8), dp(7), dp(8), dp(7));
        toolbar.setBackgroundColor(0xFFF0F2F7);

        EditText address = new EditText(getContext());
        address.setSingleLine(true);
        address.setText("https://www.google.com");
        address.setHint("Search or enter website");
        address.setTextSize(13);
        address.setPadding(dp(10), 0, dp(10), 0);
        address.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_URI);
        address.setImeOptions(EditorInfo.IME_ACTION_GO);
        address.setBackground(roundDrawable(Color.WHITE, 18));
        toolbar.addView(address, new LinearLayout.LayoutParams(0, dp(42), 1));

        WebView webView = new WebView(getContext());
        webView.setBackgroundColor(Color.WHITE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        Runnable navigate = () -> {
            String raw = address.getText().toString().trim();
            if (raw.isEmpty()) return;
            String target = raw;
            if (!target.contains("://")) {
                if (target.contains(" ") || !target.contains(".")) {
                    target = "https://www.google.com/search?q=" + UriEncode.encode(raw);
                } else {
                    target = "https://" + target;
                }
            }
            webView.loadUrl(target);
        };
        Button go = smallButton("Go");
        toolbar.addView(go, new LinearLayout.LayoutParams(dp(48), dp(42)));
        go.setOnClickListener(v -> navigate.run());
        address.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE) {
                navigate.run();
                return true;
            }
            return false;
        });

        LinearLayout controls = new LinearLayout(getContext());
        controls.setGravity(Gravity.CENTER_VERTICAL);
        controls.setPadding(dp(8), 0, dp(8), dp(4));
        Button back = smallButton("‹");
        Button forward = smallButton("›");
        Button reload = smallButton("↻");
        Button home = smallButton("⌂");
        controls.addView(back);
        controls.addView(forward);
        controls.addView(reload);
        controls.addView(home);
        back.setOnClickListener(v -> { if (webView.canGoBack()) webView.goBack(); });
        forward.setOnClickListener(v -> { if (webView.canGoForward()) webView.goForward(); });
        reload.setOnClickListener(v -> webView.reload());
        home.setOnClickListener(v -> { address.setText("https://www.google.com"); navigate.run(); });

        body.addView(toolbar);
        body.addView(controls);
        body.addView(webView, new LinearLayout.LayoutParams(-1, 0, 1));
        webView.setWebViewClient(new WebViewClient() {
            @Override public void onPageFinished(WebView view, String url) {
                address.setText(url);
            }
        });
        createWindow("Browser · WebView", body, Math.min(getWidth() - dp(28), dp(520)),
                Math.min(getHeight() - dp(130), dp(560)));
        webView.loadUrl("https://www.google.com");
    }

    private void createWindow(String title, View content, int requestedWidth, int requestedHeight) {
        int maxWidth = Math.max(dp(260), getWidth() - dp(24));
        int maxHeight = Math.max(dp(260), getHeight() - dp(120));
        int width = Math.min(requestedWidth > 0 ? requestedWidth : dp(340), maxWidth);
        int height = Math.min(requestedHeight > 0 ? requestedHeight : dp(380), maxHeight);

        LinearLayout window = new LinearLayout(getContext());
        window.setOrientation(LinearLayout.VERTICAL);
        window.setBackground(roundDrawable(0xFFF9FAFE, 20));
        window.setElevation(dp(16));
        window.setClipToOutline(true);

        LinearLayout titleBar = new LinearLayout(getContext());
        titleBar.setGravity(Gravity.CENTER_VERTICAL);
        titleBar.setPadding(dp(10), 0, dp(8), 0);
        titleBar.setBackground(roundDrawable(0xFFE6EAF3, 20));
        TextView dots = label("●  ●  ●", 12, 0xFFDF625E, true);
        titleBar.addView(dots);
        TextView titleView = label(title, 12, ink, true);
        titleView.setGravity(Gravity.CENTER);
        titleBar.addView(titleView, new LinearLayout.LayoutParams(0, dp(42), 1));
        Button minimize = windowButton("−");
        Button maximize = windowButton("□");
        Button close = windowButton("×");
        titleBar.addView(minimize);
        titleBar.addView(maximize);
        titleBar.addView(close);
        window.addView(titleBar, new LinearLayout.LayoutParams(-1, dp(44)));

        FrameLayout bodyFrame = new FrameLayout(getContext());
        bodyFrame.addView(content, new FrameLayout.LayoutParams(-1, -1));
        TextView resizeGrip = label("◢", 13, 0xFF7C8496, true);
        resizeGrip.setGravity(Gravity.CENTER);
        resizeGrip.setBackground(roundDrawable(0xFFE8EBF2, 6));
        FrameLayout.LayoutParams gripParams = new FrameLayout.LayoutParams(dp(24), dp(24), Gravity.BOTTOM | Gravity.RIGHT);
        gripParams.setMargins(0, 0, dp(4), dp(4));
        bodyFrame.addView(resizeGrip, gripParams);
        window.addView(bodyFrame, new LinearLayout.LayoutParams(-1, 0, 1));

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height, Gravity.TOP | Gravity.START);
        int offset = dp(18) * (nextWindowOffset++ % 5);
        params.leftMargin = Math.max(dp(8), (getWidth() - width) / 2 + offset - dp(36));
        params.topMargin = Math.max(dp(62), (getHeight() - height) / 2 + offset - dp(10));
        windowsLayer.addView(window, params);
        window.bringToFront();
        titleBar.bringToFront();

        final int[] drag = new int[4];
        titleBar.setOnTouchListener((v, event) -> {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) window.getLayoutParams();
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                drag[0] = (int) event.getRawX();
                drag[1] = (int) event.getRawY();
                drag[2] = lp.leftMargin;
                drag[3] = lp.topMargin;
                window.bringToFront();
                return true;
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                int dx = (int) event.getRawX() - drag[0];
                int dy = (int) event.getRawY() - drag[1];
                lp.leftMargin = Math.max(0, Math.min(getWidth() - window.getWidth(), drag[2] + dx));
                lp.topMargin = Math.max(dp(54), Math.min(getHeight() - dp(82), drag[3] + dy));
                window.setLayoutParams(lp);
                return true;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                return true;
            }
            return true;
        });

        final int[] resizeStart = new int[4];
        resizeGrip.setOnTouchListener((v, event) -> {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) window.getLayoutParams();
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                resizeStart[0] = (int) event.getRawX();
                resizeStart[1] = (int) event.getRawY();
                resizeStart[2] = lp.width;
                resizeStart[3] = lp.height;
                window.bringToFront();
                return true;
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                int dx = (int) event.getRawX() - resizeStart[0];
                int dy = (int) event.getRawY() - resizeStart[1];
                lp.width = NativeBridge.clampDimension(resizeStart[2] + dx, dp(250),
                        Math.max(dp(250), getWidth() - lp.leftMargin));
                lp.height = NativeBridge.clampDimension(resizeStart[3] + dy, dp(200),
                        Math.max(dp(200), getHeight() - lp.topMargin));
                window.setLayoutParams(lp);
                return true;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                return true;
            }
            return true;
        });

        minimize.setOnClickListener(v -> window.setVisibility(View.GONE));
        close.setOnClickListener(v -> {
            if (content instanceof LinearLayout) {
                releaseWebViews((ViewGroup) content);
            }
            windowsLayer.removeView(window);
        });
        final boolean[] maximized = {false};
        final int[] restore = {params.leftMargin, params.topMargin, width, height};
        maximize.setOnClickListener(v -> {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) window.getLayoutParams();
            if (!maximized[0]) {
                restore[0] = lp.leftMargin; restore[1] = lp.topMargin;
                restore[2] = lp.width; restore[3] = lp.height;
                lp.leftMargin = dp(8); lp.topMargin = dp(58);
                lp.width = Math.max(dp(250), getWidth() - dp(16));
                lp.height = Math.max(dp(250), getHeight() - dp(136));
            } else {
                lp.leftMargin = restore[0]; lp.topMargin = restore[1];
                lp.width = restore[2]; lp.height = restore[3];
            }
            maximized[0] = !maximized[0];
            window.setLayoutParams(lp);
        });
    }

    private void releaseWebViews(ViewGroup group) {
        for (int i = 0; i < group.getChildCount(); i++) {
            View child = group.getChildAt(i);
            if (child instanceof WebView) {
                ((WebView) child).stopLoading();
                ((WebView) child).destroy();
            } else if (child instanceof ViewGroup) {
                releaseWebViews((ViewGroup) child);
            }
        }
    }

    private LinearLayout verticalPanel() {
        LinearLayout panel = new LinearLayout(getContext());
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setPadding(dp(16), dp(16), dp(16), dp(16));
        panel.setBackgroundColor(0xFFF9FAFE);
        return panel;
    }

    private Button smallButton(String text) {
        Button button = new Button(getContext());
        button.setText(text);
        button.setTextSize(12);
        button.setMinWidth(0);
        button.setMinimumWidth(0);
        button.setPadding(dp(6), 0, dp(6), 0);
        return button;
    }

    private Button windowButton(String text) {
        Button button = new Button(getContext());
        button.setText(text);
        button.setTextSize(12);
        button.setTextColor(ink);
        button.setMinWidth(dp(30));
        button.setMinimumWidth(dp(30));
        button.setPadding(0, 0, 0, 0);
        button.setBackgroundColor(Color.TRANSPARENT);
        return button;
    }

    private TextView label(String text, int sp, int color, boolean bold) {
        TextView view = new TextView(getContext());
        view.setText(text);
        view.setTextSize(sp);
        view.setTextColor(color);
        view.setTypeface(Typeface.create("sans-serif", bold ? Typeface.BOLD : Typeface.NORMAL));
        view.setGravity(Gravity.CENTER_VERTICAL);
        return view;
    }

    private GradientDrawable roundDrawable(int color, int radiusDp) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radiusDp));
        return drawable;
    }

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private static class UriEncode {
        static String encode(String value) {
            try { return java.net.URLEncoder.encode(value, "UTF-8"); }
            catch (Exception ignored) { return value; }
        }
    }

    private class BackgroundView extends View {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        BackgroundView(Context context) { super(context); setLayerType(View.LAYER_TYPE_SOFTWARE, null); }
        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            float w = getWidth(), h = getHeight();
            paint.setShader(new LinearGradient(0, 0, w, h,
                    new int[]{0xFF0B1633, 0xFF344D83, 0xFF7964A4, 0xFFE6A18F},
                    null, Shader.TileMode.CLAMP));
            canvas.drawRect(0, 0, w, h, paint);
            paint.setShader(null);
            paint.setColor(0x2278D8FF);
            canvas.drawCircle(w * .78f, h * .34f, Math.min(w, h) * .29f, paint);
            paint.setColor(0x20FFFFFF);
            canvas.drawCircle(w * .20f, h * .74f, Math.min(w, h) * .20f, paint);
            paint.setColor(0x24FFB7D5);
            canvas.drawCircle(w * .57f, h * .88f, Math.min(w, h) * .15f, paint);
            paint.setColor(0x16FFFFFF);
            for (int i = 0; i < 28; i++) {
                float x = (i * 97f) % Math.max(1, w);
                float y = (i * 151f) % Math.max(1, h);
                canvas.drawCircle(x, y, (i % 3) + 1, paint);
            }
        }
    }
}
