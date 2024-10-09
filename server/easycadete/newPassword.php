<head>
  <!-- ======= Header ======= -->
  
  <meta charset="utf-8">
  <meta content="width=device-width, initial-scale=1.0" name="viewport">

  <title>Cambiar de contraseña</title>
  <meta content="" name="description">
  <meta content="" name="keywords">

  <!-- Favicons -->
  <link href="assets/img/favicon.png" rel="icon">
  <link href="assets/img/apple-touch-icon.png" rel="apple-touch-icon">

  <!-- Google Fonts -->
  <link href="https://fonts.gstatic.com" rel="preconnect">
  <link href="https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i|Nunito:300,300i,400,400i,600,600i,700,700i|Poppins:300,300i,400,400i,500,500i,600,600i,700,700i" rel="stylesheet">

  <!-- Vendor CSS Files -->
  <link href="assets/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
  <link href="assets/vendor/bootstrap-icons/bootstrap-icons.css" rel="stylesheet">
  <link href="assets/vendor/boxicons/css/boxicons.min.css" rel="stylesheet">
  <!--<link href="assets/vendor/quill/quill.snow.css" rel="stylesheet">
  <link href="assets/vendor/quill/quill.bubble.css" rel="stylesheet">
  <link href="assets/vendor/remixicon/remixicon.css" rel="stylesheet">
  <link href="assets/vendor/simple-datatables/style.css" rel="stylesheet">
--> 
  <!-- Template Main CSS File -->
  <link href="assets/css/style.css" rel="stylesheet">

  <!-- =======================================================
  * Template Name: NiceAdmin
  * Template URL: https://bootstrapmade.com/nice-admin-bootstrap-admin-html-template/
  * Updated: Apr 20 2024 with Bootstrap v5.3.3
  * Author: BootstrapMade.com
  * License: https://bootstrapmade.com/license/
  ======================================================== -->
  
  <!-- End Header --></head>
<?php
// Database credentials
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
$mensaje=false;

if ( isset( $_POST['button'])){
      

    if ($_SERVER["REQUEST_METHOD"] == "POST") {
        if (isset($_GET['email']) && isset($_POST['new_password'])&& isset($_POST["confirm_password"])&& $_POST['new_password'] ==$_POST["confirm_password"]) {
            $email = $_GET['email'];
            $new_password = password_hash($_POST['new_password'], PASSWORD_BCRYPT);

            // Prepare and bind
            $stmt = $conn->prepare("UPDATE `persona` SET `Contraseña` = ? WHERE `Email` = ?");
            $stmt->bind_param("ss", $new_password, $email);

            // Execute the statement
            if ($stmt->execute()) {
                echo "Password updated successfully";
            } else {
                echo "Error updating password: " . $stmt->error;
            }

            // Close statement
            $stmt->close();
        } else {
            echo "Email or new password not provided.";
            $mensaje=true;
        }
    } else {
        echo "Invalid request method.";
    }
}

// Close connection
$conn->close();
?>
<div class="container">
    
   
    
    
   
  
   
   
    

      <section class="section register min-vh-100 d-flex flex-column align-items-center justify-content-center py-4">
        <div class="container">
          <div class="row justify-content-center">
            <div class="col-lg-4 col-md-6 d-flex flex-column align-items-center justify-content-center">

              <div class="d-flex justify-content-center py-4">
                <a href="" class="logo d-flex align-items-center w-auto">
                  <img src="assets/img/logo.png" alt="">
                  <span class="d-none d-lg-block">Panel de Administración</span>
                </a>
              </div><!-- End Logo -->

              <div class="card mb-3">

                <div class="card-body">

                  <div class="pt-4 pb-2">
                    <h5 class="card-title text-center pb-0 fs-4">Ingresa tu nueva contraseña</h5>
                    <p class="text-center small">Ingresa tus contraseña nuevos</p>
                  </div>

                  <form class="row g-3  
                  <?php if($vacioContra || $vacioUsu) {echo"was-validated ";}?>
                  " novalidate method="post">
                  <?php
                  
                    
                    if ($mensaje==true){
                    ?>
                  <div class="alert alert-warning alert-dismissible fade show" role="alert">
                    <i class="bi bi-exclamation-triangle me-1"></i>
                    contraseñas no son iguales.
                    </div>
                    <?php } ?>


                    <div class="col-12">
                      <label for="yourUsername" class="form-label">Contraseña</label>
                      <div class="input-group has-validation">
                        
                        <input class="form-control" id="yourUsername" required name="new_password"value="">
               
                        
                        
                      </div>
                    </div>

                    <div class="col-12">
                      <label for="yourPassword" class="form-label">Confirmar contraseña</label>
                      <input class="form-control" id="yourPassword" required name="confirm_password" value="">
                      
                      
                    </div>

                
                    <div class="col-12">
                      <button class="btn btn-primary w-100" name="button" type="submit" novalidate>Cambiar contraseña</button>
                    </div>
                    
       
                  </form>

                </div>
              </div>

              <div class="credits">
                <!-- All the links in the footer should remain intact. -->
                <!-- You can delete the links only if you purchased the pro version. -->
                <!-- Licensing information: https://bootstrapmade.com/license/ -->
                <!-- Purchase the pro version with working PHP/AJAX contact form: https://bootstrapmade.com/nice-admin-bootstrap-admin-html-template/ -->
                Designed by <a href="https://bootstrapmade.com/">BootstrapMade</a>
              </div>

            </div>
          </div>
        </div>

      </section>

    </div>