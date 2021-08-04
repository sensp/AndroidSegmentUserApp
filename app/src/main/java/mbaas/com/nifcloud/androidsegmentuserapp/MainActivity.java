package mbaas.com.nifcloud.androidsegmentuserapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.nifcloud.mbaas.core.DoneCallback;
import com.nifcloud.mbaas.core.FetchCallback;
import com.nifcloud.mbaas.core.NCMB;
import com.nifcloud.mbaas.core.NCMBException;
import com.nifcloud.mbaas.core.NCMBObject;
import com.nifcloud.mbaas.core.NCMBUser;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOGIN = 0;
    private CustomAdapter customAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //**************** APIキーの設定とSDKの初期化 **********************
        NCMB.initialize(this.getApplicationContext(), "YOUR_APPLICATION_KEY", "YOUR_CLIENT_KEY");

        //ユーザーログイン呼ぶ
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // ログアウト処理
        if (id == R.id.action_logout) {
            NCMBUser.logoutInBackground(new DoneCallback() {
                @Override
                public void done(NCMBException e) {
                    if (e != null) {
                        //エラー時の処理
                    }
                }
            });
            //ユーザーログイン呼ぶ
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {
                //進行動作状況表示
                setDispProcess("ログインに成功しました。");

                //ログインに成功場合はーザー情報を表示させる
                doCurrentUserInfo();
            }
        }
    }

    //ログインに成功場合はユーザー情報を表示させる
    public void doCurrentUserInfo() {

        // カレントユーザ情報の取得
        NCMBUser userInfo = NCMBUser.getCurrentUser();

        //新規更新
        TextView _txtNewKey = (TextView)findViewById(R.id.txtNewKey);
        TextView _txtNewValue = (TextView)findViewById(R.id.txtNewValue);
        _txtNewKey.setText(""); //初期化
        _txtNewValue.setText(""); //初期化

        userInfo.fetchInBackground(new FetchCallback<NCMBObject>() {
                @Override
                public void done(NCMBObject userObject, NCMBException e) {
                    if (e != null) {
                        //エラー時の処理
                    } else {
                        //取得成功時の処理
                        //ListViewのAdapter Object生成
                        customAdapter = new CustomAdapter(getApplicationContext(), userObject);

                        //ListViewに設定・表示
                        ListView listView1 = (ListView)findViewById(R.id.listViewUser);
                        listView1.setAdapter(customAdapter);

                        //リスト表示用高さを設定
                        setListviewHeight(listView1);
                    }
                }
            });

    }

    //更新Btn押す処理
    public void doUpdateUserInfo(View view) {

        //入力値のチェック
        String strCheckError = validateNewData();
        if (!strCheckError.isEmpty()) {
            //入力チェックエラーの場合
            setDispProcess(strCheckError);
            return;
        }else{

            //更新処理開始
            customAdapter.getUserObject().saveInBackground(new DoneCallback() {
                @Override
                public void done(NCMBException e) {

                    if (e != null) {
                        //エラー発生時の処理
                        setDispProcess("更新失敗しました!\n"+ e.getMessage());
                        //doCurrentUserInfo();
                    } else {
                        //ユーザー情報を再表示させる
                        setDispProcess("更新に成功しました。");
                        doCurrentUserInfo();
                    }
                }
            });
        }
    }

    //新規更新入力チェック
    public String validateNewData() {
        String errMsg = "";

        //新規入力のチェック
        TextView _txtNewKey = (TextView)findViewById(R.id.txtNewKey);
        TextView _txtNewValue = (TextView)findViewById(R.id.txtNewValue);

        //新規入力Key値
        String newKey = _txtNewKey.getText().toString();
        //新規入力Value値
        String newValue = _txtNewValue.getText().toString();

        if (newKey.isEmpty() && !newValue.isEmpty() ) {
            errMsg = "新規key値を入力ください!";
        }else if (Constant.ignoreKeys.contains(newKey.trim())) {
            errMsg ="key値は更新対象名ではありません!";
        }else if (!newKey.isEmpty() && !newKey.trim().matches("^[0-9a-zA-Z]+$")) {
            errMsg ="key値は半角英数字のみです!";
        }else{
            //新規更新データを作成
            if (newKey.isEmpty() && newValue.isEmpty() ) {
                //更新対象ではない
            }else{
                //更新データを設定
                try {
                    customAdapter.getUserObject().put(newKey.trim(), newValue.trim());
                } catch (NCMBException e) {
                    e.printStackTrace();
                }
            }
        }

        return errMsg;
    }

    //進行動作状況表示
    private void setDispProcess(String strProcess) {

        if(!strProcess.isEmpty()){
            TextView txtProcess = (TextView) findViewById(R.id.txtProcess);
            txtProcess.setText(strProcess);
        }

    }

    //リスト表示用ListView高さを設定
    private void setListviewHeight(ListView listView) {

        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        int winH = dm.heightPixels;

        //ListView高さを設定(端末サイズによるScrollする為)
        ViewGroup.LayoutParams params = listView.getLayoutParams();

        if(winH > 1000) {
            params.height = winH / 2;
        }else if(winH < 500){
            params.height = 100;
        }else{
            params.height = 350;
        }
        listView.setLayoutParams(params);

        listView.requestLayout();
    }

}
