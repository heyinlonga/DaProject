package software.ecenter.study.bean.MineBean;

import java.util.List;

/**
 * Created by Mike on 2018/5/16.
 */

public class QusetionLookBean {
    private int questionRole;
    private String questionText;
    private List<ImageLookBean> questionImageList;
    private String questionAudio;
    private boolean isPlaying;
    private String audioDuration;
    private int curDuration;

    public int getCurDuration() {
        return curDuration;
    }

    public void setCurDuration(int curDuration) {
        this.curDuration = curDuration;
    }

    public String getAudioDuration() {
        return audioDuration;
    }

    public void setAudioDuration(String audioDuration) {
        this.audioDuration = audioDuration;
    }

    public int getQuestionRole() {
        return questionRole;
    }

    public void setQuestionRole(int questionRole) {
        this.questionRole = questionRole;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<ImageLookBean> getQuestionImageList() {
        return questionImageList;
    }

    public void setQuestionImageList(List<ImageLookBean> questionImageList) {
        this.questionImageList = questionImageList;
    }

    public String getQuestionAudio() {
        return questionAudio;
    }

    public void setQuestionAudio(String questionAudio) {
        this.questionAudio = questionAudio;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
