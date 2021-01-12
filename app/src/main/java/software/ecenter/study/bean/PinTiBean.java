package software.ecenter.study.bean;

import java.io.Serializable;

/**
 * Message
 * Create by Administrator
 * Create by 2019/11/21
 */
public class PinTiBean implements Serializable {
    /**
     * id : 1
     * title : null
     * audioUrl : null
     */

    private String id;
    private String title;
    private String localUrl;
    private String audioUrl;
    private String score;
    private boolean select;
    private int duration;

    public String getLocalUrl() {
        return localUrl;
    }

    public void setLocalUrl(String localUrl) {
        this.localUrl = localUrl;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    @Override
    public String toString() {
        return "PinTiBean{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", audioUrl='" + audioUrl + '\'' +
                ", score='" + score + '\'' +
                ", duration=" + duration +
                '}';
    }
}
