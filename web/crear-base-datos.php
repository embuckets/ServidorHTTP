<?php 
// MySQLi Object-Oriented
    $servername = "localhost";
    $username = "root";
    $password = "labsim";
    
    // Create connection
    $conn = new mysqli($servername, $username, $password);
    
    // Check connection
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    } 
    echo "Connected successfully" . PHP_EOL;
    $baseDatos = $_POST["nombre"];
    $query = "create database " . $baseDatos;
    if ($conn->query($sql) === TRUE) {
        echo "Database created successfully";
    } else {
        echo "Error creating database: " . $conn->error;
    }
    
    $conn->close();
?>