package software.ecenter.study.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Message
 * Create by Administrator
 * Create by 2019/10/31
 */
public class TiBean implements Serializable {
    /**
     * isCorrect : false
     * myAnswer : A
     * optionImgUrl : http://zygxkt.oss-cn-beijing.aliyuncs.com/BookImg/aeff861d31264334ae0883f2b0c401e4
     * options : ["A:<p>王五<\/p>","B:<p>小明<\/p>"]
     * question : <p>你是谁</p>
     */

    private boolean isCorrect;
    private String id;
    private String answer;
    private String myAnswer;
    private String optionImgUrl;
    private String question;
    private String answerExplanation;
    private List<String> options;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnswerExplanation() {
        return answerExplanation;
    }

    public void setAnswerExplanation(String answerExplanation) {
        this.answerExplanation = answerExplanation;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public String getMyAnswer() {
        return myAnswer;
    }

    public void setMyAnswer(String myAnswer) {
        this.myAnswer = myAnswer;
    }

    public String getOptionImgUrl() {
        return optionImgUrl;
    }

    public void setOptionImgUrl(String optionImgUrl) {
        this.optionImgUrl = optionImgUrl;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
