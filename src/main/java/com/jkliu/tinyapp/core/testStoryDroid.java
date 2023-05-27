package com.jkliu.tinyapp.core;

import com.jkliu.tinyapp.utils.JavaProcessCall;

import java.io.IOException;

public class testStoryDroid {
    public static void main(String[] args) throws IOException {
        String cmd = "/home/jiakun/miniconda3/envs/py27/bin/python /home/jiakun/Projects/TinyAppAll/Related/StoryDroid/code-v2.0/gate.py /home/jiakun/Projects/TinyAppAll/Related/StoryDroid/main-folder/ /home/jiakun/Projects/TinyAppAll/Related/StoryDroid/main-folder/apks/ a2dp.Vol_169.apk";
        JavaProcessCall.execute(cmd);
    }

}
