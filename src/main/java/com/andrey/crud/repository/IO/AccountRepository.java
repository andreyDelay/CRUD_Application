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

    private final String separator = "=";


    @Override
    public Account save(Account account) throws WriteFileException, ReadFileException {
        //TODO можно читать мапу и записывать если нет объекта, передавая в метод saveAll()
        account.setId(IOUtils.generateID(filepath,separator));
        String dataToWrite = objectToRepositoryFormat(account);

        IOUtils.writeFile(dataToWrite, filepath, StandardOpenOption.APPEND);

        return account;
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
        Iterator<String> rows = fromRepository.iterator();
        List<Account> list = new ArrayList<>();
        while (rows.hasNext()) {
            String current = rows.next();
            if (current.equals("{")){
                String [][] tmp = new String[3][2];
                int cnt = 0;
                current = rows.next().trim();
                while (!current.equals("}")) {
                    tmp[cnt++] = current.split(separator);
                    current = rows.next().trim();
                }
                list.add(new Account(
                            Long.parseLong(tmp[0][1]),
                            tmp[1][1],
                            AccountStatus.valueOf(tmp[2][1]))
                                    );
            }
        }
        Map<Long, Account> accounts = list.stream()
                                        .collect(
                                        Collectors.toMap(
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
                                .collect(Collectors.joining());
        return IOUtils.writeFile(dataToWrite, filepath, StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Override
    public Account update(Long id, Account account) throws WriteFileException, ReadFileException {
        Map<Long, Account> accounts = findAll();
        for (Map.Entry<Long,Account> entry: accounts.entrySet()) {
            if (entry.getKey().equals(id)) {
                entry.setValue(account);
                Account updated = entry.getValue();
                saveAll(new ArrayList<>(accounts.values()));
                return updated;
            }
        }
        return null;
    }

    @Override
    public Account delete(Long id) throws WriteFileException, ReadFileException {
        Map<Long, Account> accounts = findAll();
        Account removed = accounts.get(id);
        if (removed != null) {
            accounts.remove(id);
            saveAll(new ArrayList<>(accounts.values()));
            return removed;
        }
        return null;
    }

    private String objectToRepositoryFormat(Account account) {
        StringBuilder str =  new StringBuilder()
                            .append("{").append("\n")
                            .append("\t")
                            .append("id:=").append(account.getId()).append("\n")
                            .append("\t")
                            .append("name:=").append(account.getAccountName()).append("\n")
                            .append("\t")
                            .append("status:=").append(account.getStatus().toString()).append("\n")
                            .append("}\n");
        return str.toString();
    }
}
