package com.ecdc.androidlibs.network;

import com.ecdc.androidlibs.database.CacheProvider;
import com.ecdc.androidlibs.database.entity.CategoryWrapper;
import com.ecdc.androidlibs.database.entity.DepartmentWrapper;
import com.ecdc.androidlibs.database.entity.Order;
import com.ecdc.androidlibs.database.entity.OrderWrapper;
import com.ecdc.androidlibs.database.entity.ProductWrapper;
import com.ecdc.androidlibs.database.entity.Table;
import com.ecdc.androidlibs.database.entity.TableStatistics;
import com.ecdc.androidlibs.database.temporary.OrderCreate;
import com.ecdc.androidlibs.database.temporary.OrderSessionAdd;
import com.ecdc.androidlibs.database.temporary.Session;
import com.ecdc.androidlibs.database.entity.TableWrapper;
import com.ecdc.androidlibs.database.entity.User;
import com.ecdc.androidlibs.network.service.OrderService;
import com.ecdc.androidlibs.network.service.PlaceService;
import com.ecdc.androidlibs.network.service.ProductService;
import com.ecdc.androidlibs.network.service.StatisticsService;
import com.ecdc.androidlibs.network.service.TableService;
import com.ecdc.androidlibs.network.service.UserService;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ConnectionSpec;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


import static java.util.Arrays.asList;

public final class NetworkProvider {

    private static volatile NetworkProvider mInstance = null;

    private Retrofit retrofit;

    private NetworkProvider() {
        retrofit = new Retrofit.Builder()
                .baseUrl(NetworkEndPoint.BASE_URL)
                .client(requireHttpClient())
                .addConverterFactory(requireGson())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static NetworkProvider self() {
        if (mInstance == null)
            mInstance = new NetworkProvider();
        return mInstance;
    }

    public <T> T getService(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }

    private <T> void execute(Call<NetworkResponse<T>> call, final RequestCallBack<T> callBack) {
        try {
            Response<NetworkResponse<T>> response = call.execute();
            if (response.isSuccessful()) {
                NetworkResponse<T> body = response.body();
                if (body != null) {
                    if (body.isSuccessful()) {
                        if (callBack != null)
                            callBack.onSuccessful(body.getData());
                    } else {
                        if (callBack != null)
                            callBack.onFailure(body.errorMessage());
                    }
                }
            } else {
                if (callBack != null)
                    callBack.onFailure(response.message());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private OkHttpClient requireHttpClient() {

        ConnectionSpec tls = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .allEnabledTlsVersions()
                .allEnabledCipherSuites()
                .supportsTlsExtensions(true).build();

        OkHttpClient.Builder builder = requireHttpClientBuilder();
        builder.connectionSpecs(asList(tls, ConnectionSpec.CLEARTEXT));
        builder.addInterceptor(headerInterceptor());
        builder.addInterceptor(queryInterceptor());
        builder.readTimeout(NetworkEndPoint.DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(NetworkEndPoint.DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(NetworkEndPoint.DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        return builder.build();
    }

    private Interceptor headerInterceptor() {
        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("X-Platform", "android")
                        .addHeader("X-Version", NetworkEndPoint.BUILD_VERSION)
                        .build();
                return chain.proceed(request);
            }
        };
    }

    private Interceptor queryInterceptor() {
        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request
                        .url()
                        .newBuilder()
                        .addQueryParameter("ts", String.valueOf(System.currentTimeMillis()))
                        .addQueryParameter("token", CacheProvider.self().getToken())
                        .build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        };
    }

    private GsonConverterFactory requireGson() {
        return GsonConverterFactory.create(new GsonBuilder().create());
    }

    private OkHttpClient.Builder requireHttpClientBuilder() {
        // return UnsafeOkHttpClient.getUnsafeOkHttpClientBuilder();
        return new OkHttpClient.Builder();
    }

    public Observable<NetworkResponse<User>> logIn(String userName, String password)
    {
        return getService(UserService.class).logIn(userName, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NetworkResponse<OrderWrapper>> getServingTable(int page, int size)
    {
        return getService(TableService.class).getServingTable(0, page, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NetworkResponse<DepartmentWrapper>> getDepartment()
    {
        return getService(PlaceService.class).getDepartment()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NetworkResponse<TableWrapper>> getAllTable(int page, int size)
    {
        return getService(TableService.class).getAllTable(page, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NetworkResponse<Table>> changeTableStatus(String id, int status)
    {
        return getService(TableService.class).changeTableStatus(id, status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NetworkResponse<TableWrapper>> getTableByDepartment(String departmentId, int page, int size)
    {
        return getService(TableService.class).getTableByDepartmentId(departmentId, page, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NetworkResponse<TableWrapper>> searchTableByName(String key, int page, int size)
    {
        return getService(TableService.class).searchTableByName(key, page, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NetworkResponse<CategoryWrapper>> getCategory()
    {
        return getService(ProductService.class).getCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NetworkResponse<ProductWrapper>> getAllProducts(int page, int size)
    {
        return getService(ProductService.class).getAllProduct(page, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NetworkResponse<ProductWrapper>> getProductByCategory(String id, int page, int size)
    {
        return getService(ProductService.class).getProductByCategory(id, page, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NetworkResponse<ProductWrapper>> searchProductByName(String name, int page, int size)
    {
        return getService(ProductService.class).searchProductByName(name, page, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NetworkResponse<Object>> createOrder(String tableId, String departmentId, Session session)
    {
        return getService(OrderService.class).createOrder(new OrderCreate(tableId, departmentId, session))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NetworkResponse<Object>> addSession(OrderSessionAdd session)
    {
        return getService(OrderService.class).addSession(session)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NetworkResponse<OrderWrapper>> orderBilling(Order order) {
        return  getService(OrderService.class).orderBilling(order)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NetworkResponse<Object>> changeOrderStatus(String orderId, int status) {
        return getService(OrderService.class).changeOrderStatus(orderId, status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
   /*====START: STATISTICS SESSION =====*/
    public Observable<NetworkResponse<TableStatistics>> getTableStatistics(long startDate, long endDate){
        return getService(StatisticsService.class). getStatisticsTable(startDate, endDate)
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<NetworkResponse<OrderWrapper>> getAllOrderStatistics(int searchStatus, long startDate, long endDate, int page, int pageSize){
        return getService(StatisticsService.class).getAllOrderStatistics(searchStatus,startDate,endDate,page, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NetworkResponse<OrderWrapper>>getAllStatistics(int searchStatus, int pageNum, int pageSize){
        return getService(StatisticsService.class). getAllStatistics(searchStatus,pageNum,pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /*====END: STATISTICS SESSION =====*/
}
