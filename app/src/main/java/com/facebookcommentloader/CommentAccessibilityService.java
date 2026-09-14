package com.facebookcommentloader;

import android.accessibilityservice.AccessibilityService;
import android.os.Handler;
import android.os.Looper;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class CommentAccessibilityService extends AccessibilityService {

    private static CommentAccessibilityService instance;

    private final Handler handler = new Handler(Looper.getMainLooper());

    private boolean running = false;

    private final Runnable worker = new Runnable() {
        @Override
        public void run() {

            if (!running) {
                return;
            }

            AccessibilityNodeInfo root = getRootInActiveWindow();

            if (root != null) {

                boolean clicked = false;

                // التعليقات
                clicked = clickMatchingText(
                        root,
                        "عرض المزيد من التعليقات"
                );

                // الردود
                if (!clicked) {
                    clicked = clickMatchingText(
                            root,
                            "عرض المزيد من الردود"
                    );
                }

                // الصيغة الظاهرة في Facebook
                if (!clicked) {
                    clicked = clickMatchingText(
                            root,
                            "عرض الردود السابقة"
                    );
                }

                if (!clicked) {
                    clicked = clickMatchingText(
                            root,
                            "عرض التعليقات السابقة"
                    );
                }

                // صيغ إنجليزية احتياطية
                if (!clicked) {
                    clicked = clickMatchingText(
                            root,
                            "View more comments"
                    );
                }

                if (!clicked) {
                    clicked = clickMatchingText(
                            root,
                            "View more replies"
                    );
                }

                if (!clicked) {
                    clicked = clickMatchingText(
                            root,
                            "View previous replies"
                    );
                }

                if (!clicked) {
                    clicked = clickMatchingText(
                            root,
                            "View previous comments"
                    );
                }

                // إذا لم نجد زرًا، نمرر للأسفل
                if (!clicked) {
                    scrollScrollableNode(root);
                }
            }

            handler.postDelayed(this, 1200);
        }
    };

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();

        instance = this;

        if (running) {
            handler.removeCallbacks(worker);
            handler.post(worker);
        }
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // العمل الرئيسي يتم بواسطة worker
    }

    public static boolean startLoader() {

        if (instance == null) {
            return false;
        }

        instance.running = true;

        instance.handler.removeCallbacks(instance.worker);
        instance.handler.post(instance.worker);

        return true;
    }

    public static void stopLoader() {

        if (instance == null) {
            return;
        }

        instance.running = false;
        instance.handler.removeCallbacks(instance.worker);
    }

    private boolean clickMatchingText(
            AccessibilityNodeInfo node,
            String text) {

        if (node == null) {
            return false;
        }

        CharSequence nodeText = node.getText();

        if (nodeText != null
                && nodeText.toString().contains(text)) {

            AccessibilityNodeInfo target = node;

            // نص الزر قد يكون داخل عنصر قابل للضغط
            while (target != null && !target.isClickable()) {
                target = target.getParent();
            }

            if (target != null) {
                return target.performAction(
                        AccessibilityNodeInfo.ACTION_CLICK
                );
            }
        }

        CharSequence nodeDescription = node.getContentDescription();

        if (nodeDescription != null
                && nodeDescription.toString().contains(text)) {

            AccessibilityNodeInfo target = node;

            while (target != null && !target.isClickable()) {
                target = target.getParent();
            }

            if (target != null) {
                return target.performAction(
                        AccessibilityNodeInfo.ACTION_CLICK
                );
            }
        }

        for (int i = 0; i < node.getChildCount(); i++) {

            AccessibilityNodeInfo child = node.getChild(i);

            if (child != null) {

                if (clickMatchingText(child, text)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean scrollScrollableNode(
            AccessibilityNodeInfo node) {

        if (node == null) {
            return false;
        }

        if (node.isScrollable()) {

            boolean result = node.performAction(
                    AccessibilityNodeInfo.ACTION_SCROLL_FORWARD
            );

            if (result) {
                return true;
            }
        }

        for (int i = 0; i < node.getChildCount(); i++) {

            AccessibilityNodeInfo child = node.getChild(i);

            if (child != null) {

                if (scrollScrollableNode(child)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void onInterrupt() {

        running = false;
        handler.removeCallbacks(worker);
    }

    @Override
    public void onDestroy() {

        running = false;
        handler.removeCallbacks(worker);

        if (instance == this) {
            instance = null;
        }

        super.onDestroy();
    }
}
