package com.app.fixirman.database.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.app.fixirman.model.appointment.Appointment;
import com.app.fixirman.model.appointment.AppointmentStatus;
import com.app.fixirman.model.categroy.Category;
import com.app.fixirman.model.categroy.ServiceProviders;
import com.app.fixirman.model.categroy.ServiceType;
import com.app.fixirman.model.categroy.SubCategory;
import com.app.fixirman.model.chat.InboxModel;
import com.app.fixirman.model.chat.MessageModel;
import com.app.fixirman.model.faq.FAQModel;
import com.app.fixirman.model.notification.Notification;
import com.app.fixirman.model.provider.ProviderDetail;
import com.app.fixirman.model.provider.ReviewsModel;
import com.app.fixirman.model.request.RequestBid;
import com.app.fixirman.model.request.RequestDetail;
import com.app.fixirman.model.request.RequestService;
import com.app.fixirman.model.user.AdModel;
import com.app.fixirman.model.user.User;
import com.app.fixirman.model.user.UserAddress;

import static androidx.room.OnConflictStrategy.REPLACE;
@Dao
public interface EventDao {

    @Query("Select * From User")
    LiveData<List<User>> getAllUsers();

    @Query("Select * from User where id =:id")
    LiveData<User> getUser(String id);

    @Query("DELETE from User")
    void clearMembersTable();

    @Insert(onConflict = REPLACE)
    void saveMembersList(User user);

    @Update
    void updateUser(User u);

    @Query("Delete from Category")
    void clearCategories();

    @Insert(onConflict = REPLACE)
    void insertCategories(List<Category> categories);

    @Query("Select * From Category")
    LiveData<List<Category>> getCategories();

    @Query("Select * From Category")
    List<Category> getCategoryList();


    @Query("SELECT * FROM Category WHERE title LIKE :query")
    List<Category> filterCategories(String query);

    @Insert(onConflict = REPLACE)
    void insertSubCategories(List<SubCategory> subCategories);

    @Query("Delete from SubCategory where categoryId = :categoryId")
    void clearSubCategories(int categoryId);

    @Query("Select * From SubCategory where categoryId = :categoryId")
    LiveData<List<SubCategory>> getSubCategories(String categoryId);

    @Query("Select * From SubCategory where categoryId = :categoryId")
    List<SubCategory> getSubCategoryList(String categoryId);

    @Query("SELECT * FROM SubCategory WHERE title LIKE :query")
    List<SubCategory> filterSubCategories(String query);

    @Query("Delete From ServiceProviders where categoryId = :categoryId AND subCategoryId = :subCategoryId")
    void clearServiceProviders(String categoryId, String subCategoryId);

    @Insert(onConflict = REPLACE)
    void insertServiceProviders(List<ServiceProviders> serviceProviders);

    @Query("Select * From ServiceProviders where categoryId = :categoryId AND subCategoryId = :subCategoryId")
    LiveData<List<ServiceProviders>> getServiceProviders(String categoryId, String subCategoryId);

    //getAppointments

    @Query("Delete From Appointment")
    void clearAppointments();

    @Insert(onConflict = REPLACE)
    void insertAppointments(List<Appointment> appointments);

    @Insert(onConflict = REPLACE)
    void insertAppointment(Appointment model);

    @Query("Select * From Appointment")
    LiveData<List<Appointment>> getAppointments();

    @Query("Select * From Appointment where status > '3' order by requestId desc")
    LiveData<List<Appointment>> getPastAppointment();

    @Query("Select * From Appointment where status < '4' order by requestId desc")
    LiveData<List<Appointment>> getUpComingAppointment();

    @Query("Select * From Appointment where requestId = :appointmentId")
    LiveData<Appointment> getAppointment(String appointmentId);

    @Query("Delete From AppointmentStatus where requestId = :requestId")
    void clearAppointmentsStatusHistory(String requestId);

    @Query("Delete From AppointmentStatus")
    void clearAllAppointmentsStatusHistory();

    @Insert(onConflict = REPLACE)
    void insertAppointmentsStatusHistory(List<AppointmentStatus> statusHistory);

    @Query("Select * From AppointmentStatus where requestId = :requestId order by id asc")
    LiveData<List<AppointmentStatus>> getStatusHistory(String requestId);

    //notifications

    @Query("Delete From Notification")
    void clearNotifications();

    @Insert(onConflict = REPLACE)
    void insertNotifications(List<Notification> notifications);

    @Query("Select * From Notification order by id")
    LiveData<List<Notification>> getNotifications();

