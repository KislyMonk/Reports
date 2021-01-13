<%-- 
    Document   : upload
    Created on : 05.02.2018, 21:40:42
    Author     : Андрей
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="<c:url value="main.css" />" rel="stylesheet">
        <title>Анализ по городам</title>
    </head>
    <body>
        <form method="POST" enctype="multipart/form-data"
            action="/Reports/maintenanceSigma">
            Загрузи файлик:  <input type="file" name="file"><br /> 
            <input type="submit" value="Upload"> 
        </form> 
        Сообщение от сервера: ${message}

<table width="1450" style="border-collapse:collapse;">
    <tbody>
    <tr height="8">
        <td width="1446" colspan="25" bgcolor="#bcd2ee" >
        <div align="center"><font size="1" color="blue">Отчет за период
        ${range}
        </font></div>
        </td>
    </tr>
    <tr height="8">
    <td width="108" rowspan="3" class="header" >
    <div align="center"><font size="1">МР</font></div>
    </td><td width="71" rowspan="3" class="header" >
    <div align="center"><font size="1">Город</font></div>
    </td><td width="1304" colspan="23" class="header" >
    <div align="center"><font size="1">Обслуживание и качество.
    Отчетный период 00:00 пн - 23:59 вс. В отчет попадают
    заявки: 1. X=Все закрытые за отчетный период
    2. Y=Не завершенные на 23:59 воскресенья отчетной
    недели, длительность решения которых превышает
    24ч. Для незавершенных заявок длительность
    определяется от момента регистрации до
    23:59 воскресенья отчетной недели. </font></div>
    </td>
    </tr>
    <tr height="8" class="header">
    <td width="71" rowspan="2" class="header" >
    <div align="center"><font size="1">Количество заявок закрытых
    за отчетную неделю (X)</font></div>
    </td><td width="86" rowspan="2" class="header" >
    <div align="center"><font size="1">Количество заявок не завершенных
    на 23:59 вс, с временем решения более 24ч (Y)</font></div>
    </td><td width="152" colspan="4" class="header" >
    <div align="center"><font size="1">Количество заявок решенных
    в срок или длящихся на 23:59 вс отчетной недели</font></div>
    </td><td width="81" rowspan="2" class="header" >
    <div align="center"><font size="1">Среднее время решения
    заявки на техническую поддержку, час</font></div>
    </td><td width="79" rowspan="2" class="header" >
    <div align="center"><font size="1">Количество повторных
    заявок на техническую поддержку</font></div>
    </td><td width="86" rowspan="2" class="header" >
    <div align="center"><font size="1">Количество заявок из X,
    решенных на второй линии технической поддержки,
    без выезда</font></div>
    </td><td width="133" colspan="4" class="header" >
    <div align="center"><font size="1">Количество заявок из N=X+Y,
    решенных, обработанных или находящихся
    на 23:59 вс на второй линии.</font></div>
    </td><td width="83" rowspan="2" class="header" >
    <div align="center"><font size="1">Среднее время решения
    или обработки заявки абонента 2-ой линией
    тех. поддержки на выборке N=X+Y, час</font></div>
    </td><td width="98" rowspan="2" class="header" >
    <div align="center"><font size="1">Количество повторных
    заявок из N=X+Y на тех. поддержку, в которых
    предыдущее обращение было закрыто на 2-ой
    линии тех. поддержки</font></div>
    </td><td width="87" rowspan="2" class="header" >
    <div align="center"><font size="1">Количество заявок из N,
    решенных на третьей линии (с выездом) технической
    поддержки.</font></div>
    </td><td width="157" colspan="4" class="header" >
    <div align="center"><font size="1">Количество заявок из N=X+Y,
    решенных или находящихся на третьей линии
    (выезд) на 23:59вс. </font></div>
    </td><td width="76" rowspan="2" class="header" >
    <div align="center"><font size="1">Среднее время решения
    заявки абонента 3-ей линией тех. поддержки
    на выборке N, ч</font></div>
    </td><td width="97" rowspan="2" class="header" >
    <div align="center"><font size="1">Количество повторных
    заявок из N на тех. поддержку, в которых
    предыдущее обращение было закрыто на 3-ей
    линии тех. поддержки</font></div>
    </td>
    </td><td width="97" rowspan="2" class="header" >
    <div align="center"><font size="1">Среднее время
    "Решение о переводе на 2ЛТП" час.
    </font></div>
    </td>
    </tr>
    <tr height="8">
    <td width="23" class="header" >
    <div align="center"><font size="1">l до 36ч</font></div>
    </td><td width="44" class="header" >
    <div align="center"><font size="1">36ч-48ч</font></div>
    </td><td width="44" class="header" >
    <div align="center"><font size="1">48ч-72ч</font></div>
    </td><td width="38" class="header" >
    <div align="center"><font size="1">свыше 72ч</font></div>
    </td><td width="21" class="header" >
    <div align="center"><font size="1">до 6ч</font></div>
    </td><td width="34" class="header" >
    <div align="center"><font size="1">6ч-8ч</font></div>
    </td><td width="34" class="header" >
    <div align="center"><font size="1">8-24ч</font></div>
    </td><td width="40" class="header" >
    <div align="center"><font size="1">свыше 24ч</font></div>
    </td><td width="24" class="header" >
    <div align="center"><font size="1">до 36ч</font></div>
    </td><td width="45" class="header" >
    <div align="center"><font size="1">36ч-48ч</font></div>
    </td><td width="45" class="header" >
    <div align="center"><font size="1">48ч-72ч</font></div>
    </td><td width="39" class="header" >
    <div align="center"><font size="1">свыше 72ч</font></div>
    </td></tr>
        
        <%-- Начало строки таблицы --%>
        <c:forEach var="cityModel" items="${cityModelsList}">
            
    <tr height="8">     
        <td width="68" >
        <div align="center"><font size="1">${cityModel.getRegionName()}</font></div>
        </td><td width="71" >
        <div align="center"><font size="1">${cityModel.getName()}</font></div>
        </td><td width="71" >
        <div align="center"><font size="1">${cityModel.getCountL2TP()}</font></div>
        </td><td width="86" >
        <div align="center"><font size="1">${cityModel.getAtWorkCount()}</font></div>
        </td><td width="23" >
        <div align="center"><font size="1">${cityModel.getCompleateAt36h()}</font></div>
        </td><td width="44" >
        <div align="center"><font size="1">${cityModel.getCompleateAt48h()}</font></div>
        </td><td width="44" >
        <div align="center"><font size="1">${cityModel.getCompleateAt72h()}</font></div>
        </td><td width="38" >
        <div align="center"><font size="1">${cityModel.getCompleateAtMore72h()}</font></div>
        </td><td width="81" >
        <div align="center"><font size="1"><fmt:formatNumber value="${cityModel.getAveregeTimeL23TP()}" pattern="###.##"/></font></div>
        </td><td width="79" >
        <div align="center"><font size="1"><fmt:formatNumber value="${cityModel.getRepeatedTTL2()/8 + cityModel.getRepeatedTTL3()}" pattern="###"/></font></div>
        </td><td width="86" >
        <div align="center"><font size="1">${(cityModel.getCountL2TP() + cityModel.getAtWorkCount()) - cityModel.getCountL3TP()}</font></div>
        </td><td width="21" >
        <div align="center"><font size="1">${cityModel.getCompleateAt6hL2()}</font></div>
        </td><td width="34" >
        <div align="center"><font size="1">${cityModel.getCompleateAt8hL2()}</font></div>
        </td><td width="34" >
        <div align="center"><font size="1">${cityModel.getCompleateAt24hL2()}</font></div>
        </td><td width="40" >
        <div align="center"><font size="1">${cityModel.getCompleateAtMore24hL2()}</font></div>
        </td><td width="83" >
        <div align="center"><font size="1"><fmt:formatNumber value="${cityModel.getAveregeTimeL2TP()}" pattern="###.##"/></font></div>
        </td><td width="98" >
        <div align="center"><font size="1"><fmt:formatNumber value="${cityModel.getRepeatedTTL2()/8}" pattern="###"/></font></div>
        </td><td width="87" >
        <div align="center"><font size="1">${cityModel.getCountL3TP()}</font></div>
        </td><td width="24" >
        <div align="center"><font size="1">${cityModel.getCompleateAt36hL3()}</font></div>
        </td><td width="45" >
        <div align="center"><font size="1">${cityModel.getCompleateAt48hL3()}</font></div>
        </td><td width="45" >
        <div align="center"><font size="1">${cityModel.getCompleateAt72hL3()}</font></div>
        </td><td width="39" >
        <div align="center"><font size="1">${cityModel.getCompleateAtMore72hL3()}</font></div>
        </td><td width="76" >
        <div align="center"><font size="1"><fmt:formatNumber value="${cityModel.getAveregeTimeL3TP()}" pattern="###.##"/></font></div>
        </td><td width="97" >
        <div align="center"><font size="1">${cityModel.getRepeatedTTL3()}</font></div>
        </td>
        </td><td width="97" >
        <div align="center"><font size="1"><fmt:formatNumber value="${cityModel.getDecisionTime()}" pattern="###.##"/></font></div>
        </td>
    </tr>
    
        </c:forEach>
    <%-- Конец строки таблицы --%>
</tbody></table>
    </body>
    
    <div style="position: relative; float: left; padding: 10px">
        Были на ремонте в магнитогорске более 24х часов
        <ol>
            <c:forEach var="row" items="${timeMoreThen24h}">
            <li >
                ${row}
            </li>
            </c:forEach>
        </ol>
    </div>
    <div style="position: relative; float: left; padding: 10px">
        Повторные обращения на 3ЛТП Магнитогорск
        <ol>
            <c:forEach var="row" items="${repOn3LTPMGN}">
            <li >
                ${row}
            </li>
            </c:forEach>
        </ol>
    </div>
    <div style="position: relative; float: left; padding: 10px">
        Повторные обращения на 3ЛТП без Магнитогорска
        <ol>
            <c:forEach var="row" items="${repOn3LTP}">
            <li >
                ${row}
            </li>
            </c:forEach>
        </ol>
    </div>
    <div style="position: relative; float: left; padding: 10px">
        Среднее время за период:
        <ol>
            <li>2ЛТП: <fmt:formatNumber value="${avgTime2LTP}" pattern="###.##"/></li>
            <li>3ЛТП: <fmt:formatNumber value="${avgTime3LTP}" pattern="###.##"/></li>
        </ol>
    </div>

</html>
