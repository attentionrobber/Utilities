package com.example.utilities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.utilities.Util_Class.Logger;

public class UnitActivity extends AppCompatActivity {

    Button btn_length, btn_area, btn_weight;
    EditText editText_length, editText_area, editText_weight;
    TextView tv_length_output, tv_area_output, tv_weight_output;
    TextView tv_mm, tv_cm, tv_m, tv_km, tv_inch, tv_ft, tv_yd, tv_mile;
    TextView tv_mg, tv_g, tv_kg, tv_ton, tv_kt, tv_lb, tv_gr, tv_oz;
    TextView tv_m2, tv_km2, tv_ft2, tv_yd2, tv_a, tv_ha, tv_ac, tv_pyeong;
    LinearLayout layout_length, layout_area, layout_weight;
    Spinner spinner_length_from, spinner_length_to, spinner_area_from, spinner_area_to, spinner_weight_from, spinner_weight_to;

    String spinner_length[] = {"밀리미터(mm)", "센티미터(cm)", "미터(m)", "킬로미터(km)", "인치(inch)", "피트(ft)", "야드(yd)", "마일(mile)"};
    String spinner_area[] = {"제곱미터(m^2)", "제곱키로미터(km^2)", "제곱피트(ft^2)", "제곱야드(yd^2)", "아르(a)", "헥타르(ha)", "에이커(ac)", "평"};
    String spinner_weight[] = {"밀리그램(mg)", "그램(g)", "킬로그램(kg)", "톤(t)", "킬로톤(kt)", "파운드(lb)", "그레인(gr)", "온스(oz)"};

    double output_mm, output_cm, output_m, output_km, output_inch, output_ft, output_yd, output_mile;
    double output_mg, output_g, output_kg, output_ton, output_kt, output_lb, output_gr, output_oz;
    double output_m2, output_km2, output_ft2, output_yd2, output_a, output_ha, output_ac, output_pyeong;

    final String LENGTH = "LENGTH";
    final String WEIGHT = "WEIGHT";
    final String AREA = "AREA";
    String unitFlag = "LENGTH"; // 어떤 종류의 단위를 선택했는지 나타내는 상태 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);

