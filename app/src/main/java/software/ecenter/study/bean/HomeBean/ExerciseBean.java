package software.ecenter.study.bean.HomeBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mike on 2018/5/11.
 */

public class ExerciseBean implements Serializable {

    private static final long serialVersionUID = -4075737124835292371L;
    private String exerciseId;
    private String exerciseTitle;
    private int exerciseType;//题类型（1、单选，2、多选，3、判断）
    private String yourAnswer; //你的答案
    private String rightAnswer;//正确答案
    private String yourAnswerShow;//你的正确答案，字母
    private String rightAnswerShow; //正确答案，字母
    private String exerciseAnalysis;//问题解析
    private List<ExerciseAnswerBean> exerciseAnswer;
    private int type;
    private boolean isCheck;//选中
    private boolean isCheckMode;//先择模式


    public String getYourAnswerShow() {
        return yourAnswerShow;
    }

    public void setYourAnswerShow(String yourAnswerShow) {
        this.yourAnswerShow = yourAnswerShow;
    }

    public String getRightAnswerShow() {
        return rightAnswerShow;
    }

    public void setRightAnswerShow(String rightAnswerShow) {
        this.rightAnswerShow = rightAnswerShow;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(String exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getExerciseTitle() {
        return exerciseTitle;
    }

    public void setExerciseTitle(String exerciseTitle) {
        this.exerciseTitle = exerciseTitle;
    }

    public int getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(int exerciseType) {
        this.exerciseType = exerciseType;
    }

    public String getYourAnswer() {
        return yourAnswer;
    }

    public void setYourAnswer(String yourAnswer) {
        this.yourAnswer = yourAnswer;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public String getExerciseAnalysis() {
        return exerciseAnalysis;
    }

    public void setExerciseAnalysis(String exerciseAnalysis) {
        this.exerciseAnalysis = exerciseAnalysis;
    }

    public List<ExerciseAnswerBean> getExerciseAnswer() {
        return exerciseAnswer;
    }

    public void setExerciseAnswer(List<ExerciseAnswerBean> exerciseAnswer) {
        this.exerciseAnswer = exerciseAnswer;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isCheckMode() {
        return isCheckMode;
    }

    public void setCheckMode(boolean checkMode) {
        isCheckMode = checkMode;
    }
}
