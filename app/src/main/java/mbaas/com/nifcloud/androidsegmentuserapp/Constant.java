package mbaas.com.nifcloud.androidsegmentuserapp;

import java.util.Arrays;
import java.util.List;

/**
 * 定数宣言
 */
public class Constant {

    // 既存ユーザーフィールド11種(更新不可項目)
    public static final List<String> ignoreKeys = Arrays.asList(
            "objectId",
            "userName",
            "password",
            "temporaryPassword",
            "mailAddress",
            "authData",
            "sessionInfo",
            "sessionToken",
            "mailAddressConfirm",
            "acl",
            "createDate",
            "updateDate"
    );

}
