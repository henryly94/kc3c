package sjtukc3c.smallcar.Utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Author: wenhao.zhu[weehowe.z@gmail.com]
 * Created on 8:50 PM 12/26/16.
 */

public class JsonParser {

    public static String parseResult(String json) {
        StringBuilder ret = new StringBuilder();
        try {
            JSONTokener token = new JSONTokener(json);
            JSONObject joResult = new JSONObject(token);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // Use first rank candidate
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }
}