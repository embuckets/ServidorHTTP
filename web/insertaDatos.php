<?php
    $servername = "localhost";
    $username = "root";
    $password = "evangelion01";
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
    
        $nodo = $row_data[0];
        $tiempo = $row_data[1];
        $posx = $row_data[2];
        $posy = $row_data[3];
        $temperatura = $row_data[4];
        $luminosidad = $row_data[5];
        $humedad = $row_data[6];
    
        //display data
        echo "Se leyeron:" . PHP_EOL;
        echo $nodo;
        echo $tiempo;
        echo $posx;
        echo $posy;
        echo $temperatura;
        echo $luminosidad;
        echo $humedad;
        echo PHP_EOL;

        $stmt = $conn->prepare("insert into lecturas (nodo,posicionx,posiciony,temperatura,luminosidad,humedad,hora) values(?,?,?,?,?,?,?)");
        $stmt->bind_param("iddiiis", $nodo,$posx,$posy,$temperatura,$luminosidad,$humedad,$tiempo);
        
        
        if ($stmt->execute()) {
            echo "New record created successfully";
        } else {
            echo "Error: " . $conn->errno;
            echo "New record was not inserted";
        }
        sleep(5);
    }
    $conn->close();
?>