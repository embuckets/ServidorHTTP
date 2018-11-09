<?php
// MySQLi Object-Oriented
$servername = "localhost";
$username = "root";
$password = "evangelion01";

// Create connection
$conn = new mysqli($servername, $username, $password);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 
echo "Connected successfully" . PHP_EOL;
echo "Closing connection";
$conn->close();
?>