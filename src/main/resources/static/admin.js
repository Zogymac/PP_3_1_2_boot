document.addEventListener('DOMContentLoaded', function() {
    fetchUsers();
    fetchRoles();

    document.getElementById('addUserForm').addEventListener('submit', function(event) {
        event.preventDefault();
        addUser();
    });

    document.getElementById('editUserForm').addEventListener('submit', function(event) {
        event.preventDefault();
        editUser();
    });

    document.getElementById('v-pills-profile-tab').addEventListener('click', function() {
        fetchUsersForUserTab();
    });

    document.getElementById('deleteUserForm').addEventListener('submit', function(event) {
        event.preventDefault();
        deleteUser();
    });
});

function fetchUsers() {
    fetch('http://localhost:8080/api/users')
        .then(response => response.json())
        .then(users => {
            const usersTableBody = document.getElementById('usersTableBody');
            usersTableBody.innerHTML = '';
            users.forEach(user => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${user.id}</td>
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                    <td>${user.role.map(role => role.name.replace('ROLE_', '')).join(', ')}</td>
                    <td><button class="btn btn-success btn-sm" onclick="showEditUserModal(${user.id})">Edit</button></td>
                    <td><button class="btn btn-danger btn-sm" onclick="deleteUserModal(${user.id})">Delete</button></td>
                `;
                usersTableBody.appendChild(row);
            });
        })
        .catch(error => console.log(error));
}

function fetchRoles() {
    fetch('http://localhost:8080/api/roles')
        .then(response => response.json())
        .then(roles => {
            const rolesSelect = document.getElementById('roles');
            rolesSelect.innerHTML = '';
            roles.forEach(role => {
                const option = document.createElement('option');
                option.value = role.id;
                option.text = role.name.replace('ROLE_', '');
                rolesSelect.appendChild(option);
            });

            const editRolesSelect = document.getElementById('editRoles');
            editRolesSelect.innerHTML = '';
            roles.forEach(role => {
                const option = document.createElement('option');
                option.value = role.id;
                option.text = role.name.replace('ROLE_', '');
                editRolesSelect.appendChild(option);
            });
        })
        .catch(error => console.log(error));
}

function fetchUsersForUserTab() {
    fetch('http://localhost:8080/api/users')
        .then(response => response.json())
        .then(users => {
            const userTabTableBody = document.querySelector('#userPage tbody');
            userTabTableBody.innerHTML = '';
            users.forEach(user => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${user.id}</td>
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                    <td>${user.role.map(role => role.name.replace('ROLE_', '')).join(', ')}</td>
                `;
                userTabTableBody.appendChild(row);
            });
        })
        .catch(error => console.log(error));
}

function addUser() {
    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const rolesSelect = document.getElementById('roles');
    const selectedRoles = Array.from(rolesSelect.selectedOptions).map(option => ({
        id: option.value,
        name: `ROLE_${option.text}`
    }));

    const user = {
        name: name,
        email: email,
        password: password,
        role: selectedRoles
    };

    fetch('http://localhost:8080/api/admin/edit', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    })
        .then(response => response.json())
        .then(data => {
            console.log(data);
            fetchUsers();
            document.getElementById('name').value = '';
            document.getElementById('email').value = '';
            document.getElementById('password').value = '';
            document.getElementById('roles').selectedIndex = -1;
            const userTableTab = new bootstrap.Tab(document.getElementById('nav-home-tab'));
            userTableTab.show();
        })
        .catch(error => console.log(error));
}

function showEditUserModal(userId) {
    fetch(`http://localhost:8080/api/admin/${userId}`)
        .then(response => response.json())
        .then(user => {
            document.getElementById('editUserId').value = user.id;
            document.getElementById('editName').value = user.name;
            document.getElementById('editEmail').value = user.email;
            const editRolesSelect = document.getElementById('editRoles');
            Array.from(editRolesSelect.options).forEach(option => {
                option.selected = user.role.some(role => role.id == option.value);
            });
            const editUserModal = new bootstrap.Modal(document.getElementById('editUserModal'));
            editUserModal.show();
        })
        .catch(error => console.log(error));
}

function deleteUserModal(userId) {
    fetch(`http://localhost:8080/api/admin/${userId}`)
        .then(response => response.json())
        .then(user => {
            document.getElementById('deleteUserId').value = user.id;
            document.getElementById('deleteName').value = user.name;
            document.getElementById('deleteEmail').value = user.email;
            const editRolesSelect = document.getElementById('deleteRoles');
            Array.from(editRolesSelect.options).forEach(option => {
                option.selected = user.role.some(role => role.id == option.value);
            });
            const deleteUserModal = new bootstrap.Modal(document.getElementById('deleteUserModal'));
            deleteUserModal.show();
        })
        .catch(error => console.log(error));
}

function editUser() {
    const id = document.getElementById('editUserId').value;
    const name = document.getElementById('editName').value;
    const email = document.getElementById('editEmail').value;
    const rolesSelect = document.getElementById('editRoles');
    const selectedRoles = Array.from(rolesSelect.selectedOptions).map(option => ({
        id: option.value,
        name: `ROLE_${option.text}`
    }));

    const user = {
        id: id,
        name: name,
        email: email,
        role: selectedRoles
    };

    fetch('http://localhost:8080/api/admin/edit', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    })
        .then(response => response.json)
        .then(data => {
            console.log(user)
            console.log(data)
            fetchUsers()

            const editUserModalElement = document.getElementById('editUserModal');
            const editUserModal = bootstrap.Modal.getInstance(editUserModalElement);
            editUserModal.hide();
        })
        .catch(error => console.log(error))
}

function deleteUser() {
    const id = document.getElementById('deleteUserId').value;
    fetch(`http://localhost:8080/api/admin/delete/${id}`, {
        method: 'POST'
    })
        .then(response => response.json)
        .then(response => {
            console.log(response)
            fetchUsers();
            const deleteUserModalElement = document.getElementById('deleteUserModal');
            const deleteUserModal = bootstrap.Modal.getInstance(deleteUserModalElement);
            deleteUserModal.hide();
        })
        .catch(error => console.log(error))
}