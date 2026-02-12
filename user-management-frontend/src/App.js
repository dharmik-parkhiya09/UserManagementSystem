import React, { useState } from "react";

function App() {
  const [userId, setUserId] = useState("");
  const [userName, setUserName] = useState("");
  const [users, setUsers] = useState([]);
  const [singleUser, setSingleUser] = useState(null);

  // Add user locally
  const handleAddUser = (e) => {
    e.preventDefault();
    if (!userName.trim()) return;
    const newUser = { id: Date.now(), name: userName };
    setUsers([...users, newUser]);
    setUserName("");
  };

  // Fetch all users (dummy data for now)
  const handleGetUsers = () => {
    setUsers([
      { id: 1, name: "Dharmik" },
      { id: 2, name: "Alex" },
      { id: 3, name: "Priya" },
    ]);
  };

  // Get single user from list
  const handleGetUserById = (e) => {
    e.preventDefault();
    const user = users.find((u) => u.id.toString() === userId);
    setSingleUser(user || null);
  };

  // Delete user by ID
  const handleDeleteUserById = (e) => {
    e.preventDefault();
    setUsers(users.filter((u) => u.id.toString() !== userId));
    setUserId("");
    setSingleUser(null);
  };

  return (
      <div style={{ padding: 20 }}>
        <h1>User Management System</h1>

        {/* Add User */}
        <div>
          <h2>Add User</h2>
          <form onSubmit={handleAddUser}>
            <input
                type="text"
                placeholder="User Name"
                value={userName}
                onChange={(e) => setUserName(e.target.value)}
            />
            <button type="submit">Add User</button>
          </form>
        </div>

        {/* Fetch All Users */}
        <div>
          <h2>Get All Users</h2>
          <button onClick={handleGetUsers}>Fetch Users</button>
          <ul>
            {users.map((user) => (
                <li key={user.id}>
                  {user.id} - {user.name}
                </li>
            ))}
          </ul>
        </div>

        {/* Get User by ID */}
        <div>
          <h2>Get User By Id</h2>
          <form onSubmit={handleGetUserById}>
            <input
                type="text"
                placeholder="User ID"
                value={userId}
                onChange={(e) => setUserId(e.target.value)}
            />
            <button type="submit">Get User</button>
          </form>
          {singleUser && (
              <div>
                <h3>User Found:</h3>
                <p>ID: {singleUser.id}</p>
                <p>Name: {singleUser.name}</p>
              </div>
          )}
        </div>

        {/* Delete User */}
        <div>
          <h2>Delete User By Id</h2>
          <form onSubmit={handleDeleteUserById}>
            <input
                type="text"
                placeholder="User ID"
                value={userId}
                onChange={(e) => setUserId(e.target.value)}
            />
            <button type="submit">Delete User</button>
          </form>
        </div>
      </div>
  );
}

export default App;
