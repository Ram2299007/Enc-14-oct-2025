package com.Appzia.enclosure.Fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.selectLanguage;
import com.Appzia.enclosure.databinding.FragmentSplashFragmwntBinding;


public class SplashFragment extends Fragment {


    FragmentSplashFragmwntBinding binding;
    Animation animationSlideUpLogo, animationSlideUpTitle, animationSlideUpSubTitle, animationSlideUpSubTitle2;
    Animation mail, dot1, dot2, dot3, professional, personal, sofar, billionPeople, dot4, dot5, dot6, q, blueTemplate, tempTxt, nextTxt, nextImg;

    String themColor;
    ColorStateList tintList;
    @Override
    public void onStart() {
        super.onStart();

    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSplashFragmwntBinding.inflate(inflater, container, false);

        binding.mainLogo.setVisibility(View.VISIBLE);
        animationSlideUpLogo = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up_mainlogo);
        binding.mainLogo.startAnimation(animationSlideUpLogo);

        binding.mainTitle.setVisibility(View.VISIBLE);
        animationSlideUpTitle = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up_title);
        binding.mainTitle.startAnimation(animationSlideUpTitle);


        animationSlideUpTitle.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                animationSlideUpSubTitle2 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                binding.subtitle.startAnimation(animationSlideUpSubTitle2);

                binding.subtitle.setTextColor(Color.parseColor("#808080"));

                binding.subtitle.setTextScaleX(0.8f);
                binding.subtitle.setVisibility(View.VISIBLE);


                animationSlideUpSubTitle2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        // main animations

                        mail = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in3);
                        binding.mail.setVisibility(View.VISIBLE);
                        binding.mail.startAnimation(mail);


                        mail.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {


                                dot1 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in3);
                                binding.dot1.setVisibility(View.VISIBLE);
                                binding.dot1.startAnimation(dot1);


                                dot1.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {

                                        dot2 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in3);

                                        binding.dot2.setVisibility(View.VISIBLE);
                                        binding.dot2.startAnimation(dot2);


                                        dot2.setAnimationListener(new Animation.AnimationListener() {
                                            @Override
                                            public void onAnimationStart(Animation animation) {

                                            }

                                            @Override
                                            public void onAnimationEnd(Animation animation) {
                                                dot3 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in3);

                                                binding.dot3.setVisibility(View.VISIBLE);
                                                binding.dot3.startAnimation(dot3);

                                                dot3.setAnimationListener(new Animation.AnimationListener() {
                                                    @Override
                                                    public void onAnimationStart(Animation animation) {

                                                    }

                                                    @Override
                                                    public void onAnimationEnd(Animation animation) {

                                                        professional = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in3);

                                                        binding.professional.setVisibility(View.VISIBLE);
                                                        binding.professional.startAnimation(professional);

                                                        professional.setAnimationListener(new Animation.AnimationListener() {
                                                            @Override
                                                            public void onAnimationStart(Animation animation) {

                                                            }

                                                            @Override
                                                            public void onAnimationEnd(Animation animation) {

                                                                personal = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in3);

                                                                binding.personal.setVisibility(View.VISIBLE);
                                                                binding.personal.startAnimation(personal);


                                                                personal.setAnimationListener(new Animation.AnimationListener() {
                                                                    @Override
                                                                    public void onAnimationStart(Animation animation) {

                                                                    }

                                                                    @Override
                                                                    public void onAnimationEnd(Animation animation) {

                                                                        sofar = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in3);

                                                                        binding.sofar.setVisibility(View.VISIBLE);
                                                                        binding.sofar.startAnimation(sofar);

                                                                        sofar.setAnimationListener(new Animation.AnimationListener() {
                                                                            @Override
                                                                            public void onAnimationStart(Animation animation) {

                                                                            }

                                                                            @Override
                                                                            public void onAnimationEnd(Animation animation) {

                                                                                billionPeople = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in3);

                                                                                binding.billionPeople.setVisibility(View.VISIBLE);
                                                                                binding.billionPeople.startAnimation(billionPeople);

                                                                                billionPeople.setAnimationListener(new Animation.AnimationListener() {
                                                                                    @Override
                                                                                    public void onAnimationStart(Animation animation) {

                                                                                    }

                                                                                    @Override
                                                                                    public void onAnimationEnd(Animation animation) {
                                                                                        dot4 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in3);

                                                                                        binding.dot4.setVisibility(View.VISIBLE);
                                                                                        binding.dot4.startAnimation(dot4);


                                                                                        dot4.setAnimationListener(new Animation.AnimationListener() {
                                                                                            @Override
                                                                                            public void onAnimationStart(Animation animation) {

                                                                                            }

                                                                                            @Override
                                                                                            public void onAnimationEnd(Animation animation) {
                                                                                                dot5 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in3);

                                                                                                binding.dot5.setVisibility(View.VISIBLE);
                                                                                                binding.dot5.startAnimation(dot5);

                                                                                                dot5.setAnimationListener(new Animation.AnimationListener() {
                                                                                                    @Override
                                                                                                    public void onAnimationStart(Animation animation) {

                                                                                                    }

                                                                                                    @Override
                                                                                                    public void onAnimationEnd(Animation animation) {

                                                                                                        dot6 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in3);

                                                                                                        binding.dot6.setVisibility(View.VISIBLE);
                                                                                                        binding.dot6.startAnimation(dot6);

                                                                                                        dot6.setAnimationListener(new Animation.AnimationListener() {
                                                                                                            @Override
                                                                                                            public void onAnimationStart(Animation animation) {

                                                                                                            }

                                                                                                            @Override
                                                                                                            public void onAnimationEnd(Animation animation) {
                                                                                                                q = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in3);

                                                                                                                binding.q.setVisibility(View.VISIBLE);
                                                                                                                binding.q.startAnimation(q);

                                                                                                                q.setAnimationListener(new Animation.AnimationListener() {
                                                                                                                    @Override
                                                                                                                    public void onAnimationStart(Animation animation) {

                                                                                                                    }

                                                                                                                    @Override
                                                                                                                    public void onAnimationEnd(Animation animation) {

                                                                                                                        blueTemplate = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in3);

                                                                                                                        binding.blueTemplate.setVisibility(View.VISIBLE);
                                                                                                                        binding.blueTemplate.startAnimation(blueTemplate);

                                                                                                                        blueTemplate.setAnimationListener(new Animation.AnimationListener() {
                                                                                                                            @Override
                                                                                                                            public void onAnimationStart(Animation animation) {

                                                                                                                            }

                                                                                                                            @Override
                                                                                                                            public void onAnimationEnd(Animation animation) {
                                                                                                                                binding.viewpager.setVisibility(View.VISIBLE);
                                                                                                                                binding.viewpager.startFlipping();
                                                                                                                                tempTxt = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in3);

                                                                                                                                nextTxt = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in3);

                                                                                                                                binding.nextTxt.setVisibility(View.VISIBLE);
                                                                                                                                binding.nextTxt.startAnimation(nextTxt);

                                                                                                                                nextTxt.setAnimationListener(new Animation.AnimationListener() {
                                                                                                                                    @Override
                                                                                                                                    public void onAnimationStart(Animation animation) {

                                                                                                                                    }

                                                                                                                                    @Override
                                                                                                                                    public void onAnimationEnd(Animation animation) {

                                                                                                                                        nextImg = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in3);

                                                                                                                                        binding.nextImg.setVisibility(View.VISIBLE);
                                                                                                                                        binding.nextImg.startAnimation(nextImg);

                                                                                                                                    }

                                                                                                                                    @Override
                                                                                                                                    public void onAnimationRepeat(Animation animation) {

                                                                                                                                    }
                                                                                                                                });

                                                                                                                            }

                                                                                                                            @Override
                                                                                                                            public void onAnimationRepeat(Animation animation) {

                                                                                                                            }
                                                                                                                        });
                                                                                                                    }

                                                                                                                    @Override
                                                                                                                    public void onAnimationRepeat(Animation animation) {

                                                                                                                    }
                                                                                                                });
                                                                                                            }

                                                                                                            @Override
                                                                                                            public void onAnimationRepeat(Animation animation) {

                                                                                                            }
                                                                                                        });
                                                                                                    }

                                                                                                    @Override
                                                                                                    public void onAnimationRepeat(Animation animation) {

                                                                                                    }
                                                                                                });
                                                                                            }

                                                                                            @Override
                                                                                            public void onAnimationRepeat(Animation animation) {

                                                                                            }
                                                                                        });
                                                                                    }

                                                                                    @Override
                                                                                    public void onAnimationRepeat(Animation animation) {

                                                                                    }
                                                                                });

                                                                            }

                                                                            @Override
                                                                            public void onAnimationRepeat(Animation animation) {

                                                                            }
                                                                        });
                                                                    }

                                                                    @Override
                                                                    public void onAnimationRepeat(Animation animation) {

                                                                    }
                                                                });
                                                            }

                                                            @Override
                                                            public void onAnimationRepeat(Animation animation) {

                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onAnimationRepeat(Animation animation) {

                                                    }
                                                });
                                            }

                                            @Override
                                            public void onAnimationRepeat(Animation animation) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });


                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), selectLanguage.class);
                startActivity(intent);

            }
        });


        return binding.getRoot();

    }


}