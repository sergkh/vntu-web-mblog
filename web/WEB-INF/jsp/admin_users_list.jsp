<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="includes/header.jsp" %>

<!-- Todo: make users list like <a href="http://bootsnipp.com/snipps/table-with-users">this</a> -->    

<div class="container">
    <table class="table table-condensed table-hover">
      <thead>
        <tr>
          <th>E-mail</th>
          <th>Дата реєстрації</th>
          <th>Дата блокування</th>
          <th style="width: 140px;"></th>
        </tr>
      </thead>
      
      <tbody>
        <tr>
          <td>the_mark7@gmail.com</td>
          <td>2012/05/06</td>
          <td>2013/05/06</td>
          <td>
            <button type="button" class="btn btn-success btn-mini"><i class="icon-remove"></i> Заблокувати</button>
          </td>
          <td>
            <button type="button" class="btn btn-mini"><i class="icon-arrow-up"></i> В модератори</button>
          </td>
         </tr>
         <tr>
          <td>ash11927@gmail.com</td>
          <td>2010/08/21</td>
          <td>2013/08/21</td>
          <td>
            <button type="button" class="btn btn-success btn-mini"><i class="icon-remove"></i> Заблокувати</button>
          </td>
          <td>
            <button type="button" class="btn btn-warning btn-mini"><i class="icon-fire"></i> З модераторів</button>
          </td>
         </tr>
         <tr>
          <td>audann84@gmail.com</td>
          <td>2011/12/01</td>
          <td>2013/12/01</td>
          <td>
            <button type="button" class="btn btn-danger btn-mini"><i class="icon-ok"></i> Розблокувати</button>
          </td>
          <td>
            <button type="button" class="btn btn-mini"><i class="icon-arrow-up"></i> В модератори</button>
          </td>
         </tr>
      </tbody>
    </table>
    
    <div class="pagination text-center">
	    <ul>
	        <li><a href="#">Prev</a></li>
	        <li class="active"><a href="#">1</a></li>
	        <li><a href="#">2</a></li>
	        <li><a href="#">3</a></li>
	        <li><a href="#">4</a></li>
	        <li><a href="#">Next</a></li>
	    </ul>
	</div>
    
</div>



<%@include file="includes/footer.jsp" %>