    @Query("Select count(*) From Notification where readStatus = 'N'")
    LiveData<Integer> getUnreadNotifications();

    @Query("Select * From Notification where id =:notificationId")
    Notification getNotification(int notificationId);

    @Update
    void updateNotification(Notification model);

    //for chats

    @Insert(onConflict = REPLACE)
    void insertInboxModel(InboxModel model);

    @Update
    void updateInboxModel(InboxModel model);

    @Query("Select * From InboxModel order by timeStamp desc")
    LiveData<List<InboxModel>> getInboxListing();

    @Query("Delete From InboxModel")
    void clearCompleteInbox();

    @Insert(onConflict = REPLACE)
    void insertMessage(MessageModel model);

    @Update
    void updateMessage(MessageModel model);

    @Query("Select * from MessageModel")
    List<MessageModel> getMessages();

    @Query("Delete From MessageModel")
    void clearAllMessages();

    //provider methods

    @Insert(onConflict = REPLACE)
    void insertProviderDetail(ProviderDetail model);

    @Query("Select * From ProviderDetail where employeeId = :employeeId")
    LiveData<ProviderDetail> getProviderDetail(String employeeId);

    //provider reviews methods
    @Query("Delete From ReviewsModel where employeeId = :employeeId")
    void clearReviews(String employeeId);

    @Insert(onConflict = REPLACE)
    void insertReviews(List<ReviewsModel> reviewsModel);

    @Query("Select * From ReviewsModel where employeeId = :employeeId")
    LiveData<List<ReviewsModel>> getReviews(String employeeId);

    @Query("Select AVG(rating) from ReviewsModel where employeeId = :employeeId")
    LiveData<Integer> getRatingAvg(String employeeId);

    @Query("Select COUNT(*) From ReviewsModel where employeeId = :employeeId")
    LiveData<Integer> getTotalReviews(String employeeId);

    //faq
    @Query("Delete From FAQModel")
    void clearFAQ();

    @Insert(onConflict = REPLACE)
    void insertFAQ(List<FAQModel> faq);

    @Query("Select * From FAQModel")
    List<FAQModel> getFAQs();

    //ads
    @Query("Select * From AdModel")
    LiveData<List<AdModel>> getAds();

    @Query("Delete From AdModel")
    void clearAds();

    @Insert(onConflict = REPLACE)
    void insertAds(List<AdModel> ads);

    //service types
    @Query("Delete From ServiceType")
    void clearServiceTypes();

    @Insert(onConflict = REPLACE)
    void insertServiceTypes(List<ServiceType> serviceTypes);

    @Query("Select * From ServiceType")
    LiveData<List<ServiceType>> getServiceTypes();


    @Query("Select * From UserAddress where userId = :userId")
    LiveData<List<UserAddress>> getAddresses(int userId);

    @Insert(onConflict = REPLACE)
    void insertAddresses(List<UserAddress> userAddress);

    @Query("Delete From useraddress where userId = :userId")
    void clearUserAddresses(int userId);

    @Insert(onConflict = REPLACE)
    void insertRequestModel(RequestDetail requestDetail);

    @Query("Delete From RequestService where requestId = :id")
    void clearRequestServices(Integer id);


    @Insert(onConflict = REPLACE)
    void insertRequestServices(List<RequestService> requestService);

    @Query("Delete From RequestDetail")
    void clearRequests();

    @Query("Select * From RequestDetail order by id desc")
    LiveData<List<RequestDetail>> getRequests();

    @Query("Select * From RequestDetail where status = 'PENDING' OR status = 'START' OR status = 'ACCEPT' OR status = 'SEARCHING...' order by id desc")
    LiveData<List<RequestDetail>> getOnGoingRequests();

    @Query("Select * From RequestDetail where status != 'PENDING' OR status != 'START' OR status != 'ACCEPT' OR status != 'SEARCHING...' order by id desc")
    LiveData<List<RequestDetail>> getPastRequests();

    @Query("Delete From UserAddress where id = :itemId")
    void deleteItem(int itemId);

    @Query("Select * From RequestDetail where id = :requestId")
    LiveData<RequestDetail> getRequestDetail(Integer requestId);

    @Query("Delete From RequestBid where requestId = :id")
    void clearBidders(Integer id);

    @Insert(onConflict = REPLACE)
    void insertBidders(List<RequestBid> bidders);

    @Query("Select * From RequestBid where requestId = :requestId")
    LiveData<List<RequestBid>> getRequestBidders(int requestId);
}