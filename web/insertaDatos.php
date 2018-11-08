<?php
    $servername = "localhost";
    $username = "root";
    $password = "labsim";
    $dbname = "espacios";

    //Create connection
    $conn = new mysqli($servername, $username, $password, $dbname);
    // Check connection
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }

    $txt_file    = file_get_contents('data.csv');
    $rows        = explode("\n", $txt_file);
    // array_shift($rows);
    
    foreach($rows as $row => $data)
    {
        //get row data
        $row_data = explode(',', $data);
    
        $nodo           = $row_data[0];
        $tiempo       = $row_data[1];
        $posx  = $row_data[2];
        $posy       = $row_data[3];
        $temperatura       = $row_data[4];
        $luminosidad       = $row_data[5];
        $humedad       = $row_data[6];
    
        //display data
        echo $nodo;
        echo $tiempo;
        echo $posx;
        echo $posy;
        echo $temperatura;
        echo $luminosidad;
        echo $humedad;
        echo PHP_EOL;

        $sql = "INSERT INTO lecturas ("$nodo . "," . $tiempo . "," .  "firstname, lastname, email)
    VALUES ('John', 'Doe', 'john@example.com')";


    }

    $sql = "INSERT INTO MyGuests (firstname, lastname, email)
    VALUES ('John', 'Doe', 'john@example.com')";

    if ($conn->query($sql) === TRUE) {
        echo "New record created successfully";
    } else {
        echo "Error: " . $sql . "<br>" . $conn->error;
    }

    $conn->close();


    

?>