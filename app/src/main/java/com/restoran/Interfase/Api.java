package com.restoran.Interfase;

import com.restoran.Models.Catecories;
import com.restoran.Models.Orders;
import com.restoran.Models.Products;
import com.restoran.Models.ZalStol;
import com.restoran.Models.Login;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    @GET("/login.php")
    Call<Login> getStr(@Query("login") String login, @Query("pass") String pass);

    @GET("/zal.php")
    Call<ZalStol> getZals();

    @GET("/category.php")
    Call<Catecories> getCatecories();

    @GET("/podcategory.php")
    Call<Catecories> getPodCatecories(@Query("id") String id);

    @GET("/products.php")
    Call<Products> getProducts(@Query("id")String id);

   @GET("/fastproducts.php")
    Call<Products> getFastProducts();

    @GET("/get_zakaz.php")
    Call<Orders> getzakaz(@Query("id_stol")String idstol);

    @POST("/add_zakaz.php")
    Call<ResponseBody> addZakaz(@Body RequestBody requestBody);

    @POST("/updateZakaz.php")
    Call<ResponseBody> updateZakaz(@Body RequestBody requestBody);

}
