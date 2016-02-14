#!/usr/bin/env python
# -*- coding: utf-8 -*-

import requests
from colorama import init, Fore


init(autoreset=True)


def print_status_code(status_code):
    if status_code / 100 == 2:
        print(Fore.GREEN + str(status_code) + ' -> OK')
    elif status_code == 403:
        print(Fore.RED + str(status_code) + ' -> FORBIDDEN')
    elif status_code == 404:
        print(Fore.YELLOW + str(status_code) + ' -> NOT_FOUND')
    elif status_code == 400:
        print(Fore.YELLOW + str(status_code) + ' -> BAD REQUEST')


session = requests.Session()

new_user = {'username': 'user4', 'password': '4user', 'roles': [3, 6], 'homepage': '/page1'}
update_user_4 = {'roles': [3, 6, 8]}

# Normal User

print(Fore.CYAN + "Normal user trying to list users...")
r = session.get('http://user1:1user@localhost:8080/users/')
print_status_code(r.status_code)

print(Fore.CYAN + "Normal user trying to get one user...")
r = session.get('http://user1:1user@localhost:8080/users/user1')
print_status_code(r.status_code)

print(Fore.CYAN + "Normal user trying to get page that does not exist...")
r = session.get('http://user1:1user@localhost:8080/usxxx')
print_status_code(r.status_code)

print(Fore.CYAN + "Normal user trying to create a user...")
r = session.post('http://user1:1user@localhost:8080/users/admin/create', json=new_user)
print_status_code(r.status_code)

print(Fore.CYAN + "Normal user trying to update a user...")
r = session.put('http://user1:1user@localhost:8080/users/admin/user1/update', json=update_user_4)
print_status_code(r.status_code)

print(Fore.CYAN + "Normal user trying to delete a user...")
r = session.delete('http://user1:1user@localhost:8080/users/admin/user1/delete')
print_status_code(r.status_code)



# Admin User
print(Fore.CYAN + "Admin user trying to list users...")
r = session.get('http://admin:nimda@localhost:8080/users/')
print_status_code(r.status_code)

print(Fore.CYAN + "Admin user trying to get one user...")
r = session.get('http://admin:nimda@localhost:8080/users/user1')
print_status_code(r.status_code)

print(Fore.CYAN + "Admin user trying to get page that does not exist...")
r = session.get('http://admin:nimda@localhost:8080/usxxx')
print_status_code(r.status_code)

print(Fore.CYAN + "Admin user trying to create a user...")
r = session.post('http://admin:nimda@localhost:8080/users/admin/create', json=new_user)
print_status_code(r.status_code)


print(Fore.CYAN + "Admin user trying to update a user...")
r = session.put('http://admin:nimda@localhost:8080/users/admin/user4/update', json=update_user_4)
print_status_code(r.status_code)

print(Fore.CYAN + "Admin user trying to delete a user...")
r = session.delete('http://admin:nimda@localhost:8080/users/admin/user4/delete')
print_status_code(r.status_code)
