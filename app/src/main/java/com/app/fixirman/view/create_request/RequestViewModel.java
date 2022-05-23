package com.app.fixirman.view.create_request;

import android.util.ArrayMap;
import android.widget.ProgressBar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.fixirman.api_db_helper.DaoHelper;
import com.app.fixirman.model.categroy.Category;
import com.app.fixirman.model.categroy.ServiceType;
import com.app.fixirman.model.categroy.SubCategory;
import com.app.fixirman.model.request.RequestBid;
import com.app.fixirman.model.request.RequestDetail;
import com.app.fixirman.model.user.UserAddress;

import java.util.List;

import javax.inject.Inject;

public class RequestViewModel extends ViewModel {
    private final String TAG = "RequestVm";

    private MutableLiveData<ArrayMap<Integer,RequestModel>> selectedItems;
    private MutableLiveData<ArrayMap<Integer,SubCategory>> selectedServices;
    private ArrayMap<Integer,RequestModel> requestModels;
    private RequestModel currentModel;
    private UserAddress address;
    private String paymentMethod;
    private boolean isFirstAdded;

    private final DaoHelper helper;
    @Inject
    public RequestViewModel(DaoHelper helper){
        this.helper = helper;
        isFirstAdded = false;
    }
    public void init(ProgressBar pb,int userId){
        //get service type
        helper.getOnlineSubCategories(pb,userId,currentModel.getCategory().getId());
    }

    public MutableLiveData<ArrayMap<Integer,RequestModel>> getSelectedItems() {
        if(selectedItems == null){
            selectedItems = new MutableLiveData<>();
        }
        return selectedItems;
    }

    public MutableLiveData<ArrayMap<Integer, SubCategory>> getSelectedServices() {
        if(selectedServices == null){
            selectedServices = new MutableLiveData<>();
        }
        return selectedServices;
    }

    public void addToCart(){
        if(requestModels == null)
            requestModels = new ArrayMap<>();
        requestModels.put(currentModel.getCategory().getId(),currentModel);
        selectedItems.setValue(requestModels);
        currentModel = new RequestModel();
        selectedServices.setValue(new ArrayMap<>());
        isFirstAdded = true;
    }

    public void addCategory(Category category) {
        currentModel = new RequestModel();
        currentModel.setCategory(category);
    }
    public void setServices(ArrayMap<Integer, SubCategory> list){
        currentModel.setServices(list);
        if(selectedServices == null){
            selectedServices = new MutableLiveData<>();
        }
        selectedServices.setValue(list);
    }
    public void setImages(List<String> images){
        currentModel.setImages(images);
    }
    public void setTime(String timeSlot){
        currentModel.setTime(timeSlot);
    }
    public void setDate(String date){
        currentModel.setDate(date);
    }
    public void setDescription(String text){
        currentModel.setDescription(text);
    }
    public void  setServiceType(int serviceType){
        currentModel.setServiceTypeId(serviceType);
    }
    public void setCouponId(int couponId){
        currentModel.setCouponId(couponId);
    }

    public RequestModel getCurrentModel() {
        return currentModel;
    }

    public UserAddress getAddress() {
        return address;
    }

    public void setAddress(UserAddress address) {
        this.address = address;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }


    public LiveData<List<SubCategory>> getSubCategories() {
        return helper.getSubCategories(currentModel.getCategory().getId()+"");
    }

    public String getCategoryName() {
        try {
            return currentModel.getCategory().getTitle();
        }catch (Exception e){
            return "Please Select Services";
        }
    }

    public boolean isFirstAdded() {
        return isFirstAdded;
    }

    public LiveData<List<ServiceType>> getServiceTypes(int userId, ProgressBar pb) {
        return helper.getServiceTypes(userId,pb);
    }

    public List<Category> getCategoryList() {
        return helper.getCategoryList();
    }

    public boolean isAlreadyAdded(Integer id) {
        return requestModels != null && requestModels.containsKey(id);
    }

    public void removeItem(int keyId) {
        requestModels.remove(keyId);
        selectedItems.setValue(requestModels);
    }
    public boolean isItemsAdded(){
        return requestModels != null && requestModels.size() > 0;
    }

    public ArrayMap<Integer, RequestModel> getRequestModels() {
        return requestModels != null ? requestModels : new ArrayMap<>();
    }

    public void saveRequestDetail(RequestDetail requestDetail) {
        helper.insertRequestDetail(requestDetail);
    }

    public void getOnlineRequestDetail(ProgressBar pb,Integer id, int userId) {
        helper.getOnlineRequestDetail(pb,userId,id);
    }

    public LiveData<RequestDetail> getRequestDetail(Integer requestId) {
        return helper.getRequestDetail(requestId);
    }

    public LiveData<List<RequestBid>> getBidders(Integer requestId) {
        return helper.getRequestBidders(requestId);
    }
}