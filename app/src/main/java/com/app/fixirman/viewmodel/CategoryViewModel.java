package com.app.fixirman.viewmodel;

import android.widget.ProgressBar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.fixirman.api_db_helper.DaoHelper;
import com.app.fixirman.model.categroy.Category;
import com.app.fixirman.model.categroy.ServiceProviders;
import com.app.fixirman.model.categroy.SubCategory;
import com.app.fixirman.model.user.AdModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CategoryViewModel extends ViewModel {
    private DaoHelper helper;
    private MutableLiveData<List<SubCategory>> filteredResult;

    @Inject
    public CategoryViewModel(DaoHelper daoHelper){
        helper = daoHelper;
    }

    //load categories from server
    public void init(ProgressBar pb,int userId){
        helper.getOnlineCategories(pb,userId);
    }

    //load sub categories from server
    public void init(ProgressBar pb,int userId,int subCategoryId){
        helper.getOnlineSubCategories(pb,userId,subCategoryId);
    }

    //get service providers from server
    public void init(ProgressBar pb, int userId, String categoryId, String subCategoryId) {
        helper.getOnlineServiceProviders(pb,userId,categoryId,subCategoryId);
    }

    public LiveData<List<Category>> getCategories(){
        return helper.getCategories();
    }

    public LiveData<List<ServiceProviders>> getServiceProviders(String categoryId, String subCategoryId){
        return helper.getServiceProviders(categoryId,subCategoryId);
    }

    public MutableLiveData<List<SubCategory>> getFilteredResult() {
        if(filteredResult == null)
            filteredResult = new MutableLiveData<>();
        return filteredResult;
    }
    public void setFilteredResult(List<SubCategory> list){
        filteredResult.setValue(list);
    }
    public void clearFilteredResults(){
        filteredResult.setValue(new ArrayList<>());
    }

    public void getFilteredResults(String query) {
         helper.getFilteredResults(query,filteredResult);
    }

    public void inertSubCategories(List<SubCategory> subCategories) {
        helper.insertSubCategories(subCategories);
    }

    public LiveData<List<AdModel>> getAds() {
        return helper.getAds();
    }
}
