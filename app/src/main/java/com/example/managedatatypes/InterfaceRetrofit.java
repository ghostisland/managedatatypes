package com.example.managedatatypes;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

// IN THIS INTERFACE WE ARE GOING TO ENCODE THE DETAILS OF THE PARAMETERS AND REQUEST METHODS
public interface InterfaceRetrofit {

    // THIS POST REQUEST GOES TO THE "/selectuser" ENDPOINT
    @POST("/selectuser")
    // WE THEN HAVE A METHOD THAT WILL RETURN AN OBJECT OF CLASS "Call" WHICH WILL STORE THE "MongoVariables" OBJECT
    // WE WILL CALL THIS "selectUserMongo" WITH A BODY THROUGH WHICH WE WILL SEND THE EMAIL AND PASSWORD TO THE SERVER
    // WE WILL STORE THE VALUES IN A "HashMap"
    Call<MongoVariables> selectUserMongo(@Body HashMap<String, String> map);

    @POST("/insert")
        // THIS TIME THIS CALL OBJECT IS NOT GOING TO HOLD THE SELECT USER RESULT OBJECT
        // INSTEAD IT WILL HOLD A "Void" OBJECT BECAUSE WHEN THE USER SIGNS UP THE SERVER DOESN'T SEND ANY NAME OR EMAIL
        // THIS WILL ALSO HAVE A BODY THROUGH WHICH WE WILL SEND THE NAME AND EMAIL AND PASSWORD
    Call<Void> insertMongo(@Body HashMap<String, String> map);

    // *****************************************ADDITIONAL CODE***************************************************

    // WE INTRODUCE A "Call" OBJECT - "Call<T> (retrofit)"
    // FOR THE TYPE WE PASS "List" AND POST BECAUSE THIS IS WHAT WE WANT TO GET BACK FROM THIS CALL
    // THIS WILL BE A LIST OF POSTS WHICH IS A JSON ARRAY OF POST JSON OBJECTS THAT WE SAW EARLIER
    // WE GIVE THIS METHOD A NAME "getPosts()"
    // IN A JAVA INTERFACE WE DON'T PROVIDE THE METHOD BODY "()" JUST DECLARE THE METHODS
    // WHOEVER IMPLEMENTS THESE METHODS HAS TO PROVIDE A BODY FOR THESE METHODS
    // RETROFIT WILL AUTOMATICALLY GENERATE ALL THE CODE WE NEED TO GET OUR LIST OF POST BACK
    // TO TELL RETROFIT WHAT TO DO WE HAVE TO ANNOTATE THIS METHOD WHICH IS A GET REQUEST
    // GET IS TO GET DATA FROM THE SERVER AND WE PASS "posts" AS A STRING
    // THIS IS BECAUSE THE STING IS CONTAINED IN E.G.: "https://jsonplaceholder.typicode.com/posts"
    // "https://jsonplaceholder.typicode.com/" IS THE BASE URL AND "posts" IS THE RELATIVE URL
    // THE "Call" OBJECT ENCAPSULATES A SINGLE REQUEST AND RESPONSE
    // LATER WE WILL USE THIS "Call" OBJECT TO EXECUTE THE ACTUAL GET REQUEST

    @GET("/selectall")
    Call<List<MongoVariables>> selectAllMongo();

    @GET("/checkexists")
    Call<List<MongoVariables>> checkExistsMongo();

    @GET("/deleteall")
    Call<Void> deleteAllMongo();

    @POST("/deleteuser")
    Call<Void> deleteUserMongo(@Body HashMap<String, String> map);

    @POST("/update")
    Call<Void> updateMongo(@Body HashMap<String, String> map);

    // THIS IS THE PHP CODE

    @GET("/phpmanagedatatypes/selectall.php")
    Call<List<MySQLPHPVariables>> selectAllMySQLPHP();

    @FormUrlEncoded
    @POST("/phpmanagedatatypes/selectuser.php")
    Call<MongoVariables> selectUserMySQLPHP(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("/phpmanagedatatypes/insert.php")
    Call<MongoVariables> insertMySQLPHP(
            @Field("date") String date,
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("/phpmanagedatatypes/update.php")
    Call<Void> updateMySQLPHP(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("/phpmanagedatatypes/deleteuser.php")
    Call<Void> deleteUserMySQLPHP(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("/phpmanagedatatypes/deleteall.php")
    Call<Void> deleteAllMySQLPHP();
}


