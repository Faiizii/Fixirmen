package com.app.fixirman.api.params;


public class HttpParams {

    public static final String APP_VERSION_PARAM = "app_version";

    public static final String API_USERS = "get_users";
    public static final String API_PHONE_EXISTS = "user/check_phone_number";
    public static final String API_USER_LOGIN = "login_user";
    public static final String API_USER_LOGOUT = "logout_user";
    public static final String API_USER_REGISTRATION = "register_user";
    public static final String API_UPDATE_PASSWORD = "update_password_user";
    public static final String API_GET_CATEGORIES = "api/load_categories";
    public static final String API_GET_SUB_CATEGORIES = "api/load_services";
    public static final String API_GET_SERVICE_TYPES = "api/load_service_types";
    public static final String API_GET_SERVICE_PROVIDERS = "get_service_providers";
    public static final String API_GET_APPOINTMENTS = "get_user_appointments";
    public static final String API_GET_APPOINTMENT_DETAIL = "get_appointment_details";
    public static final String API_GET_NOTIFICATIONS = "notification/load_notifications";
    public static final String API_READ_NOTIFICATION = "notification/read_notification";
    public static final String API_GET_PROVIDER_DETAIL = "get_provider_details";

    public static final String API_UPDATE_PROFILE = "user/update_profile";
    public static final String API_GET_TIME_SLOTS = "api/get_time_slots";

    public static final String API_CREATE_APPOINTMENT = "create_appointment";

    public static final String API_SEND_MAIL = "mail_to_customer_support";
    public static final String API_GET_FAQ = "user/get_faq";

    public static final String API_UPDATE_ADDRESS = "user/save_address";
    public static final String API_DELETE_ADDRESS = "user/delete_address";
    public static final String API_FILTER_SUB_CATEGORIES = "filter_sub_categories";
    public static final String QUERY = "query";

    public static final String NOTIFICATION_ID = "notification_id";
    public static final String APPOINTMENT_ID = "request_id";

    public static final String EMPLOYEE_ID = "employee_id";
    public static final String USER_ID = "user_id";
    public static final String IS_APPROVED = "is_approved";
    public static final String COUNT = "count";

    public static final String CATEGORY_ID = "category_id";
    public static final String SUB_CATEGORY_ID = "sub_category_id";

    public static final String PARAM_TYPE = "type";
    public static final String USER_TYPE = "user_type";
    public static final String USER_PHONE = "phone";
    public static final String USER_IMAGE = "image";
    public static final String USER_PASSWORD = "password";
    public static final String USER_FULL_NAME = "full_name";
    public static final String USER__NAME = "name";
    public static final String USER_USERNAME = "username";
    public static final String USER_ADDRESS = "address";
    public static final String USER_ADDRESS_TITLE = "address_title";
    public static final String USER_DESCRIPTION = "description";
    public static final String USER_EMAIL = "email";
    public static final String USER_CNIC = "cnic";

    //update address
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String ADDRESS_TYPE = "address_type";

    //device params
    public static final String PARAM_APP_VERSION = "app_version";
    public static final String MANUFACTURER = "manufacturer";
    public static final String MODEL = "model";
    public static final String PLATFORM = "platform";
    public static final String VERSION = "version";
    public static final String TOKEN = "token";
    public static final String SERIAL = "serial";
    public static final String STATUS = "status";
    public static final String UUID = "uuid";

    //card data
    public static final String CARD_NUMBER = "card_number";
    public static final String CARD_EXPIRY_DATE = "expiry_date";
    public static final String CARD_CVC = "cvc";
    public static final String POSTAL_CODE = "postal_code";

    //create appointment
    public static final String  TIME = "time";
    public static final String  DATE = "date";
    public static final String  DESCRIPTION = "description";
    public static final String  ADDRESS_ID = "address_id";
    public static final String  SERVICE_ID = "emp_category_id";
    public static final String  PAYMENT_ID = "payment_id";

    //mail params
    public static final String  MESSAGE = "message";

    //language params
    public static final String  LANGUAGE_ID = "language_id";

    //coupon
    public static final String  APPLY_COUPON = "api/apply_coupons";
    public static final String  COUPON_CODE = "coupon_code";

    //request
    public static final String  API_CREATE_REQUEST = "api/create_request";
    public static final String  API_GET_REQUEST_DETAIL = "api/get_request_detail";
    public static final String  API_GET_USER_REQUEST = "api/get_user_requests";
    public static final String  API_ACCEPT_BID = "api/bid_accept";
    public static final String  API_CHANGE_REQUEST_STATUS = "api/change_bid_status";
    public static final String  API_SUBMIT_RATING = "api/submit_rating";
    public static final String   PAYMENT_METHOD = "payment_method";
    public static final String   REQUEST_ITEMS = "items";
    public static final String   REQUEST_ID = "request_id";
    public static final String   BID_ID = "bid_id";
    public static final String   RATING = "rating";
    public static final String   FEEDBACK = "feedback";
    public static final String   RATING_BY = "rating_by";


}
