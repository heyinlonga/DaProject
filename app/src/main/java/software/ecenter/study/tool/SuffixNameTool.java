package software.ecenter.study.tool;

import android.view.View;

import com.bumptech.glide.Glide;

import software.ecenter.study.R;

/**
 * Created by Mike on 2018/5/12.
 * 后缀名转换
 */

public class SuffixNameTool {

    //本地存储为无后缀名
/*
    //假后缀
    public static String  getSuffixFakeName(String type){

        if("MP3".equals(type)|| "mp3".equals(type)){

            return "xx1";
        }else if("mp4".equals(type) || "MP4".equals(type)){
            return "xx2";

        }else if("jpg".equals(type)){

            return "xx3";
        }else if("png".equals(type)){

            return "xx4";
        }else if("gif".equals(type)){

            return "xx5";
        }else if("doc".equals(type)){
            return "xx6";

        }else if("pdf".equals(type)){
            return "xx7";
        }else if("ppt".equals(type)){
            return "xx8";

        }else if("txt".equals(type)){
            return "xx9";
        }
        return "error";
    }
*/

    //真后缀
    public static String  getSuffixReallyName(String type){

        if(type.contains("MP3")|| type.contains("mp3")){

            return "mp3";
        }else if(type.contains("mp4") || type.contains("MP4")){
            return "mp4";

        }else if(type.contains("jpg")){

            return "jpg";
        }else if(type.contains("png")){

            return "png";
        }else if(type.contains("gif")){

            return "gif";
        }else if(type.contains("doc")){
            return "doc";

        }else if(type.contains("pdf")){
            return "pdf";
        }else if(type.contains("ppt")){
            return "ppt";

        }else if(type.contains("txt")){
            return "txt";
        }
        return "error";
    }


}
