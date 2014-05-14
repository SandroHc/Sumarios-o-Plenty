<!doctype html>
<?php include('config.php') ?>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Sumários 'o Plenty</title>
	<link href='favicon.ico' rel='shortcut icon' type='image/x-icon'/>
	<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="css/style.css" />
</head>
<body>
<div class="container">
	<div class="alerts">
		<?php
			if(isSet($_SESSION["succ"])) {
				createAlert("success", "Sucesso!", $_SESSION["succ"]);
				$_SESSION["succ"] = NULL;
			}
			if(isSet($_SESSION["info"])) {
				createAlert("info", "Informação", $_SESSION["info"]);
				$_SESSION["info"] = NULL;
			}
			if(isSet($_SESSION["warn"])) {
				createAlert("warning", "Aviso!", $_SESSION["warn"]);
				$_SESSION["warn"] = NULL;
			}
			if(isSet($_SESSION["err"])) {
				createAlert("danger", "Atenção!", $_SESSION["err"]);
				$_SESSION["err"] = NULL;
			}
		?>
	</div>
	<?php include('header.php') ?>
	<div>
		<?php
			startConnection();
			global $con;
			$query = "SELECT * FROM $db_database.licoes ORDER BY 'licao' ASC";
			$result = mysqli_query($con, $query) or die ("Erro: ". $con->error);
			echo "<table class='table'><thead class='centered'>
			<td style='width:5%'><span class='glyphicon glyphicon-asterisk' data-toggle='tooltip' data-placement='top' title='Lição'></span></td>
			<td style='width:5%'><span class='glyphicon glyphicon-pencil' data-toggle='tooltip' data-placement='top' title='Disciplina'></span></span></td>
			<td style='width:5%'><span class='glyphicon glyphicon-tags' data-toggle='tooltip' data-placement='top' title='Módulo'></span></td>
			<td><span class='glyphicon glyphicon-font' data-toggle='tooltip' data-placement='top' title='Planificação'></span></td>
			<td style='width:10%'><span class='glyphicon glyphicon-time' data-toggle='tooltip' data-placement='top' title='Data'></span></td>
			<td style='width:70px'><span class='glyphicon glyphicon-cog' data-toggle='tooltip' data-placement='top' title='Ações'></span></td>
			</thead>";
			while($row = mysqli_fetch_array($result)) {
				//print_r($row);
				echo "<tr>
				<td class='centered'>". $row['licao'] ."</td>
				<td class='centered'>". $row['disciplina'] ."</td>
				<td class='centered'>". $row['modulo'] ."</td>
				<td>". htmlspecialchars_decode($row['planificacao']) ."</td>
				<td class='centered'>". $row['data'] ."</td>
				<td class='centered'><span class='badge' data-toggle='modal' data-target='#addLicao' onClick='alterar(\"". $row['licao'] ."\", \"". $row['disciplina'] ."\", \"". $row['modulo'] ."\", \"". $row['planificacao'] ."\", \"". $row['data'] ."\")'><span class='glyphicon glyphicon-pencil'></span></span><span class='badge' onClick='remover(". $row['licao'] .")'><span class='glyphicon glyphicon-minus'></span></span></td>
				</tr>";
			}
			echo "</table>";
			mysqli_free_result($result);
			endConnection();
		?>
	</div>

	<!-- ADICIONAR SUMÁRIOS -->
	<div class="modal fade" id="addLicao">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title">Adicionar Sumário</h4>
				</div>
				<div class="modal-body">
					<form action="db/add.php" method="POST" name="addLicaoForm">
						<input type="hidden" name="update" id="update" class="form-control" value="" required>
						<div class="input-group">
							<span class="input-group-addon"><span class="glyphicon glyphicon-asterisk"></span></span>
							<input type="text" name="licao" id="licao" class="form-control" placeholder="Lição" required>
						</div>
						<div class="input-group">
							<span class="input-group-addon"><span class="glyphicon glyphicon-pencil"></span></span>
							<input type="number" name="disciplina" id="disciplina" class="form-control" placeholder="Disciplina (ID)" required>
						</div>
						<div class="input-group">
							<span class="input-group-addon"><span class="glyphicon glyphicon-tags"></span></span>
							<input type="number" name="modulo" id="modulo" class="form-control" placeholder="Módulo">
						</div>
						<div class="input-group">
							<span class="input-group-addon"><span class="glyphicon glyphicon-font"></span></span>
							<textarea name="planificacao" id="planificacao" class="form-control" placeholder="Planificação" required></textarea>
						</div>
						<div class="input-group">
							<span class="input-group-addon"><span class="glyphicon glyphicon-time"></span></span>
							<input type="datetime" name="data" id="data" class="form-control" placeholder="Data">
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Fechar</button>
					<button type="button" class="btn btn-primary" onClick="document.addLicaoForm.submit()">Adicionar</button>
				</div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
</div>
<script src="//code.jquery.com/jquery-2.1.1.min.js"></script>
<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
<script>
	var isLogged = <?php echo (isSet($_SESSION["login"]) ? "true" : "false") ?>;

	function remover(nome) {
		if(isLogged)
			window.location.href = "db/rem.php?name=" + nome;
		else
			window.location.href = "login.php";
	}

	function alterar(licao, disciplina, modulo, planificacao, data) {
		if(isLogged) {
			$('#licao').val(licao);
			$('#disciplina').val(disciplina);
			$('#modulo').val(modulo);
			$('#planificacao').val(planificacao);
			$('#data').val(data);
			$('#update').val("true");
		} else
			window.location.href = "login.php";
	}

	function limparForm() {
		document.addLicaoForm.reset();
		$('#update').val("");
	}
</script>
</body>
</html>