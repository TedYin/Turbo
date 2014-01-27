package com.turbo.common;

import com.turbo.app.TurboBaseApp;
import com.turbo.net.VolleyNetHelper;
import com.turbo.net.VolleyNetHelper.NetCallBack;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 应用更新助手
 * @author Ted
 */
public class TurboUpdateHelper {
    //网络助手
    private static VolleyNetHelper helper = TurboBaseApp.getNetHellper();
    
    public interface UpdateCallBack{
        /**
         * 发现新版本
         * @param bean
         */
        public void haveNew(TurboUpdateBean bean);
        
        /**
         * 没有新版本
         */
        public void noNew();
        
        /**
         * 更新出错
         */
        public void onError(String errorMsg);
    }
    
    /**
     * 更新应用
     * 要求服务器返回的数据格式如下：
     * JSON：
     * [
     *      versionCode:1
     *      versionName:1.0.1
     *      description: 更新说明
     *      updateUrl: APK下载地址
     * ]
     * @param spec 更新地址
     * @param callBack 更新回调函数
     */
    public static void updateAPK(String spec,final UpdateCallBack callBack){
        helper.doJsonGet(TurboBaseApp.getAppContext(), spec, null, new NetCallBack<JSONObject>() {
            @Override
            public void onSuccess(JSONObject resp) {
                TurboUpdateBean bean = TurboUpdateBean.parse(resp.toString());
                if(CommonUtils.getVersionCode(TurboBaseApp.getAppContext()) < bean.getVersionCode()){
                    callBack.haveNew(bean);
                }else{
                    callBack.noNew();
                }
            }
            @Override
            public void onError(String errorMsg) {
                callBack.onError(errorMsg);
            }
        });
    }
    
    /**
     * 更新信息
     * @author Ted
     */
   static class TurboUpdateBean{
        private int versionCode;
        private String versionName;
        private String description;
        private String updateUrl;
        public int getVersionCode() {
            return versionCode;
        }
        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }
        public String getVersionName() {
            return versionName;
        }
        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }
        public String getUpdateUrl() {
            return updateUrl;
        }
        public void setUpdateUrl(String updateUrl) {
            this.updateUrl = updateUrl;
        }
        
        @Override
        public String toString() {
            JSONObject obj = new JSONObject();
            try {
                obj.put("versionCode", versionCode);
                obj.put("versionName", versionName);
                obj.put("description", description);
                obj.put("updateUrl", updateUrl);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return obj.toString();
        }
        
        
        public static TurboUpdateBean parse(String json){
            TurboUpdateBean bean = null;
            try {
                JSONObject obj = new JSONObject(json);
                if(!obj.has("versionCode")) return null;
                if(!obj.has("versionName")) return null;
                if(!obj.has("description")) return null;
                if(!obj.has("updateUrl")) return null;
                
                bean = new TurboUpdateBean();
                bean.setVersionCode(obj.getInt("versionCode"));
                bean.setVersionName(obj.getString("versionName"));
                bean.setVersionName(obj.getString("description"));
                bean.setVersionName(obj.getString("updateUrl"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return bean;
        }
        
    }
}
