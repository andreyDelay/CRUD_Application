//package repositories.account;
//import model.Account;
//
//import java.io.*;
//import java.nio.file.*;
//import java.nio.file.attribute.BasicFileAttributes;
//import java.util.HashSet;
//import java.util.Set;
//
//public class AccountsAccess implements AccountInterface {
//    private Path filepath = Paths.get("src\\resources\\accounts.txt");
//
//    @Override
//    public Set<Account> readData() {
//        checkTheFile(filepath);
//            try (
//                    ObjectInputStream is =
//                         new ObjectInputStream(
//                                 new FileInputStream("src\\resources\\accounts.txt")))
//            {
//               Set<Account> allAccounts = (Set<Account>) is.readObject();
//               return allAccounts;
//
//            } catch (IOException e) {
//                System.out.println
//                        ("Ошибка при чтении файла в классе " + this.getClass().getName() + " : " + e);
//            } catch (ClassNotFoundException e) {
//                System.out.println
//                        ("Не удалось инициализировать класс при чтении из файла в классе "
//                                + this.getClass().getName() + " : " + e);
//            }
//        return null;
//    }
//
//
//    @Override
//    public void writeData(Set<Account> objectsCollection) {
//        try (
//                ObjectOutputStream os =
//                        new ObjectOutputStream(
//                                new FileOutputStream("src\\resources\\accounts.txt")))
//        {
//
//            os.writeObject(objectsCollection);
//
//        } catch (IOException e) {
//            System.out.println
//                    ("Ошибка при записи в файл в классе " + this.getClass().getName() + " : " + e);
//        }
//    }
//
//    private void checkTheFile(Path filepath) {
//        if (!Files.exists(filepath)) {
//            createFile(filepath);
//        } else {
//            try {
//                BasicFileAttributes attributes = Files.readAttributes(filepath, BasicFileAttributes.class);
//                if (attributes.size() < 50.0) {
//                    Files.delete(filepath);
//                    createFile(filepath);
//                } else {
//                    return;
//                }
//            } catch (IOException e) {
//                System.out.println("Ошибка чтения атрибутов в классе " + this.getClass().getName()
//                + ", в методе checkTheFile(): " + e);
//            }
//        }
//    }
//
//    private void createFile(Path filepath) {
//        try (
//                ObjectOutputStream outputStream =
//                        new ObjectOutputStream(
//                                Files.newOutputStream(filepath,
//                                        StandardOpenOption.CREATE_NEW,
//                                        StandardOpenOption.WRITE))
//        )
//        {
//
//            Set<Account> emptySet = new HashSet<>();
//            outputStream.writeObject(emptySet);
//
//        } catch (InvalidPathException e) {
//            System.out.println("Ошибка в указании пути: " + e);
//            ;
//        } catch (IOException e) {
//            System.out.println("Ошибка ввода-вывода: " + e);
//        }
//    }
//}
