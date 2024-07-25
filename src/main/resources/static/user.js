document.addEventListener('DOMContentLoaded', function() {
    fetchUsers();
});

function fetchUsers() {
    fetch('http://localhost:8080/api/users')
        .then(response =>  response.json())
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
