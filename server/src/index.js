const express = require('express');
const http = require('http');
const { Server } = require("socket.io");
const mysql = require('mysql2/promise'); // Import mysql2 with promise support

const app = express();
const server = http.createServer(app);
const io = new Server(server);

const PORT = process.env.PORT || 3000;

// Database connection pool setup
const pool = mysql.createPool({
  host: process.env.DB_HOST || 'localhost',
  user: process.env.DB_USER || 'root',
  password: process.env.DB_PASSWORD || 'password',
  database: process.env.DB_NAME || 'saju_dating_app',
  waitForConnections: true,
  connectionLimit: 10,
  queueLimit: 0
});

// Test database connection
pool.getConnection()
  .then(connection => {
    console.log('Successfully connected to MySQL database!');
    connection.release(); // Release the connection
  })
  .catch(err => {
    console.error('Failed to connect to MySQL database:', err.message);
    process.exit(1); // Exit if database connection fails
  });


app.get('/', (req, res) => {
  res.send('Saju Dating App Server is running!');
});

// Example route to test DB connection (optional)
app.get('/test-db', async (req, res) => {
  try {
    const [rows] = await pool.query('SELECT 1 + 1 AS solution');
    res.json({ message: 'Database connection test successful', solution: rows[0].solution });
  } catch (error) {
    res.status(500).json({ message: 'Database connection test failed', error: error.message });
  }
});


io.on('connection', (socket) => {
  console.log('a user connected');
  socket.on('disconnect', () => {
    console.log('user disconnected');
  });
});

server.listen(PORT, () => {
  console.log(`Server is listening on port ${PORT}`);
});
