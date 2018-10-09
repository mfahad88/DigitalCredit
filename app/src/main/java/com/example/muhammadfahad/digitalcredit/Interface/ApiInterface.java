package com.example.muhammadfahad.digitalcredit.Interface;

import com.example.muhammadfahad.digitalcredit.Model.CustomerDetail;
import com.example.muhammadfahad.digitalcredit.Model.LoanDetail;
import com.example.muhammadfahad.digitalcredit.Model.TenureDetail;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
	
	@GET("/api/fetch/{mobileNo}")
	Call<CustomerDetail>getScore(@Path("mobileNo") String mobileNo);

	@POST("/api/insert")
	Call<String> getUserId(@Body CustomerDetail detail);

	@GET("/api/fetch/loan/{userId}/{status}")
	Call<List<LoanDetail>> getLoan(@Path("userId")String userId, @Path("status") String status);

	@GET("/api/fetch/tenure")
	Call<List<TenureDetail>> getTenure();
}
