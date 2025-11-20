const express = require('express');
const http = require('http');
const { Server } = require("socket.io");
const pool = require('./db'); // Import the pool from db.js
require('dotenv').config();

const authRoutes = require('./routes/auth');

const app = express();
const server = http.createServer(app);
const io = new Server(server);

const PORT = process.env.PORT || 3000;

// Middleware
app.use(express.json()); // To parse JSON bodies

// Routes
app.use('/api/auth', authRoutes);


app.get('/', (req, res) => {
  res.send('Saju Dating App Server is running!');
});

io.on('connection', (socket) => {
  console.log('a user connected');
  socket.on('disconnect', () => {
    console.log('user disconnected');
  });
});

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
