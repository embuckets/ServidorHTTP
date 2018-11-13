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
    $baseDatos = $_POST["nombre"];
    $query = "create database " . $baseDatos;
    echo $query . PHP_EOL;
    if ($conn->query($query) === TRUE) {
        echo "Database created successfully";
    } else {
        echo "Error creating database: " . $conn->error;
    }
    
    $conn->close();
?>