<#import "partials/main.ftlh" as main/>

<@main.renderWith>
    <h1 class="mt-4">Tool 1</h1>
    <div class="container-fluid">
        <p>*** Тут можно посмотреть те элементы которые мониторятся</p>
        <p>*** Их можно удалить, кнопка с права</p>
        <p>*** Если: </p>
        <ul>
            <li>
                В поле статус показано <i style="color: green">green</i> тогда элемент который нужно было мониторить успешно сохранился
            </li>
            <li>
                В поле статус показано <i style="color: red">red</i> тогда элемент который нужно было мониторить не смог по каким то причинам найтись и сохранится
            </li>
        </ul>


        <#if clientItems ??>
            <table class="table myTable">
                <thead class="thead-light">
                <tr>
                    <th scope="col" class="width15">id</th>
                    <th scope="col" class="width30">Сайт</th>
                    <th scope="col" class="width20">Время проверки</th>
                    <th scope="col" class="width15">Статус</th>
                    <th scope="col" class="width15">Настройка</th>
                </tr>
                </thead>
                <tbody>
                <#list clientItems as item>
                    <tr>
                        <td class="width15">${item.id}</a></td>
                        <td class="width30"><a href="${item.url}"><p>
                                    <#assign minititle=(item.url!"")>
                                    <#if minititle?length &lt; 35>
                                        ${minititle}
                                    <#else>
                                        ${minititle?substring(0,34)} ...
                                    </#if></p></a></td>
                        <td class="width20">каждые ${item.time}</td>
                        <td class="width15" style="color: ${item.htmlValueState}">${item.htmlValueState}</td>
                        <td class="width15">
                            <form action="/tool_1/delete-item?id=${item.getId()}" method="post">
                                <#if _csrf??>
                                    <input type="hidden" name="${(_csrf.parameterName)!'csrf'}"
                                           value="${(_csrf.token)!'--no-token--'}"/>
                                </#if>
                                <button type="submit" class="btn btn-primary">Удалить</button>
                            </form>
                        </td>
                    </tr>
                </#list>
                </tbody>

            </table>
        </#if>
    </div>
</@main.renderWith>