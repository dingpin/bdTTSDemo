package com.bdttsdemo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class VoiceUtils {
    //////////////////需配置部分////////////////////////
    //AppId
    private String APPID = "15411101";
    //ApiKey
    private String APIKEY = "jguhLMmrFFQuoZdQkslVyENt";
    //SecretKey
    private String SECRETKEY = "Ew96BZnRsX4haOIg09rBvch28uDvuk3R";
    ////////////////////配置结束////////////////////////////////////

    /**
     * 纯在线或者离在线融合
     */
    protected TtsMode ttsMode = TtsMode.MIX;

    /**
     * 初始化的其它参数，用于setParam
     */
    private Map<String, String> params;

    /**
     * 合成引擎的回调
     */
    private SpeechSynthesizer mSpeechSynthesizer;

    private String mSampleDirPath;

    private static final String SAMPLE_DIR_NAME = "baiduTTS";

    protected String offlineVoice = OfflineResource.VOICE_FEMALE;

    public static final String TAG = "ContentValues";

    //初始化
    public void init(Context context, int speaker) {
        initialEnv(context);
        initialTts(context, speaker);
    }

    //获取解析器
    public SpeechSynthesizer getSyntheszer() {
        return mSpeechSynthesizer;
    }

    //初始化配置文件
    private void initialEnv(Context context) {

    }

    //初始化解析器,主要用于WIFI网络下初始化调用
    private void initialTts(Context context, int speaker) {
        boolean isMix = ttsMode.equals(TtsMode.MIX);
        this.mSpeechSynthesizer=SpeechSynthesizer.getInstance();
        mSpeechSynthesizer.setContext(context);
        mSpeechSynthesizer.setSpeechSynthesizerListener(new MyListnener());

        // 请替换为语音开发者平台上注册应用得到的App ID (离线授权)
        this.mSpeechSynthesizer.setAppId(APPID);
        // 请替换为语音开发者平台注册应用得到的apikey和secretkey (在线授权)
        this.mSpeechSynthesizer.setApiKey(APIKEY, SECRETKEY);

        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER,speaker+"");
        // 设置Mix模式的合成策略
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
        // 授权检测接口(只是通过AuthInfo进行检验授权是否成功。)

        Map<String, String> params = getParams(context);

        AuthInfo authInfo = mSpeechSynthesizer.auth(TtsMode.MIX);
        if (!authInfo.isSuccess()) {
            String errorMsg = authInfo.getTtsError().getDetailMessage();
            Log.i(TAG,"errormsg"+errorMsg);
        }else {
            Log.i(TAG,"验证通过，离线正式授权文件存在。");
        }

        // 初始化tts
        mSpeechSynthesizer.initTts(TtsMode.ONLINE);
    }

    protected Map<String, String> getParams(Context context) {

        Map<String, String> params = new HashMap<String, String>();
        // 以下参数均为选填
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        params.put(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置合成的音量，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_VOLUME, "9");
        // 设置合成的语速，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_SPEED, "5");
        // 设置合成的语调，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_PITCH, "5");

        params.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
        // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在

        // 离线资源文件， 从assets目录中复制到临时目录，需要在initTTs方法前完成
        OfflineResource offlineResource = createOfflineResource(offlineVoice,context);
        params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
        params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE,
                offlineResource.getModelFilename());
        return params;
    }

    protected OfflineResource createOfflineResource(String voiceType,Context context) {
        OfflineResource offlineResource = null;
        try {
            offlineResource = new OfflineResource(context, voiceType);
        } catch (IOException e) {
            // IO 错误自行处理
            e.printStackTrace();
//            toPrint("【error】:copy files from assets failed." + e.getMessage());
        }
        return offlineResource;
    }

    //合成监听器
    class MyListnener implements SpeechSynthesizerListener {
        @Override
        public void onSynthesizeStart(String s) {
            //合成准备工作
        }

        @Override
        public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {
            //合成数据和进度的回调接口，分多次回调
        }

        @Override
        public void onSynthesizeFinish(String s) {
            //合成正常结束，每句合成正常结束都会回调，如果过程中出错，则回调onError，不再回调此接口
        }

        @Override
        public void onSpeechStart(String s) {
            //播放开始，每句播放开始都会回调
        }

        @Override
        public void onSpeechProgressChanged(String s, int i) {
            //播放进度回调接口，分多次回调
        }

        @Override
        public void onSpeechFinish(String s) {
            // 播放正常结束，每句播放正常结束都会回调，如果过程中出错，则回调onError,不再回调此接口
        }

        @Override
        public void onError(String s, SpeechError speechError) {
            //当合成或者播放过程中出错时回调此接口
        }
    }
}
