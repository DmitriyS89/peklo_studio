<#import "partials/main.ftlh" as main/>

<@main.renderWith>
    <h1 class="mt-4">Tool 1</h1>
    <div class="container-fluid">
        <p>*** There will be information of instrument</p>
        <p>*** There will be information of instrument</p>
        <p>*** There will be information of instrument</p>

        <#if clientItems ??>
            <table class="table myTable">
                <thead class="thead-light">
                <tr>
                    <th scope="col" class="width50">Сайт</th>
                    <th scope="col" class="width20">Время проверки</th>
                    <th scope="col" class="width15">Статус</th>
                    <th scope="col" class="width15">Настройка</th>
                </tr>
                </thead>
                <tbody>
                <#list clientItems as item>
                    <tr>
                        <td class="width50"><a href="${item.url}"><p>${item.url}</p></a></td>
                        <td class="width20">каждые ${item.time}</td>
                        <td class="width15" style="color: ${item.htmlValueState}">${item.htmlValueState}</td>
                        <td class="width15">
                            <form action="/tool_1/delete-item?id=${item.getId()}" method="post">
                                <#if _csrf??>
                                    <input type="hidden" name="${(_csrf.parameterName)!'csrf'}" value="${(_csrf.token)!'--no-token--'}"/>
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