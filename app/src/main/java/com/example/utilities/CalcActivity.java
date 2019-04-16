package com.example.utilities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

public class CalcActivity extends AppCompatActivity implements View.OnClickListener {

    EditText tv_preview;
    TextView tv_result;

    boolean sign_toggle = false; // 기호가 눌렸는지 안눌렸는지 체크하는 토글
    boolean back_toggle = false; // 백스페이스가 눌렸는지 체크하는 토글

    ArrayList<String> list = new ArrayList<>(); // 수식(숫자, 기호)을 담는 List


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);

        setWidget();
    }

    private void setWidget() {
        tv_preview = findViewById(R.id.tv_preview);
        tv_result = findViewById(R.id.tv_result);

        findViewById(R.id.btn_0).setOnClickListener(this);
        findViewById(R.id.btn_1).setOnClickListener(this);
        findViewById(R.id.btn_2).setOnClickListener(this);
        findViewById(R.id.btn_3).setOnClickListener(this);
        findViewById(R.id.btn_4).setOnClickListener(this);
        findViewById(R.id.btn_5).setOnClickListener(this);
        findViewById(R.id.btn_6).setOnClickListener(this);
        findViewById(R.id.btn_7).setOnClickListener(this);
        findViewById(R.id.btn_8).setOnClickListener(this);
        findViewById(R.id.btn_9).setOnClickListener(this);
        findViewById(R.id.btn_C).setOnClickListener(this);
        findViewById(R.id.btn_plus).setOnClickListener(this);
        findViewById(R.id.btn_minus).setOnClickListener(this);
        findViewById(R.id.btn_multiply).setOnClickListener(this);
        findViewById(R.id.btn_divide).setOnClickListener(this);
        findViewById(R.id.btn_result).setOnClickListener(this);
        findViewById(R.id.btn_dot).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // 안드로이드 메이저 컴포넌트(액티비티, 서비스, 컨텐트 프로바이더, 브로드캐스트 리시버)를 로드하기 위해서는
        // intent 를 통해서 로드할 컴포넌트를 지정해야한다.
        String preview = tv_preview.getText().toString();
        String result;
        switch (v.getId()) {
            case R.id.btn_0: addEquation("0"); break;
            case R.id.btn_1: addEquation("1"); break;
            case R.id.btn_2: addEquation("2"); break;
            case R.id.btn_3: addEquation("3"); break;
            case R.id.btn_4: addEquation("4"); break;
            case R.id.btn_5: addEquation("5"); break;
            case R.id.btn_6: addEquation("6"); break;
            case R.id.btn_7: addEquation("7"); break;
            case R.id.btn_8: addEquation("8"); break;
            case R.id.btn_9: addEquation("9"); break;
            case R.id.btn_plus:
                if (lastStrIsNum(preview)) {
                    addEquation("+");
                    sign_toggle = true;
                }
                break;
            case R.id.btn_minus:
                if (lastStrIsNum(preview)) {
                    addEquation("-");
                    sign_toggle = true;
                }
                break;
            case R.id.btn_multiply:
                if (lastStrIsNum(preview)) {
                    addEquation("*");
                    sign_toggle = true;
                }
                break;
            case R.id.btn_divide:
                if (lastStrIsNum(preview)) {
                    addEquation("/");
                    sign_toggle = true;
                }
                break;
            case R.id.btn_C:
                tv_preview.setText(" ");
                tv_result.setText("0");
                list.clear(); // list.clear()가 처음상태일때 실행되면 앱 다운. -> 초기화를 null 에서 new ArrayList<>()로 바꿈으로써 해결
                break;
            case R.id.btn_result:
                if (preview.endsWith("+") || preview.endsWith("-") || preview.endsWith("*") || preview.endsWith("/")) // 마지막 문자가 기호인 경우
                    preview = preview.substring(0, preview.length()-1); // 마지막 문자를 지운다.

                showResult(calculate(preview));
                break;
            case R.id.btn_dot:
                if (preview.equals("")) {
                    addEquation("0");
                    addEquation(".");
                }
                if (lastStrIsNum(preview)) { // 수식의 마지막 글자가 숫자일 경우
                    if ( !isDotExist(preview) ) // 수식에서 현재 입력중인 숫자에 .(dot)이 없을 경우
                        addEquation(".");
                }
                break;
            case R.id.btn_back: // TODO: 계산후 결과값이 출력된 상태로 백스페이스로 preview 수식 삭제하다가 숫자 입력시 버그
                back_toggle = true;
                if ((preview.length() == 1) || (preview.length() == 0)) // tv_preview 가 1글자 또는 0글자일 경우
                    prev_setText(" ");
                else
                    prev_setText( preview.substring(0, preview.length()-1) );// substring 으로 마지막 문자를 지운다.
                tv_preview.setSelection(tv_preview.length()); // EditText 의 Cursor 를 맨뒤로 보낸다.
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

        list = new ArrayList<>(); // 숫자, 기호로 나누어 담을 ArrayList 생성.

        int i;
        for(i = 0; i < split.length; i++) {
            list.add(i, split[i]);  // 수식을 숫자부분과 기호부분으로 나누어 ArrayList 에 담는다.
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
                pre = Double.parseDouble(list.get(i - 1)); // TODO: -33+33 = error 앞자리가 음수면 계산 불가능한 오류
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
        double num = Double.parseDouble(str);
        String result = new DecimalFormat("###,###.#########").format(num);

        tv_result.setText(result);
    }

    /**
     * preview 텍스트뷰에 수식(숫자와 기호)을 적을 수 있게 해준다.
     * @param str
     */
    public void addEquation(String str) {
        String preview = tv_preview.getText().toString();
        String result = tv_result.getText().toString();

        /**
         * 결과값이 없으면 preview 창에 수식, 숫자 계속 추가
         * 결과값이 있으면서 누른 버튼이 기호일 때 result 가 preview 로
         * 결과값이 있으면서 누른 버튼이 숫자일 때 preview 는 reset 된다.
         */
        if ( !result.equals("0")) {
            if (str.equals("+") || str.equals("-") || str.equals("*") || str.equals("/") || back_toggle) { // 결과값이 있으면서 누른 버튼이 기호, 백스페이스일 때
                result = result.replace(",", ""); // 결과값의 쉼표를 없애준다.
                prev_setText(result.concat(str)); // 결과 값에 누른 버튼을 추가
                tv_result.setText("0");
                back_toggle = false;
            }
            else { // 결과 값이 있으면서 누른 버튼이 숫자일 때
                prev_setText(str);
                tv_result.setText("0");
                Log.i("TESTT", str); //TODO: 계산 결과값 나온 후 .(dot) 누르면 0. 이 나오도록 변경
            }
        } else
            prev_setText(preview.concat(str)); // 결과값이 없는 경우 preview 에 누른 버튼값을 추가

        tv_preview.setSelection(tv_preview.length()); // EditText 의 Cursor 를 맨뒤로 보낸다.
    }

    /**
     * preview 텍스트뷰에 한 글자씩 입력하는 함수.
     * @param str
     */
    public void prev_setText(String str) {
        if (str.equals("."))
            tv_preview.setText("0.");
        tv_preview.setText(str);
        tv_preview.setSelection(tv_preview.length());
    }

    /**
     * tv_preview 에서 수식을 넘겨받아
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

        if(split[split.length-1].contains("."))
            return true;

        return false;
    }

}
