package net.joaoqalves.services;

import net.joaoqalves.dao.PageDAO;
import net.joaoqalves.dao.RoleDAO;
import net.joaoqalves.dao.UserDAO;
import net.joaoqalves.model.EntityNotValidException;
import net.joaoqalves.model.page.InvalidPageException;
import net.joaoqalves.model.page.Page;
import net.joaoqalves.model.role.Role;
import net.joaoqalves.model.role.InvalidRoleException;
import net.joaoqalves.model.user.*;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueObjectException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class UserService {

    private UserDAO userDAO;
    private RoleDAO roleDAO;
    private PageDAO pageDAO;

    public UserService(final UserDAO userDAO, final RoleDAO roleDAO, final PageDAO pageDAO) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.pageDAO = pageDAO;
    }

    public Optional<User> getOne(final String username) {
        return userDAO.getOne(username);
    }

    public List<User> getAll() {
        return userDAO.getAll();
    }

    public User create(final NewUser newUser)
            throws EntityNotValidException, UserAlreadyExistsException, InvalidRoleException,
            InvalidPageException, HibernateException {

        if(newUser.getUsername() == null || newUser.getPassword() == null) {
            throw new EntityNotValidException("Mandatory fields: username, password");
        }

        pageDAO.findByMatchingRoute(newUser.getHomepage()).orElseThrow(
                () -> new InvalidPageException(newUser.getHomepage()));

        Set<Role> userRoles = validateRoles(newUser.getRoles());

        User user = new User();
        user.setUsername(newUser.getUsername());
        user.setPassword(newUser.getPassword());
        user.setRoles(userRoles);
        user.setHomepage(newUser.getHomepage());
        try {
            userDAO.save(user);
        } catch (NonUniqueObjectException e) {
            throw new UserAlreadyExistsException(user.getUsername());
        }

        return user;
    }

    public User update(final String username, final UpdateUser updateUser)
            throws UserNotExists, InvalidRoleException, InvalidPageException {

        Optional<User> optUser = userDAO.getOne(username);
        if(optUser.isPresent()) {
            User toUpdate = optUser.get();

            if(updateUser.getPassword() != null) {
                toUpdate.setPassword(updateUser.getPassword());
            }

            if(updateUser.getRoles() != null) {
                toUpdate.setRoles(validateRoles(updateUser.getRoles()));
            }

            if(updateUser.getHomepage() != null) {
                toUpdate.setHomepage(validatePage(updateUser.getHomepage()).getPage());
            }

            userDAO.save(toUpdate);
            return toUpdate;
        } else {
            throw new UserNotExists(username);
        }
    }

    public User delete(final String username) throws UserNotExists {
        Optional<User> optUser = userDAO.delete(username);
        if(optUser.isPresent()) {
             return optUser.get();
        } else {
             throw new UserNotExists(username);
        }
    }

    /**
     * This is where Java 8 sucks. In Scala you
     * could throw the Exception this without the second for :(
     * In Java 8 you'd need to wrap the lambda function
     * map(Integer -> Optional[Role]) into another one.
     * It's not complicated but it's tedious. That's
     * why I'm not doing it :)
     *
     * God Bless Scala
     */
    private Set<Role> validateRoles(final Set<Integer> roleIds) throws InvalidRoleException {
        Set<Optional<Role>> dbRoles = roleIds
                .parallelStream()
                .map(roleId -> roleDAO.getOne(roleId))
                .collect(Collectors.toSet());

        Set<Role> userRoles = new HashSet<>();
        for(Optional<Role> r: dbRoles) {
            userRoles.add(r.orElseThrow(() -> new InvalidRoleException("One or more roles are not valid.")));
        }

        return userRoles;
    }

    private Page validatePage(final String page) throws InvalidPageException {
        return pageDAO.findByMatchingRoute(page).orElseThrow(
                () -> new InvalidPageException(page));
    }

}
