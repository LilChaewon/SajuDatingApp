const http = require('http');
const { Server } = require("socket.io");
const pool = require('./db');
require('dotenv').config();
const app = require('./app'); // Import the app
const initializeSocket = require('./socket');

const server = http.createServer(app);
const io = new Server(server);
const PORT = process.env.PORT || 3000;

// Initialize Socket.IO logic
initializeSocket(io);

// Test database connection and start server
pool.getConnection()
  .then(connection => {
    console.log('Successfully connected to MySQL database!');
    connection.release();
    server.listen(PORT, () => {
      console.log(`Server is listening on port ${PORT}`);
    });
  })
  .catch(err => {
    console.error('Failed to connect to MySQL database:', err.message);
    process.exit(1);
  });
