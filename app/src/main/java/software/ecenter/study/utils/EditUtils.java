package software.ecenter.study.utils;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Message: 输入框的判断
 * Created by  ChenLong.
 * Created by Time on 2017/12/13.
 */

public class EditUtils {
    /**
     * 判断一个字符串中是否包含有Emoji表情
     *
     * @param input
     * @return true 有Emoji
     */
    public static boolean isEmojiCharacter(CharSequence input) {
        for (int i = 0; i < input.length(); i++) {
            if (isEmojiCharacter(input.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    //密码显隐藏
    public static void showAndHidePsd(View view, EditText editText) {
        view.setSelected(!view.isSelected());
        if (view.isSelected()) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        editText.setSelection(editText.getText().toString().length());
    }

    /**
     * 是否是Emoji 表情,
     *
     * @param codePoint
     * @return true 是Emoji表情
     */
    public static boolean isEmojiCharacter(char codePoint) {
        // Emoji 范围
        boolean isScopeOf = (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF) && (codePoint != 0x263a))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));

        return !isScopeOf;
    }

    /**
     * 去除字符串中的Emoji表情
     *
     * @param source
     * @return
     */
    public static String removeEmoji(CharSequence source) {
        String result = "";
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            if (isEmojiCharacter(c)) {
                continue;
            }
            result += c;
        }
        return result;
    }

    /**
     * 只去掉表情
     *
     * @param text
     */
    public static void showEditNoEmoji(final EditText text) {
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //如果 输入的内容包含有Emoji
                if (EditUtils.isEmojiCharacter(charSequence)) {
                    //那么就去掉
                    text.setText(EditUtils.removeEmoji(charSequence));
                    text.setSelection(EditUtils.removeEmoji(charSequence).length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * 只去掉表情  并提示
     *
     * @param text
     */
    public static void showEditNoEmoji(final EditText text, final Context context, final String tishi, final String numText) {
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //如果 输入的内容包含有Emoji
                if (EditUtils.isEmojiCharacter(charSequence)) {
                    //那么就去掉
                    text.setText(EditUtils.removeEmoji(charSequence));
                    text.setSelection(EditUtils.removeEmoji(charSequence).length());
                    if (!TextUtils.isEmpty(tishi)) {
                        ToastUtils.showToastSHORT(context, tishi);
                    }
                }
                String s = charSequence.toString();
                if (s.length() >= 100) {
                    text.setText(s.substring(0,99));
                    text.setSelection(text.length());
                    if (!TextUtils.isEmpty(tishi)) {
                        ToastUtils.showToastSHORT(context, numText);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    //过滤换行
    public static void setEditTextKonge(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.toString().contentEquals(",") || source.toString().contentEquals("，"))
                    return "";
                else return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    //判断空格
    public static boolean isKongGe(String s) {
        String[] split = s.split(" ");
        return split == null || split.length <= 0;
    }

    //去掉表情  输入的字数监控
    public static void showTextNum(final EditText editText, final TextView textView) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //如果 输入的内容包含有Emoji
                if (EditUtils.isEmojiCharacter(charSequence)) {
                    //那么就去掉
                    editText.setText(EditUtils.removeEmoji(charSequence));
                    editText.setSelection(EditUtils.removeEmoji(charSequence).length());
                }
                if (!TextUtils.isEmpty(charSequence.toString())) {
                    textView.setText(charSequence.toString().length() + "/200");
                } else {
                    textView.setText("0/200");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    //限制2位小数
    public static void showTwoDouBle(EditText text) {
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String temp = s.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    s.delete(posDot + 3, posDot + 4);
                }
            }
        });
    }
}
