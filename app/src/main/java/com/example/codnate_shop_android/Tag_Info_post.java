
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

public class Tag_Info_post extends AsyncTask<Correct_data,Void,String>{

    //リスナー
    private Listener listener;
    //遷移してきたアクティビティへの戻り値を返す時に使う
    private Activity mActivity;
    //改行文字列
    @Override
    protected String doInBackground(Correct_data... params) {

        //受け取ったParamを格納
        int image_idx = params[0].idx;
        int correct = params[0].correct;
        int user_id = params[0].id;

        //接続するためのクラスを宣言
        HttpURLConnection con = null;
        String readline = "";

        try {
            //URLクラス宣言
            URL url = new URL("http://13.59.167.90:8080/post_tag_info");
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
            malt.textPost(printStream,"image_idx",image_idx,boundary);
            malt.textPost(printStream,"correct",correct,boundary);
            malt.textPost(printStream,"id",user_id,boundary);


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
    public void onPostExecute(String string){
        super.onPostExecute(string);

        if(listener != null){
            listener.onSuccess(string);
        }
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    public interface Listener{
        void onSuccess(String result);
    }

}

