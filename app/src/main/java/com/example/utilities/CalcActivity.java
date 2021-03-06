package com.example.utilities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import java.text.DecimalFormat;
import java.util.ArrayList;

public class CalcActivity extends AppCompatActivity implements View.OnClickListener {

    EditText tv_preview;
    TextView tv_result;

    //boolean sign_toggle = false; // 기호가 눌렸는지 안눌렸는지 체크하는 토글
    boolean back_toggle = false; // 백스페이스가 눌렸는지 체크하는 토글
    //boolean dot_toggle = false;

    private String PLUS, MINUS, MULTIPLY, DIVIDE; // "+", "-", "×", "÷"
    private ArrayList<String> list = new ArrayList<>(); // 수식(숫자, 기호)을 담는 List
    private String current = ""; // 기호를 기준으로 현재 입력중인 숫자 ex) 589+413 에서 413이 해당됨.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);

        PLUS = getString(R.string.btn_plus); // "+"
        MINUS = getString(R.string.btn_minus); // "-"
        MULTIPLY = getString(R.string.btn_multiply); // "×"
        DIVIDE = getString(R.string.btn_divide); // "÷"

        setWidget();
    }

    private void setWidget() {
        tv_preview = findViewById(R.id.tv_preview);
        tv_preview.addTextChangedListener(textWatcher);
        tv_result = findViewById(R.id.tv_result);
        tv_result.setTextIsSelectable(true); // selectable

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
        String result = tv_result.getText().toString();

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
            case R.id.btn_dot: addEquation("."); break;

            case R.id.btn_plus:
                if (lastStrIsSign(preview)) // 수식의 마지막 문자가 기호일 경우
                    prev_setText(preview.substring(0, preview.length() - 1).concat(PLUS)); // + 로 교체
                if (lastStrIsNum(preview)) // 수식의 마지막 글자가 숫자일 경우
                    addEquation(PLUS); // + 기호 추가
                break;

            case R.id.btn_minus:
                if (lastStrIsSign(preview)) // 수식의 마지막 문자가 기호일 경우
                    prev_setText(preview.substring(0, preview.length() - 1).concat(MINUS)); // - 로 교체
                if (lastStrIsNum(preview))
                    addEquation(MINUS);
                break;

            case R.id.btn_multiply:
                if (lastStrIsSign(preview)) // 수식의 마지막 문자가 기호일 경우
                    prev_setText(preview.substring(0, preview.length() - 1).concat(MULTIPLY)); // × 로 교체
                if (lastStrIsNum(preview))
                    addEquation(MULTIPLY);
                break;

            case R.id.btn_divide:
                if (lastStrIsSign(preview)) // 수식의 마지막 문자가 기호일 경우
                    prev_setText(preview.substring(0, preview.length() - 1).concat(DIVIDE)); // ÷ 로 교체
                if (lastStrIsNum(preview))
                    addEquation(DIVIDE);
                break;

            case R.id.btn_C:
                tv_preview.setText("");
                tv_result.setText("0");
                list.clear(); // list.clear()가 처음상태일때 실행되면 앱 다운. -> 초기화를 null 에서 new ArrayList<>()로 바꿈으로써 해결
                break;

            case R.id.btn_back:
                if ((preview.length() == 1) || (preview.length() == 0)) // tv_preview 가 1글자 또는 0글자일 경우
                    prev_setText("");
                else
                    prev_setText( preview.substring(0, preview.length()-1) );// substring 으로 마지막 문자를 지운다.

                /*
                  결과값이 있을 때 백스페이스 한번 누르면 결과값이 프리뷰로 가고
                  두번째부턴 일반적인 백스페이스 동작
                 */
                if (!result.equals("0") && !back_toggle) { // 결과값이 있을 때
                    result = result.replace(",", ""); // 결과값의 쉼표를 없애준다.
                    prev_setText(result); // 결과값이 프리뷰로 간다.
                    tv_result.setText("0"); // 결과값은 초기화
                    back_toggle = true;
                }

                tv_preview.setSelection(tv_preview.length()); // EditText 의 Cursor 를 맨뒤로 보낸다.
                break;

            case R.id.btn_result:
                back_toggle = false; // 백스페이스 버튼 초기화
                if (lastStrIsSign(preview)) // 마지막 문자가 기호인 경우
                    preview = preview.substring(0, preview.length()-1); // 마지막 문자를 지운다.
                if (!preview.equals("")) // 입력값이 존재할 경우
                    showResult(calculate(preview));
                break;

            default : break;
        }
    }
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String preview = s.toString(); //tv_preview.getText().toString();
            String split[] = preview.split("(?<=[×÷+-])|(?=[×÷+-])"); // 기호[ +, -, ×, ÷ ] 를 구분하는 정규표현식을 이용해서 숫자, 기호를 split 해줌
