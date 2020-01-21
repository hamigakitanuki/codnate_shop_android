package com.example.codnate_shop_android;

import java.util.ArrayList;

public class Post_data_list {
    public ArrayList<Post_String_data> post_data_string = new ArrayList<Post_String_data>();

    public void add_data(Post_String_data data){
        post_data_string.add(data);
    }
    public void add_data(Post_float_data data){
        add_data(new Post_String_data(data.data_name,String.valueOf(data.data_name)));
    }
    public void add_data(Post_int_data data){
        add_data(new Post_String_data(data.data_name,String.valueOf(data.data_name)));
    }

}
