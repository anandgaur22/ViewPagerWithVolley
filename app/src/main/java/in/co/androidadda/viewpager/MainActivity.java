package in.co.androidadda.viewpager;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout sliderDotspanel;

    RequestQueue rq;
    List<SliderUtils> sliderImg;
    ViewPagerAdapter viewPagerAdapter;

    String request_url = "http://165.227.88.236:9000/api/v1/chalenj/get_featured_chalenj_list/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rq = CustomVolleyRequest.getInstance(this).getRequestQueue();

        sliderImg = new ArrayList<>();

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);

        appLoginNetworkCall();


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    public void appLoginNetworkCall() {

        HashMap<String, String> postparams = new HashMap<String, String>();

        postparams.put("user", "100");

        final JSONObject jsonObject = new JSONObject(postparams);
        Log.d("tag", "callVolleyServer: " + jsonObject);


        JsonObjectRequest req = new JsonObjectRequest(request_url, new JSONObject(postparams), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("tag", "request " + response);
                if (response != null) {

                    try {
                        if (response.getInt("status") == 1) {

                            JSONArray jsonArray=response.getJSONArray("data");

                            for (int i=0;i<jsonArray.length();i++){
                                SliderUtils sliderUtils = new SliderUtils();
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                sliderUtils.setSliderImageUrl(jsonObject1.getString("chalenj_featured_image"));
                                sliderImg.add(sliderUtils);

                                Log.d("", "onResponseslider: "+jsonObject1.getString("chalenj_featured_image"));
                            }

                            viewPagerAdapter = new ViewPagerAdapter(sliderImg, MainActivity.this);

                            viewPager.setAdapter(viewPagerAdapter);


                        } else {

                            Toast.makeText(MainActivity.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        // Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {

                    Toast.makeText(MainActivity.this, "server response null", Toast.LENGTH_SHORT).show();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



                VolleyLog.e("Error: ", "" + error.getMessage());
                Toast.makeText(MainActivity.this, "no internet connection", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };

        CustomVolleyRequest.getInstance(getApplicationContext()).addToRequestQueue(req);
    }

}
