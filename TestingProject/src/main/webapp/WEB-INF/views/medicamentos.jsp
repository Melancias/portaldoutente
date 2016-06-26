<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="pt">

<head>

	 <title>Portal do Utente</title>

	<!-- Bootstrap Core CSS -->
    <link href="/resources/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="/resources/css/main.css" rel="stylesheet">
    
    <link rel="stylesheet" href="https://code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
  	<script src="https://code.jquery.com/jquery-1.10.2.js"></script>
  	<script src="https://code.jquery.com/ui/1.11.4/jquery-ui.js"></script>

</head>
<style>
.ui-autocomplete { max-height: 200px; overflow-y: auto; overflow-x: hidden;}
</style>

<body background=<c:url value="/resources/gfx/Final2.png"/> />

 <div class="container">
            
            <div class="col-lg-12"> 
                <div class= "rowMajor">
                	<div id="divisento"></div>
					<div class="col-md-11">Portal do Utente</div>
					<div class="col-md-1" id="entrar">${username}</div>
				</div>
            
            </div>
        <nav>
          <ul>
            <li id="login">
              <a id="login-trigger" href="#">
                Opções <span>▼</span>
              </a>
              <div id="login-content">
                <a button href= "/perfil/dados" id = "aaa">Ver Perfil</button></a><br>
                <a button href= "/logout" id ="aaa">Logout</button></a>
              </div>                     
            </li>
          </ul>
        </nav>
        <div class="col-lg-12" id="caixaGig">  <!--  -->
	
		<div id="wrapper">

			<!-- Sidebar -->
			<div id="sidebar-wrapper">
                <ul class="barra">
                        
                    <li id="consulta">
                        <a href="/calendario/view">Marcação de Consultas</a>
                    </li>
                    <li id="medicacao2">
                        <a href="/medicacao/view">Registar Medicação</a>
                    </li>
                    <li id="medicao">
                        <a href="/medicoes">Medições</a>
                    </li>
                    <li id="cirurgia">
                        <a href="/cirurgia">Cirurgia</a>
                    </li>
                    <li id="isencao">
                        <a href="/isencao">Pedido de Isenção</a>
                    </li>
                </ul>
            </div>
    </div>
<form method="post" id="medform" >
<div id="automed" style="display:flex;align-items:center;">
<label for="autocomplete">Escolha um Medicamento: </label>
<input id="autocomplete" name = "nome" style="width: 40%;">
</div>
Quantidade: 
<input id="quant" type="text" name="dosagem"><br>
Indicacoes: <br>
<textarea id="indicacoes" rows="10" cols="60" name="indicacoes"></textarea><br>
</form>
<input id="medicacao" type="submit" onclick="medicamentos()">

<div  class="exame">
		<p id="texto2">Consulte abaixo os seus Exames MÃ©dicos</p>

        <table id="tabela_exames">

            <tr id = "texto_tab">
                <td>NOME</td>
                <td>DOSE</td> 
                <td>INDICAÇÕES</td>
                <td>Renovar</td>
                <td>Apagar</td>
            </tr>

         <c:forEach items="${lista}" var="medicacao">
    <tr id = "texto_tab">
    	<td> <c:out value="${medicacao.nomeMedicamento }"/> </td>
        <td><c:out value="${medicacao.dose}"/></td>
        <td><c:out value="${medicacao.indicacoes}"/></td>
        <td><button id = "renovar" value="${medicacao.getId()}" onclick="renovar(${medicacao.id})"> Pedir Renovacao </button></td>
        <td><button id = "apagar" value="${medicacao.getId() }" onclick="apagar(${medicacao.id})"> Apagar </button></td>
        
    </tr>
</c:forEach>
        </table>
      <script>
function renovar(ID) { 
	path="https://" + window.location.host + "/";
	console.log(ID);
	$.post(path+"confirmarCirurgia", {"id":ID},function(data){
		if (data==true){window.location.replace(path + "index");}
	else {alert("Falhou a Confirmar a Cirurgia!");}
})}

</script>
      


<script>
$(document).ready(function(){
	var link="<c:url value="/resources/meds3.json"> </c:url>";
$.get(link).done(function( tags ) {
  $( "#autocomplete" ).autocomplete({
    source: function( request, response ) {
            var matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex( request.term ), "i" );
            response( $.grep( tags, function( item ){
                return matcher.test( item );
            }) );
        }
  });
  });
});
</script>
<script>
function medicamentos()
{
		path="https://" + window.location.host + "/medicacao/inserir";
		$.post(path, $("form").serialize()).done(function( data ) {
			if (data==true){window.location.reload();}
			
			else {alert(data)}}

		);

}
</script>

    <!-- Menu Toggle Script -->
<script>
     $(document).ready(function(){
   $('#login-trigger').click(function(){
     $(this).next('#login-content').slideToggle();
     $(this).toggleClass('active');          
    
     if ($(this).hasClass('active')) $(this).find('span').html('&#x25B2;')
       else $(this).find('span').html('&#x25BC;')
     })
 });
 </script>
