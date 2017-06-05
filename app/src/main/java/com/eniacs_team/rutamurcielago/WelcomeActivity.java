package com.eniacs_team.rutamurcielago;

/**
 * Created by Francisco on 6/1/2017.
 */

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.data;
import static com.eniacs_team.rutamurcielago.R.mipmap.current;

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private BaseDatos baseDatos;
    private Context context;
    private MediaPlayer mPlayer;
    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            String testString = Integer.toString(position);
            //Toast.makeText(context, testString, Toast.LENGTH_SHORT).show();
            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText("Listo");
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText("Siguiente");
                btnSkip.setVisibility(View.VISIBLE);
            }
            if(mPlayer.isPlaying())
            {
                int time = 0;
                if (position < layouts.length) {
                    switch (position){
                        case 0:
                            mPlayer.seekTo(time);
                            Toast.makeText(context, testString, Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            time =  6393;
                            mPlayer.seekTo(time);
                            Toast.makeText(context, testString, Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            time =  11209;
                            mPlayer.seekTo(time);
                            Toast.makeText(context, testString, Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            time = 13301;
                            mPlayer.seekTo(time);
                            Toast.makeText(context, testString, Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            launchHomeScreen();
                            break;
                    }

                }
                else
                {
                    launchHomeScreen();
                }
            }

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        context = this;

        setContentView(R.layout.activity_welcome);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);

        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.slide_screen1,
                R.layout.slide_screen2,
                R.layout.slide_screen3,
                R.layout.slide_screen4};

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        baseDatos = new BaseDatos(context);
        //baseDatos = BaseDatos.getInstancia();
       // mPlayerBuilder();
        playAudio();

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });

    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        finish();
    }

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * Método llamado cuando la actividad muere. Destruye el media player para que no quede basura.
     */
    @Override
    protected void onDestroy() {
        stopAudio();
        super.onDestroy();
    }

    private void playAudio() {
        try {
            AssetFileDescriptor descriptor = baseDatos.selectAudio(5);
            mPlayerBuilder();
            mPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(),descriptor.getLength());
            descriptor.close();
            //mPlayer.create(this.context,R.raw.introduccion);
            mPlayer.prepare();
            mPlayer.start();
            int time = 0;
            int current = getItem(0);
            if (current < layouts.length) {
                switch (current){
                    case 0:
                       // mPlayer.seekTo(0);
                        break;
                    case 1:
                        time = (int) 6.393;
                        mPlayer.seekTo(time);
                        break;
                    case 2:
                        time = (int) 11.209;
                        mPlayer.seekTo(time);
                        break;
                    case 3:
                        time = (int) 13.301;
                        mPlayer.seekTo(time);
                        break;
                    default:
                        launchHomeScreen();
                        break;
                }

            }
            else
            {
                launchHomeScreen();
            }
        } catch (Exception e) {
            Log.i("Audio", "Error " + e);
        }
    }

    private void stopAudio() {
        if(mPlayer != null)
        {
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void mPlayerBuilder() {
        //Creo reproductor de audio
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
            //Listener para que se borre cuando termine de reproducirse audio
            MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopAudio();
                }
            };

            MediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    //Si es primer slide de bienvenida
                    if(30 < percent && percent < 31)
                    {
                        viewPager.setCurrentItem(1);
                    }
                    //Si es segundo slide de bienvenida
                    else if (54 < percent && percent < 55)
                    {
                        viewPager.setCurrentItem(2);
                    }
                    //Si es tercero slide de bienvenida
                    else if(64 < percent && percent < 65)
                    {
                        viewPager.setCurrentItem(3);
                    }
                }
            };
            mPlayer.setOnCompletionListener(onCompletionListener);
            mPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}