package edu.app.gridpuzzlegame.outboardingfragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

import edu.app.gridpuzzlegame.authscrees.LoginScreen;
import edu.app.gridpuzzlegame.R;

public class OutBoardingScreen extends AppCompatActivity {
    ImageButton buttonNext;
    Button buttonStart;
    AppCompatButton buttonSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_boarding_screen);
        saveFirstApp();

        buttonNext = findViewById(R.id.btnNext);
        buttonStart = findViewById(R.id.btnStart);
        buttonSkip = findViewById(R.id.btnSkip);

        ViewPager2 viewPager2 = findViewById(R.id.viewPager2);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new OutBoardingPage1());
        fragments.add(new OutBoardingPage2());
        fragments.add(new OutBoardingPage3());

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle(), fragments);

        if (viewPager2.getCurrentItem() == 2) {
            buttonNext.setVisibility(View.INVISIBLE);
            buttonStart.setVisibility(View.VISIBLE);
        }
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (viewPager2.getCurrentItem() < 2) {
                    if (viewPager2.getCurrentItem() == 1) {
                        buttonNext.setVisibility(View.INVISIBLE);
                        buttonStart.setVisibility(View.VISIBLE);
                    }
                    viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
                } else {
                    buttonNext.setVisibility(View.INVISIBLE);
                    buttonStart.setVisibility(View.VISIBLE);
                }

            }
        });
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), LoginScreen.class);
                startActivity(intent);
                finish();
            }
        });


        viewPager2.setAdapter(viewPagerAdapter);

        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager2.setCurrentItem(2);
                buttonNext.setVisibility(View.INVISIBLE);
                buttonStart.setVisibility(View.VISIBLE);
            }
        });

    }

    public void saveFirstApp() {
        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFirst", true);
        editor.commit();

    }
}