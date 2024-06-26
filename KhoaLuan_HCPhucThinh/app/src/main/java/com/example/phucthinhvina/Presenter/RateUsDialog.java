package com.example.phucthinhvina.Presenter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;

import com.example.phucthinhvina.R;


public class RateUsDialog extends Dialog {

    Button btnSubmit,btnCancel;
    RatingBar ratingBar;
    ImageView ratingImage;
    private  float userRate = 0;

    public RateUsDialog(@NonNull Context context){
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_us_dialog_layout);

        //btnSubmit = (Button) findViewById(R.id.btnSubmit);
        ratingBar = findViewById(R.id.ratingBar);
        ratingImage = findViewById(R.id.ratingImage);
//        btnCancel = (Button) findViewById(R.id.btnCancel);
//
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dismiss();
//            }
//        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
                if( rating <= 1 ){
                    ratingImage.setImageResource(R.drawable.one_star);
                }
                else if( rating <= 2 ){
                    ratingImage.setImageResource(R.drawable.two_star);
                }
                else if( rating <= 3 ){
                    ratingImage.setImageResource(R.drawable.three_star);
                }
                else if( rating <= 4 ){
                    ratingImage.setImageResource(R.drawable.four_star);
                }
                else if( rating <= 5 ){
                    ratingImage.setImageResource(R.drawable.five_star);
                }
                // animate emoji image
                animateImage(ratingImage);

                userRate= rating;
            }
        });




    }

    private  void animateImage(ImageView ratingImage){

        ScaleAnimation scaleAnimation = new ScaleAnimation(0,1f,0,1f,
                Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);

        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(200);
        ratingImage.startAnimation(scaleAnimation);
    }














}
