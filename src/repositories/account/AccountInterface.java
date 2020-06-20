package repositories.account;

import model.Account;
import repositories.GenericRepository;
import java.util.Set;

public interface AccountInterface extends GenericRepository<Account, Long> {

    @Override
    default void writeData(Set<model.Account> obj) {
    }

    @Override
    default Set<model.Account> readData() {
        return null;
    }
}
