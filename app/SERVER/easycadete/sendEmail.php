<?php
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

require 'vendor\autoload.php';

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $to = $_POST['email'];
    $subject = $_POST['subject'];
    $link = $_POST['link'];
    $encodedEmail = urlencode($to);
    $fullLink = $link . "?email=" . $encodedEmail;
    $message = "Here is your link: " . $fullLink;
    $headers = "From: hectormzacrias@gmail.com";

    $mail = new PHPMailer(true);

    try {
        // Server settings
        $mail->isSMTP();
        $mail->Host = 'sandbox.smtp.mailtrap.io'; // Set the SMTP server to send through
        $mail->SMTPAuth = true;
        $mail->Username = '47e41fee27bc84'; // SMTP username
        $mail->Password = '1ea45e35d51691'; // SMTP password
        $mail->SMTPSecure = PHPMailer::ENCRYPTION_STARTTLS;
        $mail->Port = 587;

        // Recipients
        $mail->setFrom('hectormzacrias@gmail.com', 'Mailer');
        $mail->addAddress($to);

        // Content
        $mail->isHTML(true);
        $mail->Subject = $subject;
        $mail->Body    = $message;

        $mail->send();
        echo 'Email successfully sent to ' . $to;
    } catch (Exception $e) {
        echo "Email could not be sent. Mailer Error: {$mail->ErrorInfo}";
    }
} else {
    echo "Invalid request method.";
}
?>