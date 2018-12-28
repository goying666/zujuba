package renchaigao.com.zujuba.util;

import okhttp3.MediaType;

/**
 * Created by Administrator on 2018/7/24/024.
 */

public class FinalDefine {
    /**
     * 参数类型
     * "text", 文本
     * "image", 图片
     * "audio",音频
     * "video"，视频
     * "object",其他
     */
    public static final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/x-markdown; charset=utf-8");
    public static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
    public static final MediaType MEDIA_TYPE_MUL = MediaType.parse("multipart/form-data");

    public static final MediaType MEDIA_TYPE_AUDIO = MediaType.parse("audio/mp3");
    public static final MediaType MEDIA_TYPE_VIDEO = MediaType.parse("video/mp4");
    public static final MediaType MEDIA_TYPE_OBJECT = MediaType.parse("application/octet-stream");
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
}
