package com.andrey.crud.repository.IO;

import com.andrey.crud.exeptions.ReadFileException;
import com.andrey.crud.exeptions.WriteFileException;
import com.andrey.crud.model.Account;
import com.andrey.crud.model.AccountStatus;
import com.andrey.crud.repository.AccountIORepository;
import com.andrey.crud.utils.IOUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AccountRepository implements AccountIORepository<Account> {

    private Path filepath = Paths.get("src\\resources\\accounts.txt");

    private final String separator = "/";

    @Override
    public boolean save(Account obj) throws WriteFileException, ReadFileException {
        obj.setId(IOUtils.generateID(filepath,separator));
        String dataToWrite = obj.getAccountName();

        return IOUtils.writeFile(dataToWrite, filepath, StandardOpenOption.APPEND);
    }

    @Override
    public Optional<Account> find(Long id) throws ReadFileException {
        Map<Long, Account> accounts = findAll();
        Account required = accounts.get(id);
        if (Objects.nonNull(required)) {
            return Optional.of(required);
        }
        return Optional.empty();
    }

    @Override
    public Map<Long, Account> findAll() throws ReadFileException{
        List<String> fromRepository = IOUtils.readFile(filepath);
        Map<Long, Account> accounts = fromRepository.stream()
                                                .map(row -> row.split("/"))
                                                .filter(value -> value.length == 2)
                                                .map(value -> new Account(
                                                        Long.parseLong(value[0]),
                                                        value[1],
                                                        AccountStatus.valueOf(value[2])))
                                                .collect(Collectors.toMap(
                                                                Account::getId,
                                                                Function.identity(),
                                                                (a1,a2) -> a1,
                                                                HashMap::new));
        return accounts;
    }

    @Override
    public boolean saveAll(List<Account> list) throws WriteFileException {
        String dataToWrite = list.stream()
                                .map(this::objectToRepositoryFormat)
                                .collect(Collectors.joining("\n"));
        return IOUtils.writeFile(dataToWrite, filepath, StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Override
    public boolean update(Long id, Account obj) throws WriteFileException, ReadFileException {
        Map<Long, Account> accounts = findAll();
                            accounts.values().stream()
                            .filter(value -> value.getId().equals(id))
                            .peek(value -> value = obj);
        //TODO проверить меняется ли объект в мапе после обработки в стриме
        String dataToWrite = accounts.values()
                                    .stream()
                                    .map(this::objectToRepositoryFormat)
                                    .collect(Collectors.joining("\n"));

        return IOUtils.writeFile(dataToWrite, filepath, StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Override
    public boolean delete(Long id) throws WriteFileException, ReadFileException {
        Map<Long, Account> accounts = findAll();
        Account removed = accounts.get(id);
        if (removed != null) {
            accounts.remove(id);
            return saveAll(new ArrayList<>(accounts.values()));
        }
        return false;
    }

    private String objectToRepositoryFormat(Account account) {
        return new StringBuilder()
                .append(account.getId()).append("/")
                .append(account.getAccountName()).append("/")
                .append(account.getStatus().toString())
                .toString();
    }
}