//            String str = "";
//            if (s.length() != 0) str = String.valueOf(s.charAt(s.length() - 1)); // 마지막글자(=입력값)를 추출한다.
            current = split[split.length-1]; // 기호를 기준으로 나눈 현재 입력중인 숫자를 가져온다. ex) 597+143 에서 143을 추출
            Log.i("WATCHER", split[split.length-1]);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 덧셈 뺄셈 나눗셈 곱셈 연산 함수
     */
    public String calculate(String str) {
        //String str_split1[] = str.split("\\W"); // 숫자 찾기
        //String str_split2[] = str.split("\\d"); // 기호 찾기
        //String split[] = str.split("(?<=[*/+-])|(?=[*/+-])"); // 기호( +, -, *, / ) 를 구분하는 정규표현식
        String split[] = str.split("(?<=[×÷+-])|(?=[×÷+-])"); // 기호[ +, -, ×, ÷ ] 를 구분하는 정규표현식을 이용해서 숫자, 기호를 split 해줌

        list = new ArrayList<>(); // 숫자, 기호로 나누어 담을 ArrayList 생성.

        int i;
        for(i = 0; i < split.length; i++) {
            if (!split[i].equals("")) // 공백이 포함된 리스트 생성 방지
                list.add(split[i]);  // 수식을 숫자부분과 기호부분으로 나누어 ArrayList 에 담는다.
        }

        // 수식의 마지막 글자가 기호일 경우 그 기호를 삭제해준다.
        if( list.get(list.size()-1).equals(PLUS) || list.get(list.size()-1).equals(MINUS) || list.get(list.size()-1).equals(MULTIPLY) || list.get(list.size()-1).equals(DIVIDE) ) {
            list.remove(list.size()-1);
        }

        // 수식의 첫 숫자가 음수일 경우( ex) -23 + 30 = )
        boolean isMinus = false; // 첫 숫자가 음수인지 아닌지 체크하는 변수
        if (list.get(0).equals(MINUS)) {
            list.remove(0);
            isMinus = true;
        }

        double pre, suf; // 수식의 앞숫자(pre)와 뒤숫자(suf)
        double result; // 수식의 결과

        // 곱셈(곱하기), 나눗셈(나누기)
        for(i = 0; i < list.size(); i++) {
            if (list.get(i).equals(MULTIPLY)) { // * 기호일 경우
                if (isMinus) {
                    pre = Double.parseDouble(list.get(i-1)) * -1; // 기호의 앞숫자(pre)와
                    isMinus = false;
                } else
                    pre = Double.parseDouble(list.get(i-1)); // 기호의 앞숫자(pre)와
                suf = Double.parseDouble(list.get(i+1)); // 기호의 뒷숫자(suf)를
                result = pre * suf; // 곱해준다.

                list.set(i, result+""); // * 기호를 곱하기의 결과로 바꿔준다.
                list.remove(i-1); // pre를 제거한다.
                list.remove(i); // suf를 제거한다.
                i = 0;
            }
            if (list.get(i).equals(DIVIDE)) {
                if (isMinus) pre = Double.parseDouble(list.get(i-1)) * -1;
                else pre = Double.parseDouble(list.get(i-1));
                suf = Double.parseDouble(list.get(i+1));
                result = pre / suf;

                list.set(i, result + "");
                list.remove(i-1);
                list.remove(i);
                i = 0;
            }
        }
        // 덧셈(더하기), 뺄셈(빼기)
        for(i = 0; i < list.size(); i++) {
            if (list.get(i).equals(PLUS)) {
                if (isMinus) {
                    pre = Double.parseDouble(list.get(i-1)) * -1;
                    isMinus = false;
                } else
                    pre = Double.parseDouble(list.get(i-1));
                suf = Double.parseDouble(list.get(i+1));
                result = pre + suf;

                list.set(i, result + "");
                list.remove(i-1);
                list.remove(i);
                i = 0;
            }
            if (list.get(i).equals(MINUS)) {
                if (isMinus) {
                    pre = Double.parseDouble(list.get(i-1)) * -1;
                    isMinus = false;
                } else
                    pre = Double.parseDouble(list.get(i-1));
                suf = Double.parseDouble(list.get(i+1));
                result = pre - suf;

                list.set(i, result + "");
                list.remove(i-1);
                list.remove(i);
                i = 0;
            }
        }

        return list.get(0); // 기호의 앞자리수와 뒷자리수를 계산 후 계산값으로 바꾸기를 반복한 결과를 리턴
    }

    /**
     * 포맷에 맞추어 결과 출력하는 함수
     */
    public void showResult(String str) {
        double num = Double.parseDouble(str);
        String result = new DecimalFormat("###,###.#########").format(num);

        tv_result.setText(result);
    }

    /**
     * preview 텍스트뷰에 수식(숫자와 기호와 .(dot))을 적을 수 있게 해준다.
     * @param str
     */
    public void addEquation(String str) {
        String preview = tv_preview.getText().toString();
        String result = tv_result.getText().toString();

        /*
          결과값이 없으면 preview 창에 수식, 숫자 계속 추가
          결과값이 있으면서 누른 버튼이 기호일 때 result 가 preview 로
          결과값이 있으면서 누른 버튼이 숫자일 때 preview 는 reset 된다.
         */
        if (result.equals("0")) { // 결과값이 없는 경우(= 첫번째 계산일 경우, C버튼 누른 후 계산일 경우)
            if (isNumber(str)) { // 입력한 버튼이 숫자
                if (current.equals("0")) { // (기호를 기준으로 나누어)입력중인 숫자가 0인 경우
                    prev_setText(preview.substring(0, preview.length()-1).concat(str)); // 0으로 시작하는 숫자 입력 방지 ex) 05+3 -> 5+3
                } else
                    prev_setText(preview.concat(str)); // preview 에 입력한 값을 추가해 수식을 이어 적는다.
            }
            else if (isSign(str)) { // 입력한 버튼이 기호(+-*/)
                prev_setText(preview.concat(str)); // preview 에 입력한 값을 추가해 수식을 이어 적는다.
            }
            else if (str.equals(".")) { // 입력한 버튼이 .(dot)
                if (preview.equals("")) // preview 에 아무것도 없으면
                    prev_setText("0."); // 0. 으로 표시
                if (lastStrIsNum(preview) && !isDotExist(current)) // 수식의 마지막 문자가 숫자일 경우 && 입력중인 숫자에 .(dot)이 없을 경우
                    prev_setText(preview.concat(str));
            }
        } else { // 결과값이 있는 경우(= 계산을 한번 이상 했을 경우)
            if (isNumber(str)) { // 숫자를 입력하면 새 수식을 만든다.
                prev_setText(str);
                tv_result.setText("0");
            } else if (isSign(str)) { // 기호 입력시
                result = result.replace(",", ""); // 결과값의 쉼표를 없애준다.
                prev_setText(result.concat(str)); // 결과값을 preview 로 올리고 기호를 추가해 수식을 이어 적는다.
                tv_result.setText("0"); // 결과값은 0으로 만든다.
            } else if (str.equals(".")) { // .(dot)을 입력시
                prev_setText("0."); // 0. 으로 표시
                tv_result.setText("0");
            }
        }
        tv_preview.setSelection(tv_preview.length()); // EditText 의 Cursor 를 맨뒤로 보낸다.
    }

    /**
     * preview 텍스트뷰에 한 글자씩 입력하는 함수
     * @param str
     */
    public void prev_setText(String str) {
//        if (str.equals(".")) {
//            tv_preview.setText("0.");
//        } else
            tv_preview.setText(str);

        tv_preview.setSelection(tv_preview.length()); // EditText 의 Cursor 맨뒤로 보냄
    }

    /**
     * tv_preview 에서 수식을 넘겨받아
     * 맨 끝자리 문자가 숫자인지 아닌지 검사한다. -> 숫자에만 .(dot)을 쓸 수 있도록 하기위함.
     * 파라미터의 마지막 문자가 0 ~ 9 로 끝나면 true 반환.
     */
    public boolean lastStrIsNum(String str) {
        return str.endsWith("0") || str.endsWith("1") || str.endsWith("2") || str.endsWith("3") || str.endsWith("4")
                || str.endsWith("5") || str.endsWith("6") || str.endsWith("7") || str.endsWith("8") || str.endsWith("9");
    }

    /**
     * 누른 버튼이 숫자인지 검사한다.
     */
    public boolean isNumber(String str) {
        return str.equals("0") || str.equals("1") || str.equals("2") || str.equals("3") || str.equals("4")
                || str.equals("5") || str.equals("6") || str.equals("7") || str.equals("8") || str.equals("9");
    }

    /**
     * 맨 끝자리 문자가 기호인지 아닌지 검사한다. -> 기호가 연속으로 여러개 입력되면 replace 되도록함.
     * 파라미터의 마지막 문자가 + - * / 로 끝나면 true 반환.
     */
    public boolean lastStrIsSign(String str) {
        return str.endsWith(PLUS) || str.endsWith(MINUS) || str.endsWith(MULTIPLY) || str.endsWith(DIVIDE);
    }

    /**
     * 누른 버튼이 기호인지 검사한다.
     */
    public boolean isSign(String str) {
        return str.equals(PLUS) || str.equals(MINUS) || str.equals(MULTIPLY) || str.equals(DIVIDE);
    }

    /**
     * 수식에서 현재 입력중인 숫자가 .(dot)을 포함하고 있는지 검사 -> 숫자에서 .(dot)은 하나만 입력가능 하도록 함.
     */
    public boolean isDotExist(String str) {
        String split[] = str.split("(?<=[*/+-])|(?=[*/+-])");
        return split[split.length - 1].contains(".");
    }

}
