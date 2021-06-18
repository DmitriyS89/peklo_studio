<#import "partials/main.ftlh" as main/>

<@main.renderWith>
    <h1 class="mt-4">Tool 1</h1>
    <div class="container-fluid">
        <p>*** There will be information of instrument</p>
        <p>*** There will be information of instrument</p>
        <p>*** There will be information of instrument</p>

        <a href="/tool_1/client-items">
            <button type="button" class="btn btn-info">Ваши сохранённые элементы</button>
        </a>

        <div class="w-100 text-danger my-4">
            <#if success == "true">
                <div class="bg-warning px-2">
                    Успешно!
                </div>
            <#elseif success = "false">
                <div class="bg-warning px-2">
                    Ошибка!
                </div>
            </#if>
        </div>

        <form class="mx-auto w-50 d-flex flex-row" action="/tool_1/run" method="get">
            <div class="my-auto mx-1">
                <input required type="url" class="form-control" name="url" id="exampleFormControlInput1"
                       placeholder="domain">
            </div>
            <div class="my-auto mx-1" style="display: flex;width: 20%;align-items: center;">
                <label for="ourScripts">Скрипты</label>
                <input id="ourScripts" type="checkbox" class="form-control" name="isScript">
            </div>
            <div class="my-auto mx-1">
                <button type="submit" class="btn btn-secondary">Go</button>
            </div>
            <#if _csrf??>
                <input type="hidden" name="${(_csrf.parameterName)!'csrf'}" value="${(_csrf.token)!'--no-token--'}"/>
            </#if>
        </form>
    </div>
</@main.renderWith>