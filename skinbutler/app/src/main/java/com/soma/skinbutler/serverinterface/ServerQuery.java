package com.soma.skinbutler.serverinterface;

import com.soma.skinbutler.serverinterface.request.LoginRequest;
import com.soma.skinbutler.serverinterface.request.SignUpRequest;
import com.soma.skinbutler.serverinterface.request.SkinRequest;
import com.soma.skinbutler.serverinterface.response.StdSkinResponse;
import com.soma.skinbutler.serverinterface.response.UserResponse;
import com.squareup.okhttp.Callback;

import retrofit.Call;

/**
 * Created by yebonkim on 17. 11. 14..
 */
public class ServerQuery {
    public static void login(LoginRequest request, retrofit.Callback callback){
        Call<UserResponse> call = ServiceGenerator.createService(ServerAPI.class).login(request);
        call.enqueue(callback);
    }

    public static void signUp(SignUpRequest request, retrofit.Callback callback) {
        Call<UserResponse> call = ServiceGenerator.createService(ServerAPI.class).signUp(request);
        call.enqueue(callback);
    }

    public static void sendSkin(SkinRequest request, retrofit.Callback callback) {
        Call<StdSkinResponse> call = ServiceGenerator.createService(ServerAPI.class).sendSkin(request);
        call.enqueue(callback);
    }

}
