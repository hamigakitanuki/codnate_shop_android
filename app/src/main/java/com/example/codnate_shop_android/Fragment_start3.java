package com.example.codnate_shop_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_start3 extends Fragment {
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_start_3,container,false);
        Button button = view.findViewById(R.id.start_3_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = view.findViewById(R.id.start_3_name);
                String name = editText.getText().toString();
                if(name.isEmpty()){
                    Toast.makeText(getActivity().getApplicationContext(),"名前を入力してください", Toast.LENGTH_SHORT).show();
                }else{
                    SharedPreferences data = getActivity().getSharedPreferences("DATA", MODE_PRIVATE);
                    Post_String_data post_name = new Post_String_data("name",name);
                    Post_float_data post_latitube = new Post_float_data("latitube",data.getFloat("latitube",0));
                    Post_float_data post_longitube = new Post_float_data("longitube",data.getFloat("longitube",0));

                    Post_data_list post_data_list = new Post_data_list();
                    post_data_list.add_data(post_name);
                    post_data_list.add_data(post_latitube);
                    post_data_list.add_data(post_longitube);

                    Data_Post data_post = new Data_Post(AWS_IP.post_shop_info);
                    data_post.setListener(task());
                    data_post.execute(post_data_list);
                    getActivity().finish();
                }
            }
        });
        return view;
    }

    Data_Post.Listener task(){
        return new Data_Post.Listener() {
            @Override
            public void onSuccess(String result_data) {
                Toast.makeText(getActivity().getApplicationContext(),result_data,Toast.LENGTH_SHORT);
            }
        };
    }
}