        setWidget();
        setListener();
    }

    private void setWidget() {
        btn_length = findViewById(R.id.btn_length);
        btn_area = findViewById(R.id.btn_area);
        btn_weight = findViewById(R.id.btn_weight);

        editText_length = findViewById(R.id.editText_length);
        editText_area = findViewById(R.id.editText_area);
        editText_weight = findViewById(R.id.editText_weight);

        tv_length_output = findViewById(R.id.tv_length_output);
        tv_area_output = findViewById(R.id.tv_area_output);
        tv_weight_output = findViewById(R.id.tv_weight_output);

        tv_mm = findViewById(R.id.tv_mm);
        tv_cm = findViewById(R.id.tv_cm);
        tv_m = findViewById(R.id.tv_m);
        tv_km = findViewById(R.id.tv_km);
        tv_inch = findViewById(R.id.tv_inch);
        tv_ft = findViewById(R.id.tv_ft);
        tv_yd = findViewById(R.id.tv_yd);
        tv_mile = findViewById(R.id.tv_mile);

        tv_mg = findViewById(R.id.tv_mg);
        tv_g = findViewById(R.id.tv_g);
        tv_kg = findViewById(R.id.tv_kg);
        tv_ton = findViewById(R.id.tv_ton);
        tv_kt = findViewById(R.id.tv_kt);
        tv_lb = findViewById(R.id.tv_lb);
        tv_gr = findViewById(R.id.tv_gr);
        tv_oz = findViewById(R.id.tv_oz);

        tv_m2 = findViewById(R.id.tv_m2);
        tv_km2 = findViewById(R.id.tv_km2);
        tv_ft2 = findViewById(R.id.tv_ft2);
        tv_yd2 = findViewById(R.id.tv_yd2);
        tv_a = findViewById(R.id.tv_a);
        tv_ha = findViewById(R.id.tv_ha);
        tv_ac = findViewById(R.id.tv_ac);
        tv_pyeong = findViewById(R.id.tv_pyeong);

        spinner_length_from = findViewById(R.id.spinner_length_from);
        spinner_length_to = findViewById(R.id.spinner_length_to);
        spinner_area_from = findViewById(R.id.spinner_area_from);
        spinner_area_to = findViewById(R.id.spinner_area_to);
        spinner_weight_from = findViewById(R.id.spinner_weight_from);
        spinner_weight_to = findViewById(R.id.spinner_weight_to);

        layout_length = findViewById(R.id.layout_length);
        layout_area = findViewById(R.id.layout_area);
        layout_weight = findViewById(R.id.layout_weight);

        spinner_length_from = findViewById(R.id.spinner_length_from);
        spinner_length_to = findViewById(R.id.spinner_length_to);
        spinner_area_from =findViewById(R.id.spinner_area_from);
        spinner_area_to =findViewById(R.id.spinner_area_to);
        spinner_weight_from = findViewById(R.id.spinner_weight_from);
        spinner_weight_to = findViewById(R.id.spinner_weight_to);

        // 스피너 데이터(length, area, weight)를 어댑터로 생성
        ArrayAdapter<String> adapter_length = new ArrayAdapter<>(this, R.layout.spniner_dropdown_item, spinner_length); // R. 은 내가 정의한 리소스 android.R. 은 android에 정의되어있는 리소스
        ArrayAdapter<String> adapter_area = new ArrayAdapter<>(this, R.layout.spniner_dropdown_item, spinner_area);
        ArrayAdapter<String> adapter_weight = new ArrayAdapter<>(this, R.layout.spniner_dropdown_item, spinner_weight);

        // 스피너 어댑터에 등록
        spinner_length_from.setAdapter(adapter_length);
        spinner_length_to.setAdapter(adapter_length);
        spinner_area_from.setAdapter(adapter_area);
        spinner_area_to.setAdapter(adapter_area);
        spinner_weight_from.setAdapter(adapter_weight);
        spinner_weight_to.setAdapter(adapter_weight);
    } // setWidget();

    private void setListener() {
        btn_length.setOnClickListener(clickListener);
        btn_area.setOnClickListener(clickListener);
        btn_weight.setOnClickListener(clickListener);

        editText_area.addTextChangedListener(textWatcher);
        editText_length.addTextChangedListener(textWatcher);
        editText_weight.addTextChangedListener(textWatcher);

        spinner_length_from.setOnItemSelectedListener(itemSelectedListener);
        spinner_length_to.setOnItemSelectedListener(toItemSelectedListener);
        spinner_weight_from.setOnItemSelectedListener(itemSelectedListener);
        spinner_weight_to.setOnItemSelectedListener(toItemSelectedListener);
        spinner_area_from.setOnItemSelectedListener(itemSelectedListener);
        spinner_area_to.setOnItemSelectedListener(toItemSelectedListener);
    } // setListener();

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.btn_length :
                    layout_length.setVisibility(View.VISIBLE);
                    layout_area.setVisibility(View.GONE);
                    layout_weight.setVisibility(View.GONE);
                    unitFlag = LENGTH;
                    break;
                case R.id.btn_weight :
                    layout_length.setVisibility(View.GONE);
                    layout_area.setVisibility(View.GONE);
                    layout_weight.setVisibility(View.VISIBLE);
                    unitFlag = WEIGHT;
                    break;
                case R.id.btn_area :
                    layout_length.setVisibility(View.GONE);
                    layout_area.setVisibility(View.VISIBLE);
                    layout_weight.setVisibility(View.GONE);
                    unitFlag = AREA;
                    break;
            }
        }
    }; // clickListener

    /**
     * TextView에 숫자를 입력할때 마다 결과값을 실시간으로 보여준다.
     */
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(!(s.toString().equals(""))) {
                switch (unitFlag) {
                    case LENGTH:
                        convertLength(spinner_length_from.getSelectedItemPosition(), spinner_length_to.getSelectedItemPosition(), editText_length.getText().toString());
                        break;
                    case WEIGHT:
                        convertWeight(spinner_weight_from.getSelectedItemPosition(), spinner_weight_to.getSelectedItemPosition(), editText_weight.getText().toString());
                        break;
                    case AREA:
                        convertArea(spinner_area_from.getSelectedItemPosition(), spinner_area_to.getSelectedItemPosition(), editText_area.getText().toString());
                        break;
                    default: break;
                }
            } else if(s.toString().equals("")) {
                tv_length_output.setText("0"); tv_weight_output.setText("0"); tv_area_output.setText("0");
                tv_mm.setText("0");tv_cm.setText("0");tv_m.setText("0");tv_km.setText("0");tv_inch.setText("0");tv_ft.setText("0");tv_yd.setText("0");tv_mile.setText("0");
                tv_mg.setText("0");tv_g.setText("0");tv_kg.setText("0");tv_ton.setText("0");tv_kt.setText("0");tv_lb.setText("0");tv_gr.setText("0");tv_oz.setText("0");
                tv_m2.setText("0");tv_km2.setText("0");tv_ft2.setText("0");tv_yd2.setText("0");tv_a.setText("0");tv_ha.setText("0");tv_ac.setText("0");tv_pyeong.setText("0");
            }
        }
    }; // textWatcher

    /**
     *  앞쪽에 있는 Spinner를 선택했을 때(바뀔 때)의 Listener
     */
    AdapterView.OnItemSelectedListener itemSelectedListener;
    {
        itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (unitFlag) {
                    case LENGTH:
                        convertLength(position, spinner_length_to.getSelectedItemPosition(), editText_length.getText().toString());
                        break;
                    case WEIGHT:
                        convertWeight(position, spinner_weight_to.getSelectedItemPosition(), editText_weight.getText().toString());
                        break;
                    case AREA:
                        convertArea(position, spinner_area_to.getSelectedItemPosition(), editText_area.getText().toString());
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    } // itemSelectedListener

    /**
     * 뒷쪽에 있는 Spinner를 선택했을 때(바뀔 때)의 Listener
     */
    AdapterView.OnItemSelectedListener toItemSelectedListener;
    {
        toItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (unitFlag) {
                    case LENGTH:
                        convertLength(spinner_length_from.getSelectedItemPosition(), position, editText_length.getText().toString());
                        break;
                    case WEIGHT:
                        convertWeight(spinner_weight_from.getSelectedItemPosition(), position, editText_weight.getText().toString());
                        break;
                    case AREA:
                        convertArea(spinner_area_from.getSelectedItemPosition(), position, editText_area.getText().toString());
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    } // toItemSelectedListener


    /**
     * 숫자 표시 형식을 맞춰주고 TextView setText 를 해주는 함수
     * @param tv
     * @param num
     */
    @SuppressLint("DefaultLocale")
    public void setTextAndStringFormat(TextView tv, double num) {
        String format;
        if(num == (int)num) { // num 값이 자연수일 경우 소수점 없이 출력
            format = String.format("%.0f", num);
        } else { // num 값이 실수일 경우 최대 소수점 6자리까지 출력
            format = String.format("%.6f", num);
            num = Double.parseDouble(format); // 실수 num을 강제로 6자리까지 변환
            if (format.endsWith("0")) { // 소수점이 125.500000와 같이 끝날 경우 125.5로 표시
                format = String.format("%s", num);
            }
        }
        //Logger.print("UnitAc","value: "+format);
        tv.setText(format);
    }

    /**
     * spinner_to (스피너의 뒷부분(출력값))을 switch 해주는 함수
     * @param slT
     */
    private void switchResult(int slT) {
        switch (unitFlag) {
            case LENGTH:
                switch (slT) {
                    case 0: setTextAndStringFormat(tv_length_output, output_mm); break;
                    case 1: setTextAndStringFormat(tv_length_output, output_cm); break;
                    case 2: setTextAndStringFormat(tv_length_output, output_m); break;
                    case 3: setTextAndStringFormat(tv_length_output, output_km); break;
                    case 4: setTextAndStringFormat(tv_length_output, output_inch); break;
                    case 5: setTextAndStringFormat(tv_length_output, output_ft); break;
                    case 6: setTextAndStringFormat(tv_length_output, output_yd); break;
                    case 7: setTextAndStringFormat(tv_length_output, output_mile); break;
                    default: break;
                } break;
            case WEIGHT:
                switch (slT) {
                    case 0: setTextAndStringFormat(tv_weight_output, output_mg); break;
                    case 1: setTextAndStringFormat(tv_weight_output, output_g); break;
                    case 2: setTextAndStringFormat(tv_weight_output, output_kg); break;
                    case 3: setTextAndStringFormat(tv_weight_output, output_ton); break;
                    case 4: setTextAndStringFormat(tv_weight_output, output_kt); break;
                    case 5: setTextAndStringFormat(tv_weight_output, output_lb); break;
                    case 6: setTextAndStringFormat(tv_weight_output, output_gr); break;
                    case 7: setTextAndStringFormat(tv_weight_output, output_oz); break;
                    default: break;
                } break;
            case AREA:
                switch (slT) {
                    case 0: setTextAndStringFormat(tv_area_output, output_m2); break;
                    case 1: setTextAndStringFormat(tv_area_output, output_km2); break;
                    case 2: setTextAndStringFormat(tv_area_output, output_ft2); break;
                    case 3: setTextAndStringFormat(tv_area_output, output_yd2); break;
                    case 4: setTextAndStringFormat(tv_area_output, output_a); break;
                    case 5: setTextAndStringFormat(tv_area_output, output_ha); break;
                    case 6: setTextAndStringFormat(tv_area_output, output_ac); break;
                    case 7: setTextAndStringFormat(tv_area_output, output_pyeong); break;
                    default: break;
                }
                break;
        }
    }

    /**
     * 길이를 변환하는 함수 ver.2
     * 0 is mm,  1 is cm,  2 is m,  3 is km,  4 is inch,  5 is ft,  6 is yd,  7 is mile
     * @param slF(spinner_length_from)
     * @param slT(spinner_length_to)
     * @param editText(value)
     */
    private void convertLength(int slF, int slT, String editText) {

        double input = 0;
        if( !(editText_length.getText().toString().equals("")) ) {
            input = Double.parseDouble(editText); // 입력값을 editText 에서 가져온다.
        }

        if (slF == 0) { // mm를 다른 단위로 변환
            output_mm = input; setTextAndStringFormat(tv_mm, output_mm);
            output_cm = input * 0.1; setTextAndStringFormat(tv_cm, output_cm); // mm -> cm
            output_m = input * 0.001; setTextAndStringFormat(tv_m, output_m); // mm -> m
            output_km = input * 0.000001; setTextAndStringFormat(tv_km, output_km); // mm -> km
            output_inch = input * 0.03937; setTextAndStringFormat(tv_inch, output_inch); // mm -> inch
            output_ft = input * 0.003281; setTextAndStringFormat(tv_ft, output_ft); // mm -> ft
            output_yd = input * 0.001094; setTextAndStringFormat(tv_yd, output_yd); // mm -> yd
            output_mile = input * 0.000000621; setTextAndStringFormat(tv_mile, output_mile); // mm -> mile
            switchResult(slT);
        } else if (slF == 1) {
            output_mm = input * 10; setTextAndStringFormat(tv_mm, output_mm);
            output_cm = input; setTextAndStringFormat(tv_cm, output_cm);
            output_m = input * 0.01; setTextAndStringFormat(tv_m, output_m);
            output_km = input * 0.00001; setTextAndStringFormat(tv_km, output_km);
            output_inch = input * 0.393701; setTextAndStringFormat(tv_inch, output_inch);
            output_ft = input * 0.032808; setTextAndStringFormat(tv_ft, output_ft);
            output_yd = input * 0.010936; setTextAndStringFormat(tv_yd, output_yd);
            output_mile = input * 0.00000621; setTextAndStringFormat(tv_mile, output_mile);
            switchResult(slT);
        } else if (slF == 2) {
            output_mm = input * 1000; setTextAndStringFormat(tv_mm, output_mm);
            output_cm = input * 10; setTextAndStringFormat(tv_cm, output_cm);
            output_m = input; setTextAndStringFormat(tv_m, output_m);
            output_km = input * 0.001; setTextAndStringFormat(tv_km, output_km);
            output_inch = input * 39.370079; setTextAndStringFormat(tv_inch, output_inch);
            output_ft = input * 3.28084; setTextAndStringFormat(tv_ft, output_ft);
            output_yd = input * 1.093613;setTextAndStringFormat(tv_yd, output_yd);
            output_mile = input * 0.00062137; setTextAndStringFormat(tv_mile, output_mile);
            switchResult(slT);
        } else if (slF == 3) {
            output_mm = input * 1000; setTextAndStringFormat(tv_mm, output_mm);
            output_cm = input * 0.1; setTextAndStringFormat(tv_cm, output_cm);
            output_m = input * 0.001; setTextAndStringFormat(tv_m, output_m);
            output_km = input; setTextAndStringFormat(tv_km, output_km);
            output_inch = input * 39370.0787; setTextAndStringFormat(tv_inch, output_inch);
            output_ft = input * 3280.8399; setTextAndStringFormat(tv_ft, output_ft);
            output_yd = input * 1093.6133; setTextAndStringFormat(tv_yd, output_yd);
            output_mile = input * 0.621371; setTextAndStringFormat(tv_mile, output_mile);
            switchResult(slT);
        } else if (slF == 4) {
            output_mm = input * 25.4; setTextAndStringFormat(tv_mm, output_mm);
            output_cm = input * 2.54; setTextAndStringFormat(tv_cm, output_cm);
            output_m = input * 0.0254; setTextAndStringFormat(tv_m, output_m);
            output_km = input * 0.000025; setTextAndStringFormat(tv_km, output_km);
            output_inch = input; setTextAndStringFormat(tv_inch, output_inch);
            output_ft = input * 0.083333; setTextAndStringFormat(tv_ft, output_ft);
            output_yd = input * 0.027778; setTextAndStringFormat(tv_yd, output_yd);
            output_mile = input * 0.000016; setTextAndStringFormat(tv_mile, output_mile);
            switchResult(slT);
        } else if (slF == 5) { // ft
            output_mm = input * 304.8; setTextAndStringFormat(tv_mm, output_mm);
            output_cm = input * 30.48; setTextAndStringFormat(tv_cm, output_cm);
            output_m = input * 0.3048; setTextAndStringFormat(tv_m, output_m);
            output_km = input * 0.000305; setTextAndStringFormat(tv_km, output_km);
            output_inch = input * 12; setTextAndStringFormat(tv_inch, output_inch);
            output_ft = input; setTextAndStringFormat(tv_ft, output_ft);
            output_yd = input * 0.333333; setTextAndStringFormat(tv_yd, output_yd);
            output_mile = input * 0.000189; setTextAndStringFormat(tv_mile, output_mile);
            switchResult(slT);
        } else if (slF == 6) { // yd
            output_mm = input * 914.4; setTextAndStringFormat(tv_mm, output_mm);
            output_cm = input * 91.44; setTextAndStringFormat(tv_cm, output_cm);
            output_m = input * 0.9144; setTextAndStringFormat(tv_m, output_m);
            output_km = input * 0.000914; setTextAndStringFormat(tv_km, output_km);
            output_inch = input * 36; setTextAndStringFormat(tv_inch, output_inch);
            output_ft = input * 3; setTextAndStringFormat(tv_ft, output_ft);
            output_yd = input; setTextAndStringFormat(tv_yd, output_yd);
            output_mile = input * 0.000568; setTextAndStringFormat(tv_mile, output_mile);
            switchResult(slT);
        } else if (slF == 7) { // mile
            output_mm = input * 1609344; setTextAndStringFormat(tv_mm, output_mm);
            output_cm = input * 160934.4; setTextAndStringFormat(tv_cm, output_cm);
            output_m = input * 1609.344; setTextAndStringFormat(tv_m, output_m);
            output_km = input * 1.609344; setTextAndStringFormat(tv_km, output_km);
            output_inch = input * 63360; setTextAndStringFormat(tv_inch, output_inch);
            output_ft = input * 5280; setTextAndStringFormat(tv_ft, output_ft);
            output_yd = input * 1760; setTextAndStringFormat(tv_yd, output_yd);
            output_mile = input; setTextAndStringFormat(tv_mile, output_mile);
            switchResult(slT);
        }
    }

    /**
     * 무게를 변환하는 함수
     * 0 is mg,  1 is g,  2 is kg,  3 is t,  4 is kt,  5 is lb,  6 is gr,  7 is oz
     * 기준 단위는 밀리그램(mg)
     * @param swF
     * @param swT
     */
    public void convertWeight(int swF, int swT, String editText) {

        double input = 0;
        if( !(editText_weight.getText().toString().equals("")) ) {
            input = Double.parseDouble(editText); // 입력값을 editText 에서 가져온다.
        }

        if (swF == 0) { // mg를 다른 단위로 변환
            output_mg = input; setTextAndStringFormat(tv_mg, output_mg);
            output_g = input * 0.001; setTextAndStringFormat(tv_g, output_g); // mg -> g
            output_kg = input * 0.000001; setTextAndStringFormat(tv_kg, output_kg); // mg -> kg
            output_ton = input * 0.000000001; setTextAndStringFormat(tv_ton, output_ton); // mg -> ton
            output_kt = input * 0.000000000001; setTextAndStringFormat(tv_kt, output_kt); // mg -> kt
            output_lb = input * 0.0000022; setTextAndStringFormat(tv_lb, output_lb); // mg -> lb
            output_gr = input * 0.015432; setTextAndStringFormat(tv_gr, output_gr); // mg -> gr
            output_oz = input * 0.000035; setTextAndStringFormat(tv_oz, output_oz); // mg -> oz
            switchResult(swT);
        } else if (swF == 1) { // g를 다른 단위로 변환
            output_mg = input * 1000; setTextAndStringFormat(tv_mg, output_mg); // g -> mg
            output_g = input; setTextAndStringFormat(tv_g, output_g);
            output_kg = input * 0.001; setTextAndStringFormat(tv_kg, output_kg); // g -> kg
            output_ton = input * 0.000001; setTextAndStringFormat(tv_ton, output_ton); // g -> t
            output_kt = input * 0.000000001; setTextAndStringFormat(tv_kt, output_kt); // g -> kt
            output_lb = input * 0.002205; setTextAndStringFormat(tv_lb, output_lb); // g -> lb
            output_gr = input * 15.432358; setTextAndStringFormat(tv_gr, output_gr); // g -> gr
            output_oz = input * 0.035274; setTextAndStringFormat(tv_oz, output_oz); // g -> oz
            switchResult(swT);
        } else if (swF == 2) { // kg를 다른 단위로 변환
            output_mg = input * 1000000; setTextAndStringFormat(tv_mg, output_mg); // kg -> mg
            output_g = input * 1000; setTextAndStringFormat(tv_g, output_g); // kg -> g
            output_kg = input; setTextAndStringFormat(tv_kg, output_kg);
            output_ton = input * 0.001; setTextAndStringFormat(tv_ton, output_ton); // kg -> ton
            output_kt = input * 0.000001; setTextAndStringFormat(tv_kt, output_kt); // kg -> kt
            output_lb = input * 2.204623; setTextAndStringFormat(tv_lb, output_lb); // kg -> lb
            output_gr = input * 15432.3584; setTextAndStringFormat(tv_gr, output_gr); // kg -> gr
            output_oz = input * 35.273962; setTextAndStringFormat(tv_oz, output_oz);      // kg -> oz
            switchResult(swT);
        } else if (swF == 3) { // ton를 다른 단위로 변환
            output_mg = input * 1000000000; setTextAndStringFormat(tv_mg, output_mg); // ton -> mg
            output_g = input * 1000000; setTextAndStringFormat(tv_g, output_g); // ton -> g
            output_kg = input * 1000; setTextAndStringFormat(tv_kg, output_kg); // ton -> kg
            output_ton = input; setTextAndStringFormat(tv_ton, output_ton);
            output_kt = input * 0.001; setTextAndStringFormat(tv_kt, output_kt); // ton -> kt
            output_lb = input * 2204.62262; setTextAndStringFormat(tv_lb, output_lb); // ton -> lb
            output_gr = input * 15432358.4; setTextAndStringFormat(tv_gr, output_gr); // ton -> gr
            output_oz = input * 35273.9619; setTextAndStringFormat(tv_oz, output_oz); // ton -> oz
            switchResult(swT);
        } else if (swF == 4) { // kt를 다른 단위로 변환
            output_mg = input * 1000000000000L; setTextAndStringFormat(tv_mg, output_mg); // kt -> mg
            output_g = input * 1000000000; setTextAndStringFormat(tv_g, output_g); // kt -> g
            output_kg = input * 1000000; setTextAndStringFormat(tv_kg, output_kg);    // kt -> kg
            output_ton = input * 1000; setTextAndStringFormat(tv_ton, output_ton);    // kt -> t
            output_kt = input; setTextAndStringFormat(tv_kt, output_kt);
            output_lb = input * 2204622.62; setTextAndStringFormat(tv_lb, output_lb); // kt -> lb
            output_gr = input * 15432358400L; setTextAndStringFormat(tv_gr, output_gr); // kt -> gr
            output_oz = input * 35273961.9; setTextAndStringFormat(tv_oz, output_oz); // kt -> oz
            switchResult(swT);
        } else if (swF == 5) { // lb를 다른 단위로 변환
            output_mg = input * 453592.37; setTextAndStringFormat(tv_mg, output_mg); // lb -> mg
            output_g = input * 453.59237; setTextAndStringFormat(tv_g, output_g); // lb -> g
            output_kg = input * 0.453592; setTextAndStringFormat(tv_kg, output_kg);    // lb -> kg
            output_ton = input * 0.000454; setTextAndStringFormat(tv_ton, output_ton);    // lb -> t
            output_kt = input * 0.000000454; setTextAndStringFormat(tv_kt, output_kt); // lb -> kt
            output_lb = input; setTextAndStringFormat(tv_lb, output_lb);
            output_gr = input * 7000; setTextAndStringFormat(tv_gr, output_gr); // lb -> gr
            output_oz= input * 16; setTextAndStringFormat(tv_oz, output_oz); // lb -> oz
            switchResult(swT);
        } else if (swF == 6) { // gr를 다른 단위로 변환
            output_mg = input * 64.79891; setTextAndStringFormat(tv_mg, output_mg); // gr -> mg
            output_g = input * 0.064799; setTextAndStringFormat(tv_g, output_g); // gr -> g
            output_kg = input * 0.000065; setTextAndStringFormat(tv_kg, output_kg);    // gr -> kg
            output_ton = input * 0.000000065; setTextAndStringFormat(tv_ton, output_ton);    // gr -> t
            output_kt = input * 0.000000000065; setTextAndStringFormat(tv_kt, output_kt); // gr -> kt
            output_lb = input * 0.000143; setTextAndStringFormat(tv_lb, output_lb); // gr -> lb
            output_gr = input; setTextAndStringFormat(tv_gr, output_gr);
            output_oz = input * 0.002286; setTextAndStringFormat(tv_oz, output_oz); // gr -> oz
            switchResult(swT);
        } else if (swF == 7) { // oz를 다른 단위로 변환
            output_mg = input * 28349.5231; setTextAndStringFormat(tv_mg, output_mg); // oz -> mg
            output_g = input * 28.349523; setTextAndStringFormat(tv_g, output_g); // oz -> g
            output_kg = input * 0.02835; setTextAndStringFormat(tv_kg, output_kg);    // oz -> kg
            output_ton = input * 0.000028; setTextAndStringFormat(tv_ton, output_ton);    // oz -> t
            output_kt = input * 0.000000028; setTextAndStringFormat(tv_kt, output_kt); // oz -> kt
            output_lb = input * 0.0625; setTextAndStringFormat(tv_lb, output_lb); // oz -> lb
            output_gr = input * 437.5; setTextAndStringFormat(tv_gr, output_gr); // oz -> gr
            output_oz = input; setTextAndStringFormat(tv_oz, output_oz);
            switchResult(swT);
        }
    }

    /**
     * 넓이를 변환하는 함수
     * 0 is m^2,  1 is km^2,  2 is ft^2,  3 is yd^2,  4 is a,  5 is ha,  6 is ac, 7 is 평
     * 기준 단위는 제곱미터(m^2)
     * @param saF
     * @param saT
     */
    public void convertArea(int saF, int saT, String editText) {
        double input = 0;

        if( !(editText_area.getText().toString().equals("")) ) {
            input = Double.parseDouble(editText);
        }

        if (saF == 0) { // m^2를 다른 단위로 변환
            output_m2 = input; setTextAndStringFormat(tv_m2, output_m2);
            output_km2 = input * 0.000001; setTextAndStringFormat(tv_km2, output_km2); // m^2 -> km^2
            output_ft2 = input * 10.76391; setTextAndStringFormat(tv_ft2, output_ft2); // m^2 -> ft^2
            output_yd2 = input * 1.19599; setTextAndStringFormat(tv_yd2, output_yd2); // m^2 -> yd^2
            output_a = input * 0.01; setTextAndStringFormat(tv_a, output_a); // m^2 -> a
            output_ha = input * 0.0001; setTextAndStringFormat(tv_ha, output_ha); // m^2 -> ha
            output_ac = input * 0.000247; setTextAndStringFormat(tv_ac, output_ac); // m^2 -> ac
            output_pyeong = input * 0.3025; setTextAndStringFormat(tv_pyeong, output_pyeong); // m^2 -> 평
            switchResult(saT);
        } else if (saF == 1) { // km^2를 다른 단위로 변환
            output_m2 = input * 1000000; setTextAndStringFormat(tv_m2, output_m2); // km^2 -> m^2
            output_km2 = input; setTextAndStringFormat(tv_km2, output_km2);
            output_ft2 = input * 10763910.4; setTextAndStringFormat(tv_ft2, output_ft2); // km^2 -> ft^2
            output_yd2 = input * 1195990.05; setTextAndStringFormat(tv_yd2, output_yd2); // km^2 -> yd^2
            output_a = input * 10000; setTextAndStringFormat(tv_a, output_a); // km^2 -> a
            output_ha = input * 100; setTextAndStringFormat(tv_ha, output_ha); // km^2 -> ha
            output_ac = input * 247.105381; setTextAndStringFormat(tv_ac, output_ac); // km^2 -> ac
            output_pyeong = input * 302500; setTextAndStringFormat(tv_pyeong, output_pyeong); // km^2 -> 평
            switchResult(saT);
        } else if (saF == 2) { // ft^2를 다른 단위로 변환
            output_m2 = input * 0.092903; setTextAndStringFormat(tv_m2, output_m2); // ft^2 -> m^2
            output_km2 = input * 0.000000093; setTextAndStringFormat(tv_km2, output_km2); // ft^2 -> km^2
            output_ft2 = input; setTextAndStringFormat(tv_ft2, output_ft2);
            output_yd2 = input * 0.111111; setTextAndStringFormat(tv_yd2, output_yd2); // ft^2 -> yd^2
            output_a = input * 0.000929; setTextAndStringFormat(tv_a, output_a); // ft^2 -> a
            output_ha = input * 0.00000929; setTextAndStringFormat(tv_ha, output_ha); // ft^2 -> ha
            output_ac = input * 0.000023; setTextAndStringFormat(tv_ac, output_ac); // ft^2 -> ac
            output_pyeong = input * 0.028103; setTextAndStringFormat(tv_pyeong, output_pyeong); // ft^2 -> 평
            switchResult(saT);
        } else if (saF == 3) { // yd^2를 다른 단위로 변환
            output_m2 = input * 0.836127; setTextAndStringFormat(tv_m2, output_m2); // yd^2 -> m^2
            output_km2 = input * 0.000000836; setTextAndStringFormat(tv_km2, output_km2); // yd^2 -> km^2
            output_ft2 = input * 9; setTextAndStringFormat(tv_ft2, output_ft2); // yd^2 -> ft^2
            output_yd2 = input; setTextAndStringFormat(tv_yd2, output_yd2);
            output_a = input * 0.008361; setTextAndStringFormat(tv_a, output_a); // yd^2 -> a
            output_ha = input * 0.000084; setTextAndStringFormat(tv_ha, output_ha); // yd^2 -> ha
            output_ac = input * 0.000207; setTextAndStringFormat(tv_ac, output_ac); // yd^2 -> ac
            output_pyeong = input * 0.252929; setTextAndStringFormat(tv_pyeong, output_pyeong); // yd^2 -> 평
            switchResult(saT);
        } else if (saF == 4) { // a를 다른 단위로 변환
            output_m2 = input * 100; setTextAndStringFormat(tv_m2, output_m2); // a -> m^2
            output_km2 = input * 0.0001; setTextAndStringFormat(tv_km2, output_km2); // a -> km^2
            output_ft2 = input * 1076.39104; setTextAndStringFormat(tv_ft2, output_ft2); // a -> ft^2
            output_yd2 = input * 119.599005; setTextAndStringFormat(tv_yd2, output_yd2); // a -> yd^2
            output_a = input; setTextAndStringFormat(tv_a, output_a);
            output_ha = input * 0.01; setTextAndStringFormat(tv_ha, output_ha); // a -> ha
            output_ac = input * 0.024711; setTextAndStringFormat(tv_ac, output_ac); // a -> ac
            output_pyeong = input * 30.25; setTextAndStringFormat(tv_pyeong, output_pyeong); // a -> 평
            switchResult(saT);
        } else if (saF == 5) { // ha를 다른 단위로 변환
            output_m2 = input * 10000; setTextAndStringFormat(tv_m2, output_m2); // ha -> m^2
            output_km2 = input * 0.01; setTextAndStringFormat(tv_km2, output_km2); // ha -> km^2
            output_ft2 = input * 107639.104; setTextAndStringFormat(tv_ft2, output_ft2); // ha -> ft^2
            output_yd2 = input * 11959.9005; setTextAndStringFormat(tv_yd2, output_yd2); // ha -> yd^2
            output_a = input * 100; setTextAndStringFormat(tv_a, output_a); // ha -> a
            output_ha = input; setTextAndStringFormat(tv_ha, output_ha);
            output_ac = input * 2.471054; setTextAndStringFormat(tv_ac, output_ac); // ha -> ac
            output_pyeong = input * 3025; setTextAndStringFormat(tv_pyeong, output_pyeong); // ha -> 평
            switchResult(saT);
        } else if (saF == 6) { // ac를 다른 단위로 변환
            output_m2 = input * 4046.85642; setTextAndStringFormat(tv_m2, output_m2); // ac -> m^2
            output_km2 = input * 0.004047; setTextAndStringFormat(tv_km2, output_km2); // ac -> km^2
            output_ft2 = input * 43560; setTextAndStringFormat(tv_ft2, output_ft2); // ac -> ft^2
            output_yd2 = input * 4840; setTextAndStringFormat(tv_yd2, output_yd2); // ac -> yd^2
            output_a = input * 40.468564; setTextAndStringFormat(tv_a, output_a); // ac -> a
            output_ha = input * 0.404686; setTextAndStringFormat(tv_ha, output_ha); // ac -> ha
            output_ac = input; setTextAndStringFormat(tv_ac, output_ac);
            output_pyeong = input * 1224.17407; setTextAndStringFormat(tv_pyeong, output_pyeong); // ac -> 평
            switchResult(saT);
        } else if (saF == 7) { // 평을 다른 단위로 변환
            output_m2 = input * 3.305785; setTextAndStringFormat(tv_m2, output_m2); // 평 -> m^2
            output_km2 = input * 0.0000033058; setTextAndStringFormat(tv_km2, output_km2); // 평 -> km^2
            output_ft2 = input * 35.583175; setTextAndStringFormat(tv_ft2, output_ft2); // 평 -> ft^2
            output_yd2 = input * 3.953686; setTextAndStringFormat(tv_yd2, output_yd2); // 평 -> yd^2
            output_a = input * 0.033058; setTextAndStringFormat(tv_a, output_a); // 평 -> a
            output_ha = input * 0.000331; setTextAndStringFormat(tv_ha, output_ha); // 평 -> ha
            output_ac = input * 0.000817; setTextAndStringFormat(tv_ac, output_ac); // 평 -> ac
            output_pyeong = input; setTextAndStringFormat(tv_pyeong, output_pyeong);
            switchResult(saT);
        }
    }

}
