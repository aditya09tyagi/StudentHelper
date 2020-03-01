package com.example.projectsetup.ui.company

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectsetup.data.models.Company
import com.example.projectsetup.data.models.Error
import com.example.projectsetup.data.models.Resource
import com.example.projectsetup.data.network.StudentHelperRepository
import javax.inject.Inject

class CompanyViewModel @Inject constructor(
    private val studentHelperRepository: StudentHelperRepository
) : ViewModel(), StudentHelperRepository.OnGetCompanyByIdListener {

    private val _companyLiveData = MutableLiveData<Resource<Company>>()
    val companyLiveData: LiveData<Resource<Company>>
        get() = _companyLiveData

    fun getCompanyById(companyId: String) {
        _companyLiveData.postValue(Resource.loading())
        studentHelperRepository.getCompanyById(companyId, this)
    }

    override fun onGetCompanyByIdSuccess(company: Company) {
        _companyLiveData.postValue(Resource.success(company))
    }

    override fun onGetCompanyByIdFailure(error: Error) {
        _companyLiveData.postValue(Resource.error(error))
    }
}