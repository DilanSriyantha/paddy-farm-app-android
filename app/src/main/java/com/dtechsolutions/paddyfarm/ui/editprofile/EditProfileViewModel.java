package com.dtechsolutions.paddyfarm.ui.editprofile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.dtechsolutions.paddyfarm.data.models.User;
import com.dtechsolutions.paddyfarm.data.models.UserUpdateRequest;
import com.dtechsolutions.paddyfarm.data.repositories.UsersRepository;
import com.dtechsolutions.paddyfarm.utils.BaseViewModel;
import com.dtechsolutions.paddyfarm.utils.Resource;

public class EditProfileViewModel extends BaseViewModel {
    private final String TAG = "[EditProfileViewModel]";

    private final UsersRepository usersRepository;

    private final MutableLiveData<UserUpdateRequest> updateTrigger = new MutableLiveData<>();
    private final MutableLiveData<Integer> profileTrigger = new MutableLiveData<>();

    private final LiveData<Resource<User>> updateResult;
    private final LiveData<Resource<User>> profileResult;

    public EditProfileViewModel() {
        usersRepository = new UsersRepository();

        updateResult = Transformations.switchMap(updateTrigger, usersRepository::updateProfile);
        profileResult = Transformations.switchMap(profileTrigger, onChange -> usersRepository.getProfile());
    }

    public void fetchProfile() {
        Integer current = profileTrigger.getValue();
        profileTrigger.postValue(current == null ? 1 : current + 1);
    }

    public void updateUser(UserUpdateRequest request) {
        updateTrigger.postValue(request);
    }

    public LiveData<Resource<User>> getUpdateResult() {
        return updateResult;
    }

    public LiveData<Resource<User>> getProfileResult() {
        return profileResult;
    }

//    private final UsersRepository usersRepository;
//
//    private final MutableLiveData<Resource<User>> result;
//    private final MutableLiveData<Resource<User>> profile;
//
//    public EditProfileViewModel() {
//        usersRepository = new UsersRepository();
//        result = new MutableLiveData<>();
//        profile = new MutableLiveData<>();
//    }
//
//    public void updateUser(UserUpdateRequest request) {
//        usersRepository.updateProfile(request).observeForever(result::postValue);
//    }
//
//    public LiveData<Resource<User>> getResult() {
//        return result;
//    }
//
//    public void fetchProfile() {
//        usersRepository.getProfile().observeForever(profile::postValue);
//    }
//
//    public LiveData<Resource<User>> getProfile() {
//        return profile;
//    }
}
