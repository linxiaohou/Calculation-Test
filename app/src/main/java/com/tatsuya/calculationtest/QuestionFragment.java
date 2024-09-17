package com.tatsuya.calculationtest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.tatsuya.calculationtest.databinding.FragmentQuestionBinding;

public class QuestionFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_question, container, false);
        MyViewModel myViewModel;
        myViewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
        myViewModel.generator();
        myViewModel.getCurrentScore().setValue(0);
        FragmentQuestionBinding binding;
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_question, container, false);
        binding.setData(myViewModel);
        binding.setLifecycleOwner(requireActivity());
        final StringBuilder builder = new StringBuilder();
        View.OnClickListener listener = v -> {
            if (v.getId() == R.id.button0) {
                builder.append("0");
            } else if (v.getId() == R.id.button1) {
                builder.append("1");
            } else if (v.getId() == R.id.button2) {
                builder.append("2");
            } else if (v.getId() == R.id.button3) {
                builder.append("3");
            } else if (v.getId() == R.id.button4) {
                builder.append("4");
            } else if (v.getId() == R.id.button5) {
                builder.append("5");
            } else if (v.getId() == R.id.button6) {
                builder.append("6");
            } else if (v.getId() == R.id.button7) {
                builder.append("7");
            } else if (v.getId() == R.id.button8) {
                builder.append("8");
            } else if (v.getId() == R.id.button9) {
                builder.append("9");
            } else if (v.getId() == R.id.buttonClear) {
                builder.setLength(0);
            }
            if (builder.length() == 0) {
                binding.textView9.setText(getString(R.string.input_indicator));
            } else {
                binding.textView9.setText(builder.toString());
            }
        };
        binding.button0.setOnClickListener(listener);
        binding.button1.setOnClickListener(listener);
        binding.button2.setOnClickListener(listener);
        binding.button3.setOnClickListener(listener);
        binding.button4.setOnClickListener(listener);
        binding.button5.setOnClickListener(listener);
        binding.button6.setOnClickListener(listener);
        binding.button7.setOnClickListener(listener);
        binding.button8.setOnClickListener(listener);
        binding.button9.setOnClickListener(listener);
        binding.buttonClear.setOnClickListener(listener);
        binding.buttonSubmit.setOnClickListener(v -> {
            if (builder.length() == 0) {
                Toast.makeText(requireActivity(), getString(R.string.input_blank_message), Toast.LENGTH_SHORT).show();
                return;
            }
            if (Integer.valueOf(builder.toString()).equals(myViewModel.getAnswer().getValue())) {
                myViewModel.answerCorrect();
                builder.setLength(0);
                binding.textView9.setText(getString(R.string.answer_correct_message));
            } else {
                NavController controller = Navigation.findNavController(v);
                if (myViewModel.win_flag) {
                    controller.navigate(R.id.action_questionFragment_to_winFragment);
                    myViewModel.win_flag = false;
                    myViewModel.save();
                } else {
                    controller.navigate(R.id.action_questionFragment_to_loseFragment);
                }
            }
        });
        return binding.getRoot();
    }
}