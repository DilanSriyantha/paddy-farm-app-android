package com.dtechsolutions.paddyfarm.ui.editprofile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dtechsolutions.paddyfarm.data.models.User;
import com.dtechsolutions.paddyfarm.data.models.UserUpdateRequest;
import com.dtechsolutions.paddyfarm.data.repositories.UsersRepository;
import com.dtechsolutions.paddyfarm.utils.BaseViewModel;
import com.dtechsolutions.paddyfarm.utils.Resource;

public class EditProfileViewModel extends BaseViewModel {
    private final UsersRepository usersRepository;
    private final MutableLiveData<Resource<User>> result;
    private final MutableLiveData<Resource<User>> profile;

    public EditProfileViewModel() {
        usersRepository = new UsersRepository();
        result = new MutableLiveData<>();
        profile = new MutableLiveData<>();
    }

    public void updateUser(UserUpdateRequest request) {
        usersRepository.updateProfile(request).observeForever(result::postValue);
    }

    public LiveData<Resource<User>> getResult() {
        return result;
    }

    public void fetchProfile() {
        usersRepository.getProfile().observeForever(profile::postValue);
    }

    public LiveData<Resource<User>> getProfile() {
        return profile;
    }
}
