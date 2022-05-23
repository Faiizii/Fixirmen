package com.app.fixirman.api.http;

//@Field requires a mandatory
//@Query can pass a null value.
//@Body – Sends Java objects as request body

import com.app.fixirman.api.params.HttpParams;
import com.app.fixirman.model.NormalResponse;
import com.app.fixirman.model.appointment.AppointmentResponse;
import com.app.fixirman.model.categroy.CategoryResponse;
import com.app.fixirman.model.faq.FAQResponse;
import com.app.fixirman.model.notification.NotificationResponse;
import com.app.fixirman.model.provider.ProviderDetailResponse;
import com.app.fixirman.model.request.RequestResponse;
import com.app.fixirman.model.user.UserResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

    @FormUrlEncoded
    @POST(HttpParams.API_USERS)
    Call<UserResponse> fetchAllUsers(
            @Field(HttpParams.USER_ID) int userId,
            @Field(HttpParams.IS_APPROVED) String is_approved,
            @Field(HttpParams.COUNT) int count
    );

    @FormUrlEncoded
    @POST(HttpParams.API_PHONE_EXISTS)
    Call<UserResponse> verifyMyPhone(
            @Field(HttpParams.USER_PHONE) String phoneNumber,
            @Field(HttpParams.USER_TYPE) String userType,
            @Field(HttpParams.PARAM_TYPE) String verificationType,
            @Field(HttpParams.MANUFACTURER) String manufacturer,
            @Field(HttpParams.MODEL) String model,
            @Field(HttpParams.PLATFORM) String platform,
            @Field(HttpParams.UUID) String uuid,
            @Field(HttpParams.SERIAL) String serial,
            @Field(HttpParams.VERSION) String version,
            @Field(HttpParams.TOKEN) String token,
            @Field(HttpParams.STATUS) String status,
            @Field(HttpParams.PARAM_APP_VERSION) String appVersion
    );
    @FormUrlEncoded
    @POST(HttpParams.API_USER_LOGIN)
    Call<UserResponse> loginUser(
            @Field(HttpParams.USER_PHONE) String phone,
            @Field(HttpParams.USER_PASSWORD) String password,
            @Field(HttpParams.MANUFACTURER) String manufacturer,
            @Field(HttpParams.MODEL) String model,
            @Field(HttpParams.PLATFORM) String platform,
            @Field(HttpParams.UUID) String uuid,
            @Field(HttpParams.SERIAL) String serial,
            @Field(HttpParams.VERSION) String version,
            @Field(HttpParams.TOKEN) String token,
            @Field(HttpParams.STATUS) String status,
            @Field(HttpParams.PARAM_APP_VERSION) String appVersion
    );

    @FormUrlEncoded
    @POST(HttpParams.API_USER_LOGOUT)
    Call<NormalResponse> logOut(
            @Field(HttpParams.USER_ID) int userId,
            @Field(HttpParams.USER_TYPE) String type,
            @Field(HttpParams.MANUFACTURER) String manufacturer,
            @Field(HttpParams.MODEL) String model,
            @Field(HttpParams.PLATFORM) String platform,
            @Field(HttpParams.UUID) String uuid,
            @Field(HttpParams.SERIAL) String serial,
            @Field(HttpParams.VERSION) String version,
            @Field(HttpParams.TOKEN) String token,
            @Field(HttpParams.STATUS) String status,
            @Field(HttpParams.PARAM_APP_VERSION) String appVersion
    );

    @FormUrlEncoded
    @POST(HttpParams.API_USER_REGISTRATION)
    Call<UserResponse> RegisterUser(
            @Field(HttpParams.USER_PHONE) String phone,
            @Field(HttpParams.USER_PASSWORD) String password,
            @Field(HttpParams.USER_EMAIL) String email,
            @Field(HttpParams.USER_USERNAME) String username,
            @Field(HttpParams.USER_FULL_NAME) String fullName,
            @Field(HttpParams.USER_DESCRIPTION) String description,
            @Field(HttpParams.USER_ADDRESS) String address,
            @Field(HttpParams.LATITUDE) String latitude,
            @Field(HttpParams.LONGITUDE) String longitude,
            @Field(HttpParams.MANUFACTURER) String manufacturer,
            @Field(HttpParams.MODEL) String model,
            @Field(HttpParams.PLATFORM) String platform,
            @Field(HttpParams.UUID) String uuid,
            @Field(HttpParams.SERIAL) String serial,
            @Field(HttpParams.VERSION) String version,
            @Field(HttpParams.TOKEN) String token,
            @Field(HttpParams.STATUS) String status,
            @Field(HttpParams.PARAM_APP_VERSION) String appVersion
    );

    @FormUrlEncoded
    @POST(HttpParams.API_UPDATE_PASSWORD)
    Call<UserResponse> updatePassword(
            @Field(HttpParams.USER_PHONE) String phone,
            @Field(HttpParams.USER_PASSWORD) String password
    );

    @FormUrlEncoded
    @POST(HttpParams.API_GET_CATEGORIES)
    Call<CategoryResponse> fetchCategories(
            @Field(HttpParams.USER_ID) int userId
    );

    @FormUrlEncoded
    @POST(HttpParams.API_GET_SERVICE_TYPES)
    Call<CategoryResponse> fetchServiceTypes(
            @Field(HttpParams.USER_ID) int userId
    );

    @FormUrlEncoded
    @POST(HttpParams.API_GET_SUB_CATEGORIES)
    Call<CategoryResponse> fetchSubCategories(
            @Field(HttpParams.USER_ID) int userId,
            @Field(HttpParams.CATEGORY_ID) int categoryId
    );

    @FormUrlEncoded
    @POST(HttpParams.API_FILTER_SUB_CATEGORIES)
    Call<CategoryResponse> filterSubCategories(
            @Field(HttpParams.USER_ID) int userId,
            @Field(HttpParams.QUERY) String query
    );

    @FormUrlEncoded
    @POST(HttpParams.API_GET_SERVICE_PROVIDERS)
    Call<CategoryResponse> getServiceProviders(
            @Field(HttpParams.USER_ID) int userId,
            @Field(HttpParams.CATEGORY_ID) String categoryId,
            @Field(HttpParams.SUB_CATEGORY_ID) String subCategoryId
    );

    @FormUrlEncoded
    @POST(HttpParams.API_GET_APPOINTMENTS)
    Call<AppointmentResponse> getAppointments(
            @Field(HttpParams.USER_ID) int userId
    );

    @FormUrlEncoded
    @POST(HttpParams.API_GET_APPOINTMENT_DETAIL)
    Call<AppointmentResponse> getAppointmentDetail(
            @Field(HttpParams.APPOINTMENT_ID) String appointmentId,
            @Field(HttpParams.USER_ID) int userId
    );

    @FormUrlEncoded
    @POST(HttpParams.API_GET_NOTIFICATIONS)
    Call<NotificationResponse> getNotifications(
            @Field(HttpParams.USER_ID) int userId
    );

    @FormUrlEncoded
    @POST(HttpParams.API_READ_NOTIFICATION)
    Call<NormalResponse> readNotification(
            @Field(HttpParams.NOTIFICATION_ID) Integer notificationId,
            @Field(HttpParams.USER_ID) int userId
    );

    @FormUrlEncoded
    @POST(HttpParams.API_GET_PROVIDER_DETAIL)
    Call<ProviderDetailResponse> getProviderDetail(
            @Field(HttpParams.USER_ID) int userId,
            @Field(HttpParams.EMPLOYEE_ID) String employeeId,
            @Field(HttpParams.SUB_CATEGORY_ID) String subCategoryId
    );


    @Multipart
    @POST(HttpParams.API_UPDATE_PROFILE)
    Call<UserResponse> updateProfile(
            @Part(HttpParams.USER_ID) RequestBody id,
            @Part(HttpParams.USER__NAME) RequestBody name,
            @Part(HttpParams.USER_PHONE) RequestBody phone,
            @Part(HttpParams.USER_EMAIL) RequestBody email,
            @Part(HttpParams.USER_CNIC) RequestBody cnic,
            @Part(HttpParams.DESCRIPTION) RequestBody description,
            @Part(HttpParams.USER_TYPE) RequestBody userType,
            @Part MultipartBody.Part image
    );

    @FormUrlEncoded
    @POST(HttpParams.API_GET_TIME_SLOTS)
    Call<ProviderDetailResponse> getTimeSlots(
            @Field(HttpParams.USER_ID) int userId,
            @Field(HttpParams.SUB_CATEGORY_ID) String  subCategoryId
    );

    @FormUrlEncoded
    @POST(HttpParams.API_CREATE_APPOINTMENT)
    Call<AppointmentResponse> createAppointment(
            @Field(HttpParams.CARD_NUMBER) String cardNumber,
            @Field(HttpParams.CARD_EXPIRY_DATE) String expiryDate,
            @Field(HttpParams.CARD_CVC) String cvc,
            @Field(HttpParams.POSTAL_CODE) String postalCode,
            @Field(HttpParams.USER_ID) int userId,
            @Field(HttpParams.TIME) String time,
            @Field(HttpParams.DATE) String date,
            @Field(HttpParams.DESCRIPTION) String description,
            @Field(HttpParams.ADDRESS_ID) String  addressId,
            @Field(HttpParams.USER_ADDRESS) String  addressLine,
            @Field(HttpParams.LATITUDE) Double  latitude,
            @Field(HttpParams.LONGITUDE) Double  longitude,
            @Field(HttpParams.SUB_CATEGORY_ID) String subCategoryId
    );

    @FormUrlEncoded
    @POST(HttpParams.API_SEND_MAIL)
    Call<NormalResponse> sendMailToCustomerSupport(
            @Field(HttpParams.USER_ID) int userId,
            @Field(HttpParams.USER_EMAIL) String email,
            @Field(HttpParams.MESSAGE) String message
    );

    @FormUrlEncoded
    @POST(HttpParams.API_GET_FAQ)
    Call<FAQResponse> getFAQ(
            @Field(HttpParams.USER_ID) int userId,
            @Field(HttpParams.LANGUAGE_ID) int languageId
    );

    @FormUrlEncoded
    @POST(HttpParams.API_UPDATE_ADDRESS)
    Call<UserResponse> updateAddressOnServer(
            @Field(HttpParams.USER_ID) int userId,
            @Field(HttpParams.USER_ADDRESS) String address,
            @Field(HttpParams.USER_ADDRESS_TITLE) String addressInfo,
            @Field(HttpParams.LATITUDE) double latitude,
            @Field(HttpParams.LONGITUDE) double longitude,
            @Field(HttpParams.ADDRESS_TYPE) String addressType,
            @Field(HttpParams.ADDRESS_ID) int addressId
    );
    @FormUrlEncoded
    @POST(HttpParams.API_DELETE_ADDRESS)
    Call<NormalResponse> deleteAddressOnServer(
            @Field(HttpParams.USER_ID) int userId,
            @Field(HttpParams.ADDRESS_ID) int addressId
    );

    @FormUrlEncoded
    @POST(HttpParams.APPLY_COUPON)
    Call<AppointmentResponse> applyCoupon(
            @Field(HttpParams.COUPON_CODE) String code,
            @Field(HttpParams.USER_ID) int userId
    );

    @FormUrlEncoded
    @POST(HttpParams.API_CREATE_REQUEST)
    Call<RequestResponse> createRequest(
            @Field(HttpParams.USER_ID) int userId,
            @Field(HttpParams.PAYMENT_METHOD) String paymentMethod,
            @Field(HttpParams.ADDRESS_ID) Integer addressId,
            @Field(HttpParams.USER_ADDRESS) String address,
            @Field(HttpParams.LATITUDE) Double latitude,
            @Field(HttpParams.LONGITUDE) Double longitude,
            @Field(HttpParams.REQUEST_ITEMS) String items
    );

    @FormUrlEncoded
    @POST(HttpParams.API_GET_REQUEST_DETAIL)
    Call<RequestResponse> getRequestDetails(
            @Field(HttpParams.USER_ID) int userId,
            @Field(HttpParams.REQUEST_ID) int requestId
    );

    @FormUrlEncoded
    @POST(HttpParams.API_GET_USER_REQUEST)
    Call<RequestResponse> getUserRequests(
            @Field(HttpParams.USER_ID) int userId,
            @Field(HttpParams.USER_TYPE) String userType
    );


    @FormUrlEncoded
    @POST(HttpParams.API_ACCEPT_BID)
    Call<NormalResponse> acceptBid(
            @Field(HttpParams.USER_ID) int userId,
            @Field(HttpParams.BID_ID) int bidId
    );
    @FormUrlEncoded
    @POST(HttpParams.API_CHANGE_REQUEST_STATUS)
    Call<NormalResponse> changeRequestStatus(
            @Field(HttpParams.USER_ID) int userId,
            @Field(HttpParams.BID_ID) int bidId,
            @Field(HttpParams.STATUS) String status
    );
    @FormUrlEncoded
    @POST(HttpParams.API_SUBMIT_RATING)
    Call<NormalResponse> submitRating(
            @Field(HttpParams.USER_ID) int userId,
            @Field(HttpParams.REQUEST_ID) int requestId,
            @Field(HttpParams.CATEGORY_ID) int categoryId,
            @Field(HttpParams.RATING) float rating,
            @Field(HttpParams.FEEDBACK) String reviewMessage,
            @Field(HttpParams.RATING_BY) int ratingBy
    );
}