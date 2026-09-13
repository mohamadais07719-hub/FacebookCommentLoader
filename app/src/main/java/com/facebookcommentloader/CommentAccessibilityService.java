package com.facebookcommentloader;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class CommentAccessibilityService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo root = getRootInActiveWindow();

        if (root == null) {
            return;
        }

        clickMatchingText(root, "عرض المزيد من التعليقات");
        clickMatchingText(root, "عرض المزيد من الردود");
    }

    private boolean clickMatchingText(
            AccessibilityNodeInfo node,
            String text) {

        if (node == null) {
            return false;
        }

        CharSequence nodeText = node.getText();

        if (nodeText != null
                && nodeText.toString().contains(text)
                && node.isClickable()) {

            return node.performAction(
                    AccessibilityNodeInfo.ACTION_CLICK
            );
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
