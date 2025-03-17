import java.util.Scanner;

class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}

class UserLimitException extends Exception {
    public UserLimitException(String message) {
        super(message);
    }
}

class InvalidUsernameException extends Exception {
    public InvalidUsernameException(String message) {
        super(message);
    }
}

class InvalidPasswordException extends Exception {
    public InvalidPasswordException(String message) {
        super(message);
    }
}

class UserNotFoundException extends Exception {
    public UserNotFoundException(String message) {
        super(message);
    }
}

class UserStorage {
    private User[] users = new User[15];
    private int count = 0;

    public void addUser(String username, String password) throws UserLimitException, InvalidUsernameException, InvalidPasswordException {
        if (count >= 15) {
            throw new UserLimitException("Забагато Котиків! Нажаль розробник не прописав те що можна додати більше 15 Котиків!");
        }
        if (username.length() < 5 || username.indexOf(' ') != -1) {
            throw new InvalidUsernameException("Ім'я котика має містити не менше 5 символів і не мати пробілів, дотримуйтеся інструкцій!");
        }
        if (!isValidPassword(password)) {
            throw new InvalidPasswordException("Опаньки а тут помилочка! Пароль не відповідає вимогам спробуйте інший!");
        }
        users[count++] = new User(username, password);
    }

    public void removeUser(String username) throws UserNotFoundException {
        for (int i = 0; i < count; i++) {
            if (users[i].getUsername().equals(username)) {
                users[i] = users[count - 1];
                users[count - 1] = null;
                count--;
                return;
            }
        }
        throw new UserNotFoundException("Котика не знайдено.");
    }

    public boolean authenticate(String username, String password) {
        for (int i = 0; i < count; i++) {
            if (users[i].getUsername().equals(username) && users[i].checkPassword(password)) {
                return true;
            }
        }
        return false;
    }

    public void listUsers() {
        if (count == 0) {
            System.out.println("Немає зареєстрованих Котиків(.");
            return;
        }
        System.out.println("Список Котиків:");
        for (int i = 0; i < count; i++) {
            System.out.println((i + 1) + ". " + users[i].getUsername());
        }
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 10 || password.indexOf(' ') != -1) return false;
        int digits = 0;
        boolean special = false;
        boolean latin = true;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (c >= '0' && c <= '9') digits++;
            else if (!(c >= 'a' && c <= 'z') && !(c >= 'A' && c <= 'Z')) {
                special = true;
            }
            if (c < 32 || c > 126) latin = false;
        }
        return digits >= 3 && special && latin;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserStorage storage = new UserStorage();
        boolean running = true;

        while (running) {
            System.out.println("\nМеню про Котиків!");
            System.out.println("1 - Додати Котика");
            System.out.println("2 - Видалити Котика(ну може не треба?)");
            System.out.println("3 - Аутентифікація");
            System.out.println("4 - Переглянути всіх Котиків");
            System.out.println("5 - Вихід");
            System.out.print("Оберіть дію: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Помилочка :Введіть число від 1 до 5.І не тикайте свої букви!Помилок і так забагато!!");
                scanner.next();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Введіть ім'я Котика: ");
                        String username = scanner.nextLine().trim();
                        System.out.print("Введіть пароль: ");
                        String password = scanner.nextLine().trim();
                        storage.addUser(username, password);
                        System.out.println("Котика додано!");
                        break;
                    case 2:
                        storage.listUsers();
                        System.out.print("Введіть ім'я Котика для видалення(ну може все ж таки подумати ?): ");
                        username = scanner.nextLine().trim();
                        storage.removeUser(username);
                        System.out.println("Котика видалено(ну і нашо, гарненький був..).");
                        break;
                    case 3:
                        System.out.print("Введіть ім'я Котика: ");
                        username = scanner.nextLine().trim();
                        System.out.print("Введіть пароль: ");
                        password = scanner.nextLine().trim();
                        if (storage.authenticate(username, password)) {
                            System.out.println("Аутентифікація успішна.");
                        } else {
                            System.out.println("Помилочка аутентифікації.");
                        }
                        break;
                    case 4:
                        storage.listUsers();
                        break;
                    case 5:
                        System.out.println("Вихід...");
                        running = false;
                        break;
                    default:
                        System.out.println("Невірний вибір.");
                }
            } catch (Exception e) {
                System.out.println("Помилочка: " + e.getMessage());
            }
        }

        scanner.close();
    }
}