package ua.goit.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ua.goit.UserInfo.User;


import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class HttpUtil {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final Gson GSON = new Gson();
    private static final Type COLLECTION_TYPE = new TypeToken<List<User>>() {
    }.getType();

    public static User createUser(String url, User user){
        HttpRequest request = requestWithBody("POST", url, user);
        HttpResponse<String> response = getResponse(request);
        return GSON.fromJson(response.body(), User.class);
    }

    public static User setNewUserName(String url, int id, String newUserName){
        User user = getUserById(url, id);
        user.setUsername(newUserName);
        HttpRequest request = requestWithBody("PUT", String.format("%s/%d", url, id), user);
        HttpResponse<String> response = getResponse(request);
        return GSON.fromJson(response.body(), User.class);
    }

    public static User getUserById(String url, int id){
        HttpRequest request = sendGet(String.format("%s/%d", url, id));
        HttpResponse<String> response = getResponse(request);
        return GSON.fromJson(response.body(), User.class);
    }

    public static User getUserByName(String url, String name){
        HttpRequest request = sendGet(String.format("%s%s", url, name));
        HttpResponse<String> response = getResponse(request);
        List<User> list = GSON.fromJson(response.body(), COLLECTION_TYPE);
        return list.get(0);
    }

    public static HttpResponse<String> deleteUser(String url, int id){
        User user = getUserById(url, id);
        HttpRequest request = requestWithBody("DELETE", String.format("%s/%d", url, id), user);
        return getResponse(request);
    }

    public static List<User> getUsers(String url){
        HttpRequest request = sendGet(url);
        return GSON.fromJson(getResponse(request).body(), COLLECTION_TYPE);
    }

    private static HttpRequest sendGet(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
    }
    private static HttpRequest requestWithBody(String methodName, String url, User user) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-type", "application/json")
                .method(methodName, HttpRequest.BodyPublishers.ofString(GSON.toJson(user)))
                .build();
    }
    private static HttpResponse<String> getResponse(HttpRequest request) {
        HttpResponse<String> response = null;
        try {
            response = CLIENT.send(request,HttpResponse.BodyHandlers.ofString());
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
}

