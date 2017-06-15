package ko.sh.com.mini_project_movie;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ArrayList<String> img = new ArrayList<String>();
    ArrayList<String> level = new ArrayList<String>();
    ArrayList<String> re_img = new ArrayList<String>();
    ArrayList<String> re_level = new ArrayList<String>();
    ArrayList<String> re_name= new ArrayList<String>();
    ArrayList<String> name_num1 = new ArrayList<String>();
    ArrayList<String> name_num2 = new ArrayList<String>();
    ArrayList<String> name_num3 = new ArrayList<String>();

    int i=0;

    LinearLayout li[] = new LinearLayout[10];
    Bitmap bitmap[]=new Bitmap[10];
    private final String url = "http://movie.naver.com/movie/running/current.nhn";
    ImageView im[] = new ImageView[10];
    TextView tv[]=new TextView[10];
    TextView lv[]=new TextView[10];
    TextView num1[]=new TextView[10];
    TextView num2[]=new TextView[10];
    TextView num3[]=new TextView[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int im_r[] = {R.id.im1, R.id.im2, R.id.im3, R.id.im4,R.id.im5, R.id.im6, R.id.im7, R.id.im8, R.id.im9, R.id.im10};
        int tv_r[]={R.id.name1,R.id.name2,R.id.name3,R.id.name4,R.id.name5,R.id.name6,R.id.name7,R.id.name8,R.id.name9,R.id.name10};
        int lv_r[]={R.id.ranking1,R.id.ranking2,R.id.ranking3,R.id.ranking4,R.id.ranking5,R.id.ranking6,R.id.ranking7,R.id.ranking8,R.id.ranking9,R.id.ranking10};
        int layout_R []={R.id.all1, R.id.all2, R.id.all3, R.id.all4, R.id.all5, R.id.all6, R.id.all7, R.id.all8, R.id.all9, R.id.all10};
        int num1_r[]={R.id.num1_name1,R.id.num2_name1,R.id.num3_name1,R.id.num4_name1,R.id.num5_name1,R.id.num6_name1,R.id.num7_name1,R.id.num8_name1,R.id.num9_name1,R.id.num10_name1};
        int num2_r[]={R.id.num1_name2,R.id.num2_name2,R.id.num3_name2,R.id.num4_name2,R.id.num5_name2,R.id.num6_name2,R.id.num7_name2,R.id.num8_name2,R.id.num9_name2,R.id.num10_name2};
        int num3_r[]={R.id.num1_name3,R.id.num2_name3,R.id.num3_name3,R.id.num4_name3,R.id.num5_name3,R.id.num6_name3,R.id.num7_name3,R.id.num8_name3,R.id.num9_name3,R.id.num10_name3};

        for(int j=0;j<im.length;j++){
            final int index;
            index = j;
            im[index] = (ImageView) findViewById(im_r[index]);
            tv[index] = (TextView) findViewById(tv_r[index]);
            li[index] = (LinearLayout) findViewById(layout_R[index]);
            lv[index] = (TextView) findViewById(lv_r[index]);
            num1[index]=(TextView) findViewById(num1_r[index]);
            num2[index]=(TextView) findViewById(num2_r[index]);
            num3[index]=(TextView) findViewById(num3_r[index]);
            li[index].setOnClickListener(this);
        }
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();
    }

    @Override
    public void onClick(View v) {
        final CharSequence[] items = {"CGV", "LOTTE CINEMA", "MEGABOX"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);     // 여기서 this는 Activity의 this

// 여기서 부터는 알림창의 속성 설정
        builder.setTitle("예매할 곳 선정")        // 제목 설정
                .setItems(items, new DialogInterface.OnClickListener(){    // 목록 클릭시 설정
                    public void onClick(DialogInterface dialog, int index){
                        if(index==0){
                            Uri uri = Uri.parse("http://www.cgv.co.kr/ticket/");
                            Intent it  = new Intent(Intent.ACTION_VIEW,uri);
                            startActivity(it);}
                        if(index==1){
                            Uri uri = Uri.parse("http://www.lottecinema.co.kr/LCHS/Contents/ticketing/ticketing.aspx");
                            Intent it  = new Intent(Intent.ACTION_VIEW,uri);
                            startActivity(it);}
                        if(index==2){
                            Uri uri = Uri.parse("http://www.megabox.co.kr/?show=booking&p=step1");
                            Intent it  = new Intent(Intent.ACTION_VIEW,uri);
                            startActivity(it);}
                        Toast.makeText(getApplicationContext(), items[index], Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog dialog = builder.create();    // 알림창 객체 생성
        dialog.show();    // 알림창 띄우기

    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                Document doc = Jsoup.connect(url).get();
                Elements contents = doc.select("img"); //내용 중에서 원하는 부분을 가져온다.
                Elements aa = doc.select("div .thumb");      //이미지
                Elements tests = doc.select("span .lst_dsc, .tit"); //내용 중에서 원하는 부분을 가져온다.
                Elements textpp=doc.select("span .info_txt1, .link_txt");

                i=0;
                //이미지 src가지고 오는거
                for(Element e: aa){
                    if(i==10)
                        break;
                    String text = e.toString(); //원하는 부분은 Elements형태로 되어 있으므로 이를 String 형태로 바꾸어 준다.
                    img.add(text);
                    i++;
                }
                i=0;
                for(Element e: tests){
                    if(i==10)
                        break;
                    String text = e.toString(); //원하는 부분은 Elements형태로 되어 있으므로 이를 String 형태로 바꾸어 준다.
                    level.add(text);
                    i++;
                }
                i=0;
                for(Element e :textpp){
                    if(i==30)
                        break;
                    String text = e.text();
                    if(i%3 == 0){
                        name_num1.add(text);
                    }else if(i%3 == 1){
                        name_num2.add(text);
                    }else if(i%3==2){
                        name_num3.add(text);
                    }
                    i++;
                }


                for(int j=0;j<10;j++){
                    int k = img.get(j).indexOf("img src=");
                    int r = img.get(j).indexOf("\" alt=");
                    String s = img.get(j).substring(k+9, r);
                    re_img.add(s);
                }

                for(int j=0;j<10;j++){
                    int k = img.get(j).indexOf("alt=\"");
                    int r = img.get(j).indexOf("\" onerror");
                    String s = img.get(j).substring(k+5,r);
                    re_name.add(s);
                }

                for(int j=0;j<10;j++){
                    int k = level.get(j).indexOf("rating");
                    int r = level.get(j).indexOf("</span>");
                    String s = level.get(j).substring(k, r);
                    int a = s.indexOf("_");
                    int b = s.indexOf("\">");
                    String fi = s.substring(a+1,  b);
                    re_level.add(fi);
                }


                for(int j=0;j<10;j++) {
                    try {
                        final int index;
                        index =j;
                        URL ur = new URL(re_img.get(j));

                        HttpURLConnection conn = (HttpURLConnection) ur.openConnection();
                        conn.setDoInput(true);
                        conn.connect();

                        InputStream is = conn.getInputStream();
                        bitmap[j] = BitmapFactory.decodeStream(is);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            for(int j=0;j<10;j++){
                final int index;
                index =j;
                im[index].setImageBitmap(bitmap[index]);
                tv[index].setText(" 제목 : "+re_name.get(index));
                lv[index].setText(" 연령 : "+re_level.get(index));
                num1[index].setText(" 장르 : "+name_num1.get(index));
                num2[index].setText(" 감독 : "+name_num2.get(index));
                num3[index].setText(" 배우 : "+name_num3.get(index));
            }
        }
    }
}
