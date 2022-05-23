package com.app.fixirman.viewmodel;

import android.util.ArrayMap;
import android.widget.ProgressBar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.fixirman.api_db_helper.DaoHelper;
import com.app.fixirman.model.appointment.Appointment;
import com.app.fixirman.model.appointment.AppointmentStatus;
import com.app.fixirman.model.categroy.SubCategory;
import com.app.fixirman.model.provider.ProviderDetail;
import com.app.fixirman.model.provider.ReviewsModel;
import com.app.fixirman.model.request.RequestDetail;
import com.app.fixirman.view.create_request.RequestModel;

import java.util.List;

import javax.inject.Inject;

public class AppointmentViewModel extends ViewModel {
    private DaoHelper helper;
    private MutableLiveData<ArrayMap<Integer, SubCategory>> selectedServices;
    @Inject
    public AppointmentViewModel(DaoHelper helper){
        this.helper = helper;
        selectedServices = new MutableLiveData<>();
    }
    public void init(ProgressBar pb,int userId){
        helper.getUserRequests(pb,userId);
    }

    public void init(ProgressBar pb,int userId,String subCategoryId,String employeeId){
        helper.getOnlineProviderDetail(pb,employeeId,subCategoryId,userId);
    }
    //load sub categories from server
    public void init(ProgressBar pb,int userId,int subCategoryId){
        helper.getOnlineSubCategories(pb,userId,subCategoryId);
    }
    public LiveData<List<Appointment>> getPastAppointments(){
        return helper.getPastAppointments();
    }

    public LiveData<List<Appointment>> getUpcomingAppointments(){
        return helper.getUpComingAppointments();
    }

    public LiveData<Appointment> getAppointment(String appointmentId) {
        LiveData<Appointment> temp  = helper.getAppointment(appointmentId);
        if(temp==null){
            temp = new MutableLiveData<>();
        }
        return temp;
    }

    public LiveData<ProviderDetail> getProviderDetail(String employeeId) {
        return helper.getProviderDetail(employeeId);
    }

    public LiveData<List<ReviewsModel>> getReviews(String employeeId){
        return helper.getReviews(employeeId);
    }
    public LiveData<Integer> getTotalReviews(String employeeId){
        return helper.getTotalReviews(employeeId);
    }
    public LiveData<Integer> getRatingAvg(String employeeId){
        return helper.getRatingAvg(employeeId);
    }

    public void saveAppointment(Appointment appointment) {
        helper.saveAppointment(appointment);
    }

    public LiveData<List<AppointmentStatus>> getStatusHistory(String requestId) {
        return helper.getAppointmentStatusHistory(requestId);
    }

    public LiveData<List<SubCategory>> getSubCategories(int categoryId) {
        return helper.getSubCategories(categoryId+"");
    }

    //cart methods
    public void setSelectList(ArrayMap<Integer, SubCategory> list) {
        selectedServices.setValue(list);
    }

    public MutableLiveData<ArrayMap<Integer, SubCategory>> getSelectedServices() {
        return selectedServices;
    }

    public LiveData<List<RequestDetail>> getUpcomingRequests() {
        return helper.getUpcomingRequests();
    }

    public LiveData<List<RequestDetail>> getPastRequests() {
        return helper.getPastRequests();
    }
}
