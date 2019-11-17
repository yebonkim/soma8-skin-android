package com.soma.skinbutler.serverinterface;



import com.soma.skinbutler.serverinterface.request.LoginRequest;
import com.soma.skinbutler.serverinterface.request.SignUpRequest;
import com.soma.skinbutler.serverinterface.request.SkinRequest;
import com.soma.skinbutler.serverinterface.response.StdSkinResponse;
import com.soma.skinbutler.serverinterface.response.UserResponse;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by yebonkim on 17. 11. 24..
 */
public interface ServerAPI {
    @POST("/api/auth")
    Call<UserResponse> login(@Body LoginRequest loginRequest);

    @POST("/api/user")
    Call<UserResponse> signUp(@Body SignUpRequest signUpRequest);

    @POST("/api/user/skin")
    Call<StdSkinResponse> sendSkin(@Body SkinRequest skinRequest);
}
