package com.facebookcommentloader;

import android.accessibilityservice.AccessibilityService;
import android.os.Handler;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class CommentAccessibilityService extends AccessibilityService {

    private final Handler handler = new Handler();

    private boolean running = false;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (!running) {
            return;
        }

        AccessibilityNodeInfo root = getRootInActiveWindow();

        if (root == null) {
            return;
        }

        boolean clicked = false;

        clicked = clickMatchingText(root, "عرض المزيد من التعليقات");

        if (!clicked) {
            clicked = clickMatchingText(root, "عرض المزيد من الردود");
        }

        if (!clicked) {
            scrollDown(root);
        }
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

            AccessibilityNodeInfo clickableNode = node;

            while (clickableNode != null
                    && !clickableNode.isClickable()) {

                clickableNode = clickableNode.getParent();
            }

            if (clickableNode != null) {
                return clickableNode.performAction(
                        AccessibilityNodeInfo.ACTION_CLICK
                );
            }
        }

        for (int i = 0; i < node.getChildCount(); i++) {

            AccessibilityNodeInfo child = node.getChild(i);

            if (child != null
                    && clickMatchingText(child, text)) {

                return true;
            }
        }

        return false;
    }

    private void scrollDown(AccessibilityNodeInfo root) {

        root.performAction(
                AccessibilityNodeInfo.ACTION_SCROLL_FORWARD
        );
    }

    public void startLoader() {
        running = true;
    }

    public void stopLoader() {
        running = false;
    }

    @Override
    public void onInterrupt() {
        running = false;
    }
}            );
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            AccessibilityNodeInfo child = node.getChild(i);

            if (child != null
                    && clickMatchingText(child, text)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onInterrupt() {
    }
}
