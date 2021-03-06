package com.example.administrator.digitalcredit.Interface;

import com.example.administrator.digitalcredit.Model.AppVersion;
import com.example.administrator.digitalcredit.Model.CreateProduct;
import com.example.administrator.digitalcredit.Model.CustomerDetail;
import com.example.administrator.digitalcredit.Model.DistributorResponse;
import com.example.administrator.digitalcredit.Model.LoanDetail;
import com.example.administrator.digitalcredit.Model.MobileLocation;
import com.example.administrator.digitalcredit.Model.OrderDetailResponse;
import com.example.administrator.digitalcredit.Model.OrderRequest;
import com.example.administrator.digitalcredit.Model.Product;
import com.example.administrator.digitalcredit.Model.TenureDetail;
import com.example.administrator.digitalcredit.Model.TransactionRequest;
import com.example.administrator.digitalcredit.Model.OrderHistoryResponse;


import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {

	@GET("/api/fetch/user/{mobileNo}")
	Call<Integer> loginUser(@Path("mobileNo") String mobileNo);
	
	@GET("/api/fetch/{mobileNo}")
	Call<CustomerDetail> getCustomerDetails(@Path("mobileNo") String mobileNo);

	@POST("/api/insert")
	Call<String> insertRecord(@Body CustomerDetail detail);

	/*@GET("/api/fetch/user/{mobileNo}")
	Call<String> getUserId(@Path("mobileNo")String mobileNo);*/

	@GET("/api/fetch/loan/{userId}/{status}")
	Call<List<LoanDetail>> getLoan(@Path("userId") String userId, @Path("status") String status);

	@GET("/api/fetch/loan/{userId}")
	Call<List<LoanDetail>> getLoanAll(@Path("userId") String userId);

	@GET("/api/fetch/tenure")
	Call<List<TenureDetail>> getTenure();

	@GET("/api/fetch/processingFee/{tenure}/{amount}")
	Call<Integer> getProcessingFee(@Path("tenure") int tenure, @Path("amount") float amount);

	@POST("/api/insert/Customerloan")
	Call<Integer> CustomerLoan(@Body LoanDetail loanDetail);


	@GET("/api/fetch/category/{cat}")
	Call<Integer> getCategory(@Path("cat") String cat);

	@GET("/api/fetch/Singleloan/{userId}/{loanId}")
	Call<LoanDetail> SingleLoan(@Path("userId") String userId, @Path("loanId") String loanId);

	@PUT("/api/update/Customerloan")
	Call<Integer> updateLoanStatus(@Body LoanDetail loanDetail);

	@Multipart
	@POST("/api/app")
	Call<String> upload(@Part MultipartBody.Part file);

	@GET("/api/fetch/{mobileNo}/{status}")
	Call<Void> setStatus(@Path("mobileNo") String mobileNo, @Path("status") String status);

	@POST("/api/insert/mobileLocation")
	Call<Long> setLocation(@Body MobileLocation location);

	@GET("/api/fetch/appVersion")
	Call<AppVersion> appVersion();

	@GET("/api/fetch/product")
	Call<List<Product>> getProduct();

	@POST("/api/insert/OrderDetails/")
	Call<OrderDetailResponse> order(@Body OrderRequest orderRequest);

	@GET("/api/fetch/distributerList")
	Call<List<DistributorResponse>> distributor();

	@GET("/api/updateOrder/{OrderId}/{Status}")
	Call<Integer> updateOrder(@Path("OrderId") int OrderId, @Path("Status") char Status);

	@GET("api/fetch/order/{userId}")
	Call<List<OrderHistoryResponse>> orderHistory(@Path("userId")String userId);

	@GET("api/fetch/OrderInquiry/{OrderId}")
	Call<OrderDetailResponse> orderInquiry(@Path("OrderId")String OrderId);

	@GET("api/fetch/OrderInquiryCash/{OrderId}")
	Call<OrderDetailResponse> orderInquiryCash(@Path("OrderId")String OrderId);

	@GET("api/fetch/OrderInquiryCart/{UserId}")
	Call<OrderDetailResponse> orderInquiryCart(@Path("UserId")String UserId);
	@POST("api/insert/transaction")
	Call<Integer> trasaction(@Body TransactionRequest transactionRequest);

	@POST("api/insert/transactionCash")
	Call<Integer> trasactionCash(@Body TransactionRequest transactionRequest);

	@POST("api/insert/Product")
	Call<Integer> createProduct(@Body CreateProduct createProduct);
}
