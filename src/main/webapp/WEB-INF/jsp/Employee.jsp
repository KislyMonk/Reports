<%-- 
    Document   : Employee
    Created on : 25.02.2018, 14:04:18
    Author     : Андрей
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="<c:url value="/main.css" />" rel="stylesheet">
        <title>Анализ по сотрудникам</title>
    </head>
    <body>
        <form method="POST" enctype="multipart/form-data"
            action="/Reports/employee">
            Загрузи файлик:  <input type="file" name="file"><br /> 
            <input type="submit" value="Upload"> 
        </form> 
        Сообщение от сервера: ${message}
        
    <table width="694" style="border-collapse:collapse;">
        <tbody>
        <tr height="8">
            <td colspan="9" bgcolor="#bcd2ee" >
            <div align="center"><font size="1" color="blue">Отчет за период
            ${range}
            </font></div>
            </td>
        </tr>
        <tr height="8">
            <td width="176" class="header" >
                <div align="center">
                    <font size="1">
                        Ф.И.О.
                    </font>
                </div>
            </td>
            <td width="76" class="header" >
                <div align="center">
                    <font size="1">
                        Количество обработанных ТТ
                    </font>
                </div>
            </td>
            <td width="76" class="header" >
                <div align="center">
                    <font size="1">
                        Количество повторных обращений на Л2ТП
                    </font>
                </div>
            </td>
            <td width="76" class="header" >
                <div align="center">
                    <font size="1">
                        Процент повторных Л2ТП
                    </font>
                </div>
            </td>
            <td width="76" class="header" >
                <div align="center">
                    <font size="1">
                        Количество занесенных на ремонт
                    </font>
                </div>
            </td>
            <td width="76" class="header" >
                <div align="center">
                    <font size="1">
                        Процент занесенных на ремонт
                    </font>
                </div>
            </td>
            <td width="46" class="header" >
                <div align="center">
                    <font size="1">
                        Простые
                    </font>
                </div>
            </td>
            <td width="46" class="header" >
                <div align="center">
                    <font size="1">
                        Норма
                    </font>
                </div>
            </td>
            <td width="46" class="header" >
                <div align="center">
                    <font size="1">
                        Сложные
                    </font>
                </div>
            </td>
        </tr>
        
        <c:forEach var="model" items="${modelsList}">
            <tr height="8">
                <td>
                    <div align="center">
                        <font size="1">
                            ${model.getName()}
                        </font>
                    </div>
                </td>
                <td>
                    <div align="center">
                        <font size="1">
                            ${model.getCountTT()}
                        </font>
                    </div>
                </td>
                <td>
                    <div align="center">
                        <font size="1">
                            ${model.getRepeatedTT()}
                        </font>
                    </div>
                </td>
                <td>
                    <div align="center">
                        <font size="1">
                            <fmt:formatNumber value="${(model.getRepeatedTT()/model.getCountTT())*100}" pattern="###.##"/>
                        </font>
                    </div>
                </td>
                <td>
                    <div align="center">
                        <font size="1">
                            ${model.getCountL3TT()}
                        </font>
                    </div>
                </td>
                <td>
                    <div align="center">
                        <font size="1">
                            <fmt:formatNumber value="${(model.getCountL3TT()/model.getCountTT())*100}" pattern="###.##"/>
                        </font>
                    </div>
                </td>
                <td>
                    <div align="center">
                        <font size="1">
                            ${model.getComplexityLow()}
                        </font>
                    </div>
                </td>
                <td>
                    <div align="center">
                        <font size="1">
                            ${model.getComplexityMedium()}
                        </font>
                    </div>
                </td>
                <td>
                    <div align="center">
                        <font size="1">
                            ${model.getComplexityHight()}
                        </font>
                    </div>
                </td>
            </tr>
        </c:forEach>
            
        </tbody>
    </table>
        
    </body>
</html>
