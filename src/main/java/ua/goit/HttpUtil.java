package ua.goit;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ua.goit.UserInfo.Comment;
import ua.goit.UserInfo.Post;
import ua.goit.UserInfo.ToDo;
import ua.goit.UserInfo.User;


import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HttpUtil {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final Gson GSON = new Gson();
    private static final Type USER_COLLECTION = new TypeToken<List<User>>() {
    }.getType();
    private static final Type POST_COLLECTION = new TypeToken<List<Post>>() {
    }.getType();
    private static final Type COMMENT_COLLECTION = new TypeToken<List<Comment>>() {
    }.getType();
    private static final Type TODOS_COLLECTION = new TypeToken<List<ToDo>>() {
    }.getType();

    private static final String HOST = "https://jsonplaceholder.typicode.com";
    private static final String END_POINT = "/users";
    private static final String USER_NAME = "?username=";
    private static final String POSTS = "/posts";
    private static final String COMMENTS = "/comments";
    private static final String TODOS = "/todos";

    public static User createUser( User user){
        HttpRequest request = requestWithBody("POST", String.format("%s%s", HOST, END_POINT), user);
        HttpResponse<String> response = getResponse(request);
        return GSON.fromJson(response.body(), User.class);
    }

    public static User setNewUserName(int id, String newUserName){
        User user = getUserById(id);
        user.setUsername(newUserName);
        HttpRequest request = requestWithBody("PUT", String.format("%s/%d", String.format("%s%s", HOST, END_POINT), id), user);
        HttpResponse<String> response = getResponse(request);
        return GSON.fromJson(response.body(), User.class);
    }

    public static User getUserById(int id){
        HttpRequest request = sendGet(String.format("%s%s/%d", HOST, END_POINT, id));
        HttpResponse<String> response = getResponse(request);
        return GSON.fromJson(response.body(), User.class);
    }

    public static User getUserByName(String name){
        HttpRequest request = sendGet(String.format("%s%s%s%s", HOST, END_POINT, USER_NAME, name));
        HttpResponse<String> response = getResponse(request);
        List<User> list = GSON.fromJson(response.body(), USER_COLLECTION);
        return list.get(0);
    }

    public static int deleteUser(int id){
        User user = getUserById(id);
        HttpRequest request = requestWithBody("DELETE", String.format("%s%s/%d", HOST,END_POINT, id), user);
        return getResponse(request).statusCode();
    }

    public static List<User> getUsers(){
        HttpRequest request = sendGet(String.format("%s%s", HOST, END_POINT));
        return GSON.fromJson(getResponse(request).body(), USER_COLLECTION);
    }

    public static List<Comment> getAllCommentToLastPostOfUser(User user){
        Post lastPost = getLastPost(user);
        HttpRequest request = sendGet(String.format("%s%s/%d%s", HOST, POSTS, lastPost.getId(), COMMENTS));
        String fileName = "user-" + user.getId() + "-post-" + lastPost.getId() + "-comments.jsom";
        getResponse(request, fileName);

        return GSON.fromJson(getResponse(request).body(), COMMENT_COLLECTION);
    }
    private static Post getLastPost(User user) {
        HttpRequest request = sendGet(String.format("%s%s/%d%s", HOST, END_POINT, user.getId(), POSTS));
        List<Post> posts = GSON.fromJson(getResponse(request).body(), POST_COLLECTION);
        return Collections.max(posts, Comparator.comparingInt(Post::getId));
    }

    public static List<ToDo> getAllUncompletedToDos(User user) {
        HttpRequest request = sendGet(String.format("%s%s/%d%s", HOST, END_POINT, user.getId(), TODOS));
        List<ToDo> allToDos = GSON.fromJson(getResponse(request).body(), TODOS_COLLECTION);
        return allToDos.stream()
                .filter(todo -> !todo.isCompleted())
                .collect(Collectors.toList());
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
    private static void getResponse(HttpRequest request, String fileName) {
        try {
            CLIENT.send(request, HttpResponse.BodyHandlers.ofFile(Path.of(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

