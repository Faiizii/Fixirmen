package com.app.fixirman.api_db_helper;

import android.util.Log;
import android.view.KeyEvent;
import android.widget.ProgressBar;

import com.app.fixirman.api.http.ApiInterface;
import com.app.fixirman.database.dao.EventDao;
import com.app.fixirman.model.NormalResponse;
import com.app.fixirman.model.appointment.Appointment;
import com.app.fixirman.model.appointment.AppointmentResponse;
import com.app.fixirman.model.appointment.AppointmentStatus;
import com.app.fixirman.model.categroy.Category;
import com.app.fixirman.model.categroy.CategoryResponse;
import com.app.fixirman.model.categroy.ServiceProviders;
import com.app.fixirman.model.categroy.ServiceType;
import com.app.fixirman.model.categroy.SubCategory;
import com.app.fixirman.model.chat.InboxModel;
import com.app.fixirman.model.chat.MessageModel;
import com.app.fixirman.model.faq.FAQModel;
import com.app.fixirman.model.faq.FAQResponse;
import com.app.fixirman.model.notification.Notification;
import com.app.fixirman.model.notification.NotificationResponse;
import com.app.fixirman.model.provider.ProviderDetail;
import com.app.fixirman.model.provider.ProviderDetailResponse;
import com.app.fixirman.model.provider.ReviewsModel;
import com.app.fixirman.model.request.RequestBid;
import com.app.fixirman.model.request.RequestDetail;
import com.app.fixirman.model.request.RequestResponse;
import com.app.fixirman.model.user.AdModel;
import com.app.fixirman.model.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.fixirman.model.user.UserAddress;
import com.app.fixirman.model.user.UserResponse;
import com.app.fixirman.utils.AppConstants;
import com.app.fixirman.utils.AppUtils;
import com.app.fixirman.utils.Crashlytics;
import com.app.fixirman.view.create_request.RequestModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class DaoHelper {

    private final ApiInterface webservice;
    private final EventDao eventDao;
    private final Executor executor;

    @Inject
    public DaoHelper(ApiInterface webservice, EventDao eventDao, Executor executor) {

        this.webservice = webservice;
        this.eventDao = eventDao;
        this.executor = executor;
    }
    public void logout() {
        eventDao.clearAppointments();
        eventDao.clearAllAppointmentsStatusHistory();
        eventDao.clearNotifications();
        eventDao.clearCompleteInbox();
        eventDao.clearAllMessages();
    }
    public void registerFirebaseListeners(int userId) {
        registerInboxListener(userId);
    }
    private void registerInboxListener(int userId) {
        FirebaseDatabase.getInstance().getReference().child(userId+"").child("inbox")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if(snapshot.exists()){
                            try{
                                InboxModel model = snapshot.getValue(InboxModel.class);
                                eventDao.insertInboxModel(model);
                            }catch (Exception e){
                                Crashlytics.log("child added in dao helper for inbox loading");
                                Crashlytics.logException(e);
                            }
                        }else{
                            Crashlytics.log("child added called without snapshot existence in dao helper for inbox loading");
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if(snapshot.exists()){
                            try{
                                InboxModel model = snapshot.getValue(InboxModel.class);
                                eventDao.updateInboxModel(model);
                            }catch (Exception e){
                                Crashlytics.log("child updated in dao helper for inbox loading");
                                Crashlytics.logException(e);
                            }
                        }else{
                            Crashlytics.log("child update called without snapshot existence in dao helper for inbox loading");
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    //test function
    private LiveData<List<User>> getUpdatedUser() {
        return new DistinctLiveData<List<User>>(){
            @Override
            protected boolean equals(List<User> newObj, List<User> lastObj) {

                Log.e("distinctLiveData", "equals: called" );
                Log.e("distinctLiveData", "nw obj "+newObj );
                Log.e("distinctLiveData", "last obj "+lastObj );
                Log.e("distinctLiveData", "isEquals "+lastObj.equals(newObj) );
                return newObj != null && !newObj.equals(lastObj);
            }

            @Override
            protected LiveData<List<User>> load() { return eventDao.getAllUsers(); }
        }.asLiveData();
    }
    //test function
    public void updateUser(User u) {
        eventDao.updateUser(u);
    }
    private void getOnlineUserList(int userId,int count) {

        executor.execute(() -> {
            webservice.fetchAllUsers(userId,"Y",count).enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                    if (response.isSuccessful()) {
                        // bind data
                        UserResponse m = response.body();
                        if (m.getSuccess()==1) {
                            //eventDao.clearMembersTable();
                            //eventDao.saveMembersList(m.getUser());
                        } else {
                            //unable to retrieve
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<UserResponse> call,@NonNull Throwable t) {
                    t.printStackTrace();
                    Log.e("TAG", "onFailure " + t.getMessage());
                    //unable to retrieve
                }
            });
        });
    }
    public void getOnlineCategories(ProgressBar pb,int userId) {
        AppUtils.showProgressBar(pb);
        executor.execute(()-> webservice.fetchCategories(userId).enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<CategoryResponse> call, @NonNull Response<CategoryResponse> response) {
                AppUtils.hideProgressBar(pb);
                if(response.isSuccessful()){
                    CategoryResponse categoryResponse = response.body();
                    if(categoryResponse.getSuccess() == 1){
                        if(categoryResponse.getCategories() != null) {
                            eventDao.clearCategories();
                            eventDao.insertCategories(categoryResponse.getCategories());
                        }else{
                            Crashlytics.log("categories list is null in get online categories api in dao helper");
                        }
                    }else{
                        Crashlytics.log("Category list response is not " +
                                "successful in get online categories api in " +
                                "dao helper message: "+categoryResponse.getMessage());
                    }
                }else{
                    Crashlytics.log("response code: "+response.code());
                    Crashlytics.log("response message: "+response.message());
                    Crashlytics.logException(new Exception("get online categories api in dao helper"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryResponse> call,@NonNull Throwable t) {
                t.printStackTrace();
                Crashlytics.log("get online categories api in dao helper");
                Crashlytics.logException(t);
                AppUtils.hideProgressBar(pb);
            }
        }));
    }


    public void getOnlineSubCategories(ProgressBar pb,int userId,int categoryId) {

        AppUtils.showProgressBar(pb);
        executor.execute(()-> webservice.fetchSubCategories(userId,categoryId).enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<CategoryResponse> call, @NonNull Response<CategoryResponse> response) {
                AppUtils.hideProgressBar(pb);
                if(response.isSuccessful()){
                    CategoryResponse categoryResponse = response.body();
                    if(categoryResponse.getSuccess() == 1){
                        if(categoryResponse.getSubCategories() != null) {
                            eventDao.clearSubCategories(categoryId);
                            eventDao.insertSubCategories(categoryResponse.getSubCategories());
                        }else{
                            Crashlytics.log("sub categories list is null in get online sub categories api in dao helper");
                        }
                    }else{
                        Crashlytics.log("sub Category list response is not " +
                                "successful in get online sub categories api in " +
                                "dao helper message: "+categoryResponse.getMessage());
                    }
                }else{
                    Crashlytics.log("response code: "+response.code());
                    Crashlytics.log("response message: "+response.message());
                    Crashlytics.logException(new Exception("get online sub categories api in dao helper"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryResponse> call,@NonNull Throwable t) {
                t.printStackTrace();
                Crashlytics.log("get online sub categories api in dao helper");
                Crashlytics.logException(t);
                AppUtils.hideProgressBar(pb);
            }
        }));
    }


    public void getOnlineServiceProviders(ProgressBar pb, int userId, String categoryId, String subCategoryId) {
        AppUtils.showProgressBar(pb);
        executor.execute(()-> webservice.getServiceProviders(userId,categoryId,subCategoryId).enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<CategoryResponse> call, @NonNull Response<CategoryResponse> response) {
                AppUtils.hideProgressBar(pb);
                if(response.isSuccessful()){
                    CategoryResponse categoryResponse = response.body();
                    if(categoryResponse.getSuccess() == 1){
                        if(categoryResponse.getServiceProviders() != null) {
                            eventDao.clearServiceProviders(categoryId,subCategoryId);
                            eventDao.insertServiceProviders(categoryResponse.getServiceProviders());
                        }else{
                            Crashlytics.log("service providers list is null in get online service providers api in dao helper");
                        }
                    }else{
                        Crashlytics.log("service providers list response is not " +
                                "successful in get online service providers api in " +
                                "dao helper message: "+categoryResponse.getMessage());
                    }
                }else{
                    Crashlytics.log("response code: "+response.code());
                    Crashlytics.log("response message: "+response.message());
                    Crashlytics.logException(new Exception("get online service providers api in dao helper"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryResponse> call,@NonNull Throwable t) {
                t.printStackTrace();
                Crashlytics.log("get online service providers api in dao helper");
                Crashlytics.logException(t);
                AppUtils.hideProgressBar(pb);
            }
        }));
    }
    public void getOnlineServiceTypes(ProgressBar pb,int userId) {
        AppUtils.showProgressBar(pb);
        executor.execute(()-> webservice.fetchServiceTypes(userId).enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<CategoryResponse> call, @NonNull Response<CategoryResponse> response) {
                AppUtils.hideProgressBar(pb);
                if(response.isSuccessful()){
                    CategoryResponse categoryResponse = response.body();
                    if(categoryResponse.getSuccess() == 1){
                        if(categoryResponse.getServiceTypes() != null) {
                            eventDao.clearServiceTypes();
                            eventDao.insertServiceTypes(categoryResponse.getServiceTypes());
                        }else{
                            Crashlytics.log("service type list is null in get online get service type api in dao helper");
                        }
                    }else{
                        Crashlytics.log("service type list response is not " +
                                "successful in get online service type api in " +
                                "dao helper message: "+categoryResponse.getMessage());
                    }
                }else{
                    Crashlytics.log("response code: "+response.code());
                    Crashlytics.log("response message: "+response.message());
                    Crashlytics.logException(new Exception("get online service type api in dao helper"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryResponse> call,@NonNull Throwable t) {
                t.printStackTrace();
                Crashlytics.log("get online categories api in dao helper");
                Crashlytics.logException(t);
                AppUtils.hideProgressBar(pb);
            }
        }));
    }

    public void getOnlineAppointments(ProgressBar pb, int userId) {

        AppUtils.showProgressBar(pb);
        executor.execute(()-> webservice.getAppointments(userId).enqueue(new Callback<AppointmentResponse>() {
            @Override
            public void onResponse(@NonNull Call<AppointmentResponse> call,@NonNull Response<AppointmentResponse> response) {
                AppUtils.hideProgressBar(pb);
                if(response.isSuccessful()){
                    AppointmentResponse appointmentResponse = response.body();
                    if(appointmentResponse.getSuccess() == 1){
                        if(appointmentResponse.getAppointments() != null){
                            eventDao.clearAppointments();
                            eventDao.insertAppointments(appointmentResponse.getAppointments());
                            for(Appointment model : appointmentResponse.getAppointments()){
                                if(model.getStatusHistory() != null) {
                                    eventDao.clearAppointmentsStatusHistory(model.getRequestId());
                                    eventDao.insertAppointmentsStatusHistory(model.getStatusHistory());
                                }else{
                                    Crashlytics.log("appointment status list is null in get online appointment api in dao helper");
                                }
                            }
                        }else{
                            Crashlytics.log("Appointment list is null in get online appointment api in dao helper");
                        }
                    }else{
                        Crashlytics.log("appointments list response is not " +
                                "successful in get online appointments api in " +
                                "dao helper message: "+appointmentResponse.getMessage());
                    }
                }else{
                    Crashlytics.log("response code: "+response.code());
                    Crashlytics.log("response message: "+response.message());
                    Crashlytics.logException(new Exception("get online appointment api in dao helper"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<AppointmentResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
                AppUtils.hideProgressBar(pb);
                Crashlytics.log("get online appointment api in dao helper");
                Crashlytics.logException(t);
            }
        }));
    }

    public void getOnlineAppointmentDetail(int userId,String appointmentId) {

        executor.execute(()-> webservice.getAppointmentDetail(appointmentId,userId).enqueue(new Callback<AppointmentResponse>() {
            @Override
            public void onResponse(@NonNull Call<AppointmentResponse> call,@NonNull Response<AppointmentResponse> response) {
                if(response.isSuccessful()){
                    AppointmentResponse appointmentResponse = response.body();
                    if(appointmentResponse.getSuccess() == 1){
                        if(appointmentResponse.getAppointment() != null){
                            Appointment model = appointmentResponse.getAppointment();
                            eventDao.clearAppointments();
                            eventDao.insertAppointment(model);
                            if(model.getStatusHistory() != null) {
                                eventDao.clearAppointmentsStatusHistory(model.getRequestId());
                                eventDao.insertAppointmentsStatusHistory(model.getStatusHistory());
                            }else{
                                Crashlytics.log("appointment status list is null in get online appointment api in dao helper");
                            }
                        }else{
                            Crashlytics.log("appointment model is null in get online appointment detail api in dao helper");
                        }
                    }else{
                        Crashlytics.log("appointment detail response is not " +
                                "successful in get online appointment detail api in " +
                                "dao helper message: "+appointmentResponse.getMessage());
                    }
                }else{
                    Crashlytics.log("response code: "+response.code());
                    Crashlytics.log("response message: "+response.message());
                    Crashlytics.logException(new Exception("get online appointment detail api in dao helper"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<AppointmentResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
                Crashlytics.log("get online appointment detail api in dao helper");
                Crashlytics.logException(t);
            }
        }));
    }


    public void getOnlineNotifications(ProgressBar pb, int userId) {

        AppUtils.showProgressBar(pb);
        executor.execute(()-> webservice.getNotifications(userId).enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(@NonNull Call<NotificationResponse> call,@NonNull Response<NotificationResponse> response) {
                AppUtils.hideProgressBar(pb);
                if(response.isSuccessful()){
                    NotificationResponse notificationResponse = response.body();
                    if(notificationResponse.getSuccess() == 1){
                        if(notificationResponse.getNotifications() != null){
                            eventDao.clearNotifications();
                            eventDao.insertNotifications(notificationResponse.getNotifications());
                        }else{
                            Crashlytics.log("notifications list is null in get online notifications api in dao helper");
                        }
                    }else{
                        Crashlytics.log("notifications list response is not " +
                                "successful in get online notifications api in " +
                                "dao helper message: "+notificationResponse.getMessage());
                    }
                }else{
                    Crashlytics.log("response code: "+response.code());
                    Crashlytics.log("response message: "+response.message());
                    Crashlytics.logException(new Exception("get online notifications api in dao helper"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<NotificationResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
                AppUtils.hideProgressBar(pb);
                Crashlytics.log("get online notifications api in dao helper");
                Crashlytics.logException(t);
            }
        }));
    }

    public void readOnlineNotification(int notificationId,int userId){
        executor.execute(()-> webservice.readNotification(notificationId,userId).enqueue(new Callback<NormalResponse>() {
            @Override
            public void onResponse(@NonNull Call<NormalResponse> call, @NonNull Response<NormalResponse> response) {

                if(response.isSuccessful()){
                    NormalResponse normalResponse = response.body();
                    if(normalResponse.getSuccess() == 1){
                        Notification model = eventDao.getNotification(notificationId);
                        model.setReadStatus("Y");
                        eventDao.updateNotification(model);
                    }else{
                        Crashlytics.log("notifications read response is not " +
                                "successful in read online notifications api in " +
                                "dao helper message: "+normalResponse.getMessage());
                    }
                }else{
                    Crashlytics.log("response code: "+response.code());
                    Crashlytics.log("response message: "+response.message());
                    Crashlytics.logException(new Exception("read online notifications api in dao helper"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<NormalResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
                Crashlytics.log("read online notifications api in dao helper");
                Crashlytics.logException(t);
            }
        }));
    }
    public void getOnlineProviderDetail(ProgressBar pb,String employeeId,String subCategoryId,int userId){
        String message = "get online provider detail api in dao helper";
        AppUtils.showProgressBar(pb);
        executor.execute(()->{
            webservice.getProviderDetail(userId,employeeId,subCategoryId).enqueue(new Callback<ProviderDetailResponse>() {
                @Override
                public void onResponse(@NonNull Call<ProviderDetailResponse> call, @NonNull Response<ProviderDetailResponse> response) {
                    AppUtils.hideProgressBar(pb);
                    if(response.isSuccessful()){
                        ProviderDetailResponse detailResponse = response.body();
                        if(detailResponse.getSuccess() == 1){
                           /* ProviderDetail model = detailResponse.getProviderDetail();
                            if(model != null){
                                eventDao.insertProviderDetail(model);
                                if(model.getReviewsModel() != null){
                                    eventDao.clearReviews(employeeId);
                                    eventDao.insertReviews(model.getReviewsModel());
                                }else{
                                    Crashlytics.log("provider reviews list is null in "+message);
                                }
                            }else{
                                Crashlytics.log("provider detail is null in "+message);
                            }*/
                        }else{
                            Crashlytics.log(message+" "+detailResponse.getMessage());
                        }
                    }else{
                        Crashlytics.log("response code: "+response.code());
                        Crashlytics.log("response message: "+response.message());
                        Crashlytics.logException(new Exception(message));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ProviderDetailResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    Crashlytics.log(message);
                    Crashlytics.logException(t);
                    AppUtils.hideProgressBar(pb);
                }
            });
        });
    }
    public void getOnlineFAQList(ProgressBar pb, int userId, MutableLiveData<List<FAQModel>> liveData) {
        AppUtils.showProgressBar(pb);
        String message = "get online faq list api in dao helper";
        executor.execute(()->{
            //sending 1 just for testing purpose change it when language added
            webservice.getFAQ(userId,1).enqueue(new Callback<FAQResponse>() {
                @Override
                public void onResponse(@NonNull Call<FAQResponse> call, @NonNull Response<FAQResponse> response) {
                    AppUtils.hideProgressBar(pb);
                    if(response.isSuccessful()){
                        FAQResponse faqResponse = response.body();
                        if(faqResponse.getSuccess() == 1){
                            if(faqResponse.getFaq() != null){
                                eventDao.clearFAQ();
                                eventDao.insertFAQ(faqResponse.getFaq());
                                if(liveData != null){
                                    liveData.setValue(faqResponse.getFaq());
                                }
                            }else{
                                Crashlytics.log("faq list is null in "+message);
                            }
                        }else{
                            Crashlytics.log(message+" "+faqResponse.getMessage());
                        }
                    }else{
                        Crashlytics.log("response code: "+response.code());
                        Crashlytics.log("response message: "+response.message());
                        Crashlytics.logException(new Exception(message));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<FAQResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    Crashlytics.log(message);
                    Crashlytics.logException(t);
                    AppUtils.hideProgressBar(pb);
                }
            });
        });
    }
    public void getOnlineRequestDetail(ProgressBar pb,int userId,int requestId){
        AppUtils.showProgressBar(pb);
        String message = "get online request detail api in dao helper";
        executor.execute(()->webservice.getRequestDetails(userId,requestId).enqueue(new Callback<RequestResponse>() {
            @Override
            public void onResponse(@NonNull Call<RequestResponse> call,@NonNull Response<RequestResponse> response) {
                AppUtils.hideProgressBar(pb);
                if(response.isSuccessful()){
                    RequestResponse requestResponse = response.body();
                    if(requestResponse.getSuccess() == 1){
                        insertRequestDetail(requestResponse.getRequestDetail());
                    }else{
                        Crashlytics.log("response fail message: "+requestResponse.getMessage());
                    }
                }else{
                    Crashlytics.log("response code: "+response.code());
                    Crashlytics.log("response message: "+response.message());
                    Crashlytics.logException(new Exception(message));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RequestResponse> call,@NonNull Throwable t) {
                t.printStackTrace();
                Crashlytics.log(message);
                Crashlytics.logException(t);
                AppUtils.hideProgressBar(pb);
            }
        }));
    }
    public void getUserRequests(ProgressBar pb,int userId){
        AppUtils.showProgressBar(pb);
        String message = "get online request detail api in dao helper";
        executor.execute(()->webservice.getUserRequests(userId, AppConstants.TYPE_USER).enqueue(new Callback<RequestResponse>() {
            @Override
            public void onResponse(@NonNull Call<RequestResponse> call,@NonNull Response<RequestResponse> response) {
                AppUtils.hideProgressBar(pb);
                if(response.isSuccessful()){
                    RequestResponse requestResponse = response.body();
                    if(requestResponse.getSuccess() == 1){
                        if(requestResponse.getRequests() != null){
                            eventDao.clearRequests();
                            for (RequestDetail detail : requestResponse.getRequests()){
                                insertRequestDetail(detail);
                            }
                        }
                    }else{
                        Crashlytics.log("response fail message: "+requestResponse.getMessage());
                    }
                }else{
                    Crashlytics.log("response code: "+response.code());
                    Crashlytics.log("response message: "+response.message());
                    Crashlytics.logException(new Exception(message));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RequestResponse> call,@NonNull Throwable t) {
                t.printStackTrace();
                Crashlytics.log(message);
                Crashlytics.logException(t);
                AppUtils.hideProgressBar(pb);
            }
        }));
    }

    public LiveData<List<User>> getAllUsers(int userId,int count){
        getOnlineUserList(userId,count);
        return eventDao.getAllUsers();

    }
    public LiveData<List<Category>> getCategories() {
        return eventDao.getCategories();
    }

    public List<Category> getCategoryList() {
        return eventDao.getCategoryList();
    }

    public void insertSubCategories(List<SubCategory> subCategories) {
        eventDao.insertSubCategories(subCategories);
    }

    public LiveData<List<SubCategory>> getSubCategories(String categoryId) {
        return eventDao.getSubCategories(categoryId);
    }

    public LiveData<List<ServiceProviders>> getServiceProviders(String categoryId, String subCategoryId) {
        return eventDao.getServiceProviders(categoryId,subCategoryId);
    }

    public LiveData<List<Appointment>> getPastAppointments() {
        return eventDao.getPastAppointment();
    }
    public LiveData<List<Appointment>> getUpComingAppointments() {
        return eventDao.getUpComingAppointment();
    }

    public LiveData<Appointment> getAppointment(String appointmentId) {
        return eventDao.getAppointment(appointmentId);
    }

    public LiveData<List<Notification>> getNotifications() {
        return eventDao.getNotifications();
    }

    public LiveData<Integer> getUnreadNotificationCount() {
        return eventDao.getUnreadNotifications();
    }

    public void readNotification(int id, int userId) {
        readOnlineNotification(id,userId);
    }

    public LiveData<List<InboxModel>> getInboxListing() {
        return eventDao.getInboxListing();
    }

    public void updateMessage(MessageModel model) {
        eventDao.updateMessage(model);
    }

    public void insertMessage(MessageModel model) {
        eventDao.insertMessage(model);
    }

    public List<MessageModel> getMessages() {
        return eventDao.getMessages();
    }

    public LiveData<ProviderDetail> getProviderDetail(String employeeId) {
        return eventDao.getProviderDetail(employeeId);
    }

    public LiveData<Integer> getRatingAvg(String employeeId) {
        return eventDao.getRatingAvg(employeeId);
    }

    public LiveData<Integer> getTotalReviews(String employeeId) {
        return eventDao.getTotalReviews(employeeId);
    }
    public LiveData<List<ReviewsModel>> getReviews(String employeeId){
        return eventDao.getReviews(employeeId);
    }

    public void saveAppointment(Appointment appointment) {
        eventDao.insertAppointment(appointment);
        if(appointment.getStatusHistory() != null){
            eventDao.insertAppointmentsStatusHistory(appointment.getStatusHistory());
        }else{
            Crashlytics.log("appointment history list is null");
        }
    }

    public List<FAQModel> getFAQs() {
        return eventDao.getFAQs();
    }

    public void getFilteredResults(String query, MutableLiveData<List<SubCategory>> filteredResult) {
        List<SubCategory> subCategories = eventDao.filterSubCategories("%"+query+"%");
        if(subCategories == null){
            subCategories = new ArrayList<>();
        }
        List<Category> tempList = eventDao.filterCategories("%"+query+"%");
        for(Category category : tempList){
            subCategories.addAll(eventDao.getSubCategoryList(category.getId()+""));
        }
        filteredResult.setValue(subCategories);
    }

    public LiveData<List<AppointmentStatus>> getAppointmentStatusHistory(String requestId) {
        return eventDao.getStatusHistory(requestId);
    }

    public LiveData<List<AdModel>> getAds() {
        return eventDao.getAds();
    }

    public void insertAds(List<AdModel> ads) {
        eventDao.clearAds();
        eventDao.insertAds(ads);
    }

    public LiveData<List<ServiceType>> getServiceTypes(int userId, ProgressBar pb) {
        getOnlineServiceTypes(pb,userId);
        return eventDao.getServiceTypes();
    }

    public LiveData<List<UserAddress>> getUserAddresses(int userId) {
        return eventDao.getAddresses(userId);
    }

    public void insertAddresses(List<UserAddress> userAddress) {
        eventDao.insertAddresses(userAddress);
    }

    public void clearUserAddresses(int userId) {
        eventDao.clearUserAddresses(userId);
    }

    public void insertRequestDetail(RequestDetail requestDetail) {
        if(requestDetail != null){
            eventDao.insertRequestModel(requestDetail);

            if(requestDetail.getServices() != null){
                eventDao.clearRequestServices(requestDetail.getId());
                eventDao.insertRequestServices(requestDetail.getServices());
            }
            if(requestDetail.getBidders() != null){
                eventDao.clearBidders(requestDetail.getId());
                eventDao.insertBidders(requestDetail.getBidders());
            }
        }
    }

    public LiveData<List<RequestDetail>> getPastRequests() {
        //TODO select past request only
        return eventDao.getPastRequests();
    }

    public LiveData<List<RequestDetail>> getUpcomingRequests() {
        //TODO select upcoming request only
        return eventDao.getOnGoingRequests();
    }

    public void deleteAddress(int itemId) {
        eventDao.deleteItem(itemId);
    }

    public LiveData<RequestDetail> getRequestDetail(Integer requestId) {
        return eventDao.getRequestDetail(requestId);
    }

    public LiveData<List<RequestBid>> getRequestBidders(Integer requestId) {
        return eventDao.getRequestBidders(requestId);
    }
}