<!doctype html>
<?php
	require('config.php');
	if(isSet($_SESSION["login"]) || (isSet($_POST["pass"]) && $_POST["pass"] == 'eva-\'2')) {
		$_SESSION["login"] = true;
		echo '<meta http-equiv="Refresh" Content="0; URL=index.php">';
		//header("Location: index.php");
		die();
	}
?>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Sumários 'o Plenty ~ Login</title>
	<link href='favicon.ico' rel='shortcut icon' type='image/x-icon'/>
	<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="css/style.css" />
</head>
<body>
<form action="login.php" method="POST" name="login">
	<div class="input-group">
		<span class="input-group-addon"><span class="glyphicon glyphicon-lock"></span></span>
		<input type="password" name="pass" class="form-control" placeholder="Senha" required>
	</div>
</form>
</body>
</html>