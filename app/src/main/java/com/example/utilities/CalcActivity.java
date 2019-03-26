package com.example.utilities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class CalcActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    Button btnC, btn_plus, btn_minus, btn_multiply, btn_divide, btn_result, btn_dot, btn_back;
    TextView tv_preview, tv_result;

    boolean sign_toggle = false; // 기호가 눌렸는지 안눌렸는지 체크하는 토글

    ArrayList<String> list = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);

        setWidget();
        setListener();
    }

    private void setWidget() {
        btn0 = findViewById(R.id.btn_0);
        btn1 = findViewById(R.id.btn_1);
        btn2 = findViewById(R.id.btn_2);
        btn3 = findViewById(R.id.btn_3);
        btn4 = findViewById(R.id.btn_4);
        btn5 = findViewById(R.id.btn_5);
        btn6 = findViewById(R.id.btn_6);
        btn7 = findViewById(R.id.btn_7);
        btn8 = findViewById(R.id.btn_8);
        btn9 = findViewById(R.id.btn_9);
        btnC = findViewById(R.id.btn_C);
        btn_plus = findViewById(R.id.btn_plus);
        btn_minus = findViewById(R.id.btn_minus);
        btn_multiply = findViewById(R.id.btn_multiply);
        btn_divide = findViewById(R.id.btn_divide);
        btn_result = findViewById(R.id.btn_result);
        btn_dot = findViewById(R.id.btn_dot);
        btn_back = findViewById(R.id.btn_back);
        tv_preview = findViewById(R.id.tv_preview);
        tv_result = findViewById(R.id.tv_result);
    }
    private void setListener() {
        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btn_plus.setOnClickListener(this);
        btn_minus.setOnClickListener(this);
        btn_multiply.setOnClickListener(this);
        btn_divide.setOnClickListener(this);
        btn_result.setOnClickListener(this);
        btn_dot.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // 안드로이드 메이저 컴포넌트(액티비티, 서비스, 컨텐트 프로바이더, 브로드캐스트 리시버)를 로드하기 위해서는
        // intent를 통해서 로드할 컴포넌트를 지정해야한다.
        switch (v.getId()) {
            case R.id.btn_0:
                addEquation("0");
                break;
            case R.id.btn_1:
                addEquation("1");
                break;
            case R.id.btn_2:
                addEquation("2");
                break;
            case R.id.btn_3:
                addEquation("3");
                break;
            case R.id.btn_4:
                addEquation("4");
                break;
            case R.id.btn_5:
                addEquation("5");
                break;
            case R.id.btn_6:
                addEquation("6");
                break;
            case R.id.btn_7:
                addEquation("7");
                break;
            case R.id.btn_8:
                addEquation("8");
                break;
            case R.id.btn_9:
                addEquation("9");
                break;
            case R.id.btn_plus:
                if (lastStrIsNum(tv_preview.getText().toString())) {
                    addEquation("+");
                    sign_toggle = true;
                }
                break;
            case R.id.btn_minus:
                if (lastStrIsNum(tv_preview.getText().toString())) {
                    addEquation("-");
                    sign_toggle = true;
                }
                break;
            case R.id.btn_multiply:
                if (lastStrIsNum(tv_preview.getText().toString())) {
                    addEquation("*");
                    sign_toggle = true;
                }
                break;
            case R.id.btn_divide:
                if (lastStrIsNum(tv_preview.getText().toString())) {
                    addEquation("/");
                    sign_toggle = true;
                }
                break;
            case R.id.btn_C:
                tv_preview.setText(" ");
                tv_result.setText("0");
                list.clear(); // list.clear()가 처음상태일때 실행되면 앱 다운. -> 초기화를 null에서 new ArrayList<>()로 바꿈으로써 해결
                break;
            case R.id.btn_result:
                if ((tv_result.getText().toString().equals(""))) { // 결과창에 값이 없을 경우엔 prev창에 있는 수식의 결과를 뿌린다.
                    //Toast.makeText(this, "if", Toast.LENGTH_SHORT).show();
                    showResult(calculate(tv_preview.getText().toString()));
                } else if (!(tv_result.getText().toString().equals(""))) { // 결과창에 값이 있을 경우엔
                    //Toast.makeText(this, "else if", Toast.LENGTH_SHORT).show();
                    //tv_result.getText().toString() // TODO 결과창의 결과를 다시 연산하기 85+6 = 계산 후 *2를 하면 97이 아닌 182가 나오도록
                    showResult(calculate(tv_preview.getText().toString())); // 결과값과 다시 연산한다.
                } else {
                    //Toast.makeText(, "", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_dot:
                if (lastStrIsNum(tv_preview.getText().toString())) { // 수식의 마지막 글자가 숫자일 경우
                    if (isDotExist(tv_preview.getText().toString()) == false) { // 수식에서 현재 입력중인 숫자에 .(dot)이 없을 경우
                        addEquation(".");
                    }
                }
                break;
            case R.id.btn_back:
                String str_back = tv_preview.getText().toString();
                String str_del = "";
                if ((str_back.length() == 1) || (str_back.length() == 0)) { // tv_preview가 1글자 또는 0글자일 경우
                    prev_setText(" ");
                } else {
                    str_del = str_back.substring(0, str_back.length() - 1);
                    prev_setText(str_del);
                }
                break;
            default : break;
        }
    }

    /**
     * 덧셈 뺄셈 나눗셈 곱셈 연산 함수
     * @param str
     * @return
     */
    public String calculate(String str) {

        //String str_split1[] = str.split("\\W"); // 숫자 찾기
        //String str_split2[] = str.split("\\d"); // 기호 찾기
        String split[] = str.split("(?<=[*/+-])|(?=[*/+-])"); // 기호( +, -, *, / ) 를 구분하는 정규표현식

        list = new ArrayList<String>(); // 숫자, 기호로 나누어 담을 ArrayList 생성.

        int i = 0;
        for(i = 0; i < split.length; i++) {
            list.add(i, split[i]);  // 수식을 숫자부분과 기호부분으로 나누어 ArrayList에 담는다.
        }

        // 수식의 마지막 글자가 기호일 경우 그 기호를 삭제해준다.
        if( list.get(list.size()-1).equals("+") || list.get(list.size()-1).equals("+") || list.get(list.size()-1).equals("+") || list.get(list.size()-1).equals("+") ) {
            list.remove(list.size()-1);
        }

        double pre = 0, suf = 0; // 수식의 앞숫자(pre)와 뒤숫자(suf)
        double result = 0; // 수식의 결과

        // 곱셈(곱하기), 나눗셈(나누기)
        for(i = 0; i < list.size(); i++) {
            if (list.get(i).equals("*")) { // * 기호일 경우
                pre = Double.parseDouble(list.get(i-1)); // 기호의 앞숫자(pre)와
                suf = Double.parseDouble(list.get(i+1)); // 기호의 뒷숫자(suf)를
                result = pre * suf; // 곱해준다.

                list.set(i, result+""); // * 기호를 곱하기의 결과로 바꿔준다.
                list.remove(i-1); // pre를 제거한다.
                list.remove(i); // suf를 제거한다.
                i = 0;
            }
            if (list.get(i).equals("/")) {
                pre = Double.parseDouble(list.get(i - 1));
                suf = Double.parseDouble(list.get(i + 1));
                result = pre / suf;

                list.set(i, result + "");
                list.remove(i - 1);
                list.remove(i);
                i = 0;
            }
        }
        // 덧셈(더하기), 뺄셈(빼기)
        for(i = 0; i < list.size(); i++) {
            if (list.get(i).equals("+")) {
                pre = Double.parseDouble(list.get(i - 1));
                suf = Double.parseDouble(list.get(i + 1));
                result = pre + suf;

                list.set(i, result + "");
                list.remove(i - 1);
                list.remove(i);
                i = 0;
            }
            if (list.get(i).equals("-")) {
                pre = Double.parseDouble(list.get(i - 1));
                suf = Double.parseDouble(list.get(i + 1));
                result = pre - suf;

                list.set(i, result + "");
                list.remove(i - 1);
                list.remove(i);
                i = 0;
            }
        }

        return list.get(0); // 결과 리턴
    }

    /**
     * 결과 출력 함수
     * @param str
     */
    public void showResult(String str) {
        tv_result.setText(str);
    }

    /**
     * preview텍스트뷰에 수식(숫자와 기호)을 적을 수 있게 해준다.
     * @param str
     */
    public void addEquation(String str) {
        prev_setText(tv_preview.getText() + str);
    }

    /**
     * preview텍스트뷰에 한 글자씩 입력할 수 있는 함수.
     * @param str
     */
    public void prev_setText(String str) {
        tv_preview.setText(str);
    }

    /**
     * tv_preview에서 수식을 넘겨받아
     * 맨 끝자리 문자가 숫자인지 아닌지 검사한다. -> 숫자에만 .(dot)을 쓸 수 있도록 함.
     * @param str
     * @return
     */
    public boolean lastStrIsNum(String str) {
        if(str.endsWith("0") || str.endsWith("1") || str.endsWith("2") || str.endsWith("3") || str.endsWith("4")
                || str.endsWith("5") || str.endsWith("6") || str.endsWith("7") || str.endsWith("8") || str.endsWith("9")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * tv_preview에서 수식을 넘겨받아
     * 수식에서 현재 입력중인 숫자가 .(dot)을 포함하고 있는지 검사 -> 숫자에서 .(dot)은 하나만 입력가능 하도록 함.
     * @param str
     * @return
     */
    public boolean isDotExist(String str) {

        String split[] = str.split("(?<=[*/+-])|(?=[*/+-])");

        if(split[split.length-1].contains(".")) {
            return true;
        }
        return false;
    }

}
