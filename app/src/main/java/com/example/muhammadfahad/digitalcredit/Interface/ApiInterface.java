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
	Call<CustomerDetail> getCustomerDetails(@Path("mobileNo") String mobileNo);

	@POST("/api/insert")
	Call<String> insertRecord(@Body CustomerDetail detail);

	/*@GET("/api/fetch/user/{mobileNo}")
	Call<String> getUserId(@Path("mobileNo")String mobileNo);*/

	@GET("/api/fetch/loan/{userId}/{status}")
	Call<List<LoanDetail>> getLoan(@Path("userId")String userId, @Path("status") String status);

	@GET("/api/fetch/tenure")
	Call<List<TenureDetail>> getTenure();

	@GET("/api/fetch/processingFee/{tenure}/{amount}")
	Call<Integer> getProcessingFee(@Path("tenure")int tenure,@Path("amount")int amount);

	@POST("/api/insert/Customerloan")
	Call<Integer> CustomerLoan(@Body LoanDetail loanDetail);

	/*@GET("/api/update/loan/{userId}/{RemainAmt}")
	Call<Integer> updateLoan(@Path("userId")int userId,int RemainAmt);*/
}
