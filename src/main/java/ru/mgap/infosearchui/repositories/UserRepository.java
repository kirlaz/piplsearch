package ru.mgap.infosearchui.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.mgap.infosearchui.entity.User;

public interface UserRepository extends CrudRepository<User, String> {




}
