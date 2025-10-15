package com.Appzia.enclosure.Utils.WebserviceRetrofits;

import com.Appzia.enclosure.Model.Emoji;
import com.Appzia.enclosure.Model.change_numberModel;
import com.Appzia.enclosure.Model.emojiDataModel;
import com.Appzia.enclosure.Model.flagNewModel;
import com.Appzia.enclosure.Model.get_users_all_contactParentModel;
import com.Appzia.enclosure.Model.globalModel;
import com.Appzia.enclosure.Model.grp_list_model;
import com.Appzia.enclosure.models.get_call_log_1Model;
import com.Appzia.enclosure.models.get_group_detailsResponseModel;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface API {
    @POST("upload_user_contact_list")
    Call<globalModel> upload_user_contact_list(@Body RequestBody requestBody);

    @POST("get_country_list")
    Call<flagNewModel> get_country_list();

    @POST("search_from_all_contact")
    Call<flagNewModel> search_from_all_contact(@Body RequestBody requestBody);

    @POST("change_number")
    Call<change_numberModel> change_number(@Body RequestBody requestBody);

    @POST("verify_otp_common")
    Call<change_numberModel> verify_otp_for_delete_user(@Body RequestBody requestBody);

    @POST("delete_my_account")
    Call<change_numberModel> delete_my_account(@Body RequestBody requestBody);
    @POST("get_call_log_1")
    Call<get_call_log_1Model> get_call_log_1(@Body RequestBody requestBody);

    @POST("get_group_details")
    Call<get_group_detailsResponseModel> get_group_details(@Body RequestBody requestBody);

    @POST("get_voice_call_log")
    Call<get_call_log_1Model> get_voice_call_log_for_voice_call(@Body RequestBody requestBody);

    @POST("get_group_list")
    Call<grp_list_model> get_group_list(@Body RequestBody requestBody);

    @POST("get_users_all_contact")
    Call<get_users_all_contactParentModel> get_users_all_contact(@Body RequestBody requestBody);

    @POST("emojiController/fetch_emoji_data")
    Call<emojiDataModel> getEmojis();

    @GET("PageController/contact_file_save")
    Call<ResponseBody> saveContactFile(@Query("file_name") String fileName);
}