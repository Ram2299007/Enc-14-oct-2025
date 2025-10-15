package com.Appzia.enclosure.Fragments;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.MainActivityOld;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.databinding.FragmentVoiceCallLogBinding;

public class voiceCallLogFragment extends Fragment {

    FragmentVoiceCallLogBinding binding;
    String name;
    int pixels;
    Drawable drawable;
    String themColor;
    ColorStateList tintList;
    Context mContext;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            Constant.setSfFunction(mContext);
            Constant.setSF.putString(Constant.BACKKEY,Constant.otherBackValue);
            Constant.setSF.apply();

        }catch (Exception ignored){}
    }
    @Override
    public void onStart() {
        super.onStart();

        try{
            Constant.setSfFunction(mContext);
            Constant.setSF.putString(Constant.BACKKEY,Constant.otherBackValue);
            Constant.setSF.apply();
        }catch (Exception ignored){}

        final float scale = requireActivity().getResources().getDisplayMetrics().density;
        pixels = (int) (68 * scale + 0.5f);
        assert getFragmentManager() != null;

        try {

            Constant.getSfFuncion(binding.getRoot().getContext());
            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
            tintList = ColorStateList.valueOf(Color.parseColor(themColor));


            //   binding.menuPoint.setBackgroundColor(Color.parseColor(themColor));

            try {
                if (themColor.equals("#ff0080")) {

                    binding.call.setBackgroundTintList(tintList);
                } else if (themColor.equals("#00A3E9")) {
                    binding.call.setBackgroundTintList(tintList);

                } else if (themColor.equals("#7adf2a")) {
                    binding.call.setBackgroundTintList(tintList);

                } else if (themColor.equals("#ec0001")) {
                    binding.call.setBackgroundTintList(tintList);

                } else if (themColor.equals("#16f3ff")) {
                    binding.call.setBackgroundTintList(tintList);

                } else if (themColor.equals("#FF8A00")) {
                    binding.call.setBackgroundTintList(tintList);

                } else if (themColor.equals("#7F7F7F")) {

                    binding.call.setBackgroundTintList(tintList);
                } else if (themColor.equals("#D9B845")) {
                    binding.call.setBackgroundTintList(tintList);

                } else if (themColor.equals("#346667")) {
                    binding.call.setBackgroundTintList(tintList);

                } else if (themColor.equals("#9846D9")) {
                    binding.call.setBackgroundTintList(tintList);

                } else if (themColor.equals("#A81010")) {
                    binding.call.setBackgroundTintList(tintList);

                } else {
                    binding.call.setBackgroundTintList(tintList);
                }
            } catch (Exception ignored) {
                binding.call.setBackgroundTintList(tintList);
            }


        } catch (Exception ignored) {
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        try{
            Constant.setSfFunction(mContext);
            Constant.setSF.putString(Constant.BACKKEY,Constant.otherBackValue);
            Constant.setSF.apply();

        }catch (Exception ignored){}
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVoiceCallLogBinding.inflate(inflater, container, false);

        mContext = binding.getRoot().getContext();

        binding.call1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               name = binding.name1.getText().toString();
                binding.mainname.setText(name);int targetHeight = pixels; // The height to which the view should expand
                ValueAnimator valueAnimator = ValueAnimator.ofInt(binding.bottomcaller.getHeight(), targetHeight);
                valueAnimator.setDuration(500);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        binding.bottomcaller.getLayoutParams().height = (int) animation.getAnimatedValue();
                        binding.bottomcaller.requestLayout();
                    }
                });
                valueAnimator.start();
                binding.bottomcaller.setVisibility(View.VISIBLE);

            }
        });



        binding.call2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();


                name = binding.name2.getText().toString();
                binding.mainname.setText(name);
                // Toast.makeText(getActivity(),   binding.mainname.getText().toString(), Toast.LENGTH_SHORT).show();


                int targetHeight = pixels; // The height to which the view should expand
                ValueAnimator valueAnimator = ValueAnimator.ofInt(binding.bottomcaller.getHeight(), targetHeight);
                valueAnimator.setDuration(500);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        binding.bottomcaller.getLayoutParams().height = (int) animation.getAnimatedValue();
                        binding.bottomcaller.requestLayout();
                    }
                });
                valueAnimator.start();


                binding.bottomcaller.setVisibility(View.VISIBLE);
            }
        });
        binding.call3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();


                name = binding.name3.getText().toString();
                binding.mainname.setText(name);
                //    Toast.makeText(getActivity(),   binding.mainname.getText().toString(), Toast.LENGTH_SHORT).show();


                int targetHeight = pixels; // The height to which the view should expand
                ValueAnimator valueAnimator = ValueAnimator.ofInt(binding.bottomcaller.getHeight(), targetHeight);
                valueAnimator.setDuration(500);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        binding.bottomcaller.getLayoutParams().height = (int) animation.getAnimatedValue();
                        binding.bottomcaller.requestLayout();
                    }
                });
                valueAnimator.start();


                binding.bottomcaller.setVisibility(View.VISIBLE);
            }
        });

        binding.call4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();


                name = binding.name4.getText().toString();
                binding.mainname.setText(name);
                //   Toast.makeText(getActivity(),   binding.mainname.getText().toString(), Toast.LENGTH_SHORT).show();


                int targetHeight = pixels; // The height to which the view should expand
                ValueAnimator valueAnimator = ValueAnimator.ofInt(binding.bottomcaller.getHeight(), targetHeight);
                valueAnimator.setDuration(500);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        binding.bottomcaller.getLayoutParams().height = (int) animation.getAnimatedValue();
                        binding.bottomcaller.requestLayout();
                    }
                });
                valueAnimator.start();


                binding.bottomcaller.setVisibility(View.VISIBLE);
            }
        });

        binding.scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {


                Log.d("scrollview", "scrollX :" + scrollX + " scrollY :" + scrollY + " oldScrollX :" + oldScrollX + " oldScrollY :" + oldScrollY);
                if (scrollY >= 1000) {
                    MainActivityOld.slideUpContainer();
                }else if(scrollY <=0){
                    if(MainActivityOld.linearMain2.getVisibility() ==View.VISIBLE){
                        // Toast.makeText(mContext, "scrolled_bottom", Toast.LENGTH_SHORT).show();
                        Log.d("scrolled_bottom", "scrolled_bottom :"+scrollY);
                        MainActivityOld.slideDownContainer();
                    }

                }else {}
            }
        });

        binding.scrollView.setOnTouchListener(new View.OnTouchListener() {
            private float startY;
            private long startTime;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startY = event.getY();
                        startTime = System.currentTimeMillis();
                        break;

                    case MotionEvent.ACTION_UP:
                        float endY = event.getY();
                        long endTime = System.currentTimeMillis();
                        float distance = endY - startY;
                        long time = endTime - startTime;
                        float velocity = Math.abs(distance / time) * 1000; // Pixels per second
                        Log.d("SCROLL_SPEED", "Velocity: " + velocity + " px/s");

                        int newValue = Math.round(velocity);
                        MainActivityOld.golbalDecinal = newValue;

                        //     MainActivity.golbalDecinal = newValue;
                        Log.d("SCROLL_SPEED", "valueNew: " + String.valueOf(MainActivityOld.golbalDecinal) + " px/s");
                        break;
                }
                return false;
            }
        });

        binding.row1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.contactLayout,new showcallhistoryFragment()).commit();
            }
        });
        binding.row2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.contactLayout,new showcallhistoryFragment()).commit();
            }
        });
        binding.row3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.contactLayout,new showcallhistoryFragment()).commit();
            }
        });
        binding.row4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.contactLayout,new showcallhistoryFragment()).commit();
            }
        });

        return binding.getRoot();
    }
}