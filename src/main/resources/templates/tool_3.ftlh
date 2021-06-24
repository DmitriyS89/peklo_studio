<#import "partials/main.ftlh" as main/>

<@main.renderWith>
    <h1 class="mt-4">Инструмент, определяющий открытие ссылок вне домена, в котором эта ссылка находится</h1>
    <div class="container-fluid">
        <p>*** There will be information of instrument</p>
        <p>*** There will be information of instrument</p>
        <p>*** There will be information of instrument</p>

        <div class="w-100 text-danger my-4">
            <#if error == "connection">
                <div class="bg-warning px-2">
                    Подключения к url не удалось!
                </div>
            <#elseif error = "not-correct">
                <div class="bg-warning px-2">
                    Введнённый url не правильный!
                </div>
            <#elseif error = "ok">
                <div class="bg-success px-2 text-dark">
                    Успешно!
                </div>
            </#if>
        </div>

        <form class="mx-auto w-50 d-flex flex-row" action="/tool_3/run" method="post">
            <div class="form-group">
                <input required type="url" class="form-control" name="url" id="exampleFormControlInput1"
                       placeholder="domain">
            </div>
            <div class="form-group">
                <button type="submit" class="btn btn-secondary">Go</button>
            </div>
            <#if _csrf??>
                <input type="hidden" name="${(_csrf.parameterName)!'csrf'}" value="${(_csrf.token)!'--no-token--'}"/>
            </#if>
        </form>

        <#if results ??>
            <table class="table">
                <thead class="thead-light">
                <tr>
                    <th scope="col">Сайт</th>
                    <th scope="col">${fromUrl}</th>
                </tr>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">Ссылки</th>
                </tr>
                </thead>
                <tbody>
                <#list results as r>
                    <tr>
                        <th scope="row">${r?index + 1}</th>
                        <td><a href="${r.href}"><p class="myP">${r.href}</p></a></td>
                    </tr>
                </#list>
                </tbody>

            </table>
        </#if>
    </div>
</@main.renderWith>