package baecon.devgames.connection.client;

import java.util.List;
import java.util.Map;

import baecon.devgames.connection.client.dto.CommitDTO;
import baecon.devgames.connection.client.dto.DuplicationDTO;
import baecon.devgames.connection.client.dto.IssueDTO;
import baecon.devgames.connection.client.dto.ProjectDTO;
import baecon.devgames.connection.client.dto.PushDTO;
import baecon.devgames.connection.client.dto.UserDTO;
import baecon.devgames.model.Commit;
import baecon.devgames.model.Duplication;
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

    @GET("/users/{id}/pushes")
    List<PushDTO> getPushesOfUser(@Path("id") Long id);

    @GET("/users/{id}/commits")
    List<CommitDTO> getCommitsOfUser(@Path("id") Long id);

    @GET("/users/{id}/issues")
    List<IssueDTO> getIssuesOfUser(@Path("id") Long id);

    @GET("/users/{id}/duplications")
    List<DuplicationDTO> getDuplicationsOfUser(@Path("id") Long id);

    @GET("/projects/{id}")
    ProjectDTO getProjectById(@Path("id") Long id);

    @GET("/projects/{id}/users")
    List<UserDTO> getUsersFromProject(@Path("id") Long id);

    @GET("/projects/{id}/issues")
    List<IssueDTO> getIssuesFromProject(@Path("id") Long id);

    @GET("/projects/{id}/duplications")
    List<DuplicationDTO> getDuplicationsFromProject(@Path("id") Long id);

    @GET("/projects/{id}/pushes")
    List<PushDTO> getPushesFromProject(@Path("id") Long id);

    @GET("/projects/{id}/commits")
    List<CommitDTO> getCommitsFromProject(@Path("id") Long id);

    @GET("/pushes/{id}")
    PushDTO getPushById(@Path("id") Long id);

    @GET("/pushes/{id}/commits")
    List<CommitDTO> getCommitsFromPush(@Path("id") Long id);

    @GET("/pushes/{id}/issues")
    List<IssueDTO> getIssuesFromPush(@Path("id") Long id);

    @GET("/pushes/{id}/duplications")
    List<DuplicationDTO> getDuplicationsFromPush(@Path("id") Long id);

    @GET("/pushes/{id}/users")
    UserDTO getUserWhoPushed(@Path("id") Long id);

    @GET("/pushes/{id}/projects")
    ProjectDTO getProjectFromPush(@Path("id") Long id);

    @GET("/commits/{id}")
    CommitDTO getCommitById(@Path("id") Long id);

    @GET("/commits/{id}/pushes")
    PushDTO getPushFromCommit(@Path("id") Long id);

    @GET("/commits/{id}/projects")
    ProjectDTO getProjectFromCommit(@Path("id") Long id);

    @GET("/commits/{id}/users")
    UserDTO getUserFromCommit(@Path("id") Long id);

    @GET("/duplications/{id}")
    DuplicationDTO getDuplicationById(@Path("id") Long id);

    @GET("/duplications/{id}/pushes")
    PushDTO getPushFromDuplication(@Path("id") Long id);

    @GET("/duplications/{id}/projects")
    ProjectDTO getProjectFromDuplication(@Path("id") Long id);

    @GET("/duplications/{id}/users")
    UserDTO getUserFromDuplication(@Path("id") Long id);

    @GET("/issues/{id}")
    IssueDTO getIssueById(@Path("id") Long id);

    @GET("/issues/{id}/pushes")
    PushDTO getPushFromIssue(@Path("id") Long id);

    @GET("/issues/{id}/projects")
    ProjectDTO getProjectFromIssue(@Path("id") Long id);

    @GET("/issues/{id}/users")
    UserDTO getUserFromIssue(@Path("id") Long id);

}
