package com.example.teamproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Search extends AppCompatActivity {

    private StationInfo list;          // 데이터를 넣은 리스트변수
    private ListView listView;          // 검색을 보여줄 리스트변수
    private EditText editSearch;        // 검색어를 입력할 Input 창
    private SearchAdapter adapter;      // 리스트뷰에 연결할 아답터
    private Spinner spinner;
    private ArrayList<String> arraylist;
    private ArrayAdapter Ara_type;
    private List<String> s_list;
    private String type;
    private String start_point=null;
    private String transfer_point=null;
    private String end_point=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        list=new StationInfo();
        spinner=findViewById(R.id.select_type);
        Ara_type=ArrayAdapter.createFromResource(this,R.array.selectSearch,android.R.layout.simple_spinner_item);
        Ara_type.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(Ara_type);
        editSearch = (EditText) findViewById(R.id.editSearch);
        listView = (ListView) findViewById(R.id.listView);
        Intent get_intent=getIntent();
        Intent intent=new Intent(Search.this,subway_result.class);
        // 리스트를 생성한다.
        s_list=new ArrayList<String>();
        s_list=list.station_index;
        try{
            if(start_point==null)
                start_point=get_intent.getStringExtra("start_point");//시작역 받는 변수
            if(transfer_point==null)
                transfer_point=get_intent.getStringExtra("transfer_point");//경유 역 받는 변수
            if(end_point==null)
                end_point=get_intent.getStringExtra("end_point");//도착역 받는 변수
        }catch(NullPointerException e){}


        // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
        arraylist = new ArrayList<String>();
        arraylist.addAll(s_list);

        // 리스트에 연동될 아답터를 생성한다.
        adapter = new SearchAdapter(s_list, this);

        // 리스트뷰에 아답터를 연결한다.
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str= s_list.get(position);
                Check_sameStd(type,str);
                intent.putExtra("start_point",start_point);
                intent.putExtra("transfer_point",transfer_point);
                intent.putExtra("end_point",end_point);
                intent.putExtra(type,str);
                Log.d("viewTest", "이름 : "+str);
                startActivity(intent);
            }
        });
        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                String text = editSearch.getText().toString();
                search(text);
            }
        });

        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_DONE){
//                    intent.putExtra(type,editSearch.getText().toString());
//                    startActivity(intent);
                    InputMethodManager manager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return false;
            }

        });
        //스피너 선택시 이벤트
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        type="start_point";
                        break;
                    case 1:
                        type="transfer_point";
                        break;
                    case 2:
                        type="end_point";
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                type="start_point";
            }
        });
    }
    public void Check_sameStd(String type,String str){
        if(type.equals("start_point")&&str.equals(end_point)){
            end_point=start_point;
        }else if(type.equals("start_point")&&str.equals(transfer_point))
            transfer_point=start_point;
        if(type.equals("end_point")&&str.equals(start_point)){
            start_point=end_point;
        }else if(type.equals("end_point")&&str.equals(transfer_point))
            transfer_point=end_point;
        if(type.equals("transfer_point")&&str.equals(start_point)){
            start_point=transfer_point;
        }else if(type.equals("transfer_point")&&str.equals(end_point)){
            end_point=transfer_point;
        }

    }
    // 검색을 수행하는 메소드
    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.

            s_list.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            s_list.addAll(arraylist);
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < arraylist.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (arraylist.get(i).toLowerCase().startsWith(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    s_list.add(arraylist.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }


}

