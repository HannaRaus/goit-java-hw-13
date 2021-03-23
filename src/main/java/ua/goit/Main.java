package ua.goit;

import ua.goit.UserInfo.*;

import java.util.List;

public class Main {
    private static final User DEFAULT_USER = HttpUtil.getUserById(5);
    public static void main(String[] args){
        User customUser = createNewUser();

        System.out.println("[ex.1] - add user");
        //Метод создания работает правильно, если в ответ на JSON с объектом вернулся такой же JSON,
        //но с полем id со значением на 1 больше, чем самый большой id на сайте.
        User addedUser = HttpUtil.createUser(customUser);
        System.out.println("Added user id - " + addedUser.getId() + "\n" + addedUser);
        System.out.println("---------------------------------------------------------------");

        System.out.println("[ex.2] - update userinfo");
        //Метод создания работает правильно, если в ответ на JSON с объектом вернулся такой же JSON,
        //но с полем id со значением на 1 больше, чем самый большой id на сайте.
        System.out.println("User before update - \n" + HttpUtil.getUserById(DEFAULT_USER.getId()));
        User updatedUser = HttpUtil.setNewUserName(DEFAULT_USER.getId(),"Hanna");
        System.out.println("Updated user -> changed username = " + updatedUser.getUsername() + "\n" + updatedUser);
        System.out.println("---------------------------------------------------------------");


        System.out.println("[ex.3] - delete user");
        //удаление объекта. Здесь будем считать корректным результат - статус из группы 2хх в ответ на запрос.
        int statusCode = HttpUtil.deleteUser(DEFAULT_USER.getId());
        System.out.println("StatusCode after delete - " + statusCode);
        System.out.println("---------------------------------------------------------------");

        System.out.println("[ex.4] - get all user");
        //получение информации обо всех пользователях
        List<User> allUsers = HttpUtil.getUsers();
        System.out.println("All users :");
        allUsers.forEach(System.out::println);
        System.out.println("---------------------------------------------------------------");

        System.out.println("[ex.5] - get user by ID");
        //получение информации о пользователе с определенным id
        User userById = HttpUtil.getUserById(DEFAULT_USER.getId());
        System.out.println("Received user -> id = " + DEFAULT_USER.getId() + "\n" + userById);
        System.out.println("---------------------------------------------------------------");

        System.out.println("[ex.6] - get user by name");
        //получение информации о пользователе с опредленным username
        User userByName = HttpUtil.getUserByName(DEFAULT_USER.getUsername());
        System.out.println("Received user -> username = " + DEFAULT_USER.getUsername() + "\n" + userByName);
        System.out.println("---------------------------------------------------------------");

        System.out.println("[ex.7] - get user's last post all comments");
        //выводить все комментарии к последнему посту определенного пользователя и записывать их в файл.
        List<Comment> allCommentToLastPostOfUser = HttpUtil.getAllCommentToLastPostOfUser(userById);
        System.out.println("User's id=" + DEFAULT_USER.getId() + " last post's comments are");
        allCommentToLastPostOfUser.forEach(System.out::println);
        System.out.println("---------------------------------------------------------------");

        System.out.println("[ex.8] - get all uncompleted user's todos");
        //выводить все открытые задачи для пользователя Х.
        //Открытыми считаются все задачи, у которых completed = false.
        List<ToDo> allUncompletedToDos = HttpUtil.getAllUncompletedToDos(userById);
        System.out.println("User's id=" + DEFAULT_USER.getId() + " uncompleted ToDos are:");
        allUncompletedToDos.forEach(System.out::println);
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
