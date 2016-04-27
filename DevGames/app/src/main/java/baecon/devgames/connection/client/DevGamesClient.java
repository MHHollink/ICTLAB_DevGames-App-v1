package baecon.devgames.connection.client;

import java.util.List;
import java.util.Map;

import baecon.devgames.connection.client.dto.CommitDTO;
import baecon.devgames.connection.client.dto.ProjectDTO;
import baecon.devgames.connection.client.dto.UserDTO;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface DevGamesClient {

    @FormUrlEncoded
    @POST("/login")
    Map<String, String> login(@Field("username") String user, @Field("password") String pass);

    @GET("/users")
    UserDTO getCurrentlyLoggedInUser();

    @PUT("/users/{id}")
    Response updateUser(@Body UserDTO user, @Path("id") Long id);

    @GET("/users/{id}")
    UserDTO getUserById(@Path("id") Long id);

    @GET("/users/{id}/projects")
    List<ProjectDTO> getProjectsOfUser(@Path("id") Long id);

    @GET("/users/{id}/commits")
    List<CommitDTO> getCommitsOfUser(@Path("id") Long id);

    List<UserDTO> getUsers(Long projectId);
}
