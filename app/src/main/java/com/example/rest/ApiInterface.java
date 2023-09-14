package com.example.rest;


import com.example.response.AppDetailRP;
import com.example.response.AuthorDetailRP;
import com.example.response.AuthorRP;
import com.example.response.BookDetailRP;
import com.example.response.BraintreeCheckOutRP;
import com.example.response.CatRP;
import com.example.response.EditProfileRP;
import com.example.response.FavoriteRP;
import com.example.response.HomeRP;
import com.example.response.LoginRP;
import com.example.response.PayUMoneyHashRP;
import com.example.response.PaymentSuccessRP;
import com.example.response.PaypalTokenRP;
import com.example.response.PlanRP;
import com.example.response.PostRateRP;
import com.example.response.RateReviewRP;
import com.example.response.RazorPayTokenRP;
import com.example.response.RegisterRP;
import com.example.response.StripeCheckOutRP;
import com.example.response.SubCatListBookRP;
import com.example.response.SubCatRP;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    //get home data
    @POST("home")
    @FormUrlEncoded
    Call<HomeRP> getHomeData(@Field("data") String data);

    //get cat data
    @POST("category")
    @FormUrlEncoded
    Call<CatRP> getCatData(@Field("data") String data, @Query("page") int page);

    //get sub cat data
    @POST("subcategory")
    @FormUrlEncoded
    Call<SubCatRP> getSubCatData(@Field("data") String data, @Query("page") int page);

    //get author data
    @POST("authors")
    @FormUrlEncoded
    Call<AuthorRP> getAuthorData(@Field("data") String data, @Query("page") int page);

    //get book by sub cat data
    @POST("books_by_sub_cat")
    @FormUrlEncoded
    Call<SubCatListBookRP> getBookListBySubCatData(@Field("data") String data, @Query("page") int page);

    //get  book by cat data
    @POST("books_by_cat")
    @FormUrlEncoded
    Call<SubCatListBookRP> getBookListByCatData(@Field("data") String data, @Query("page") int page);

    //get  book by author data
    @POST("books_by_author")
    @FormUrlEncoded
    Call<SubCatListBookRP> getBookListByAuthorData(@Field("data") String data);

    //get  app detail
    @POST("app_details")
    @FormUrlEncoded
    Call<AppDetailRP> getAppDetailData(@Field("data") String data);

    //get  app book detail
    @POST("books_details")
    @FormUrlEncoded
    Call<BookDetailRP> getBookDetailData(@Field("data") String data);

    //get  rate review list
    @POST("books_reviews_list")
    @FormUrlEncoded
    Call<RateReviewRP> getRateReviewListData(@Field("data") String data);

    //get  post rate
    @POST("post_rate")
    @FormUrlEncoded
    Call<PostRateRP> getPostRateData(@Field("data") String data);

    //get  post view
    @POST("post_view")
    @FormUrlEncoded
    Call<JsonObject> getPostViewData(@Field("data") String data);

    //get  post report book
    @POST("user_reports")
    @FormUrlEncoded
    Call<PostRateRP> getReportBookData(@Field("data") String data);

    //get  post report comment
    @POST("user_reports")
    @FormUrlEncoded
    Call<PostRateRP> getReportCommentData(@Field("data") String data);

    //get  post delete comment
    @POST("delete_user_review")
    @FormUrlEncoded
    Call<PostRateRP> getDeleteCommentData(@Field("data") String data);

    //get  setting data
    @POST("app_details")
    @FormUrlEncoded
    Call<AppDetailRP> getSettingPageData(@Field("data") String data);

    //get  login
    @POST("login")
    @FormUrlEncoded
    Call<LoginRP> getLoginData(@Field("data") String data);

    //get  login social
    @POST("social_login")
    @FormUrlEncoded
    Call<LoginRP> getLoginSocialData(@Field("data") String data);

    //get  register
    @POST("signup")
    @FormUrlEncoded
    Call<RegisterRP> getRegisterData(@Field("data") String data);

    //get  forgot
    @POST("forgot_password")
    @FormUrlEncoded
    Call<RegisterRP> getForgotData(@Field("data") String data);

    //get  post continue read
    @POST("post_continue_read")
    @FormUrlEncoded
    Call<JsonObject> getBookContinueData(@Field("data") String data);

    //get  profile
    @POST("profile")
    @FormUrlEncoded
    Call<LoginRP> getProfileData(@Field("data") String data);

    //get fav data
    @POST("user_favourite_post_list")
    @FormUrlEncoded
    Call<SubCatListBookRP> getFavData(@Field("data") String data);

    //get continue data
    @POST("continue_read_list")
    @FormUrlEncoded
    Call<SubCatListBookRP> getContinueData(@Field("data") String data);

    //get edit profile data
    @POST("profile_update")
    @Multipart
    Call<EditProfileRP> getEditProfileData(@Part("data") RequestBody data, @Part MultipartBody.Part part);

    //get book latest data
    @POST("latest_books")
    @FormUrlEncoded
    Call<SubCatListBookRP> getBookLatestData(@Field("data") String data);

    //get trend book data
    @POST("trending_books")
    @FormUrlEncoded
    Call<SubCatListBookRP> getTrendingBookData(@Field("data") String data);

    //get book search data
    @POST("search_book")
    @FormUrlEncoded
    Call<SubCatListBookRP> getSearchBookData(@Field("data") String data, @Query("page") int page);

    //get fav un fav book data
    @POST("post_favourite")
    @FormUrlEncoded
    Call<FavoriteRP> getFavUnFavBookData(@Field("data") String data);

    //get plan list data
    @POST("subscription_plan")
    @FormUrlEncoded
    Call<PlanRP> getPlanListData(@Field("data") String data);

    //get  payment method
    @POST("payment_settings")
    @FormUrlEncoded
    Call<ResponseBody> getPaymentMethodData(@Field("data") String data);

    //get success payment
    @POST("{apiName}")
    @FormUrlEncoded
    Call<PaymentSuccessRP> getPaymentSuccessData(@Path ("apiName")String apiName,@Field("data") String data);

    //get stripe token
    @POST("stripe_token_get")
    @FormUrlEncoded
    Call<StripeCheckOutRP> getStripeTokenData(@Field("data") String data);

    //get paypal token
    @POST("get_braintree_token")
    @FormUrlEncoded
    Call<PaypalTokenRP> getPaypalTokenData(@Field("data") String data);

    //get paypal check out
    @POST("braintree_checkout")
    @FormUrlEncoded
    Call<BraintreeCheckOutRP> getPaypalCheckOutData(@Field("data") String data);


    //get razor pay token
    @POST("razorpay_order_id_get")
    @FormUrlEncoded
    Call<RazorPayTokenRP> getRazorPayTokenData(@Field("data") String data);

    //get payu hash key
    @POST("get_payu_hash")
    @FormUrlEncoded
    Call<PayUMoneyHashRP> getPayUMoneyHashData(@Field("data") String data);

    //get  my subscription
    @POST("check_user_plan")
    @FormUrlEncoded
    Call<ResponseBody> getMySubscriptionData(@Field("data") String data);

    //get home section data
    @POST("home_section")
    @FormUrlEncoded
    Call<HomeRP> getHomeSectionSingleData(@Field("data") String data);

    //get rent list data
    @POST("user_rent_list")
    @FormUrlEncoded
    Call<SubCatListBookRP> getRentBookListData(@Field("data") String data);

    //get filter search data
    @POST("filter_book")
    @FormUrlEncoded
    Call<SubCatListBookRP> getFilterSearchBookData(@Field("data") String data, @Query("page") int page);

    //get all author data
    @POST("all_authors")
    @FormUrlEncoded
    Call<AuthorRP> getAllAuthorData(@Field("data") String data);

    //get all cat data
    @POST("all_category")
    @FormUrlEncoded
    Call<CatRP> getAllCatData(@Field("data") String data);

    //get  app author detail
    @POST("author_info")
    @FormUrlEncoded
    Call<AuthorDetailRP> getAuthorDetailData(@Field("data") String data);

    //get  post view
    @POST("post_download")
    @FormUrlEncoded
    Call<JsonObject> getPostDownloadData(@Field("data") String data);

}
