package repositories;

import exeptions.AddAccountException;
import model.Account;
import repositories.interfaces.AccountInterface;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

public class AccountsAccess implements AccountInterface<Account> {
    /**
     * Путь к файлу
     */
    private Path repoPath = Paths.get("src\\resources\\accounts.txt");
    /**
     * Текущий аккаунт
     */
    private Account account;
    /**
     * Имя аккаунта под которым необходимо выполнить вход
     */
    private String accountName;

    @Override
    public boolean findAccount(String accName) throws AddAccountException {
        accountName = accName;
        readFile();
        return account != null;
    }

    /**
     * Метод сохраняет текущий аккаунт в репозитории
     * @param account - объект типа Account, который необходимо сохранить
     * @return - true если сохранение прошло успешно
     * @throws AddAccountException - если не удалось сохранить данные
     */
    @Override
    public boolean saveAccount(Account account) throws AddAccountException {
        return writeFile(account.getAccountName());
    }

    /**
     * Метод считывает данные из файла
     * @throws AddAccountException - если не удалось прочитать файл
     */
    private void readFile() throws AddAccountException {
        try (Stream<String> stream = Files.lines(repoPath)){
                stream.filter(line -> line.equals(accountName))
                .map(Account::new)
                .findFirst()
                .ifPresent(value -> account = value);
        } catch (IOException e) {
            throw new AddAccountException(e.getMessage());
        }
    }

    /**
     * Метод записывает данные в файл
     * @param newAcc - данные которые необходимо записать
     * @return - true если запись в файл прошла успешно
     * @throws AddAccountException - если не удалось записать данные
     */
    private boolean writeFile(String newAcc) throws AddAccountException {
        byte [] bytes = newAcc.getBytes();
        try {
            Files.write(repoPath, bytes,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new AddAccountException(e.getMessage());
        }
        return true;
    }
}
