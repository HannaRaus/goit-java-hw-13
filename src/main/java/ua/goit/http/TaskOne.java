package ua.goit.http;

import ua.goit.UserInfo.Address;
import ua.goit.UserInfo.Company;
import ua.goit.UserInfo.Geo;
import ua.goit.UserInfo.User;

import java.net.http.HttpResponse;
import java.util.List;

public class TaskOne {
    private static final String HOST = "https://jsonplaceholder.typicode.com/";
    private static final String END_POINT = "users";
    private static final String USER_NAME = "?username=";

    public static void main(String[] args){
        User hanna = createNewUser();
        int id = 5;
        String name = "Samantha";


        System.out.println("[ex.1] - add user");
        //Метод создания работает правильно, если в ответ на JSON с объектом вернулся такой же JSON,
        //но с полем id со значением на 1 больше, чем самый большой id на сайте.
        User addedUser = HttpUtil.createUser(String.format("%s%s", HOST, END_POINT), hanna);
        System.out.println("Added user id - " + addedUser.getId() + "\n" + addedUser);
        System.out.println("---------------------------------------------------------------");

        System.out.println("[ex.2] - update userinfo");
        //Метод создания работает правильно, если в ответ на JSON с объектом вернулся такой же JSON,
        //но с полем id со значением на 1 больше, чем самый большой id на сайте.
        System.out.println("User before update - \n" + HttpUtil.getUserById(String.format("%s%s", HOST, END_POINT),id));
        User updatedUser = HttpUtil.setNewUserName(String.format("%s%s", HOST, END_POINT), id,"Hanna");
        System.out.println("Updated user -> changed username = " + updatedUser.getUsername() + "\n" + updatedUser);
        System.out.println("---------------------------------------------------------------");


        System.out.println("[ex.3] - delete user");
        //удаление объекта. Здесь будем считать корректным результат - статус из группы 2хх в ответ на запрос.
        HttpResponse<String> deleteUser = HttpUtil.deleteUser(String.format("%s%s", HOST, END_POINT), id);
        System.out.println("StatusCode after delete - " + deleteUser.statusCode());
        System.out.println("---------------------------------------------------------------");

        System.out.println("[ex.4] - get all user");
        //получение информации обо всех пользователях
        List<User> allUsers = HttpUtil.getUsers(String.format("%s%s", HOST, END_POINT));
        System.out.println("All users :");
        allUsers.forEach(System.out::println);
        System.out.println("---------------------------------------------------------------");

        System.out.println("[ex.5] - get user by ID");
        //получение информации о пользователе с определенным id
        User userById = HttpUtil.getUserById(String.format("%s%s", HOST, END_POINT), id);
        System.out.println("Received user -> id = " + id + "\n" + userById);
        System.out.println("---------------------------------------------------------------");

        System.out.println("[ex.6] - get user by name");
        //получение информации о пользователе с опредленным username
        User userByName = HttpUtil.getUserByName(String.format("%s%s%s", HOST, END_POINT, USER_NAME),name);
        System.out.println("Received user -> username = " + name + "\n" + userByName);
        System.out.println("---------------------------------------------------------------");




    }
    private static User createNewUser() {
        User user = new User.Builder()
                .id(1)
                .name("Hanna")
                .username("Hanna")
                .email("hanna@gmail.com")
                .address(new Address.Builder()
                        .street("Peremohu")
                        .suite("Apt. 556")
                        .city("Kyiv")
                        .zipcode("03115")
                        .geo(new Geo.Builder()
                                .lat("-37.3159")
                                .lng("81.1496")
                                .build())
                        .build())
                .phone("1-770-736-8031 x56442")
                .website("hildegard.org")
                .company(new Company.Builder()
                        .name("Romaguera-Crona")
                        .catchPhrase("Multi-layered client-server neural-net")
                        .bs("harness real-time e-markets")
                        .build())
                .build();
        return user;
    }
}
