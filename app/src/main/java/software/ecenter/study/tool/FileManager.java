package software.ecenter.study.tool;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import software.ecenter.study.bean.HomeBean.ResourceBean;

/**
 * Created by Mike on 2018/5/7.
 */

public class FileManager {
    public String RootFilePath;

    private static FileManager manager = null;


    private String tempDir; //临时目录
    private String tempRecordingPath;
    private String documentDir;
    private String loadapkDir;//下载的apk
    // Task任务集合
    public static List<ResourceBean> LocalResourceList = new ArrayList<ResourceBean>(); //本地文件


    public static FileManager getInstance(Context context) {
        if (manager == null) {
            manager = new FileManager();
        }
        return manager;
    }


    private FileManager() {
        init();
    }

    public void init() {
        RootFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "software_ecenter_study_file";
        tempDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "software_ecenter_study_file" + File.separator + "temp";
        documentDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "software_ecenter_study_file" + File.separator + "documentDir";
        loadapkDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "software_ecenter_study_file" + File.separator + "loadapk";
        try {
            FileAccessor.makeDirs(RootFilePath);
            FileAccessor.makeDirs(tempDir);
            FileAccessor.makeDirs(documentDir);
            FileAccessor.makeDirs(loadapkDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initLocalResourceList() {
        LocalResourceList.clear();
        File file = new File(documentDir);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {


                for (File f : files) {
                    if (f.isDirectory()) {
                        File[] fileItems = f.listFiles();
                        for (File item : fileItems) {
                            if (item.getName().endsWith(".info")) {
                                ResourceBean bean = JsonTool.jsonToResourceBean(FileAccessor.readString(item));
//                                bean.setResourceSize(String.valueOf(FileAccessor.getFileSize(file.getPath())));
                                LocalResourceList.add(bean);
                            }

                        }
                    }
                }
            }
        }

    }


    public String getTempDir() {
        return tempDir;
    }

    public void setTempDir(String tempDir) {
        this.tempDir = tempDir;
    }

    public String createTempRecordingPath() {
        tempRecordingPath = tempDir + File.separator + System.currentTimeMillis() + ".mp3";
        return tempRecordingPath;
    }

    public String getDocumentDir() {
        return documentDir;
    }

    public String getLoadapkDir() {
        return loadapkDir;
    }
}
