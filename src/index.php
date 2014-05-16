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
<div id="header-bg"></div>
<div class="container">
	<?php include('header.php') ?>
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
				$plan = preg_replace("/\r\n|\r|\n/",'<br/>',$row['planificacao']);
				echo "<tr>
				<td class='centered'>". $row['licao'] ."</td>
				<td class='centered'>". $row['disciplina'] ."</td>
				<td class='centered'>". $row['modulo'] ."</td>
				<td>". htmlspecialchars_decode($plan) ."</td>
				<td class='centered'>". $row['data'] ."</td>
				<td class='centered'><span class='badge hyperlink' data-toggle='modal' data-target='#addLicao' onClick=\"alterar('". $row['licao'] ."', '". $row['disciplina'] ."', '". $row['modulo'] ."', '$plan', '". $row['data'] ."')\"><span class='glyphicon glyphicon-pencil'></span></span><span class='badge hyperlink' onClick='remover(". $row['licao'] .")'><span class='glyphicon glyphicon-trash'></span></span></td>
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
					<form action="db/add.php" method="POST" name="form">
						<input type="hidden" name="update" id="update" class="form-control" value="" required>
						<div class="input-group">
							<span class="input-group-addon"><span class="glyphicon glyphicon-asterisk"></span></span>
							<input type="text" name="licao" id="licao" class="form-control" placeholder="Lição" required>
						</div>
						<div class="input-group">
							<span class="input-group-addon"><span class="glyphicon glyphicon-pencil"></span></span>
							<select name="disciplina" class="form-control" required>
								<option value="1">Programação de Sistemas Informáticos</option>
							</select>
						</div>
						<div class="input-group">
							<span class="input-group-addon"><span class="glyphicon glyphicon-tags"></span></span>
							<select name="modulo" class="form-control" required>
								<?php
									$max = 10;
									for($i=1; $i <= $max; $i++)
										echo "<option ". ($i == $max ? "selected " : "") ."value='$i'>$i</option>";
								?>
							</select>
						</div>
						<div class="input-group">
							<span class="input-group-addon"><span class="glyphicon glyphicon-font"></span></span>
							<textarea name="planificacao" id="planificacao" class="form-control" style="height: 150px" placeholder="Planificação" required></textarea>
						</div>
						<div class="input-group">
							<span class="input-group-addon"><span class="glyphicon glyphicon-time"></span></span>
							<input type="datetime" name="data" id="data" class="form-control" placeholder="Data">
							<span class="input-group-addon hyperlink" onClick="setTodayDate()">Agora</span>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Fechar</button>
					<button type="button" class="btn btn-primary" onClick="document.form.submit()">Adicionar</button>
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

	function escapeRegExp(string) {
		return string.replace(/([.*+?^=!:${}()|\[\]\/\\])/g, "\\$1");
	}

	function alterar(licao, disciplina, modulo, planificacao, data) {
		if(isLogged || true) {
			$('#licao').val(licao);
			$('#disciplina').val(disciplina);
			$('#modulo').val(modulo);
			$('#planificacao').val(planificacao.replace(new RegExp(escapeRegExp('<br/>'), 'g'), '\n'));
			$('#data').val(data);
			$('#update').val("true");
		} else
			window.location.href = "login.php";
	}

	function limparForm() {
		document.form.reset();
		$('#update').val("");
		setTodayDate();
	}

	function setTodayDate() {
		var date = new Date();
		var y = date.getFullYear();
		var m = date.getMonth();
		var d = date.getDay();
		if(m < 10) m = '0' + m;
		if(d < 10) d = '0' + d;

		$('#data').val(y + '-' + m + '-' + d);
	}
</script>
</body>
</html>