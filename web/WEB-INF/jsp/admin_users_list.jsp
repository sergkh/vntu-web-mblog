<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="includes/header.jsp" %>

<!-- Todo: make users list like <a href="http://bootsnipp.com/snipps/table-with-users">this</a> -->    

<div class="span10">
    <table class="table table-condensed table-hover">
      <thead>
        <tr>
          <th>#</th>
          <th>E-mail</th>
          <th>Дата реєстрації</th>
          <th>Дата блокування</th>
          <th style="width: 140px;"></th>
        </tr>
      </thead>
      
      <tbody>
        <tr>
          <td>1</td>
          <td>the_mark7@gmail.com</td>
          <td>2012/05/06</td>
          <td>2013/05/06</td>
          <td>
            <button type="button" class="btn-warning"> Заблокувати </button>
          </td>
          <td>
            <button type="button" class="btn-primary"> <small> Зробити модератором </button>
          </td>
         </tr>
         <tr>
          <td>2</td>
          <td>ash11927@gmail.com</td>
          <td>2010/08/21</td>
          <td>2013/08/21</td>
          <td>
            <button type="button" class="btn-warning"> Заблокувати </button>
          </td>
          <td>
            <button type="button" class="btn-primary"> <small> Зробити модератором </button>
          </td>
         </tr>
         <tr>
          <td>3</td>
          <td>audann84@gmail.com</td>
          <td>2011/12/01</td>
          <td>2013/12/01</td>
          <td>
            <button type="button" class="btn-warning"> Заблокувати </button>
          </td>
          <td>
            <button type="button" class="btn-primary"> <small> Зробити модератором </button>
          </td>
         </tr>
         <tr>
          <td>4</td>
          <td>jr5527@gmail.com</td>
          <td>2009/04/11</td>
          <td>2013/04/11</td>
          <td>
            <button type="button" class="btn-warning"> Заблокувати </button>
          </td>
          <td>
            <button type="button" class="btn-primary"> <small> Зробити модератором </button>
          </td>
         </tr>
         <tr>
          <td>5</td>
          <td>aaron_butler@gmail.com</td>
          <td>2007/02/01</td>
          <td>2013/02/01</td>
          <td>
            <button type="button" class="btn-warning"> Заблокувати </button>
          </td>
          <td>
            <button type="button" class="btn-primary"> <small> Зробити модератором </button>
          </td>
         </tr>
      </tbody>
    </table>
</div>

<div class="pagination">
    <ul>
        <li><a href="#">Prev</a></li>
        <li class="active"><a href="#">1</a></li>
        <li><a href="#">2</a></li>
        <li><a href="#">3</a></li>
        <li><a href="#">4</a></li>
        <li><a href="#">Next</a></li>
    </ul>
</div>

<%@include file="includes/footer.jsp" %>