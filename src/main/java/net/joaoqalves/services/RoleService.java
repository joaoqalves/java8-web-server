package net.joaoqalves.services;

import net.joaoqalves.core.auth.IRoleManager;
import net.joaoqalves.model.role.Role;
import net.joaoqalves.model.user.User;

import java.util.Arrays;
import java.util.Collection;

public class RoleService implements IRoleManager<User> {

    public boolean canAccess(final String path, final User user) {

        /*user.getRoles()
                .parallelStream()
                .map(Role::getPages)
                .flatMap(Collection::stream)
                .forEach(p -> {
                    System.out.println(p.getPage());
                    Arrays.asList(p.getPage().split("/")).stream().forEach(System.out::println);
                    System.out.println(p.getPage().split("/").length);
                    System.out.println(path);
                    System.out.println(path.split("/").length);
                });*/

        return user.getRoles()
                .parallelStream()
                .map(Role::getPages)
                .flatMap(Collection::stream)
                .filter(p -> p.matches(path) && (path.split("/").length == p.getPage().split("/").length) || p.getPage().endsWith("*"))
                .anyMatch(p -> p.matches(path));
    }
}
