package repositories.interfaces;


import exeptions.AddAccountException;

public interface AccountInterface<T> {

    boolean findAccount(String accName) throws AddAccountException;

    boolean saveAccount(T account) throws AddAccountException;

}
