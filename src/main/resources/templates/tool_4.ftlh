<#import "partials/main.ftlh" as main/>

<@main.renderWith>
    <h1 class="mt-4">Инструмент,
        определяющий дубликаты
        страниц без 301 редиректов</h1>
    <div class="container-fluid">
        <p>*** There will be information of instrument</p>
        <p>*** There will be information of instrument</p>
        <p>*** There will be information of instrument</p>

        <div class="w-100 text-danger my-4">
            <#if error == "connection">
                <div class="bg-warning px-2">
                    Подключение к url не удалось!
                </div>
            <#elseif error = "not-correct">
                <div class="bg-warning px-2">
                    Введённый url неправильный!
                </div>
            <#elseif error = "ok">
                <div class="bg-success px-2 text-dark">
                    Успешно!
                </div>
            </#if>
        </div>

        <form class="mx-auto w-50 d-flex flex-row" action="/tool_4/run" method="post">
            <#--            <div class="form-group">-->
            <#--                <select class="form-control" name="protocol" id="exampleFormControlSelect1">-->
            <#--                    <option>http://</option>-->
            <#--                    <option>https://</option>-->
            <#--                </select>-->
            <#--            </div>-->
            <div class="form-group">
                <input required type="url" class="form-control" name="urlFromFront" id="exampleFormControlInput1"
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
                <tr>
                    <th>#</th>
                    <th>От-Куда</th>
                    <th>Куда</th>
                    <th>Инфо</th>
                </tr>
                <#list results as r>
                    <tr>
                        <td>${r.code}</td>
                        <td><a href="${r.urlFrom}"><#assign minititle=(r.urlFrom!"")>
                                <#if minititle?length &lt; 24>
                                    ${minititle}
                                <#else>
                                    ${minititle?substring(0,23)} ...
                                </#if></a></td>
                        <td><a href="${r.urlTo}"><#assign minititle=(r.urlTo!"")>
                                <#if minititle?length &lt; 24>
                                    ${minititle}
                                <#else>
                                    ${minititle?substring(0,23)} ...
                                </#if></a></td>
                        <td>${r.message}</td>
                    </tr>
                </#list>
            </table>
        </#if>
    </div>

</@main.renderWith>