package software.ecenter.study.tool;

import com.bumptech.glide.load.engine.Resource;
import com.google.gson.Gson;

import software.ecenter.study.bean.HomeBean.BookBean;
import software.ecenter.study.bean.HomeBean.CurriculumBean;
import software.ecenter.study.bean.HomeBean.ResourceBean;

/**
 * Created by Mike on 2018/5/12.
 */

public class JsonTool {


    public static String bookBeanToJson(BookBean book){
        Gson gson = new Gson();
        String bookInfo  = gson.toJson(book);
        return  bookInfo;
    }


    public static BookBean jsonToBookBean(String json) {
        Gson gson = new Gson();
        BookBean bookBean = gson.fromJson(json, BookBean.class);
        return  bookBean;
    }

    public static String curriculumBeanToJson(CurriculumBean curriculumBean){
        Gson gson = new Gson();
        String curriculumInfo  = gson.toJson(curriculumBean);
        return  curriculumInfo;
    }


    public static CurriculumBean jsonToCurriculumBean(String json) {
        Gson gson = new Gson();
        CurriculumBean curriculumBean = gson.fromJson(json, CurriculumBean.class);
        return  curriculumBean;
    }

    public static String resourceBeanToJson(ResourceBean resourceBean){
        Gson gson = new Gson();
        String resourceInfo  = gson.toJson(resourceBean);
        return  resourceInfo;
    }


    public static ResourceBean jsonToResourceBean(String json) {
        Gson gson = new Gson();
        ResourceBean resourceBean = gson.fromJson(json, ResourceBean.class);
        return  resourceBean;
    }

}
