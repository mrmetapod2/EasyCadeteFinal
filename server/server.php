<?php
$servername = "127.0.0.1";//reemplaza con tu IP
$username = "root";  // MySQL username
$password = "";  // MySQL password
$dbname = "easycadete";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $sql = $_POST['sql'];




    if (stripos($sql, "SELECT") === 0) {
        $result = $conn->query($sql);

        if ($result->num_rows > 0) {
            // Output data of each row
            while ($row = $result->fetch_assoc()) {
                echo json_encode($row) . "<br>";
            }
        } else {
            echo "0 results";
        }
    } else {
        if ($conn->query($sql) === TRUE) {
            // Check if the query was an INSERT operation
            if (stripos($sql, "INSERT") === 0) {
                // Get the ID of the inserted record
                $last_id = $conn->insert_id;
                echo "Query successful. Last inserted ID is: " . $last_id;
            } else {
                echo "Query successful.";
            }
        } else {
            echo "Error: " . $sql . "<br>" . $conn->error;
        }
    }
}

$conn->close();
?>
