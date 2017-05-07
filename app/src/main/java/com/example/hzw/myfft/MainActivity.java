package com.example.hzw.myfft;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class MainActivity extends AppCompatActivity {
    public Button mybutton = null
            ,mybutton0 = null
            ,mybutton1 = null
            ,mybutton2 = null
            ,mybutton3 = null
            ,mybutton4 = null
            ,mybutton5 = null
            ,mybutton6 = null
            ,mybutton7 = null
            ,mybutton8 = null
            ,mybutton9 = null
            ,mybutton10 = null
            ,mybutton11 = null
            ;
    //private Button inputButton[] = {null,null,null,null,null,null,null,null,null,null,null};
    //public TextView myshow = null;
    public EditText input = null;
    int myIntensity = 5;
    Handler mHandler;
    String zero = "";//0信号
    ProgressDialog proDia = null;
    int input_length = 0;
    int dft_length = 0;
    String choose[] = {"选择频谱图", "选择时域图"};
    int myN = 0;
    float fs_1000 = 1000 / 25000, fs_2000 = 2000 / 25000, defs = 500 / 25000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置成全屏模式
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        proDia = new ProgressDialog(MainActivity.this);
        proDia.setTitle("提示");
        proDia.setMessage("请耐心等待...");
        init();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myN++;
                Snackbar.make(view, choose[myN % 2], Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        // progressDialog.dismiss();
                        // myshow.setText(result);
                        //proDia.show();
                        Toast.makeText(MainActivity.this, "请输入数字", Toast.LENGTH_SHORT).show();

                        break;
                    case 1:
                        //proDia.onStart();
                        //proDia.show();

                        break;
                    case 2:
                        proDia.dismiss();
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
                // showToast("数据库是最新的！");
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void init() {
        mybutton = (Button) findViewById(R.id.enter);
        //myshow = (TextView) findViewById(R.id.show);
        input = (EditText) findViewById(R.id.input);
        mybutton0 = (Button) findViewById(R.id.input0);
        mybutton1 = (Button) findViewById(R.id.input1);
        mybutton2 = (Button) findViewById(R.id.input2);
        mybutton3 = (Button) findViewById(R.id.input3);
        mybutton4 = (Button) findViewById(R.id.input4);
        mybutton5 = (Button) findViewById(R.id.input5);
        mybutton6 = (Button) findViewById(R.id.input6);
        mybutton7 = (Button) findViewById(R.id.input7);
        mybutton8 = (Button) findViewById(R.id.input8);
        mybutton9 = (Button) findViewById(R.id.input9);
        mybutton10 = (Button) findViewById(R.id.delete);
        //inputButton[0].setOnClickListener(new ButtonClick());
        /*for(int i=0;i<10;i++)
            inputButton[i].setOnClickListener(new ButtonClick());*/
        mybutton.setOnClickListener(new ButtonClick());
        mybutton0.setOnClickListener(new ButtonClick());
        mybutton1.setOnClickListener(new ButtonClick());
        mybutton2.setOnClickListener(new ButtonClick());
        mybutton3.setOnClickListener(new ButtonClick());
        mybutton4.setOnClickListener(new ButtonClick());
        mybutton5.setOnClickListener(new ButtonClick());
        mybutton6.setOnClickListener(new ButtonClick());
        mybutton7.setOnClickListener(new ButtonClick());
        mybutton8.setOnClickListener(new ButtonClick());
        mybutton9.setOnClickListener(new ButtonClick());
        mybutton10.setOnClickListener(new ButtonClick());


    }

    /*
卷积：输入hn*xn
输出yn
*/
    void conv(float yn[], float hn[], float xn[], int L1, int L2) {
        float num = 0;
        for (int i = 0; i < L1 + L2 - 1; i++) {
            yn[i] = 0;
        }
        for (int i = 0; i < L1 + L2 - 1; i++) {
            if (i < L1) {
                for (int j = 0; j <= i; j++) {
                    yn[i] += hn[j] * xn[i - j];
                    //yn[0]=hn[0]*xn[0]
                    //yn[1]=hn[0]*xn[1]+hn[1]*xn[0];
                    //yn[2]=hn[0]*xn[2]+hn[1]*xn[1]+hn[2]*xn[0];
                    //yn[3]=hn[0]*xn[3]+hn[1]*xn[2]+hn[2]*xn[1]+hn[3]*xn[0];
                }
            } else {
                int k = L1 - 1;
                for (int j = 1; j <= L1 + L2 - 1 - i; j++, k--) {
                    yn[i] += xn[k] * hn[i - k];
                }
            }
        }
    }


    /*
    * an 实部 bn 虚部
    * 返回格式 实部#虚部
    * 1 dft  -1 ifft
    * */
    public String dft_in(String an, String bn, int choose) {
        String temp = "";
        String[] ak = an.split(",");
        String[] bk = bn.split(",");
        int number = ak.length;
        int N = 1;
        for (; ; N *= 2) {
            if (N / number == 1)
                break;
        }
        dft_length = N;
        float Rn[] = new float[N];
        float In[] = new float[N];
        float Rr[] = new float[N];
        float Ii[] = new float[N];
        for (int i = 0; i < N; i++) {
            if (i < number) {
                Rn[i] = Float.parseFloat(ak[i]);
                In[i] = Float.parseFloat(bk[i]);
            } else {
                Rn[i] = 0;
                In[i] = 0;
            }
            Rr[i] = 0;
            Ii[i] = 0;
        }
        for (int i = 0; i < N; i++) {
            Rr[i] = 0;
            Ii[i] = 0;
            for (int j = 0; j < number; j++) {
                Rr[i] += Rn[j] * cos(PI * (float) (2 * i * j) / (float) N) + choose * In[j] * sin(PI * (float) (2 * i * j) / (float) N);
                Ii[i] += In[j] * cos(PI * (float) (2 * i * j) / (float) N) + choose * Rn[j] * sin(PI * (float) (2 * i * j) / (float) N);
            }
        }
        if (choose == -1) {
            for (int i = 0; i < N; i++) {
                Rr[i] /= N;
                Ii[i] /= N;
            }
        }
        for (int i = 0; i < N; i++) {
            temp += "" + Rr[i];
            if (i != N - 1)
                temp += ",";
        }
        temp += "#";
        for (int i = 0; i < N; i++) {
            temp += "" + Ii[i];
            if (i != N - 1)
                temp += ",";
        }
        return temp;
    }
    //1/200s  f1=500 f2=1000 fs=1000hz fs*T=5

    /*****************
     * 输入参数：DFT和IDFT变换序列的实部an和虚部bn
     * n_length 序列长度
     * N n点变换
     * choose 1.....DFT
     * -1....IDFT
     * 输出：ak，bk
     ******************/
    void re_DFT(float an[], float bn[], float ak[], float bk[], float n_length, float N, float choose) {
        if (choose == 1) {
            for (int i = 0; i < N; i++) {
                ak[i] = 0;
                bk[i] = 0;
                for (int j = 0; j < n_length; j++) {
                    ak[i] += an[j] * cos(PI * (float) (2 * i * j) / (float) N) + choose * bn[j] * sin(PI * (float) (2 * i * j) / (float) N);
                    bk[i] += bn[j] * cos(PI * (float) (2 * i * j) / (float) N) + choose * an[j] * sin(PI * (float) (2 * i * j) / (float) N);
                }
            }
        }
        if (choose == -1) {
            for (int i = 0; i < N; i++) {
                ak[i] /= N;
                bk[i] /= N;
            }
        }
    }

    public String encode(String encodein) {
        String temp = "";
        String b[] = encodein.split("");
        for (int i = 1; i < b.length; i++) {
            temp += encode_int(Integer.parseInt(b[i]));
        }
        return temp;
    }

    public String encode_int(int number) {
        String temp[] = {"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001"};
        return temp[number];
    }

    public String decode(String decodein) {
        String temp = "";
        String b[] = decodein.split("");
        return temp;
    }

    public String dncode_int(int number) {
        String temp[] = {"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001"};
        return temp[number];
    }

    public float fskmode(int input, int number) {
        if (input == 0) {
            return (float) sin(500 * 2 * PI * number / 25000);//0-1/500 1/5000
        } else {
            return (float) sin(1000 * 2 * PI * number / 25000);//1/1000
        }
    }

    public String AddNoise(int length) {
        int i, k;
        float r0 = 0;
        String temp = "";
        float r[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        for (k = 0; k < length; k++) {
            if (k != 0)
                temp += ",";
            for (i = 0; i < 12; i++) {
                r[i] = (float) (2 * Math.random() - 1);
                r0 += r[i];
                r0 = (float) 4 * r0 / (float) 12.0;
            }
            temp += "" + r0;
            //No[k] = r0;
        }
        //Dft(No, 16*M);
        return temp;
    }

    public String addOfNoise(String sign, String noise) {
        String[] ss = sign.split(",");
        String[] nn = noise.split(",");
        String temp1 = "";
        int number = nn.length;
        for (int i = 0; i < number; i++) {
            temp1 += "" + (Float.parseFloat(ss[i]) + Float.parseFloat(nn[i]));
            if (i != number - 1)
                temp1 += ",";
        }
        return temp1;
    }

    private String returnAbsolute(String R) {
        String[] temp = R.split("#");
        String[] rr = temp[0].split(",");
        String[] ii = temp[1].split(",");
        String haha = "";
        int numer = rr.length;
        for (int i = 0; i < numer; i++) {
            haha += "" + sqrt(Float.parseFloat(rr[i]) * Float.parseFloat(rr[i]) + Float.parseFloat(ii[i]) * Float.parseFloat(ii[i]));
            if (i != numer - 1)
                haha += ",";
        }
        return haha;
    }

    private String filtering(String sign) {
        String temp = "";
        String sign_in[] = sign.split("#");
        String sign_R[] = sign_in[0].split(",");
        String sign_I[] = sign_in[1].split(",");
        int length_in = sign_R.length;
        int n1 = (int) (length_in / 25);
        int n2 = (int) (2 * length_in / 25);
        int dn = (int) (length_in / 100);
        //
        for (int i = 0; i < length_in; i++) {
            if (i <= length_in / 2) {
                if (i > 2 * dn + n2) {
                    sign_R[i] = "0";
                    sign_I[i] = "0";
                }
                /*if(i<dn)
                {
                    sign_R[i] = (Float.parseFloat(sign_R[i])/dn)+"";
                    sign_I[i] = (Float.parseFloat(sign_I[i])/dn)+"";
                }
                else if (i > n2+dn+dn) {
                    sign_R[i] = "0";
                    sign_I[i] = "0";
                }
                else if (i>n2+dn)
                {
                    sign_R[i] = (Float.parseFloat(sign_R[i])*(n2+dn-i)/dn)+"";
                    sign_I[i] = (Float.parseFloat(sign_I[i])*(n2+dn-i)/dn)+"";
                }*/
            } else {
                sign_R[i] = sign_R[length_in - i];
                sign_I[i] = Float.parseFloat(sign_I[length_in - i]) * (-1) + "";
            }
        }
        for (int i = 0; i < length_in; i++) {
            temp += sign_R[i];
            if (i != length_in - 1)
                temp += ",";
        }
        temp += "#";
        for (int i = 0; i < length_in; i++) {
            temp += sign_I[i];
            if (i != length_in - 1)
                temp += ",";
        }
        return temp;
    }

    private String judge_sign(String signIn) {
        String inuse[] = signIn.split(",");
        String temp = "";
        //dft_length-input_length  1024 800 223 1022-1023
        //1023-50=973 923 873
        //
        float i1 = 0, i2 = 0, i3 = 0, i4 = 0;
        for (int i = dft_length - 1; i > dft_length - input_length; ) {
            i1 = Float.parseFloat(inuse[i - 6]);
            i2 = Float.parseFloat(inuse[i - 18]);
            i3 = Float.parseFloat(inuse[i - 30]);
            i4 = Float.parseFloat(inuse[i - 42]);
            if (i1 * i2 > 0) {
                temp += "0";
            } else {
                temp += "1";
            }
            i -= 50;
        }
        String fanh[] = temp.split("");
        temp = "";
        for (int i = 1; i < fanh.length; i++) {
            temp += fanh[i];
            if (i != fanh.length - 1)
                temp += ",";
        }
        return temp;
    }

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            //Log.e("1111", "111111111");
            // TODO Auto-generated method stub
            try {
                proDia.onStart();
                proDia.show();
                //proDia = ProgressDialog.show(MainActivity.this, "搜索网络", "请耐心等待...");
                //proDia.show();
            } catch (Exception e) {
            }
        }
    });

    void myfunction() {
        //proDia.show();

        String mystr = encode(input.getText().toString());
        if (mystr.length() == 0) {
            mHandler.sendEmptyMessage(0);
            return;
        }
        thread.run();
        //ProgressDialog dialog2 = ProgressDialog.show(this, "提示", "计算中");
        String temp = "";
        String b[] = mystr.split("");
        int number = 0;
        for (int i = 1; i < b.length; i++) {
            number = Integer.parseInt(b[i]);
            for (int j = 0; j < 50; j++)
                temp += fskmode(number, j) + ",";
        }
        //
        //re_DFT();0-50
        int noise_count = input.getText().toString().length() * 200;
        input_length = noise_count;
        zero = "0";
        for (int i = 0; i < noise_count; i++) {
            zero += "0";
            if (i < noise_count - 1) zero += ",";
        }
        String mytempp = AddNoise(input.getText().toString().length() * 200);//4位800个
        String jianyan = addOfNoise(temp, mytempp);//加噪声的信号

        String tempFFT = dft_in(temp, zero, 1);///sign fft
        mytempp = null;
        String dft_numer = dft_in(jianyan, zero, 1);//加噪声信号fft
        ///String dft__exam[] = dft_numer.split("#");
        //滤波
        String after_fil = filtering(dft_numer);//滤波fft
        String dft_r[] = after_fil.split("#");
        //after_fil = null;
        //dft_numer = null;

        //判决
        String idft_number = dft_in(dft_r[0], dft_r[1], -1);
        ///String idft_r[] = idft_number.split("#");
        //dft_r=null;
        String idft_exam = dft_in(dft_r[0], dft_r[1], -1);
        String idft_exam1[] = idft_exam.split("#");

        String panjiu = judge_sign(idft_exam1[0]);
        idft_exam = null;
        dft_r = null;
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, ChartActivity.class);
        Bundle bundle = new Bundle();//returnAbsolute(dft_numer)idft_r[1]returnAbsolute(dft_numer)jianyan+
        String transform = "";
        if (myN % 2 == 1) {
            transform = temp +//调制后信号
                    "#" + jianyan +//加噪声
                    "#" + idft_exam1[0] +//滤波
                    "#" + panjiu;//判决
        } else {
            transform = returnAbsolute(tempFFT) +//调制后信号
                    "#" + returnAbsolute(dft_numer) +//加噪声
                    "#" + returnAbsolute(after_fil);//滤波
            //"#" + panjiu;//判决
        }
        bundle.putString("canshu", transform);

        intent.putExtras(bundle);
        //dialog2.dismiss();
        mHandler.sendEmptyMessage(2);
        //proDia.dismiss();
        startActivity(intent);
    }

    class ButtonClick implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.input0:
                    input.setText(input.getText().toString()+"0");
                    break;
                case R.id.input1:
                    input.setText(input.getText().toString()+"1");
                    break;
                case R.id.input2:
                    input.setText(input.getText().toString()+"2");
                    break;
                case R.id.input3:
                    input.setText(input.getText().toString()+"3");
                    break;
                case R.id.input4:
                    input.setText(input.getText().toString()+"4");
                    break;
                case R.id.input5:
                    input.setText(input.getText().toString()+"5");
                    break;
                case R.id.input6:
                    input.setText(input.getText().toString()+"6");
                    break;
                case R.id.input7:
                    input.setText(input.getText().toString()+"7");
                    break;
                case R.id.input8:
                    input.setText(input.getText().toString()+"8");
                    break;
                case R.id.input9:
                    input.setText(input.getText().toString()+"9");
                    break;
                case R.id.delete:
                    String use="";
                    String temp[]=input.getText().toString().split("");
                    for (int i=0;i<temp.length-1;i++)
                        use+=temp[i];
                    input.setText(use);
                    break;
                case R.id.enter:
                    myfunction();
                    break;
                default:
                    break;
            }

        }
    }
}
