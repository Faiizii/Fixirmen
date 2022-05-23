package com.app.fixirman.database;

import com.app.fixirman.database.dao.EventDao;
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

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, UserAddress.class, Category.class, SubCategory.class, ServiceProviders.class,
        Appointment.class, AppointmentStatus.class, Notification.class, MessageModel.class,
        InboxModel.class, ProviderDetail.class, ReviewsModel.class, FAQModel.class, AdModel.class,
        ServiceType.class, RequestDetail.class, RequestService.class, RequestBid.class
},
        version = 2,exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile MyDatabase INSTANCE;

    // --- DAO ---
    public abstract EventDao eventDao();
}
