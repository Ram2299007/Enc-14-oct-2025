package com.Appzia.enclosure.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import com.Appzia.enclosure.databinding.ActivityPaymentBinding;

import java.util.Objects;

public class paymentActivity extends AppCompatActivity {

    ActivityPaymentBinding binding;
    String themColor;
    ColorStateList tintList;
    public static String fontSizePref;

    @Override
    protected void onStart() {
        super.onStart();



        try {

            Constant.getSfFuncion(binding.getRoot().getContext());
            fontSizePref = Constant.getSF.getString(Constant.Font_Size, "");


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            if (fontSizePref.equals(Constant.small)) {
                binding.payment.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                binding.pa.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.selcte.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.one.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                binding.upi.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                binding.phonepay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                binding.gpay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                binding.paytm.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                binding.debit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                binding.addcard.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);

            } else if (fontSizePref.equals(Constant.medium)) {

                binding.payment.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                binding.pa.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.selcte.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.one.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                binding.upi.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.phonepay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.gpay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.paytm.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.debit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                binding.addcard.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);


            } else if (fontSizePref.equals(Constant.large)) {
                binding.payment.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
                binding.pa.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.selcte.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                binding.one.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                binding.upi.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.phonepay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.gpay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.paytm.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.debit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                binding.addcard.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            }
        } catch (Exception ignored) {

        }

        Constant.getSfFuncion(getApplicationContext());
        themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
        tintList = ColorStateList.valueOf(Color.parseColor(themColor));

        try {
            if (themColor.equals("#ff0080")) {
                binding.paynow.setBackgroundResource(R.drawable.pinkhover);


            } else if (themColor.equals("#00A3E9")) {

                //  binding.forgetPass.setTextColor(getResources().getColor(R.color.bluetohovertext));
                binding.paynow.setBackgroundResource(R.drawable.buttonhoverfordone);


            } else if (themColor.equals("#7adf2a")) {


                binding.paynow.setBackgroundResource(R.drawable.popatihover);


            } else if (themColor.equals("#ec0001")) {


                binding.paynow.setBackgroundResource(R.drawable.red_one_hover);

            } else if (themColor.equals("#16f3ff")) {


                binding.paynow.setBackgroundResource(R.drawable.blue_hover);


            } else if (themColor.equals("#FF8A00")) {


                binding.paynow.setBackgroundResource(R.drawable.orangehover);


            } else if (themColor.equals("#7F7F7F")) {


                binding.paynow.setBackgroundResource(R.drawable.gray_hover);


            } else if (themColor.equals("#D9B845")) {


                binding.paynow.setBackgroundResource(R.drawable.yellow_hover);

            } else if (themColor.equals("#346667")) {


                binding.paynow.setBackgroundResource(R.drawable.greenoriginalhover);


            } else if (themColor.equals("#9846D9")) {

                binding.paynow.setBackgroundResource(R.drawable.voilethover);


            } else if (themColor.equals("#A81010")) {


                binding.paynow.setBackgroundResource(R.drawable.red_two_hover);
            } else {

                //  binding.forgetPass.setTextColor(getResources().getColor(R.color.bluetohovertext));
                binding.paynow.setBackgroundResource(R.drawable.buttonhoverfordone);


            }
        } catch (Exception ignored) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        SwipeNavigationHelper.finishWithSwipe(paymentActivity.this);
        
       // TransitionHelper.performTransition(((Activity)getApplicationContext()));
    }
}