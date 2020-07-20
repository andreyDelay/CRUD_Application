package com.andrey.crud.repository.IO;

import com.andrey.crud.exeptions.ReadFileException;
import com.andrey.crud.exeptions.WriteFileException;
import com.andrey.crud.model.Account;
import com.andrey.crud.model.AccountStatus;
import com.andrey.crud.model.Skill;
import com.andrey.crud.repository.AccountIORepository;
import com.andrey.crud.utils.IOUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

public class AccountRepository implements AccountIORepository<Account> {

    private Path filepath = Paths.get("src\\resources\\accounts.txt");

    private final String separator = "=";


    @Override
    public Account save(Account account) throws WriteFileException, ReadFileException {
        account.setId(IOUtils.generateID(filepath,separator));
        String dataToWrite = objectToRepositoryFormat(account);

        IOUtils.writeFile(dataToWrite, filepath, StandardOpenOption.APPEND);

        return account;
    }

    @Override
    public Optional<Account> find(Long id) throws ReadFileException {
        Iterator<String> iterator = IOUtils.readFile(filepath).iterator();
        String currentLine;
        while (iterator.hasNext()) {
            currentLine = iterator.next().trim();
            if (currentLine.contains("id:") && checkId(currentLine, String.valueOf(id))){
                return Optional.of(builder(iterator, currentLine));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Account> findAll() throws ReadFileException{
        Iterator<String> iterator = IOUtils.readFile(filepath).iterator();
        List<Account> accounts = new ArrayList<>();
        String currentLine;
        while (iterator.hasNext()) {
            currentLine = iterator.next().trim();
            if (currentLine.equals("{")){
                currentLine = iterator.next().trim();
                accounts.add(builder(iterator, currentLine));
            }
        }
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
    public Account update(Long id, Account newAccount) throws WriteFileException, ReadFileException {
        List<String> fromRepository = IOUtils.readFile(filepath);
        Iterator<String> iterator = fromRepository.iterator();
        Account oldAccount = null;
        int index = 0;
        String currentLine;
        while (iterator.hasNext()) {
            index++;
            currentLine = iterator.next().trim();
            if (currentLine.contains("id:") && checkId(currentLine, String.valueOf(id))) {
                oldAccount = builder(iterator, currentLine);
                updater(fromRepository, newAccount, index);
                String dataToWrite = String.join("\n",fromRepository);
                IOUtils.writeFile(dataToWrite,filepath,StandardOpenOption.TRUNCATE_EXISTING);
                break;
            }
        }
        return oldAccount;
    }

    @Override
    public void delete(Long id) throws WriteFileException, ReadFileException {
        List<String> fromRepository = IOUtils.readFile(filepath);
        Iterator<String> iterator = fromRepository.iterator();
        int index = 0;
        String currentLine;
        while (iterator.hasNext()) {
            index++;
            currentLine = iterator.next().trim();
            if (currentLine.contains("id:") && checkId(currentLine, String.valueOf(id))) {
                for (int i = 0; i < 5; i++)
                    fromRepository.remove(index -1);


                String dataToWrite = String.join("\n",fromRepository);
                IOUtils.writeFile(dataToWrite,filepath,StandardOpenOption.TRUNCATE_EXISTING);
            }
        }
    }

    private void updater(List<String> rows, Account newValue, int index) {
        StringBuilder newData = new StringBuilder();
        newData
                .append("\t").append("name:=").append(newValue.getAccountName()).append("\n")
                .append("\t").append("status:=").append(newValue.getStatus().toString()).append("\n")
                .append("}")
                .append("\n");
        for (int i = 0; i < 2; i++)
            rows.remove(index);

        rows.set(index, newData.toString());
    }

    private boolean checkId(String currentLine, String requiredId) {
        String [] splitter = currentLine.split("=");
        return  requiredId.equals(splitter[1].trim());
    }

    private Account builder(Iterator<String> iterator, String currentLine) {
        String [][] dataForAccount = new String[3][2];
        int counter = 0;
        while (!currentLine.equals("}")) {
            dataForAccount[counter++] = currentLine.split(separator);
            currentLine = iterator.next().trim();
        }
        return new Account(Long.parseLong(dataForAccount[0][1]),
                    dataForAccount[1][1],
                    AccountStatus.valueOf(dataForAccount[2][1])
                    );
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
