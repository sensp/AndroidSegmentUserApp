package mbaas.com.nifcloud.androidsegmentuserapp;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.nifcloud.mbaas.core.NCMBException;
import com.nifcloud.mbaas.core.NCMBObject;


/**
 * Customer List view
 */
public class CustomAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater = null;
    public NCMBObject mUserObject = null;

    public CustomAdapter(Context a,  NCMBObject userObject) {
        context = a;
        mUserObject = userObject;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return mUserObject.allKeys().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder{

        public TextView item_key;
        public EditText item_value;

    }

    /**
     * return userObject
     */
    public NCMBObject getUserObject(){
        return mUserObject;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;
        String strKey = "";
        String strValue = "";

        //動的にレイアウトxmlからViewを生成
        vi = inflater.inflate(R.layout.two_line_list_item, null);

        //View要素設定
        holder = new ViewHolder();
        holder.item_key = (TextView) vi.findViewById(R.id.item_key);
        holder.item_value=(EditText)vi.findViewById(R.id.item_value);

        //Viewにセットする
        vi.setTag( holder );

        String keyName = (String)mUserObject.allKeys().get(position).toString().trim();
        Object valName = (Object)mUserObject.getString(keyName);

        holder.item_key.setText( keyName ); //keyの値
        holder.item_value.setText( valName.toString() ); //valueの値


        //更新可項目を別途Layout設定
        if(Constant.ignoreKeys.contains(keyName)){
            //valueの値を更新不可にする
            holder.item_value.setBackgroundColor(Color.BLACK);
            holder.item_value.setTextColor(Color.WHITE);
            holder.item_value.setEnabled(false);
        }else{
            //valueの値を更新可能にする
            holder.item_value.setEnabled(true);
            holder.item_value.setBackgroundColor(Color.WHITE);
            holder.item_value.setTextColor(Color.BLACK);
            //valueの値を更新後の処理する
            holder.item_value.addTextChangedListener( new TextWatcherCustom(keyName, (EditText)holder.item_value , position));
        }

        return vi;
    }

    ////////////////////////////////////////////////////////////
    // テキストの変更を検知するために必要
    class TextWatcherCustom implements TextWatcher {

        // 通知するためのエディットボックス
        private final String  mKeyText;
        private final EditText mValueEditText;
        private int mPosition;

        // コンストラクタ
        public TextWatcherCustom(final String keyText, final EditText valueEditText, final int iPosition) {
            this.mKeyText = keyText;
            this.mValueEditText = valueEditText;
            this.mPosition = iPosition;
        }

        // テキスト変更前
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //処理
        }

        // テキスト変更後
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //処理
        }

        // テキスト変更後
        public void afterTextChanged(Editable s) {
            //List更新データを設定
            try {
                mUserObject.put(this.mKeyText, s.toString());
            } catch (NCMBException e) {
                e.printStackTrace();
            }
        }

    }
    ////////////////////////////////////////////////////////////

}