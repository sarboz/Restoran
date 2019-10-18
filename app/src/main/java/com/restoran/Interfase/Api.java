package com.restoran.Interfase;

import com.restoran.Models.Catecories;
import com.restoran.Models.Login;
import com.restoran.Models.Orders;
import com.restoran.Models.Products;
import com.restoran.Models.ZalStol;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {
    @POST("/add_zakaz.php")
    Call<ResponseBody> addZakaz(@Body RequestBody requestBody);

    @GET("/category.php")
    Call<Catecories> getCatecories();

    @GET("/fastproducts.php")
    Call<Products> getFastProducts();

    @GET("/podcategory.php")
    Call<Catecories> getPodCatecories(@Query("id") String str);

    @GET("/products.php")
    Call<Products> getProducts(@Query("id") String str);

    @GET("/login.php")
    Call<Login> getStr(@Query("login") String str, @Query("pass") String str2);

    @GET("/zal.php")
    Call<ZalStol> getZals();

    @GET("/get_zakaz.php")
    Call<Orders> getzakaz(@Query("id_stol") String str);

    @POST("/updateZakaz.php")
    Call<ResponseBody> updateZakaz(@Body RequestBody requestBody);
}
