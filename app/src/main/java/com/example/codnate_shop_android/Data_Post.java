
package com.example.codnate_shop_android;


import android.app.Activity;
import android.os.AsyncTask;
import android.util.Xml;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class Data_Post extends AsyncTask<Post_data_list,Void,String>{

    //リスナー
    private Listener listener;
    private String path;

    public Data_Post(String path){
        this.path = path;
    }
    //改行文字列
    @Override
    protected String doInBackground(Post_data_list... params) {

        Post_data_list post_data_list = params[0];

        //接続するためのクラスを宣言
        HttpURLConnection con = null;
        String readline = "";

        try {
            //URLクラス宣言
            URL url = new URL(AWS_IP.IPADDRESS+path);
            //コネクションを開く
            con = (HttpURLConnection)url.openConnection();
            //POSTに変更
            con.setRequestMethod("POST");
            //コネクション上でリクエストボディ送信の許可/不許可を設定できる
            con.setDoOutput(true);
            //レスポンスのボディ受信を許可する
            con.setDoInput(true);
            // ヘッダーの設定(複数設定可能)
            con.setRequestProperty("User-Agent", "Android");
            con.setRequestProperty("Accept-Language", "jp");
            //boudaryに一意な文字列を代入
            final String boundary = UUID.randomUUID().toString();
            //コンテンツタイプを変更
            con.setRequestProperty("Content-type","multipart/form-data; boundary="+boundary);
            //リクエストボディを書き込んでいく
            //一度コネクト
            con.connect();
            Malt_part_post malt = new Malt_part_post();
            PrintStream printStream = new PrintStream(con.getOutputStream(),false, Xml.Encoding.UTF_8.name());
            String name;
            String data;
            for(int i = 0;i<post_data_list.post_data_string.size();i++){
                name = post_data_list.post_data_string.get(i).data_name;
                data = post_data_list.post_data_string.get(i).post_data;
                malt.textPost(printStream,name,data,boundary);
            }


            printStream.print("--" + boundary + "--");
            if (printStream != null) {
                printStream.close();
            }
            int rescode = con.getResponseCode();
            InputStream in = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
            readline = br.readLine();
            System.out.println(readline);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        } finally {
            if(con != null) {
                //コネクション切断
                con.disconnect();
            }
        }
        return readline;
    }
    @Override
    public void onPostExecute(String result_data){
        super.onPostExecute(result_data);

        if(listener != null){
            listener.onSuccess(result_data);
        }
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    public interface Listener{
        void onSuccess(String result_data);
    }

}

