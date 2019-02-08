package com.bdttsdemo;

import com.baidu.tts.client.SpeechSynthesizer;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class VoiceModule extends ReactContextBaseJavaModule {

    private SpeechSynthesizer mSpeechSynthesizer;

    private VoiceUtils utils;

    public VoiceModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @ReactMethod
    public void init(int speaker){
        utils=new VoiceUtils();
        utils.init(getReactApplicationContext(),speaker);
    }

    @ReactMethod
    public void speak(String msg){
        mSpeechSynthesizer=utils.getSyntheszer();
        this.mSpeechSynthesizer.speak(msg);
    }

    @Override
    public String getName() {
        return "VoiceModule";
    }

    @Override
    public void onCatalystInstanceDestroy() {
        this.mSpeechSynthesizer.release();
        super.onCatalystInstanceDestroy();
    }
}
