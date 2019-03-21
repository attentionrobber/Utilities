package com.example.utilities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class UnitActivity extends AppCompatActivity {

    Button btn_length, btn_area, btn_weight;
    EditText editText_length, editText_area, editText_weight;
    TextView tv_length_output, tv_area_output, tv_weight_output;
    LinearLayout layout_length, layout_area, layout_weight;
    Spinner spinner_length_from, spinner_length_to, spinner_area_from, spinner_area_to, spinner_weight_from, spinner_weight_to;

    String spinner_length[] = {"밀리미터(mm)", "센티미터(cm)", "미터(m)", "킬로미터(km)", "인치(inch)", "피트(ft)", "야드(yd)", "마일(mile)"};
    String spinner_area[] = {"제곱미터(m^2)", "제곱키로미터(km^2)", "제곱피트(ft^2)", "제곱야드(yd^2)", "아르(a)", "헥타르(ha)", "에이커(ac)", "평"};
    String spinner_weight[] = {"밀리그램(mg)", "그램(g)", "킬로그램(kg)", "톤(t)", "킬로톤(kt)", "파운드(lb)", "그레인(gr)", "온스(oz)"};

    int length_from = 0;
    int length_to = 0;
    int area_from = 0;
    int area_to = 0;
    int weight_from = 0;
    int weight_to = 0;

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
        ArrayAdapter<String> adapter_length = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinner_length); // R. 은 내가 정의한 리소스 android.R. 은 android에 정의되어있는 리소스
        ArrayAdapter<String> adapter_area = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinner_area);
        ArrayAdapter<String> adapter_weight = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinner_weight);

        // 스피너 어댑터에 등록
        spinner_length_from.setAdapter(adapter_length);
        spinner_length_to.setAdapter(adapter_length);
        spinner_area_from.setAdapter(adapter_area);
        spinner_area_to.setAdapter(adapter_area);
        spinner_weight_from.setAdapter(adapter_weight);
        spinner_weight_to.setAdapter(adapter_weight);

    }

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
                    unitFlag = "LENGTH";
                    break;
                case R.id.btn_weight :
                    layout_length.setVisibility(View.GONE);
                    layout_area.setVisibility(View.GONE);
                    layout_weight.setVisibility(View.VISIBLE);
                    Log.i("clicklistener", "testweight");
                    unitFlag = "WEIGHT";
                    break;
                case R.id.btn_area :
                    layout_length.setVisibility(View.GONE);
                    layout_area.setVisibility(View.VISIBLE);
                    layout_weight.setVisibility(View.GONE);
                    unitFlag = "AREA";
                    break;
            }
        }
    }; // clickListener

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
                    case "LENGTH": convertLength(spinner_length_from.getSelectedItemPosition(), spinner_length_to.getSelectedItemPosition()); break;
                    case "WEIGHT": convertWeight(spinner_weight_from.getSelectedItemPosition(), spinner_weight_to.getSelectedItemPosition()); break;
                    case "AREA": convertArea(spinner_area_from.getSelectedItemPosition(), spinner_area_to.getSelectedItemPosition()); break;
                    default: break;
                }
            } else if(s.toString().equals("")) {
                tv_length_output.setText("0");
                tv_weight_output.setText("0");
                tv_area_output.setText("0");
            }
        }
    }; // textWatcher

    /**
     *  앞쪽에 있는 Spinner가 바뀔 때(선택했을 때)의 Listener
     */
    AdapterView.OnItemSelectedListener itemSelectedListener;
    {
        itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (unitFlag) {
                    case "LENGTH":
                        convertLength(position, spinner_length_to.getSelectedItemPosition()); break;
                    case "WEIGHT":
                        convertWeight(position, spinner_weight_to.getSelectedItemPosition()); break;
                    case "AREA":
                        convertArea(position, spinner_area_to.getSelectedItemPosition());  break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    /**
     * 뒷쪽에 있는 Spinner가 바뀔 때(선택했을 때)의 Listener
     */
    AdapterView.OnItemSelectedListener toItemSelectedListener;
    {
        toItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (unitFlag) {
                    case "LENGTH":
                        convertLength(spinner_length_from.getSelectedItemPosition(), position); break;
                    case "WEIGHT":
                        convertWeight(spinner_weight_from.getSelectedItemPosition(), position); break;
                    case "AREA":
                        convertArea(spinner_area_from.getSelectedItemPosition(), position); break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }


    /**
     * 길이를 변환하는 함수
     * 0 is mm,  1 is cm,  2 is m,  3 is km,  4 is inch,  5 is ft,  6 is yd,  7 is mile
     * 기준 단위는 미터(m)
     * @param slF(sign_length_from)
     * @param slT(sign_length_to)
     */
    public void convertLength(int slF, int slT) {
        double input = 0;
        double output = 0;

        if( !(editText_length.getText().toString().equals("")) ) {
            input = Double.parseDouble(editText_length.getText().toString()); // 입력값을 editText에서 가져온다.
        }

        if( slF == slT ) {
            output = input;
        }
        // mm를 다른 단위로 변환
        if (slF == 0) switch (slT) {
            case 1: output = input * 0.1; break; // mm -> cm
            case 2: output = input * 0.001; break; // mm -> m
            case 3: output = input * 0.000001; break; // mm -> km
            case 4: output = input * 0.03937; break; // mm -> inch
            case 5: output = input * 0.003281; break; // mm -> ft
            case 6: output = input * 0.001094; break; // mm -> yd
            case 7: output = input * 0.000000621; break; // mm -> mile
            default: break;
        }
        // cm를 다른 단위로 변환
        if (slF == 1) switch (slT) {
            case 0: output = input * 10; break; // cm -> mm
            case 2: output = input * 0.01; break; // cm -> m
            case 3: output = input * 0.00001; break; // cm -> km
            case 4: output = input * 0.393701; break; // cm -> inch
            case 5: output = input * 0.032808; break; // cm -> ft
            case 6: output = input * 0.010936; break; // cm -> yd
            case 7: output = input * 0.00000621; break; // cm -> mile
            default: break;
        }
        // m를 다른 단위로 변환
        if (slF == 2) switch (slT) {
            case 0: output = input * 1000; break; // m -> mm
            case 1: output = input * 10; break; // m -> cm
            case 3: output = input * 0.001; break; // m -> km
            case 4: output = input * 39.370079; break; // m -> inch
            case 5: output = input * 3.28084; break; // m -> ft
            case 6: output = input * 1.093613; break; // m -> yd
            case 7: output = input * 0.00062137; break; // m -> mile
            default: break;
        }
        // km를 다른 단위로 변환
        if (slF == 3) switch (slT) {
            case 0: output = input * 1000; break; // km -> mm
            case 1: output = input * 0.1; break; // km -> cm
            case 2: output = input * 0.001; break; // km -> m
            case 4: output = input * 39370.0787; break; // km -> inch
            case 5: output = input * 3280.8399; break; // km -> ft
            case 6: output = input * 1093.6133; break; // km -> yd
            case 7: output = input * 0.621371; break; // km -> mile
            default: break;
        }
        // inch를 다른 단위로 변환
        if (slF == 4)  switch (slT) {
            case 0: output = input * 25.4; break; // inch -> mm
            case 1: output = input * 2.54; break; // inch -> cm
            case 2: output = input * 0.0254; break; // inch -> m
            case 3: output = input * 0.000025; break; // inch -> km
            case 5: output = input * 0.083333; break; // inch -> ft
            case 6: output = input * 0.027778; break; // inch -> yd
            case 7: output = input * 0.000016; break; // inch -> mile
            default: break;
        }
        // ft를 다른 단위로 변환
        if (slF == 5) switch (slT) {
            case 0: output = input * 304.8; break; // ft -> mm
            case 1: output = input * 30.48; break; // ft -> cm
            case 2: output = input * 0.3048; break; // ft -> m
            case 3: output = input * 0.000305; break; // ft -> km
            case 4: output = input * 12; break; // ft -> inch
            case 6: output = input * 0.333333; break; // ft -> yd
            case 7: output = input * 0.000189; break; // ft -> mile
            default: break;
        }
        // yd를 다른 단위로 변환
        if (slF == 6) switch (slT) {
            case 0: output = input * 914.4; break; // yd -> mm
            case 1: output = input * 91.44; break; // yd -> cm
            case 2: output = input * 0.9144; break; // yd -> m
            case 3: output = input * 0.000914; break; // yd -> km
            case 4: output = input * 36; break; // yd -> inch
            case 5: output = input * 3; break; // yd -> ft
            case 7: output = input * 0.000568; break; // yd -> mile
            default: break;
        }
        // mile를 다른 단위로 변환
        if (slF == 7) switch (slT) {
            case 0: output = input * 1609344; break; // mile -> mm
            case 1: output = input * 160934.4; break; // mile -> cm
            case 2: output = input * 1609.344; break; // mile -> m
            case 3: output = input * 1.609344; break; // mile -> km
            case 4: output = input * 63360; break; // mile -> inch
            case 5: output = input * 5280; break; // mile -> ft
            case 6: output = input * 1760; break; // mile -> yd
            default: break;
        }
        tv_length_output.setText(String.format("%s", output));
    }

    /**
     * 무게를 변환하는 함수
     * 0 is mg,  1 is g,  2 is kg,  3 is t,  4 is kt,  5 is lb,  6 is gr,  7 is oz
     * 기준 단위는 밀리그램(mg)
     * @param swF
     * @param swT
     */
    public void convertWeight(int swF, int swT) {

        double input = 0;
        double output = 0;
        if( !(editText_weight.getText().toString().equals("")) ) {
            input = Double.parseDouble(editText_weight.getText().toString());
        }

        if( swF == swT ) {
            output = input;
        }

        // mg를 다른 단위로 변환
        if (swF == 0) switch (swT) {
            case 1: output = input * 0.001; break; // mg -> g
            case 2: output = input * 0.000001; break; // mg -> kg
            case 3: output = input * 0.000000001; break; // mg -> t
            case 4: output = input * 0.000000000001; break; // mg -> kt
            case 5: output = input * 0.0000022; break; // mg -> lb
            case 6: output = input * 0.015432; break; // mg -> gr
            case 7: output = input * 0.000035; break; // mg -> oz
            default: break;
        }
        // g를 다른 단위로 변환
        if (swF == 1) switch (swT) {
            case 0: output = input * 1000; break; // g -> mg
            case 2: output = input * 0.001; break; // g -> kg
            case 3: output = input * 0.000001; break; // g -> t
            case 4: output = input * 0.000000001; break; // g -> kt
            case 5: output = input * 0.002205; break; // g -> lb
            case 6: output = input * 15.432358; break; // g -> gr
            case 7: output = input * 0.035274; break; // g -> oz
            default: break;
        }
        // kg를 다른 단위로 변환
        if (swF == 2) switch (swT) {
            case 0: output = input * 1000000; break;        // kg -> mg
            case 1: output = input * 1000; break;           // kg -> g
            case 3: output = input * 0.001; break;          // kg -> t
            case 4: output = input * 0.000001; break;       // kg -> kt
            case 5: output = input * 2.204623; break;       // kg -> lb
            case 6: output = input * 15432.3584; break;     // kg -> gr
            case 7: output = input * 35.273962; break;      // kg -> oz
            default: break;
        }
        // ton를 다른 단위로 변환
        if (swF == 3) switch (swT) {
            case 0: output = input * 1000000000; break;        // ton -> mg
            case 1: output = input * 1000000; break;           // ton -> g
            case 2: output = input * 1000; break;          // ton -> kg
            case 4: output = input * 0.001; break;    // ton -> kt
            case 5: output = input * 2204.62262; break;       // ton -> lb
            case 6: output = input * 15432358.4; break;      // ton -> gr
            case 7: output = input * 35273.9619; break;       // ton -> oz
            default: break;
        }
        // kt를 다른 단위로 변환
        if (swF == 4) switch (swT) {
            case 0: output = input * 1000000000000L; break; // kt -> mg
            case 1: output = input * 1000000000; break; // kt -> g
            case 2: output = input * 1000000; break;    // kt -> kg
            case 3: output = input * 1000; break;    // kt -> t
            case 5: output = input * 2204622.62; break; // kt -> lb
            case 6: output = input * 15432358400L; break; // kt -> gr
            case 7: output = input * 35273961.9; break; // kt -> oz
            default: break;
        }
        // lb를 다른 단위로 변환
        if (swF == 5) switch (swT) {
            case 0: output = input * 453592.37; break; // lb -> mg
            case 1: output = input * 453.59237; break; // lb -> g
            case 2: output = input * 0.453592; break;    // lb -> kg
            case 3: output = input * 0.000454; break;    // lb -> t
            case 4: output = input * 0.000000454; break; // lb -> kt
            case 6: output = input * 7000; break; // lb -> gr
            case 7: output = input * 16; break; // lb -> oz
            default: break;
        }
        // gr를 다른 단위로 변환
        if (swF == 6) switch (swT) {
            case 0: output = input * 64.79891; break; // gr -> mg
            case 1: output = input * 0.064799; break; // gr -> g
            case 2: output = input * 0.000065; break;    // gr -> kg
            case 3: output = input * 0.000000065; break;    // gr -> t
            case 4: output = input * 0.000000000065; break; // gr -> kt
            case 5: output = input * 0.000143; break; // gr -> lb
            case 7: output = input * 0.002286; break; // gr -> oz
            default: break;
        }
        // oz를 다른 단위로 변환
        if (swF == 7) switch (swT) {
            case 0: output = input * 28349.5231; break; // oz -> mg
            case 1: output = input * 28.349523; break; // oz -> g
            case 2: output = input * 0.02835; break;    // oz -> kg
            case 3: output = input * 0.000028; break;    // oz -> t
            case 4: output = input * 0.000000028; break; // oz -> kt
            case 5: output = input * 0.0625; break; // oz -> lb
            case 6: output = input * 437.5; break; // oz -> gr
            default: break;
        }
        tv_weight_output.setText(String.format("%s", output));
    }

    /**
     * 넓이를 변환하는 함수
     * 0 is m^2,  1 is km^2,  2 is ft^2,  3 is yd^2,  4 is a,  5 is ha,  6 is ac, 7 is 평
     * 기준 단위는 제곱미터(m^2)
     * @param saF
     * @param saT
     */
    public void convertArea(int saF, int saT) {
        double input = 0;
        double output = 0;

        if( !(editText_area.getText().toString().equals("")) ) {
            input = Double.parseDouble(editText_area.getText().toString());
        }

        if( saF == saT ) {
            output = input;
        }

        // m^2를 다른 단위로 변환
        if (saF == 0) switch (saT) {
            case 1: output = input * 0.000001; break; // m^2 -> km^2
            case 2: output = input * 10.76391; break; // m^2 -> ft^2
            case 3: output = input * 1.19599; break; // m^2 -> yd^2
            case 4: output = input * 0.01; break; // m^2 -> a
            case 5: output = input * 0.0001; break; // m^2 -> ha
            case 6: output = input * 0.000247; break; // m^2 -> ac
            case 7: output = input * 0.3025; break; // m^2 -> 평
            default: break;
        }
        // km^2를 다른 단위로 변환
        if (saF == 1) switch (saT) {
            case 0: output = input * 1000000; break; // km^2 -> m^2
            case 2: output = input * 10763910.4; break; // km^2 -> ft^2
            case 3: output = input * 1195990.05; break; // km^2 -> yd^2
            case 4: output = input * 10000; break; // km^2 -> a
            case 5: output = input * 100; break; // km^2 -> ha
            case 6: output = input * 247.105381; break; // km^2 -> ac
            case 7: output = input * 302500; break; // km^2 -> 평
            default: break;
        }
        // ft^2를 다른 단위로 변환
        if (saF == 2) switch (saT) {
            case 0: output = input * 0.092903; break; // ft^2 -> m^2
            case 1: output = input * 0.000000093; break; // ft^2 -> km^2
            case 3: output = input * 0.111111; break; // ft^2 -> yd^2
            case 4: output = input * 0.000929; break; // ft^2 -> a
            case 5: output = input * 0.00000929; break; // ft^2 -> ha
            case 6: output = input * 0.000023; break; // ft^2 -> ac
            case 7: output = input * 0.028103; break; // ft^2 -> 평
            default: break;
        }
        // yd^2를 다른 단위로 변환
        if (saF == 3) switch (saT) {
            case 0: output = input * 0.836127; break; // yd^2 -> m^2
            case 1: output = input * 0.000000836; break; // yd^2 -> km^2
            case 2: output = input * 9; break; // yd^2 -> ft^2
            case 4: output = input * 0.008361; break; // yd^2 -> a
            case 5: output = input * 0.000084; break; // yd^2 -> ha
            case 6: output = input * 0.000207; break; // yd^2 -> ac
            case 7: output = input * 0.252929; break; // yd^2 -> 평
            default: break;
        }
        // a를 다른 단위로 변환
        if (saF == 4) switch (saT) {
            case 0: output = input * 100; break; // a -> m^2
            case 1: output = input * 0.0001; break; // a -> km^2
            case 2: output = input * 1076.39104; break; // a -> ft^2
            case 3: output = input * 119.599005; break; // a -> yd^2
            case 5: output = input * 0.01; break; // a -> ha
            case 6: output = input * 0.024711; break; // a -> ac
            case 7: output = input * 30.25; break; // a -> 평
            default: break;
        }
        // ha를 다른 단위로 변환
        if (saF == 5) switch (saT) {
            case 0: output = input * 10000; break; // ha -> m^2
            case 1: output = input * 0.01; break; // ha -> km^2
            case 2: output = input * 107639.104; break; // ha -> ft^2
            case 3: output = input * 11959.9005; break; // ha -> yd^2
            case 4: output = input * 100; break; // ha -> a
            case 6: output = input * 2.471054; break; // ha -> ac
            case 7: output = input * 3025; break; // ha -> 평
            default: break;
        }
        // ac를 다른 단위로 변환
        if (saF == 6) switch (saT) {
            case 0: output = input * 4046.85642; break; // ac -> m^2
            case 1: output = input * 0.004047; break; // ac -> km^2
            case 2: output = input * 43560; break; // ac -> ft^2
            case 3: output = input * 4840; break; // ac -> yd^2
            case 4: output = input * 0.404686; break; // ac -> ha
            case 5: output = input * 40.468564; break; // ac -> a
            case 7: output = input * 1224.17407; break; // ac -> 평
            default: break;
        }
        // 평을 다른 단위로 변환
        if (saF == 7) switch (saT) {
            case 0: output = input * 3.305785; break; // 평 -> m^2
            case 1: output = input * 0.0000033058; break; // 평 -> km^2
            case 2: output = input * 35.583175; break; // 평 -> ft^2
            case 3: output = input * 3.953686; break; // 평 -> yd^2
            case 4: output = input * 0.000331; break; // 평 -> ha
            case 5: output = input * 0.033058; break; // 평 -> a
            case 6: output = input * 0.000817; break; // 평 -> ac
            default: break;
        }
        tv_area_output.setText(String.format("%s", output)); // textView에 결과값 출력
    }

}
