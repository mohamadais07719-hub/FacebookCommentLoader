package com.facebookcommentloader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 60, 40, 40);

        TextView title = new TextView(this);
        title.setText("Facebook Comment Loader");
        title.setTextSize(24);

        TextView info = new TextView(this);
        info.setText("افتح منشور Facebook ثم اضغط تشغيل.");
        info.setTextSize(17);

        Button accessibilityButton = new Button(this);
        accessibilityButton.setText("إعدادات إمكانية الوصول");

        accessibilityButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    Settings.ACTION_ACCESSIBILITY_SETTINGS
            );
            startActivity(intent);
        });

        Button startButton = new Button(this);
        startButton.setText("تشغيل");

        startButton.setOnClickListener(v -> {
            CommentAccessibilityService.startLoader();
            info.setText("الخدمة تعمل الآن...");
        });

        Button stopButton = new Button(this);
        stopButton.setText("إيقاف");

        stopButton.setOnClickListener(v -> {
            CommentAccessibilityService.stopLoader();
            info.setText("تم إيقاف الخدمة.");
        });

        layout.addView(title);
        layout.addView(info);
        layout.addView(accessibilityButton);
        layout.addView(startButton);
        layout.addView(stopButton);

        setContentView(layout);
    }
}            );
            startActivity(intent);
        });

        layout.addView(title);
        layout.addView(info);
        layout.addView(accessibilityButton);

        setContentView(layout);
    }
}            );
            startActivity(intent);
        });

        layout.addView(title);
        layout.addView(info);
        layout.addView(settingsButton);

        setContentView(layout);
    }
}
