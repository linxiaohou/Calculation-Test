package com.tatsuya.calculationtest;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import java.util.Random;

/** @noinspection DataFlowIssue*/
public class MyViewModel extends AndroidViewModel {
    SavedStateHandle handle;
    private static final String KEY_HIGH_SCORE = "key_high_score";
    private static final String KEY_CURRENT_SCORE = "key_current_score";
    private static final String KEY_LEFT_NUMBER = "key_left_number";
    private static final String KEY_RIGHT_NUMBER = "key_right_number";
    private static final String KEY_OPERATOR = "key_operator";
    private static final String KEY_ANSWER = "key_answer";
    private static final String SAVED_SHP_DATA_NAME = "saved_shp_data_name";
    boolean win_flag = false;

    public MyViewModel(@NonNull Application application, SavedStateHandle handle) {
        super(application);
        if (!handle.contains(KEY_HIGH_SCORE)) {
            SharedPreferences shp = getApplication().getSharedPreferences(SAVED_SHP_DATA_NAME, Context.MODE_PRIVATE);
            handle.set(KEY_HIGH_SCORE, shp.getInt(KEY_HIGH_SCORE, 0));
            handle.set(KEY_LEFT_NUMBER, 0);
            handle.set(KEY_RIGHT_NUMBER, 0);
            handle.set(KEY_OPERATOR, "+");
            handle.set(KEY_ANSWER, 0);
            handle.set(KEY_CURRENT_SCORE, 0);
        }
        this.handle = handle;
    }

    public MutableLiveData<Integer> getLeftNumber() {
        return handle.getLiveData(KEY_LEFT_NUMBER);
    }

    public MutableLiveData<Integer> getRightNumber() {
        return handle.getLiveData(KEY_RIGHT_NUMBER);
    }

    public MutableLiveData<String> getOperator() {
        return handle.getLiveData(KEY_OPERATOR);
    }

    public MutableLiveData<Integer> getHighScore() {
        return handle.getLiveData(KEY_HIGH_SCORE);
    }

    public MutableLiveData<Integer> getCurrentScore() {
        return handle.getLiveData(KEY_CURRENT_SCORE);
    }

    public MutableLiveData<Integer> getAnswer() {
        return handle.getLiveData(KEY_ANSWER);
    }

    void generator() {
        int LEVEL = 1000;
        int OPERATOR_RANDOM = 5;
        Random random = new Random();
        int x, y, z;
        x = random.nextInt(LEVEL) + 1;
        y = random.nextInt(LEVEL) + 1;
        z = random.nextInt(OPERATOR_RANDOM);
        if (z == 0) {
            getOperator().setValue("+");
            if (x > y) {//这样写是为了保证结果的范围小于LEVEL
                getAnswer().setValue(x);
                getLeftNumber().setValue(y);
                getRightNumber().setValue(x - y);
            } else {
                getAnswer().setValue(y);
                getLeftNumber().setValue(x);
                getRightNumber().setValue(y - x);
            }
        } else if (z == 1) {
            getOperator().setValue("-");
            if (x > y) {
                getAnswer().setValue(x - y);
                getLeftNumber().setValue(x);
                getRightNumber().setValue(y);
            } else {
                getAnswer().setValue(y - x);
                getLeftNumber().setValue(y);
                getRightNumber().setValue(x);
            }
        } else if (z == 2) {
            while (x * y > LEVEL) {
                x = random.nextInt(LEVEL) + 1;
                y = random.nextInt(LEVEL) + 1;
            }
            getOperator().setValue("×");
            getAnswer().setValue(x * y);
            getLeftNumber().setValue(x);
            getRightNumber().setValue(y);
        } else if (z == 3) {
            while (x * y > LEVEL) {
                x = random.nextInt(LEVEL) + 1;
                y = random.nextInt(LEVEL) + 1;
            }
            getOperator().setValue("÷");
            getAnswer().setValue(x);
            getLeftNumber().setValue(x * y);
            getRightNumber().setValue(y);
        }

    }

    void save() {
        SharedPreferences shp = getApplication().getSharedPreferences(SAVED_SHP_DATA_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();
        editor.putInt(KEY_HIGH_SCORE, getHighScore().getValue());
        editor.apply();
    }

    void answerCorrect() {
        getCurrentScore().setValue(getCurrentScore().getValue() + 1);
        if (getCurrentScore().getValue() > getHighScore().getValue()) {
            getHighScore().setValue(getCurrentScore().getValue());
            win_flag = true;
        }
        generator();
    }
}
