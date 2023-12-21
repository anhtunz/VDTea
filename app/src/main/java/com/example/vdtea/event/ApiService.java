package com.example.vdtea.event;

import com.example.vdtea.model.Bank;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("banks")
    Call<Bank> getBank();
}
