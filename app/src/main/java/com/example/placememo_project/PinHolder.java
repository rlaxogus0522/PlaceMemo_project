package com.example.placememo_project;

import androidx.annotation.NonNull;

import com.example.placememo_project.databinding.ItemToItemBinding;
import com.example.placememo_project.databinding.PinBinding;
import com.xwray.groupie.databinding.BindableItem;

public class PinHolder extends BindableItem<PinBinding> {
    int colorNum;
    int color[] = new int[]{0xFFE8EE9C, 0xFFE4B786, 0xFF97E486, 0xFF86E4D1, 0xFFE48694};  //-- 저장된 메모 메뉴에 표시할 색깔 등록해두기

    PinHolder(int color){
        colorNum = color;
    }
        @Override
        public void bind(@NonNull PinBinding viewBinding, int position) {
        viewBinding.pinLayout.setBackgroundColor(color[colorNum]);
        }

        @Override
        public int getLayout() {
            return R.layout.pin;
        }
    }

