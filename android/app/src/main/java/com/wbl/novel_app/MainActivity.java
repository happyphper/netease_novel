package com.wbl.novel_app;

import android.content.Intent;

import androidx.annotation.NonNull;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;

public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "samples.mob/video";

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
                .setMethodCallHandler(
                        (call, result) -> {
                            if (call.method.equals("showVideo")) {
                                Intent intent = new Intent(MainActivity.this, NoAccountSystemExampleActivity.class);
                                startActivity(intent);
                                result.success(0);
                            } else {
                                result.notImplemented();
                            }
                        }
                );
    }
}
